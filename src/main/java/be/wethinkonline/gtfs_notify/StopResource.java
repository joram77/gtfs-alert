
package be.wethinkonline.gtfs_notify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import be.wethinkonline.gtfs_notify.model.Stop;
import be.wethinkonline.gtfs_notify.service.StopService;
import be.wethinkonline.gtfs_notify.service.TripService;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("stop")
public class StopResource {
	StopService service;

	public StopResource() {

		service = new StopService();

	}


	@Path("/search")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public Response search(@QueryParam("s") String s) {

		return Response.ok(service.stopByName(s)).build();
	}
	
	@Path("/searchName")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public Response searchName(@QueryParam("s") String s) {
		List<Stop> lsStops = service.stopByName(s);
		
		ArrayList<String> lsStopNames = new ArrayList<String>();
		for(Stop st :lsStops) {
			lsStopNames.add(st.getStopName());
		
		}
		return Response.ok(lsStopNames).build();
	}



}
