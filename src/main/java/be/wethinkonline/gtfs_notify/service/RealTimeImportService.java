package be.wethinkonline.gtfs_notify.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import com.transitfeeds.gtfsrealtimetosql.GtfsRealTimeFeed;
import com.transitfeeds.gtfsrealtimetosql.GtfsRealTimeSqlRecorder;
import com.transitfeeds.gtfsrealtimetosql.GtfsRealTimeToSql;

public class RealTimeImportService {
	private static final Logger LOGGER = Logger.getLogger(RealTimeImportService.class.getName());
	String realtimeDir = "/Users/user/Documents/projects/gtfs-alert/sample_data/realtime";
	public static  String sqliteString = "jdbc:sqlite:/Users/user/Documents/projects/gtfs-alert/db/gtfs_alert.sqlite";
	// GtfsRealtimeToSql has issues https connections, thus we proxy the https
	// connection to nmbs hrough our webapp
	 String localHttpProxyDownloadNmbsUrl = "http://127.0.0.1:8080/gtfs-alert/import/realtime/download";
	 String strNmbsUrl = "https://sncb-opendata-url.example.com";

	public String getLocalHttpProxyDownloadNmbsUrl() {
		return localHttpProxyDownloadNmbsUrl;
	}

	public String getRealtimeDir() {
		return realtimeDir;
	}

	public void setRealtimeDir(String realtimeDir) {
		this.realtimeDir = realtimeDir;
	}

	public File download() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String realtimeFilePath = realtimeDir + File.separator + dateFormat.format(new Date());
		File fl = httpSaveFile(realtimeFilePath, strNmbsUrl);
		return fl;
	}

	public byte[] nmbsRealtimeGetFile(String fileName) {

		InputStream is = null;

		byte[] imageBytes = new byte[0];
		String filePath;
		if (fileName == null) {
			filePath = lastFileModified(realtimeDir).getAbsolutePath();
		} else {
			filePath = realtimeDir + File.separator + fileName;
		}
		LOGGER.info("Getting file from fs " + filePath);

		try {
			is = new FileInputStream(filePath);
			imageBytes = IOUtils.toByteArray(is);
		} catch (IOException e) {

			LOGGER.log(Level.WARNING,
					"error " + String.format("Failed while reading bytes from %s: %s", filePath, e.getMessage()));
			// Perform any other exception handling that's appropriate.
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					LOGGER.log(Level.WARNING, "error ", e);
				}
			}
		}
		return imageBytes;
	}

	@Deprecated
	public void callGtfsRealtimeToSqlThreads(String... argsGiven) {
		
		String[] args = new String[(4 + argsGiven.length)];		
		args[0] = "-u";
		args[1] = this.localHttpProxyDownloadNmbsUrl;
		args[2] = "-s";
		args[3] = this.sqliteString;

		//System.arraycopy(argsGiven, 0, args, 2, argsGiven.length);

		LOGGER.info("Starging GtfsRealtimeToSql threads ");

		try {
			System.err.println("realtime import");
			GtfsRealTimeToSql.main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.WARNING, "error ", e);
		}
	}

	public void callGtfsRealtimeToSql(String downloadUrl) {
		Connection connection = null;
		GtfsRealTimeSqlRecorder recorder = null;

		List<Handler> mHandlers = new ArrayList<Handler>();

		String mConnectionStr = sqliteString;
		URI uri = null;
		try {
			uri = new URI(downloadUrl);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			try {
				if (mConnectionStr.startsWith("jdbc:sqlite:")) {
					Class.forName("org.sqlite.JDBC");
				} else if (mConnectionStr.startsWith("jdbc:postgresql:")) {
					Class.forName("org.postgresql.Driver");
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			Logger mLogger = LOGGER;
			mLogger.setLevel(Level.FINEST);

			for (Handler handler : mHandlers) {
				mLogger.addHandler(handler);
			}

			List<GtfsRealTimeFeed> mFeeds = new ArrayList<GtfsRealTimeFeed>();

			GtfsRealTimeFeed feed = new GtfsRealTimeFeed(uri);
			feed.setOutputHeaders(false);
			feed.setCredentials(null, null);
			mFeeds.add(feed);
			for (GtfsRealTimeFeed f : mFeeds) {
				f.setLogger(mLogger);
			}

			try {
				if (connection == null) {
					mLogger.info(String.format("Connecting to database: %s", mConnectionStr));
					connection = DriverManager.getConnection(mConnectionStr, null, null);
					recorder = new GtfsRealTimeSqlRecorder(mLogger, connection);
					recorder.startup();
				}

				recorder.begin();

				for (GtfsRealTimeFeed fd : mFeeds) {
					try {
						fd.load();
					} catch (Exception e) {
						mLogger.info(getString(e));
						continue;
					}

					try {
						recorder.record(fd.getFeedMessage());
					} catch (Exception e) {
						mLogger.info(getString(e));
					}
				}

				recorder.commit();
			} catch (SQLException se) {
				mLogger.info(getString(se));
			}

			recorder.shutdown();

			//connection.close();

			for (Handler handler : mHandlers) {
				mLogger.removeHandler(handler);
				handler.close();
			}

			for (GtfsRealTimeFeed fx : mFeeds) {
				fx.setLogger(null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.WARNING, "error ", e);
		}

	}

	/**
	 * Donwload latest nmbs file and import with gtfs-realtime-tosql tool
	 */
	public void startGtfsRealtimeToSqlFromLatestRealtimeData() {
		String downloadUrl;

		downloadUrl = localHttpProxyDownloadNmbsUrl;
		// start GtfsRealtime import with latest realtime file from nmbs servers

		callGtfsRealtimeToSql(downloadUrl);
	}

	public void startGtfsRealtimeToSqlFromFile(String fileName) {
		String downloadUrl;
		if (fileName != null) {

			// start GtfsRealtime import with specific fileName in data folder
			// from realtime past archive
			downloadUrl = String.format("http://127.0.0.1:8080/gtfs-alert/import/realtime/getFile?fileName=%s",
					fileName);
			callGtfsRealtimeToSql(downloadUrl);
		}
	}

	public static File httpSaveFile(String realtimeFilePath, String strUrl) {
		File fl = new File(realtimeFilePath);
		StringBuffer sb = new StringBuffer();
		URL url = null;
		byte[] imageBytes = new byte[0];
		LOGGER.info("Downloading " + strUrl + "...");
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try (InputStream is = url.openStream()){
			
			imageBytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Failed while reading bytes from "+ strUrl);
		} 
		
		// Write file
		try {
			FileOutputStream fos = new FileOutputStream(fl);
			fos.write(imageBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.log(Level.WARNING, "error ", e);
		}
		return fl;
	}

	public static File lastFileModified(String dir) {
		File fl = new File(dir);
		File[] files = fl.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isFile();
			}
		});
		long lastMod = Long.MIN_VALUE;
		File choice = null;
		for (File file : files) {
			if (file.lastModified() > lastMod) {
				choice = file;
				lastMod = file.lastModified();
			}
		}
		return choice;
	}

	private static String getString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		return sw.toString();
	}
}
