package be.wethinkonline.gtfs_notify.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import be.wethinkonline.gtfs_notify.model.StopTimesAToBResult;

public class TripService {

	private EntityManager em;
	static EntityManagerFactory emf;

	public static EntityManagerFactory getEmf() {
		return emf;
	}
	static {
		
	 emf = Persistence.createEntityManagerFactory("gtfs_alert");

	}
	public TripService() {

	}

	/**
	 * Gets a list of trips and departure & arrival dates for trains departing from station a to
	 * destination station b on a certain date & time.
	 * 
	 * @param a Departure train station
	 * @param b Destination train station
	 * @param date
	 *            20170727
	 * @param time
	 *            7:22:00
	 * @return
	 */
	public List aToBWithStopName(String a, String b, String date, String time) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("tripPlan");
		query.setParameter(3, time + "%");

		query.setParameter(1, a+"%");
		query.setParameter(2, b+"%");
		query.setParameter(4, date + "%");

		@SuppressWarnings("unchecked")
		List<StopTimesAToBResult> samples = query.getResultList();
		em.close();
		return samples;

	}
	
	/**
	 * Gets a list of trips and departure & arrival dates for trains departing from station a stopId to
	 * destination station b stopId on a certain date & time.
	 * 
	 * @param a Departure train station
	 * @param b Destination train station
	 * @param date
	 *            20170727
	 * @param time
	 *            7:22:00
	 * @return
	 */
	public List planWithStopId(int aId, int bId, String date, String time) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("gtfs_alert");
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("tripPlanStopId");
		query.setParameter(1, aId);
		query.setParameter(2, bId);
		query.setParameter(4, date + "%");
		query.setParameter(3, time + "%");
		@SuppressWarnings("unchecked")
		List<StopTimesAToBResult> samples = query.getResultList();
		em.close();
		return samples;

	}
	
	/**
	 * Gets a list of trips and departure & arrival dates for trains departing from station a stopId to
	 * destination station b stopId on a certain date & time.
	 * 
	 * @param a Departure train station
	 * @param b Destination train station
	 * @param date
	 *            20170727
	 * @param time
	 *            7:22:00
	 * @return
	 */
	public List planWithStopIndex(int aIndex, int bIndex, String date, String time) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("gtfs_alert");
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("tripPlanStopIndex");
		query.setParameter(1, aIndex);
		query.setParameter(2, bIndex);
		query.setParameter(4, date + "%");
		query.setParameter(3, time + "%");
		@SuppressWarnings("unchecked")
		List<StopTimesAToBResult> samples = query.getResultList();
		em.close();
		return samples;

	}
	/**
	 * Gets a list of trips ID's from a to b on a certain hour.
	 * @param aIndex
	 * @param bIndex
	 * @return
	 */
	public List trip(int aIndex, int bIndex, String time) {
		em = emf.createEntityManager();
		Query query = em.createNamedQuery("trip");
		query.setParameter(1, aIndex);
		query.setParameter(2, bIndex);
		query.setParameter(3, time + "%");

		@SuppressWarnings("unchecked")
		List<StopTimesAToBResult> samples = query.getResultList();
		em.close();
		return samples;

	}

}
