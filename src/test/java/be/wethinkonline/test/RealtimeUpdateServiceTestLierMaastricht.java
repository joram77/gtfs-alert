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

public class RealtimeUpdateServiceTestLierMaastricht {
	static EntityManager em;
	static RealtimeUpdateService rtuService = new RealtimeUpdateService();
	static RealTimeImportService is = new RealTimeImportService();
	static AlertService as = new AlertService();
	static TripService ts = new TripService();
	 TestHttpServer testHttpServer;

	@BeforeClass
	public static void createPlannedData() {
		PlannedImportService ps = new PlannedImportService();
		ps.update(new File("/Users/user/Documents/projects/gtfs-alert/sample_data/planned/2020-01-09_09-19-16.zip"));
		
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
	
		tx.begin();
		 query = em.createNamedQuery("AlertUpdatesProcessed.deleteAll");
		query.executeUpdate();
		tx.commit();
		//em.close();

		Alert a = new Alert();
		String stopA ="Liers";
		String stopB =  "Maastricht (NL)";
		String departureTime = "09:14:00";
		a.setStopA(stopA);
		a.setStopB(stopB);
		a.setDepartureTime(departureTime);
		
		as.addAlert(a);


	
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
	public void findTripsToMaastrichtInPlanning() {
		
		// simulate /gtfs-alert/trip/plan?a=Liers&b=Maastricht%20(NL)&date=20200109&time=10
		String a = "Lier";
		String b = "Maastricht (NL)";
		String date = "20200109";
		String time = "09";
		ts.aToBWithStopName(a, b, date, time);
		// select trips.trip_id,trips.trip_headsign,routes.route_long_name,stops.stop_name,stops_b.stop_name as b_stop_name,stops.stop_id, stops_b.stop_id as stop_id_b, stop_times.departure_time,stop_times.arrival_time,stop_times_b.arrival_time as b_arrival_time,stop_times_b.departure_time as b_departure_time,calendar_dates."date" from trips inner join stop_times on stop_times.trip_index = trips.trip_index inner join stop_times stop_times_b on stop_times.trip_index = stop_times_b.trip_index inner join stops on stops.stop_index = stop_times.stop_index inner join stops stops_b on stops_b.stop_index = stop_times_b.stop_index inner join calendar_dates on calendar_dates.service_index = trips.service_index inner join routes on trips.route_index = routes.route_index where  stops.stop_name like ? AND (stops_b.stop_name like ?) AND stop_times.departure_time like ? AND (calendar_dates."date"like ? ) AND stop_times.stop_sequence < stop_times_b.stop_sequence order by trip_id desc;
		
	}
	


	@Test
	public void testDelay20200109LierToMaastricht() throws IOException {
		/* Delay 2017-08-04 17:16:00 -> 17:27:00 Brussel -> Aalter */
		// todo import 2017 planned sample data, now manual

		assertDelayTime("2020-01-09_09-19-46","09:14:00","09:21:00");
		System.out.println("check the planned data yeah");
		
		// bug is in tripids which are not found
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
