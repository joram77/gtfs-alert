package be.wethinkonline.gtfs_notify;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import be.wethinkonline.gtfs_notify.model.Alert;
import be.wethinkonline.gtfs_notify.service.AlertService;

@Path("alert")
public class AlertResource {
	AlertService alertService;

	public AlertResource() {

		alertService = new AlertService();

	}

	@Path("/add")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public Response add(@QueryParam("email") String email, @QueryParam("a") String a, @QueryParam("b") String b
		, @QueryParam("departureTime") String departureTime,
			@QueryParam("arrivalTime") String arrivalTime, @QueryParam("firebaseToken") String firebaseToken) {
		Alert alert = new Alert();
		alert.setStopA(a);
		alert.setStopB(b);
		alert.setE_mail(email);
		alert.setDepartureTime(departureTime);
		alert.setArrivalTime(arrivalTime);
		alert.setFirebaseToken(firebaseToken);
		return Response.ok(alertService.addAlert(alert)).build();
	}

	@Path("/updateAlertGroups")
	@GET
	@Produces({
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON
	})
	public Response updateAlertGroups() {
		alertService.updateAlertGroups();
		return Response.ok().build();
	}
}