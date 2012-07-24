package net.ab0oo.aprs.wedjat.service;

import java.util.ArrayList;
import java.util.List;

import net.ab0oo.aprs.wedjat.models.MonitoredStation;

public class CachedWedjatServiceImpl extends WedjatServiceImpl {

	private static final long serialVersionUID = 1L;
	private static final int MAX_CACHE_AGE = 10000;
	private static final int MAX_CACHE_HITS = 1000;
	private List<MonitoredStation> monitoredStationsList = new ArrayList<MonitoredStation>();
	private int cacheHits = 0;
	private long cacheUpdateTime = 0;
	/* (non-Javadoc)
	 * @see net.ab0oo.aprs.wedjat.service.WedjatService#getMonitoredStationsList(java.lang.String)
	 */
	@Override
	public List<MonitoredStation> getMonitoredStationsList(String callsign) {
		if ( (System.currentTimeMillis() - cacheUpdateTime > MAX_CACHE_AGE ) || cacheHits > MAX_CACHE_HITS ) {
			cacheHits = 0;
			cacheUpdateTime = System.currentTimeMillis();
			monitoredStationsList = monitoredStationDAO.getMonitoredStationsList(callsign);
		}
		return monitoredStationsList;
	}

}
