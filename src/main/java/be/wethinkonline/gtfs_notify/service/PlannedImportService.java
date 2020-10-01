package be.wethinkonline.gtfs_notify.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.transitfeeds.gtfs.GtfsParser;

import be.wethinkonline.util.ZipUtil;

/**
 * Imports planned data (trip schedules etc) from GTFS format file into sql
 * database tables
 * - Necessary to refresh trip.tripids. Realtime data references these tripids.
 * - Add extra stops in stop table
 * - Update changed planned stop_times table.
 * 
 * 
 * Update frequency: few times a month? 
 * - Depending on agency chainging schedules
 * - Might be good to download planned date nightly.
 * - Do diff check and create symlink if not changed (todo);,
 * 
 * 
 * Agency nmbs
 * ----
* Wat de planned data betreft, 5x per week is er een nieuwe plandata, meestal rond 6u ’s ochtends. Het is mogelijk dat bij fouten of speciale momenten er een tussentijdse upload gebeurt of op een later moment.
* Het beste is natuurlijk dat er bv. elk uur gecontroleerd wordt of er een nieuwe versie is.

 * 
 * @author user
 *
 */
public class PlannedImportService {
	private static final Logger log = Logger.getLogger(PlannedImportService.class.getName());
	String strPlannedUrl = "https://sncb-opendata-personalized-url.example.com";
	private String plannedFilePath = "/Users/user/Documents/projects/gtfs-alert/sample_data/planned";
	ZipUtil zip = new ZipUtil();

	public PlannedImportService() {
	}

	public File download() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String path = plannedFilePath + File.separator + dateFormat.format(new Date())+".zip";
		return RealTimeImportService.httpSaveFile(path, this.strPlannedUrl);

	}
	
	public void update(boolean compareLatest ) throws IOException{
		//todo bugfix: if previous zip is null, do not use otherwise nullpointer exception
		
		File previousPlannedZip = RealTimeImportService.lastFileModified(plannedFilePath);
		File latest = download();
		String hashLatestZip;
		String hashPreviousPlannedZip;
		try (InputStream is = Files.newInputStream(latest.toPath())) {
		     hashLatestZip = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
		}
		try (InputStream is = Files.newInputStream(previousPlannedZip.toPath())) {
		     hashPreviousPlannedZip = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
		}
		
		if(latest.length()==0) {
			log.info("Not importing zip file! "+latest +" Because its size is 0. Deleting empty file.");
			 latest.delete();
		}
		else if(compareLatest && hashPreviousPlannedZip.equals(hashLatestZip) )
		{
			log.info("Not importing zip file! "+latest +" Because its contents are equal to previous planned data download " + previousPlannedZip);
			log.info("Deleting latest downloaded file and hard linking to identical previous copy " + previousPlannedZip);
			boolean success = latest.delete();
			
			Files.createLink(latest.toPath(), previousPlannedZip.toPath());
		
		}
		else {
			log.info("Importing  zip file with latest planned data "+latest+"... Because its contents differ from previous planned data download " + previousPlannedZip);;
			update(latest);
			//always recalculate stop indexes in alert table, they might ahave changed from latest plaanned downlaod
			AlertService is  = new AlertService();
			is.updateAlertGroups(); //todo recalc stop indexes in alert table in step before 1
		}
	}

	public void update(File gtfsZipFile) {
		File unzipDir = new File(gtfsZipFile.getAbsolutePath().replace(".zip","/"));
		zip.unzipArchive(gtfsZipFile, unzipDir);

		String connStr = RealTimeImportService.sqliteString;

		if (connStr.startsWith("jdbc:sqlite:")) {
			// may not work without this call
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(connStr);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		GtfsParser gtfs = null;
		if (connection != null) {
			try {
				gtfs = new GtfsParser(unzipDir, connection);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// String[] exclude = line.getOptionValues("e");
		//
		// if (exclude != null) {
		// for (int i = 0; i < exclude.length; i++) {
		// gtfs.exclude(exclude[i]);
		// }
		// }

		try {
			gtfs.parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if (line.hasOption("o")) {
		// GtfsOptimizer optimizer = new GtfsOptimizer(connection);
		// optimizer.optimize();
		// }
	}
}
