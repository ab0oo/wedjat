/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs.wedjat;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import net.ab0oo.aprs.avrs.models.ReferencePoint;
import net.ab0oo.aprs.parser.CourseAndSpeedExtension;
import net.ab0oo.aprs.parser.Position;
import net.ab0oo.aprs.parser.PositionPacket;
import net.ab0oo.aprs.parser.Utilities;
import net.ab0oo.aprs.wedjat.models.AlertHistory;
import net.ab0oo.aprs.wedjat.models.MonitoredStation;
import net.ab0oo.aprs.wedjat.models.Notification;
import net.ab0oo.aprs.wedjat.models.NotificationAddress;
import net.ab0oo.aprs.wedjat.models.Rule;
import net.ab0oo.aprs.wedjat.models.Rule.RuleType;
import net.ab0oo.aprs.wedjat.models.User;
import net.ab0oo.aprs.wedjat.models.Zone;
import net.ab0oo.aprs.wedjat.service.WedjatService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author johng
 * 
 */
public class NotificationThread implements Runnable {

    static DecimalFormat           degFmt       = new DecimalFormat("###.0");
    static DecimalFormat           distFmt      = new DecimalFormat("###.00");
    static final String            FROM_ADDRESS = "alert@aprs-alert.net";
    protected static final Logger  log          = LogManager.getLogger("wedjat");
    private List<NotificationInfo> notificationList;
    private Object                 lockingObject;
    private WedjatService          wedjatService;

    public NotificationThread() {
        log.info("Notification thread starting");
    }

    /*
     * The NotificationThread uses a simple subscriber model to wait() on an object. The thread merely parks on the
     * lockingObject waiting for a notify(). When it gets one, it pulls all pending notifications off the list and
     * begins sending the notifications via email. TODO: This should be more modular, to allow for different types of
     * notifications For example, I should be able to send an APRS message via APRS-IS as a notification
     */
    @Override
    public void run() {
        NotificationInfo oneInfo = null;
        while (true) {
            try {
                @SuppressWarnings("unused")
                int size = 0;
                oneInfo = null;
                synchronized (lockingObject) {
                    while ((size = notificationList.size()) <= 0) {
                        try {
                            lockingObject.wait(60000);
                        } catch (InterruptedException ie) {
                        }
                    }
                    if (notificationList.size() > 0) {
                        oneInfo = notificationList.remove(0);
                        size--;
                    }
                    if (oneInfo != null) {
                        notify(oneInfo);
                    } else {
                        log.warn("Managed to fall out of the NotificationThread wait() without an object");
                    }
                }
            } catch (Throwable t) {
                log.fatal("HURL!  ", t);
            }
        }
    }

    private void notify(NotificationInfo info) {
        Rule rule = info.getRule();
        PositionPacket positionPacket = info.getPosition();
        Position position = positionPacket.getPosition();
        MonitoredStation ms = wedjatService.getMonitoredStationByStationId(rule.getStationId());
        String sourceCall = ms.getNickname();
        String alertString = sourceCall + " is moving";
        User user = wedjatService.getUser(rule.getUserId());
        TimeZone notificationTimeZone = user.getTimezone();
        String mSystem = user.getMeasurementSystem();
        String distanceUnit = " km ";
        if (mSystem.equalsIgnoreCase("SAE")) {
            distanceUnit = " miles ";
        }
        if (rule.getRuleType() == RuleType.INCURSION) {
            Zone alertZone = wedjatService.getZone(rule.getZoneId());
            alertString = sourceCall + " has entered zone " + alertZone.getDescription();
        } else if (rule.getRuleType() == RuleType.EXCURSION) {
            Zone alertZone = wedjatService.getZone(rule.getZoneId());
            alertString = sourceCall + " has exited zone " + alertZone.getDescription();
        } else if (rule.getRuleType() == RuleType.MOVEMENT) {
            List<ReferencePoint> referencePoints = wedjatService.listClosestCities(position);
            ReferencePoint rp1 = referencePoints.get(0);
            double distance = rp1.getMetersDistance() / 1000f;
            if (mSystem.equalsIgnoreCase("SAE")) {
                distance = Utilities.metersToMiles(distance * 1000);
            }
            alertString += ". " + distFmt.format(distance) + distanceUnit
                    + Utilities.degressToCardinal(rp1.getBearingTo()) + " of " + rp1.getCity() + ", " + rp1.getRegion();
        } else {
            log.warn("Got an unknown rule type " + rule.getRuleType() + " for " + ms.getCallsign());
        }

        List<Notification> notifications = wedjatService.getNotificationsByRuleId(rule.getRuleId());
        int count = 0;
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(notificationTimeZone);
        int minuteOfDay = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1; // make days 0-based, Sunday=0, Saturday=6
        String subject = "APRS Alert";
        for (Notification notification : notifications) {
            // check time and day values here.
            boolean validDay = ((int) Math.pow(2, today) & notification.getValidDays()) == Math.pow(2, today);
            if (notification.getStartTime() <= minuteOfDay && notification.getEndTime() > minuteOfDay && validDay) {
                NotificationAddress nAddress = wedjatService.getNotificationAddress(notification
                    .getNotificationAddressId());
                if (!nAddress.isShortForm()) {
                    subject = alertString;
                    alertString += "\n\n";
                    alertString += "Position for " + sourceCall + " reported at " + position.toString() + "\n";
                    if (positionPacket.getExtension() instanceof CourseAndSpeedExtension) {
                        CourseAndSpeedExtension cas = (CourseAndSpeedExtension) positionPacket.getExtension();
                        alertString += "movement vector is " + cas.getCourse() + " degrees at ";
                        if (mSystem.equalsIgnoreCase("SAE")) {
                            alertString += Utilities.ktsToMph(cas.getSpeed()) + " mph\n";
                        } else {
                            alertString += Utilities.kntsToKmh(cas.getSpeed()) + "km/h\n";
                        }
                    }
                    alertString += "Static map of location here:  ";
                    URI alertUrl = URI
                        .create("https://maps.googleapis.com/maps/api/staticmap?&zoom=14&size=600x600&markers=color:blue%7Clabel:"
                                + sourceCall
                                + "%7C"
                                + position.getLatitude()
                                + ","
                                + position.getLongitude()
                                + "&sensor=false");
                    alertString += alertUrl.toString();
                    alertString += "\n\nTrack this station on <a href=\"http://aprs.fi/" + sourceCall
                            + "\">APRS.fi</a>";
                }
                String toAddress = nAddress.getEmailAddress();
                log.info("Sending " + alertString + " to " + toAddress);
                SendMail sender = new SendMail(FROM_ADDRESS, toAddress, subject, alertString);
                sender.send();
                AlertHistory ah = new AlertHistory(rule.getUserId(), alertString);
                wedjatService.saveAlertHistory(ah);
                count++;
            } else {
                log.info("Notification for rule " + rule.getRuleId() + " outside notification window");
            }
        }
        log.info("Sent " + count + " notifications for rule " + rule.getRuleId());
    }

    /**
     * @param notificationList
     *            the notificationList to set
     */
    public final void setNotificationList(List<NotificationInfo> notificationList) {
        this.notificationList = notificationList;
    }

    /**
     * @param lockingObject
     *            the lockingObject to set
     */
    public final void setLockingObject(Object lockingObject) {
        this.lockingObject = lockingObject;
    }

    /**
     * @param wedjatService
     *            the wedjatService to set
     */
    public final void setWedjatService(WedjatService wedjatService) {
        this.wedjatService = wedjatService;
    }

}
