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
/*
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ab0oo.aprs.wedjat.models.Cap;
import net.ab0oo.aprs.wedjat.models.CapsFipsCounty;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.google.publicalerts.cap.Alert;
import com.google.publicalerts.cap.Area;
import com.google.publicalerts.cap.CapXmlParser;
import com.google.publicalerts.cap.Info;
import com.google.publicalerts.cap.ValuePair;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

*/
/**
 * @author johng
 *
 */
public class CapClient {
/*
    private static boolean noFetch = false;
    private static org.hibernate.SessionFactory sessionFactory;
    private static long capId = 0;
    private static long capFipsId = 0;
  
    public static void main(String[] args) {
        try {
        	setUp();
        	Session session = sessionFactory.openSession();
            URL url = new URL("http://alerts.weather.gov/cap/us.php?x=0");
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            @SuppressWarnings("unchecked")
            List<SyndEntry> entries = feed.getEntries();
            System.out.println(feed.getTitle());
            int total = entries.size(); 
            int recent = 0;
            for ( SyndEntry entry : entries ) {
                Date updateTime = entry.getUpdatedDate();
                if ( System.currentTimeMillis() - updateTime.getTime() > 900000 ) { continue; }
                recent++;
                System.out.println(entry.getTitle());
                if ( noFetch ) continue;
                URL capUrl = new URL(entry.getLink());
                BufferedReader capBr = new BufferedReader(new InputStreamReader(capUrl.openStream()));
                StringBuilder sb = new StringBuilder();
                String line = capBr.readLine();
                while ( line != null ) {
                    sb.append(line);
                    line = capBr.readLine();
                }
                CapXmlParser parser = new CapXmlParser(false);
                Alert alert = parser.parseFrom(sb.toString());
                String identifier = alert.getIdentifier();
                System.out.println("ID:  "+identifier);
                System.out.println(alert.getStatus()+":"+alert.getScope()+":"+alert.getMsgType());
                List<Info> infoList = alert.getInfoList();
                for ( Info infoItem : infoList ) {
                    System.out.println("\t"+infoItem.getHeadline());
                    for ( int i=0; i < infoItem.getCategoryCount(); i++) {
                        System.out.print("\t\tCategory: "+infoItem.getCategory(i));
                    }
                    System.out.println();
                    System.out.println("\t"+infoItem.getSeverity()+":"+infoItem.getUrgency());
                    System.out.println("\t"+infoItem.getEvent());
                    Date effectiveTime = new Date();
                    Date expireTime = new Date();
                    try {
                    	effectiveTime =  parse(infoItem.getEffective());
                    	expireTime = parse(infoItem.getExpires());
                    	System.out.println("Effective as of "+effectiveTime+", expires "+expireTime);
                    } catch ( ParseException pe ) {
                    	System.err.println("Unable to parse the date:  "+pe);
                    }
                    Cap cap = new Cap();
                    capId++;
                    cap.setId(capId);
                    cap.setCategory(infoItem.getCategory(0).toString());
                    cap.setUpdateTime(updateTime);
                    cap.setExpireTime(expireTime);
                    cap.setEvent(infoItem.getEvent());
                    cap.setEventId(alert.getIdentifier());
                    cap.setSeverity(infoItem.getSeverity().toString());
                    cap.setUrgency(infoItem.getUrgency().toString());
                    session.beginTransaction();
                    session.save(cap);
                    session.getTransaction().commit();
                    session.beginTransaction();
                    List<CapsFipsCounty> fipsCounties = new ArrayList<CapsFipsCounty>();
                    for ( int i=0; i < infoItem.getAreaCount(); i++) {
                    	Area area = infoItem.getArea(i);
                    	System.out.println("\tDesc:  "+area.getAreaDesc());
                    	List<ValuePair> vpList = area.getGeocodeList();
                    	for ( ValuePair vp : vpList ) {
                    		if ( vp.getValueName().equalsIgnoreCase("FIPS6")) {
                    			System.out.println("\t\t"+vp.getValueName()+":"+vp.getValue());
                    			CapsFipsCounty cfc = new CapsFipsCounty(vp.getValue());
                    			cfc.setId(capFipsId++);
                    			cfc.setCapsId(capId);
                    			fipsCounties.add(cfc);
                    			session.save(cfc);
                    		}
                    	}
                     	//System.out.println("\t\tArea affected: "+infoItem.getArea(i));
                    }
                    session.getTransaction().commit();
                }
            }
            session.close();
            System.out.println(total+" total alerts, "+recent+" recent alerts.");
        } catch ( Exception ex ) {
            ex.printStackTrace();
        }
    }
*/
/*
    protected static void setUp() throws Exception {
        // A SessionFactory is set up once for an application
        sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
    }
    
    public static Date parse( String input ) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
        
        //this is zero time so we need to add that TZ indicator for 
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;
        
            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }
        
        return df.parse( input );
        
    }

*/
}
