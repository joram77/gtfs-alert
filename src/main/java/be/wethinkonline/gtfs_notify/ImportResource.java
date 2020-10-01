
package be.wethinkonline.gtfs_notify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;

import be.wethinkonline.gtfs_notify.service.PlannedImportService;
import be.wethinkonline.gtfs_notify.service.RealTimeImportService;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("import")
public class ImportResource { 
	RealTimeImportService importService;
	PlannedImportService plannedImportService;
	public ImportResource() {
		
		importService= new RealTimeImportService();
		plannedImportService = new PlannedImportService();
	}
	@Path("/planned")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	// Import latest planned data in gts sql database
	public Response nmbsPlanned() 
	{
		 try {
			plannedImportService.update(false);
		} catch (IOException e) {
			return Response.status(500).entity("IO error while importing planned data ").build();
		}
		 return Response.ok().build();
		 
	}

	@Path("/planned/download")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response nmbsPlannedDownload() {
		String strNmbsUrl = "https://sncb-opendata-url.example.com";
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		URL url = null;
		byte[] imageBytes = new byte[0];
		try {
			url = new URL(strNmbsUrl);
			is = url.openStream();
			imageBytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
			e.printStackTrace();
			// Perform any other exception handling that's appropriate.
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	    return Response.ok(new ByteArrayInputStream(imageBytes)).build();
	    }

	@Path("/realtime")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response startGtfsImportScheduler() {
		importService.startGtfsRealtimeToSqlFromLatestRealtimeData();
	    return Response.ok().build();
	    }
	
	@Path("/realtime/download")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response nmbsRealtimeDownload() {
		importService.download();
	    return Response.ok(new ByteArrayInputStream(importService.nmbsRealtimeGetFile(null))).build();
	    }
	
	@Path("/realtime/getFile")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response nmbsRealtimeGetFile(@QueryParam("fileName") String fileName) {

	    if(fileName!=null) return Response.ok(new ByteArrayInputStream(importService.nmbsRealtimeGetFile(fileName))).build();
	    return Response.noContent().build();
	}
 
 
	}

