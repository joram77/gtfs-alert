package be.wethinkonline.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import be.wethinkonline.gtfs_notify.model.Stop;
import be.wethinkonline.gtfs_notify.service.StopService;

public class StopServiceTest {
	StopService stopService;

	@Before
	public void setup() {
		stopService = new StopService();
	}
@Test
	public void test() {
		List<Stop> stops = this.stopService.stopByName("aalter");
		assertEquals("8891132",(stops.get(0).getStopId())); // maria-aalter
	}

}
