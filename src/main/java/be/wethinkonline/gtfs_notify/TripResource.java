
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

import be.wethinkonline.gtfs_notify.service.TripService;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("trip")
public class TripResource {
	TripService tripService;

	public TripResource() {

		tripService = new TripService();

	}


	@Path("/plan")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public Response aToBWithStopNames(@QueryParam("a") String a, @QueryParam("b") String b,
			@QueryParam("date") String date, @QueryParam("time") String time) {

		return Response.ok(tripService.aToBWithStopName(a, b, date, time)).build();
	}

	@Path("/planWithStopIds")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public Response aToBWithStopIds(@QueryParam("a") int aId, @QueryParam("b") int bId, @QueryParam("date") String date,
			@QueryParam("time") String time) {

		return Response.ok(tripService.planWithStopId(aId, bId, date, time)).build();
	}

	@Path("/planWithStopIndex")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})

	public Response aToBWithStopIndex(@QueryParam("a") int aId, @QueryParam("b") int bId,
			@QueryParam("date") String date, @QueryParam("time") String time) {

		return Response.ok(tripService.planWithStopIndex(aId, bId, date, time)).build();
	}

}
