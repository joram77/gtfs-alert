package be.wethinkonline.gtfs_notify.service;
// todo account for timezones  of agency
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.util.*;  
import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*;  

import javax.persistence.EntityManager;
import javax.persistence.Query;

import be.wethinkonline.gtfs_notify.model.RealtimeUpdate;

public class RealtimeUpdateService {


	private EntityManager em;

	/**
	 * List of realtime update times per trip alert group tripids.
	 * 
	 * @return
	 */
	public List<RealtimeUpdate> realtimeUpdate() {
		em = TripService.emf.createEntityManager();
		Query query = em.createNamedQuery("realtime_update_unprocessed", RealtimeUpdate.class);
		List<RealtimeUpdate> samples = (List<RealtimeUpdate>) query.getResultList();
		em.close();
		return samples;

	}
	
	public List<RealtimeUpdate> allRealtimeUpdates() {
		em = TripService.emf.createEntityManager();
		Query query = em.createNamedQuery("realtime_update_all", RealtimeUpdate.class);
		List<RealtimeUpdate> samples = (List<RealtimeUpdate>) query.getResultList();
		em.close();
		return samples;

	}

	public List<RealtimeUpdate> realtimeUpdateStopMinDelay(String stopName, int minDepartureDelay) {
		em = TripService.emf.createEntityManager();
		Query query = em.createNamedQuery("realtime_update_stop_min_delay", RealtimeUpdate.class);
		query.setParameter(1, minDepartureDelay );
		query.setParameter(2, stopName);

		List<RealtimeUpdate> samples = (List<RealtimeUpdate>) query.getResultList();
		em.close();
		return samples;

	}
	
}
