package be.wethinkonline.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import be.wethinkonline.gtfs_notify.service.RealTimeImportService;

public class ImportServiceTest {

	// only to use manually until we are able to mock the realtime HTTP url  String strNmbsUrl = "https://sncb-opendata-url.example.com"
	public void realtimeNmbsDownloadedFileIsLargeEnough() {
		//ImportService.startGtfsRealtimeToSql();
		RealTimeImportService importService = new RealTimeImportService();
		File f = importService.download();
		System.out.println("file l "+ f.length());

		assertEquals(true,f.length() > 50 * 1000 ); // bytes
	}
	@Test
	public void latestDataToDb()
	{
		RealTimeImportService is = new RealTimeImportService();
		is.startGtfsRealtimeToSqlFromLatestRealtimeData();
	}

}
