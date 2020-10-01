package be.wethinkonline.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import  org.junit.Before;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import com.sun.net.httpserver.*;
import org.junit.Test;


import be.wethinkonline.gtfs_notify.model.Alert;
import be.wethinkonline.gtfs_notify.model.RealtimeUpdate;
import be.wethinkonline.gtfs_notify.service.TripService;
import be.wethinkonline.gtfs_notify.service.AlertService;
import be.wethinkonline.gtfs_notify.service.PlannedImportService;
import be.wethinkonline.gtfs_notify.service.RealTimeImportService;
import be.wethinkonline.gtfs_notify.service.RealtimeUpdateService;

public class RealtimeUpdateServiceTest {
	static EntityManager em;
	static RealtimeUpdateService rtuService = new RealtimeUpdateService();
	static RealTimeImportService is = new RealTimeImportService();
	static AlertService as = new AlertService();
	 TestHttpServer testHttpServer;

	@BeforeClass
	public static void createPlannedData() {
		PlannedImportService ps = new PlannedImportService();
		ps.update(new File("/Users/user/Documents/projects/gtfs-alert/sample_data/planned/2017-07-20.zip"));
		
	}
	
	@Before
	public void clearAlert()
	{
		em = TripService.getEmf().createEntityManager();
		EntityTransaction tx= em.getTransaction();
		tx.begin();
		Query query = em.createNamedQuery("Alert.clearTestData");
		query.executeUpdate();
		tx.commit();
		for(int i=1;i<=4;i++){
			tx= em.getTransaction();
			tx.begin();
			 query = em.createNamedQuery("Alert.createTestData"+i);
			query.executeUpdate();
			tx.commit();
		}
		tx.begin();
		 query = em.createNamedQuery("AlertUpdatesProcessed.deleteAll");
		query.executeUpdate();
		tx.commit();
		//em.close();

		as.updateAlertGroups();

	
	}

	@Before
	public void makeTest(){
		testHttpServer = new TestHttpServer();
		
	}
	
	@After
	public void destroy(){
		testHttpServer.stop();
		
	}
	


	@Test
	public void testDelay20170804AalterToBlankenberge() throws IOException {
		/* Delay 2017-08-04 8:37:00 -> 8:38:00 Aalter -> blankenberge */
		assertDelayTime("2017-08-04_09-02-45","8:37:00","08:38:00");

	}
	
	@Test
	public void testDelay20170927BrusselToAalter() throws IOException {
		/* Delay 2017-08-04 17:16:00 -> 17:27:00 Brussel -> Aalter */

		assertDelayTime("2017-09-26_17-35-06","17:16:00","17:27:00");
	}
	
	/**
	 * Aalter	120	Genk	7:23:00	2017-10-02 07:25:00	7:22:00
	 * @throws IOException 
	 */
	//@Test
	public void testDelay20171002AalterToBrussel() throws IOException {
		/* Delay 2017-08-04 17:16:00 -> 17:27:00 Brussel -> Aalter */

		assertDelayTime("2017-10-02_08-32-33","7:23:00","07:25:00");
	}

	public void testAlertAgainstCurrentDataInDb() {

		List<RealtimeUpdate> lsRtu = rtuService.realtimeUpdate();
		AlertService alertService = new AlertService();
		alertService.alert(lsRtu);

	}
	
	@Test
	public void testAlertAgainstLatestDownloadData() {
		ImportServiceTest is = new ImportServiceTest();
		is.latestDataToDb();
		List<RealtimeUpdate> lsRtu = rtuService.realtimeUpdate();
		AlertService alertService = new AlertService();
		alertService.alert(lsRtu);

	}
	@Test
	public void allRealtimeUpdates(){
		List<RealtimeUpdate> u = rtuService.allRealtimeUpdates();
		System.out.println(u);
	}
	private void assertDelayTime(String importFilename, String departHour, String realDepartHour ) throws IOException {
		// make sure http server is started!
	
		
		// todo call trip groups script to load sample data in alert table!!

		importFileFromMockedHttpServer(importFilename);

		List<RealtimeUpdate> lsRtu = rtuService.realtimeUpdate();
		RealtimeUpdate rtuAalterBlankenberge = lsRtu.get(0);
		assertEquals(departHour, rtuAalterBlankenberge.getPlannedDepartureTime());
		assertEquals(realDepartHour, rtuAalterBlankenberge.getRealDepartureTime());
	}

