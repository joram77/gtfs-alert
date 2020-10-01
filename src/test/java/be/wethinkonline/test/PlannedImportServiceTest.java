package be.wethinkonline.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import be.wethinkonline.gtfs_notify.service.RealTimeImportService;
import be.wethinkonline.gtfs_notify.service.PlannedImportService;

public class PlannedImportServiceTest {

	private PlannedImportService importService;
	@Before 
	public void setUp()
	{
		importService = new PlannedImportService();

	}
		// only to use manually until we are able to mock the realtime HTTP url  String strNmbsUrl = "https://sncb-opendata-url.example.com"
	public void plannedNmbsDownloadedFileIsLargeEnough() throws IOException {
		//ImportService.startGtfsRealtimeToSql();
		File f = importService.download();


		assertEquals(true,f.length() > 50 * 1000 ); // bytes
	}
	@Test
	public void updateDatabase() throws IOException {
		importService.update(false);
	}
}
