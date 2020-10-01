package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "gtfs_rt_trip_updates" database table.
 * 
 */
@Entity
@Table(name="\"gtfs_rt_trip_updates\"")
@NamedQuery(name="GtfsRtTripUpdate.findAll", query="SELECT g FROM GtfsRtTripUpdate g")
public class GtfsRtTripUpdate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="\"recorded\"")
	private int recorded;

	@Column(name="\"route_id\"")
	private String routeId;

	@Column(name="\"trip_date\"")
	private String tripDate;

	@Column(name="\"trip_id\"")
	private String tripId;

	@Column(name="\"trip_sr\"")
	private int tripSr;

	@Column(name="\"trip_time\"")
	private String tripTime;

	@Column(name="\"ts\"")
	private int ts;

	@Id
	@Column(name="\"update_id\"")
	private int updateId;

	@Column(name="\"vehicle_id\"")
	private String vehicleId;

	@Column(name="\"vehicle_label\"")
	private String vehicleLabel;

	@Column(name="\"vehicle_plate\"")
	private String vehiclePlate;

	public GtfsRtTripUpdate() {
	}

	public int getRecorded() {
		return this.recorded;
	}

	public void setRecorded(int recorded) {
		this.recorded = recorded;
	}

	public String getRouteId() {
		return this.routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getTripDate() {
		return this.tripDate;
	}

	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}

	public String getTripId() {
		return this.tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public int getTripSr() {
		return this.tripSr;
	}

	public void setTripSr(int tripSr) {
		this.tripSr = tripSr;
	}

	public String getTripTime() {
		return this.tripTime;
	}

	public void setTripTime(String tripTime) {
		this.tripTime = tripTime;
	}

	public int getTs() {
		return this.ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public int getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(int updateId) {
		this.updateId = updateId;
	}

	public String getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleLabel() {
		return this.vehicleLabel;
	}

	public void setVehicleLabel(String vehicleLabel) {
		this.vehicleLabel = vehicleLabel;
	}

	public String getVehiclePlate() {
		return this.vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

}