package be.wethinkonline.gtfs_notify.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY )
@XmlRootElement
public class RealtimeUpdate {
	 String stopName;
	Integer departureDelay;
	String tripHeadsign;
	String plannedDepartureTime;
	String realDepartureTime;
	String plannedArrivalTime;
	String realArrivalTime;
	Integer updateId;
	String stopId;
	String tripId;
	Integer alertTripGroupId;
	String stopBName;
	String tripDate;
	public RealtimeUpdate(String top_name, String stop_b_name ,Integer departure_delay, String trip_headsign, String planned_departure_time,
			String real_departure_time_string, String planned_arrival_time, String real_arrival_time_string,
			Integer update_id, String top_id, String trip_id, Integer alertTripGroupId, String tripDate) {
		super();
		this.stopName = top_name;
		this.stopBName = stop_b_name;
	
		this.departureDelay = departure_delay;
		this.tripHeadsign = trip_headsign;
		this.plannedDepartureTime = planned_departure_time;
		this.realDepartureTime = real_departure_time_string;
		this.plannedArrivalTime = planned_arrival_time;
		this.realArrivalTime = real_arrival_time_string;
		this.updateId = update_id;
		this.stopId = top_id;
		this.tripId = trip_id;
		this.alertTripGroupId = alertTripGroupId;
		this.tripDate = tripDate;
	}
	public RealtimeUpdate( ){}
	public String getTripDate() {
		return tripDate;
	}
	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}
	public String getStopBName() {
		return stopBName;
	}
	public void setStopBName(String stopBName) {
		this.stopBName = stopBName;
	}
	public Integer getAlertTripGroupId() {
		return alertTripGroupId;
	}
	public void setAlertTripGroupId(Integer alertTripGroupId) {
		this.alertTripGroupId = alertTripGroupId;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public Integer getDepartureDelay() {
		return departureDelay;
	}
	public Integer getDepartureDelayInMin() {
		return departureDelay / 60;
	}
	public void setDepartureDelay(Integer departureDelay) {
		this.departureDelay = departureDelay;
	}
	public String getTripHeadsign() {
		return tripHeadsign;
	}
	public void setTripHeadsign(String tripHeadsign) {
		this.tripHeadsign = tripHeadsign;
	}
	public String getPlannedDepartureTime() {
		return plannedDepartureTime;
	}
	public void setPlannedDepartureTime(String plannedDepartureTime) {
		this.plannedDepartureTime = plannedDepartureTime;
	}
	public String getRealDepartureTime() {
		return realDepartureTime;
	}
	public void setRealDepartureTime(String realDepartureTime) {
		this.realDepartureTime = realDepartureTime;
	}
	public String getPlannedArrivalTime() {
		return plannedArrivalTime;
	}
	public void setPlannedArrivalTime(String plannedArrivalTime) {
		this.plannedArrivalTime = plannedArrivalTime;
	}
	public String getRealArrivalTime() {
		return realArrivalTime;
	}
	public void setRealArrivalTime(String realArrivalTime) {
		this.realArrivalTime = realArrivalTime;
	}
	public Integer getUpdateId() {
		return updateId;
	}
	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}
	public String getStopId() {
		return stopId;
	}
	public void setStopId(String stopId) {
		this.stopId = stopId;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	
	
	
}
