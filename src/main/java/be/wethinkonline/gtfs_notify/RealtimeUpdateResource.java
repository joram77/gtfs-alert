
package be.wethinkonline.gtfs_notify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import be.wethinkonline.gtfs_notify.service.RealtimeUpdateService;
import be.wethinkonline.gtfs_notify.service.TripService;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("realtime")
public class RealtimeUpdateResource {
	RealtimeUpdateService service;

	public RealtimeUpdateResource() {

		service = new RealtimeUpdateService();

	}


	@Path("/all")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})

	public Response all() {

		return Response.ok(service.allRealtimeUpdates()).build();
	}
	
	@Path("/stopMinDelay")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})

	public Response stopMinDelay(@QueryParam("s") String s,
			@QueryParam("minDepartureDelay") int minDepartureDelay) {

		return Response.ok(service.realtimeUpdateStopMinDelay(s,minDepartureDelay)).build();
	}


}
