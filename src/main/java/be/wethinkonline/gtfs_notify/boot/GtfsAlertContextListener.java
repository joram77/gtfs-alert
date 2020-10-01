package be.wethinkonline.gtfs_notify.boot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import be.wethinkonline.gtfs_notify.model.RealtimeUpdate;
import be.wethinkonline.gtfs_notify.service.AlertService;
import be.wethinkonline.gtfs_notify.service.PlannedImportService;
import be.wethinkonline.gtfs_notify.service.RealTimeImportService;
import be.wethinkonline.gtfs_notify.service.RealtimeUpdateService;

public class GtfsAlertContextListener implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(GtfsAlertContextListener.class.getName());
	private ScheduledExecutorService executor;
	private ThreadFactory daemonFactory;
	int realtimeUpdateThreadIntervalSeconds = 30;
	int plannedUpdateThreadIntervalSeconds = 3600;
	boolean flagPlannedFirstRunDone = false;
	boolean flagPlanJobRunning = false;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		executor.shutdownNow();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * APPLICATION CONFIG to adapt before deployment
		 * 
		 * !!!!! todo config from ser vletcontext context xml !!!!!
		 */
		String mailServerPort = "25";
		AlertService alertService = new AlertService();
		alertService.setPort(mailServerPort);

		// Import relatime data & check for alerts every 30s
		daemonFactory = new DaemonThreadFactory();
		scheduleUpdatePlannedData();
		scheduleUpdateRealtimeData(alertService);

	}

	private void scheduleUpdateRealtimeData(AlertService alertService) {
		RealTimeImportService is = new RealTimeImportService();

		executor = Executors.newSingleThreadScheduledExecutor(daemonFactory);
		Runnable myJob = () -> {
			// only download realtime data once first planned run is done
			if (flagPlannedFirstRunDone == true && flagPlanJobRunning == false) {

				try {
					LOGGER.info("Starting scheduled GtfsRealtimeToSql import (1/2) ...");
					is.startGtfsRealtimeToSqlFromLatestRealtimeData();
					LOGGER.info("Starting scheduled checking for realtime alerts (2/2) ...");
					RealtimeUpdateService rtuService = new RealtimeUpdateService();
					List<RealtimeUpdate> lsRtu = rtuService.realtimeUpdate();
					LOGGER.info(lsRtu.size() + " alerts found");
					alertService.alert(lsRtu);
				} catch (Throwable e) {
					LOGGER.log(Level.WARNING, "Scheduled realtime data import job error ", e);
				}
			}
		};
		executor.scheduleAtFixedRate(myJob, 0, realtimeUpdateThreadIntervalSeconds, TimeUnit.SECONDS);
	}

	/*
	 * Wat de planned NMBS data betreft, 5x per week is er een nieuwe plandata,
	 * meestal rond 6u ’s ochtends. Het is mogelijk dat bij fouten of speciale
	 * momenten er een tussentijdse upload gebeurt of op een later moment. Het
	 * beste is natuurlijk dat er bv. elk uur gecontroleerd wordt of er een
	 * nieuwe versie is. /
	 */
	private void scheduleUpdatePlannedData()  {
		PlannedImportService ps = new PlannedImportService();

		executor = Executors.newSingleThreadScheduledExecutor(daemonFactory);
		Runnable myJob = () -> {
			try {
				flagPlanJobRunning=true;
				LOGGER.info("Starting planned GtfsToSql import ...");
				if(flagPlannedFirstRunDone==false) ps.update(false);//on startup, alwaysn reimport planned data regardless of difference with latest planned data
				else ps.update(true);
				flagPlannedFirstRunDone = true;
				flagPlanJobRunning=false;
				}
			
			catch(Throwable e){
				LOGGER.log(Level.WARNING,"Scheduled planned data import job error ",e);
			}
		};

	executor.scheduleAtFixedRate(myJob,0,plannedUpdateThreadIntervalSeconds,TimeUnit.SECONDS);
}

}
