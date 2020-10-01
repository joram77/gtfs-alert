package be.wethinkonline.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.wethinkonline.gtfs_notify.model.Alert;
import be.wethinkonline.gtfs_notify.model.RealtimeUpdate;
import be.wethinkonline.gtfs_notify.model.Stop;
import be.wethinkonline.gtfs_notify.service.AlertService;
import be.wethinkonline.gtfs_notify.service.PlannedImportService;
import be.wethinkonline.gtfs_notify.service.RealTimeImportService;
import be.wethinkonline.gtfs_notify.service.RealtimeUpdateService;
import be.wethinkonline.gtfs_notify.service.TripService;
import be.wethinkonline.test.RealtimeUpdateServiceTest.TestHttpServer;

public class AlertServiceTest {

	static EntityManager em;
	static RealtimeUpdateService rtuService = new RealtimeUpdateService();
	static RealTimeImportService is = new RealTimeImportService();
	static AlertService as = new AlertService();
	TestHttpServer testHttpServer;

	@AfterClass
	public static void cleanup() {
		em.close();
	}

	@BeforeClass
	public static void clearAlertAndCreateTestPlannedData() {
		PlannedImportService ps = new PlannedImportService();

		em = TripService.getEmf().createEntityManager();

		ps.update(new File("/Users/user/Documents/projects/gtfs-alert/sample_data/planned/2017-07-20.zip"));

	}

	/** 
	 * notably required for updateAlertGroup* tests, 
	 * which verify the alert gruops based on the sample alert data inserted here
	 */
	@Before
	public void createAlerts() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		Query query = em.createNamedQuery("Alert.clearTestData");
		query.executeUpdate();
		tx.commit();
		// clear alert trip_groups which were present by 'recreating' empty
		// based on emtpy alert table
		as.updateAlertGroups();
		for (int i = 1; i <= 4; i++) {
			tx = em.getTransaction();
			tx.begin();
			query = em.createNamedQuery("Alert.createTestData" + i);
			query.executeUpdate();
			tx.commit();
		}
		tx.begin();
		query = em.createNamedQuery("AlertUpdatesProcessed.deleteAll");
		query.executeUpdate();
		tx.commit();
	}

	public void updateAlertGroup() {
		// todo
		as.updateAlertGroup("8891140", "8813003", "7:23:00");// aleter>
																		// bxl
																		// centraal
		// 8891140 8813003
	}

	@Test
	public void updateAlertGroup2() {
		String stop_id_a="8891140";
		String stop_id_b="8813003";
		String departure_time ="8:23:00";
		as.updateAlertGroup(stop_id_a, stop_id_b,  departure_time);// aleter>
																		// bxl
																		// centraal

		Query query = em.createNamedQuery("AlertTrips.findAll");

		List<Object[]> samples = (List<Object[]>) query.getResultList();

		org.junit.Assert.assertEquals(1304, samples.get(0)[0]);
		org.junit.Assert.assertEquals(1305, samples.get(1)[0]);
		org.junit.Assert.assertEquals(1306, samples.get(2)[0]);
		org.junit.Assert.assertEquals(1307, samples.get(3)[0]);
		org.junit.Assert.assertEquals(1308, samples.get(4)[0]);
		org.junit.Assert.assertEquals(1790, samples.get(5)[0]);
		org.junit.Assert.assertEquals(1792, samples.get(6)[0]);
		org.junit.Assert.assertEquals(1794, samples.get(7)[0]);
		org.junit.Assert.assertEquals(1796, samples.get(8)[0]);
		org.junit.Assert.assertEquals(1800, samples.get(9)[0]);
		org.junit.Assert.assertEquals(10, samples.size());

	}

	@Test
	public void updateAlertGroup2ViaAllAlertGroups() {
		as.updateAlertGroups();
		String stop_id_a="8891140";
		String stop_id_b="8813003";
		String departure_time ="8:23:00";

		Query query = em.createNamedQuery("AlertTrips.findTripGroupTrips");
		query.setParameter(1, stop_id_a);
		query.setParameter(2, stop_id_b);
		query.setParameter(3, departure_time);
		List<Object[]> samples = (List<Object[]>) query.getResultList();

		org.junit.Assert.assertEquals(1304, samples.get(0)[0]);
		org.junit.Assert.assertEquals(1305, samples.get(1)[0]);
		org.junit.Assert.assertEquals(1306, samples.get(2)[0]);
		org.junit.Assert.assertEquals(1307, samples.get(3)[0]);
		org.junit.Assert.assertEquals(1308, samples.get(4)[0]);
		org.junit.Assert.assertEquals(1790, samples.get(5)[0]);
		org.junit.Assert.assertEquals(1792, samples.get(6)[0]);
		org.junit.Assert.assertEquals(1794, samples.get(7)[0]);
		org.junit.Assert.assertEquals(1796, samples.get(8)[0]);
		org.junit.Assert.assertEquals(1800, samples.get(9)[0]);
		org.junit.Assert.assertEquals(10, samples.size());

	}

	@Test
	public void addAlertAndSendFirebaseMessage() {
		//  ,8813003,8891140,'Bruxelles-Central', 'Aalter', '17:16:00', 
		String stopA = "Bruxelles-Central";
		String stopB ="Aalter";
		String stopIdA = "8813003";
		String stopIdB = "8891140";
		String departureTime = "17:16:00";
		String tokenAndroid ="cAc8pSEsOoPIM9EC-QXJO0:APA91bFI25gRObG1nVkqcHbtIz214IysxWGtR3hjxtVBi8FdKBshAtfXztD4KK-FqpHz8LdaOAJAQH-Kxp-txSkfxsTer7JAtD0orIu8_iD_NXeMQqZoUE3DDCZdh7GPH9z9o5Df4Z-Z";

		Alert a = new Alert();

		a.setStopIdA(stopIdA);

		a.setStopIdB(stopIdB);
		a.setStopA(stopA);
		a.setStopB(stopB);
		a.setDepartureTime(departureTime);

		a.setFirebaseToken(tokenAndroid);

		em.getTransaction().begin();

		em.persist(a);
		

		em.getTransaction().commit();
	
	
		
		long id = a.getAlertId();

		Query
		 query = em.createNamedQuery("Alert.findById");
		query.setParameter(1, id);
		Alert alertFromDb  = (Alert) query.getSingleResult();
	
		
		assertEquals(alertFromDb.getFirebaseToken(),tokenAndroid);
		
		

		
		
	}
	@Test
	public void composeTitle() {
		
		String stopA = "Bruxelles-Central";
		String stopB ="Aalter";
		String stopIdA = "8813003";
		String stopIdB = "8891140";
		String departureTime = "17:16:00";
		
		Alert a = new Alert();

		a.setStopIdA(stopIdA);
		a.setStopIdB(stopIdB);
		a.setStopA(stopA);
		a.setStopB(stopB);
		a.setDepartureTime(departureTime);
		
		RealtimeUpdate rtu = new RealtimeUpdate();
		rtu.setStopName(stopA);
		rtu.setStopBName(stopB);
		rtu.setDepartureDelay(4*60);
		

		String expectedTitle = "Train >"+ a.getStopB() + " +"+ rtu.getDepartureDelayInMin() + " min" 
	+ " from " + a.getStopA();
		
		assertEquals(expectedTitle, new AlertService().composeTitle(rtu));
		System.out.println(expectedTitle);
		
	}
	@Test
	public void testEmail() {
		as.setPort("2500");
		as.email("alert@example.com", "msg", "subj");
	}
}
