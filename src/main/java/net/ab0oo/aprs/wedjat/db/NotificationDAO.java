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
package net.ab0oo.aprs.wedjat.db;

import java.util.List;

import net.ab0oo.aprs.wedjat.models.Notification;

/**
 * @author johng
 *
 */
public interface NotificationDAO {
	public Notification getNotification(Long notificationId);
	public List<Notification> getNotificationsByUserId(Long userId);
	public List<Notification> getNotificationsByRuleId(Long ruleId);
}
