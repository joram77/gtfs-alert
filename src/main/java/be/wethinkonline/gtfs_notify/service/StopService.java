package be.wethinkonline.gtfs_notify.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import be.wethinkonline.gtfs_notify.model.Stop;
import be.wethinkonline.gtfs_notify.model.StopTimesAToBResult;

public class StopService {

	private EntityManager em;
	
	public List<Stop> stopByName(String s) {
		em = TripService.emf.createEntityManager();
		Query query = em.createNamedQuery("Stop.stopByName");
		query.setParameter(1, s);
		List<Stop> samples = (List<Stop>) query.getResultList();
		em.close();
		return samples;

	}
	
	public List<Stop> stopBeginningByName(String s) {
		em = TripService.emf.createEntityManager();
		Query query = em.createNamedQuery("Stop.stopBeginningByName");
		query.setParameter(1, s);
		List<Stop> samples = (List<Stop>) query.getResultList();
		em.close();
		return samples;

	}



}
