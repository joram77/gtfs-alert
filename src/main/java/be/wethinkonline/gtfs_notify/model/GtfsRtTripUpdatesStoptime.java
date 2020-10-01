package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "gtfs_rt_trip_updates_stoptimes" database table.
 * 
 */
@Entity
@Table(name="\"gtfs_rt_trip_updates_stoptimes\"")
@NamedQuery(name="GtfsRtTripUpdatesStoptime.findAll", query="SELECT g FROM GtfsRtTripUpdatesStoptime g")
public class GtfsRtTripUpdatesStoptime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="\"arrival_delay\"")
	private int arrivalDelay;

	@Column(name="\"arrival_time\"")
	private int arrivalTime;

	@Column(name="\"arrival_uncertainty\"")
	private int arrivalUncertainty;

	@Column(name="\"departure_delay\"")
	private int departureDelay;

	@Column(name="\"departure_time\"")
	private int departureTime;

	@Column(name="\"departure_uncertainty\"")
	private int departureUncertainty;

	@Column(name="\"rship\"")
	private int rship;

	@Column(name="\"stop_id\"")
	private String stopId;

	@Column(name="\"stop_sequence\"")
	private int stopSequence;

	@Id //todo not really a PK,but fine for reads
	@Column(name="\"update_id\"")
	private int updateId;

	public GtfsRtTripUpdatesStoptime() {
	}

	public int getArrivalDelay() {
		return this.arrivalDelay;
	}

	public void setArrivalDelay(int arrivalDelay) {
		this.arrivalDelay = arrivalDelay;
	}

	public int getArrivalTime() {
		return this.arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getArrivalUncertainty() {
		return this.arrivalUncertainty;
	}

	public void setArrivalUncertainty(int arrivalUncertainty) {
		this.arrivalUncertainty = arrivalUncertainty;
	}

	public int getDepartureDelay() {
		return this.departureDelay;
	}

	public void setDepartureDelay(int departureDelay) {
		this.departureDelay = departureDelay;
	}

	public int getDepartureTime() {
		return this.departureTime;
	}

	public void setDepartureTime(int departureTime) {
		this.departureTime = departureTime;
	}

	public int getDepartureUncertainty() {
		return this.departureUncertainty;
	}

	public void setDepartureUncertainty(int departureUncertainty) {
		this.departureUncertainty = departureUncertainty;
	}

	public int getRship() {
		return this.rship;
	}

	public void setRship(int rship) {
		this.rship = rship;
	}

	public String getStopId() {
		return this.stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public int getStopSequence() {
		return this.stopSequence;
	}

	public void setStopSequence(int stopSequence) {
		this.stopSequence = stopSequence;
	}

	public int getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(int updateId) {
		this.updateId = updateId;
	}

}