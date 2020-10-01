package be.wethinkonline.gtfs_notify.model;

public class StopTimesAToBResult {
	private String tripId;
	private String tripHeadsign;
	private String routeLongName;
	private String stopName;
	private String stopBName;
	private String stopId;
	private String stopIdB;
	private String stopTimesDeparture_time;
	private String stopTimesArrivalTime;
	private String stopTimesBArrivalTime;
	private String stopTimesBDepartureTime;
	private String calendarDate; 

	
	public String getCalendarDate() {
		return calendarDate;
	}
	public void setCalendarDate(String calendarDate) {
		this.calendarDate = calendarDate;
	}
	public StopTimesAToBResult(String tripId, String tripHeadsign, String routeLongName, String stopName,
			String stopBName, String stopId, String stopIdB, String stopTimesDeparture_time, String stopTimesArrivalTime,
			String stopTimesBArrivalTime, String stopTimesBDepartureTime, String calendarDate) {
		super();
		this.tripId = tripId;
		this.tripHeadsign = tripHeadsign;
		this.routeLongName = routeLongName;
		this.stopName = stopName;
		this.stopBName = stopBName;
		this.stopId = stopId;
		this.stopIdB = stopIdB;
		this.stopTimesDeparture_time = stopTimesDeparture_time;
		this.stopTimesArrivalTime = stopTimesArrivalTime;
		this.stopTimesBArrivalTime = stopTimesBArrivalTime;
		this.stopTimesBDepartureTime = stopTimesBDepartureTime;
		this.calendarDate = calendarDate;
	}
	public String getRouteLongName() {
		return routeLongName;
	}
	public void setRouteLongName(String routeLongName) {
		this.routeLongName = routeLongName;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getStopBName() {
		return stopBName;
	}
	public void setStopBName(String stopBName) {
		this.stopBName = stopBName;
	}
	public String getStopTimesDeparture_time() {
		return stopTimesDeparture_time;
	}
	public void setStopTimesDeparture_time(String stopTimesDeparture_time) {
		this.stopTimesDeparture_time = stopTimesDeparture_time;
	}
	public String getStopTimesArrivalTime() {
		return stopTimesArrivalTime;
	}
	public void setStopTimesArrivalTime(String stopTimesArrivalTime) {
		this.stopTimesArrivalTime = stopTimesArrivalTime;
	}
	public String getStopTimesBArrivalTime() {
		return stopTimesBArrivalTime;
	}
	public void setStopTimesBArrivalTime(String stopTimesBArrivalTime) {
		this.stopTimesBArrivalTime = stopTimesBArrivalTime;
	}
	public String getStopTimesBDepartureTime() {
		return stopTimesBDepartureTime;
	}
	public void setStopTimesBDepartureTime(String stopTimesBDepartureTime) {
		this.stopTimesBDepartureTime = stopTimesBDepartureTime;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public String getTripHeadsign() {
		return tripHeadsign;
	}
	public void setTripHeadsign(String tripHeadsign) {
		this.tripHeadsign = tripHeadsign;
	}
	public String getStopid() {
		return stopId;
	}
	public void setStopid(String stopid) {
		this.stopId = stopid;
	}
	public String getStopIdB() {
		return stopIdB;
	}
	public void setStopIdB(String stopid_b) {
		this.stopIdB = stopid_b;
	}
}