	private void importFileFromMockedHttpServer(String importFilename) {
		// start GtfsRealtime import with specific fileName in data folder
		// from realtime past archive
		//String downloadUrl = String.format("http://127.0.0.1:8080/gtfs-notify/import/realtime/getFile?fileName=%s",
		//		importFilename);
		byte[] b = is.nmbsRealtimeGetFile(importFilename);
		String downloadUrl = testHttpServer.localHttpServerWithSampleFile(b);
		is.callGtfsRealtimeToSql(downloadUrl);
	}


	// Integration test against Firebase servers, run manually for now
	public void testDelay20170927BrusselToAalterAddAlertAndSendFirebaseMessage() {
		// prepare realtime data which has realtime update which matches the defined alert
		importFileFromMockedHttpServer("2017-09-26_17-35-06");
		//delete alert updates processed
		em.getTransaction().begin();

		Query qDeleteProcessed = em.createNamedQuery("Alert.deleteAlertUpdatesProcessed");
		qDeleteProcessed.executeUpdate();
		
		//  ,8813003,8891140,'Bruxelles-Central', 'Aalter', '17:16:00', 
		String stopA = "Bruxelles-Central";
		String stopB ="Aalter";
		String stopIdA = "8813003";
		String stopIdB = "8891140";
		String departureTime = "17:16:00";
		String tokenAndroid ="cAc8pSEsOoPIM9EC-QXJO0:APA91bFI25gRObG1nVkqcHbtIz214IysxWGtR3hjxtVBi8FdKBshAtfXztD4KK-FqpHz8LdaOAJAQH-Kxp-txSkfxsTer7JAtD0orIu8_iD_NXeMQqZoUE3DDCZdh7GPH9z9o5Df4Z-Z";
		String tokenChromeMacPro = "f0PYvAij7-Pe6_iY8bb6L_:APA91bH8b-h-bqjTHgjkZ1XQIU60DJ6ywMjmoAmqd91tSWNLONbgU0Ntk7V6S9jrWOj79cIdb5VT0AoXuFaKQq9136m7MHYN9YER1rC7F0P8hfIJ-ZePX6DWjYz_IcmTqvEBWJ3up1u9";

		
		Alert a = new Alert();
		Alert b = new Alert();
		

		a.setStopIdA(stopIdA);

		a.setStopIdB(stopIdB);
		a.setStopA(stopA);
		a.setStopB(stopB);
		a.setDepartureTime(departureTime);

		a.setFirebaseToken(tokenAndroid);


		b.setStopIdA(stopIdA);

		b.setStopIdB(stopIdB);
		b.setStopA(stopA);
		b.setStopB(stopB);
		b.setDepartureTime(departureTime);

		b.setFirebaseToken(tokenChromeMacPro);

		

		em.persist(a);
		em.persist(b);
		

		em.getTransaction().commit();
	
	
		
		long id = a.getAlertId();

		Query
		 query = em.createNamedQuery("Alert.findById");
		query.setParameter(1, id);
		Alert alertFromDb  = (Alert) query.getSingleResult();
	
		
		assertEquals(alertFromDb.getFirebaseToken(),tokenAndroid);
		
		
		//update alert groups, so realtime update service  can find realtime update based on this trip alert group
		as.updateAlertGroup(stopIdA, stopIdB, departureTime);
		
		// trigger send firebae message
		List<RealtimeUpdate> lsRtu = rtuService.realtimeUpdate();
		as.alert(lsRtu);
		
		//todo: expected in table alerts processed
		
		
	}
	
	static class TestHttpServer {
		
		int port = 8098; // todo randomize not in use

		String httpBase  ="http://127.0.0.1:"+port;

	   HttpServer httpServer;
	   HttpHandler httpH;
	   

			   public TestHttpServer()    {

		    try {
				httpServer  = HttpServer.create(new InetSocketAddress(port), 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
		     
		   
	   }
			   
		public String localHttpServerWithSampleFile(byte[] b)  {
			  
			
	
			String contextPath ="/getFile";
			String downloadUrl = httpBase + contextPath ;
			httpH =new 

				    HttpHandler() {
				@Override
			   public void handle(

					      HttpExchange exchange) throws IOException {
			   
			      exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, b.length);
			      exchange.getResponseBody().write(b);
			      exchange.close();
			   }
			};
			httpServer.createContext(contextPath, httpH);
			httpServer.start();
			return downloadUrl;
		}
		@After
		public void stop() {httpServer.stop(0);}
	}

	
}
