package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the "stop_times" database table.
 * 
 */
@Entity
@Table(name="\"stop_times\"")
@NamedQuery(name="StopTime.findAll", query="SELECT s FROM StopTime s")
public class StopTime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="\"arrival_time\"")
	private String arrivalTime;

	@Column(name="\"arrival_time_secs\"")
	private int arrivalTimeSecs;

	@Column(name="\"departure_time\"")
	private String departureTime;

	@Column(name="\"departure_time_secs\"")
	private int departureTimeSecs;

	@Column(name="\"drop_off_type\"")
	private int dropOffType;

	@Column(name="\"last_stop\"")
	private int lastStop;

	@Column(name="\"pickup_type\"")
	private int pickupType;

	@Column(name="\"shape_dist_traveled\"")
	private BigDecimal shapeDistTraveled;

	@Column(name="\"stop_headsign\"")
	private String stopHeadsign;

	@Column(name="\"stop_index\"")
	@Id //todo not really a PK,but fine for reads
	private int stopIndex;

	@Column(name="\"stop_sequence\"")
	private int stopSequence;

	@Column(name="\"trip_index\"")
	private int tripIndex;

	public StopTime() {
	}

	public String getArrivalTime() {
		return this.arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getArrivalTimeSecs() {
		return this.arrivalTimeSecs;
	}

	public void setArrivalTimeSecs(int arrivalTimeSecs) {
		this.arrivalTimeSecs = arrivalTimeSecs;
	}

	public String getDepartureTime() {
		return this.departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public int getDepartureTimeSecs() {
		return this.departureTimeSecs;
	}

	public void setDepartureTimeSecs(int departureTimeSecs) {
		this.departureTimeSecs = departureTimeSecs;
	}

	public int getDropOffType() {
		return this.dropOffType;
	}

	public void setDropOffType(int dropOffType) {
		this.dropOffType = dropOffType;
	}

	public int getLastStop() {
		return this.lastStop;
	}

	public void setLastStop(int lastStop) {
		this.lastStop = lastStop;
	}

	public int getPickupType() {
		return this.pickupType;
	}

	public void setPickupType(int pickupType) {
		this.pickupType = pickupType;
	}

	public BigDecimal getShapeDistTraveled() {
		return this.shapeDistTraveled;
	}

	public void setShapeDistTraveled(BigDecimal shapeDistTraveled) {
		this.shapeDistTraveled = shapeDistTraveled;
	}

	public String getStopHeadsign() {
		return this.stopHeadsign;
	}

	public void setStopHeadsign(String stopHeadsign) {
		this.stopHeadsign = stopHeadsign;
	}

	public int getStopIndex() {
		return this.stopIndex;
	}

	public void setStopIndex(int stopIndex) {
		this.stopIndex = stopIndex;
	}

	public int getStopSequence() {
		return this.stopSequence;
	}

	public void setStopSequence(int stopSequence) {
		this.stopSequence = stopSequence;
	}

	public int getTripIndex() {
		return this.tripIndex;
	}

	public void setTripIndex(int tripIndex) {
		this.tripIndex = tripIndex;
	}

}