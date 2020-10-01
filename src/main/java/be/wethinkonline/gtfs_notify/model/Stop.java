package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the "stops" database table.
 * 
 */
@Entity
@Table(name="\"stops\"")
@NamedQueries({
@NamedQuery(name="Stop.findAll", query="SELECT s FROM Stop s"),
//@NamedQuery(name="Stop.stopByName", query="SELECT s FROM Stop s where s.stopName like '%' || :stopName || '%' ORDER BY s.stopId asc"),

//,
//@NamedQuery(name="Stop.stopForRealtimeDataByName", query="SELECT s FROM Stop s where s.stopName like :stopName")

})

@NamedNativeQueries({
@NamedNativeQuery(name = "Stop.stopByName", query ="SELECT stop_index, stop_id, stop_code, stop_name, stop_desc, zone_index, "
		+ "zone_id, stop_lat, stop_lon, location_type, parent_station, parent_station_index, wheelchair_boarding, stop_url, stop_timezone " + 
		"FROM Stops s where s.stop_name like '%' || ? || '%' AND s.stop_id not like 'S%'"
		+ ""
		+ "AND s.stop_id not like '%\\_%' ESCAPE '\\'"
		+ " ORDER BY s.stop_id asc"
		+ ""
		+ "",resultSetMapping="stop"),

@NamedNativeQuery(name = "Stop.stopBeginningByName", query ="SELECT stop_index, stop_id, stop_code, stop_name, stop_desc, zone_index, "
		+ "zone_id, stop_lat, stop_lon, location_type, parent_station, parent_station_index, wheelchair_boarding, stop_url, stop_timezone " + 
		"FROM Stops s where s.stop_name like ? || '%' AND s.stop_id not like 'S%'"
		+ ""
		+ "AND s.stop_id not like '%\\_%' ESCAPE '\\'"
		+ " ORDER BY s.stop_id asc"
		+ ""
		+ "",resultSetMapping="stop")

})



@SqlResultSetMappings({
@SqlResultSetMapping(name = "stop", classes = {
		@ConstructorResult(targetClass = Stop.class, columns = {
				@ColumnResult(name = "stop_index"),
				@ColumnResult(name = "stop_id"),
				@ColumnResult(name = "stop_code"),
				@ColumnResult(name = "stop_name"),
				@ColumnResult(name = "stop_desc"),
				@ColumnResult(name = "zone_index"),
				@ColumnResult(name = "zone_id"),/*
				@ColumnResult(name = "stop_lat"),
				@ColumnResult(name = "stop_lon"),
				@ColumnResult(name = "location_type"),
				@ColumnResult(name = "parent_station"),
				@ColumnResult(name = "parent_station_index"),
				@ColumnResult(name = "wheelchair_boarding"),
				@ColumnResult(name = "stop_url"),
				@ColumnResult(name = "stop_timezone")*/
				
		})
		
		})
})

public class Stop implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	public Stop( int stop_index, String stop_id, 
			String stop_code, String stop_name, String stop_desc, int zone_index, String zone_id /*, Double stop_lat, 
			Double stop_lon, int location_type, String parent_station, int parent_station_index, int wheelchair_boarding, String stop_url, 
			String stop_timezone*/) {
		
		this.stopIndex = stop_index;
		this.stopId = stop_id;
		this.stopCode = stop_code;
		this.stopName = stop_name;
		this.stopDesc = stop_desc;
		this.zoneIndex = zone_index;
		this.zoneId = zone_id;/*
		this.stopLat =  stop_lat;
		this.stopLon= stop_lon;
		this.locationType = location_type;
		this.parentStation = parent_station;
		this.parentStationIndex = parent_station_index;
		this.wheelchairBoarding = wheelchair_boarding;
		this.stopUrl=stop_url;
		this.stopTimezone=stop_timezone;*/
		
		
		
	}

	@Column(name="\"location_type\"")
	private int locationType;

	@Column(name="\"parent_station\"")
	private String parentStation;

	@Column(name="\"parent_station_index\"")
	private int parentStationIndex;

	@Column(name="\"stop_code\"")
	private String stopCode;

	@Column(name="\"stop_desc\"")
	private String stopDesc;

	@Id
	@Column(name="\"stop_id\"")
	private String stopId;

	@Column(name="\"stop_index\"")
	private int stopIndex;

	@Column(name="\"stop_lat\"")
	private Double stopLat;

	@Column(name="\"stop_lon\"")
	private Double stopLon;

	@Column(name="\"stop_name\"")
	private String stopName;

	@Column(name="\"stop_timezone\"")
	private String stopTimezone;

	@Column(name="\"stop_url\"")
	private String stopUrl;

	@Column(name="\"wheelchair_boarding\"")
	private int wheelchairBoarding;

	@Column(name="\"zone_id\"")
	private String zoneId;

	@Column(name="\"zone_index\"")
	private int zoneIndex;

	public Stop() {
	}

	public int getLocationType() {
		return this.locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}

	public String getParentStation() {
		return this.parentStation;
	}

	public void setParentStation(String parentStation) {
		this.parentStation = parentStation;
	}

	public int getParentStationIndex() {
		return this.parentStationIndex;
	}

	public void setParentStationIndex(int parentStationIndex) {
		this.parentStationIndex = parentStationIndex;
	}

	public String getStopCode() {
		return this.stopCode;
	}

	public void setStopCode(String stopCode) {
		this.stopCode = stopCode;
	}

	public String getStopDesc() {
		return this.stopDesc;
	}

	public void setStopDesc(String stopDesc) {
		this.stopDesc = stopDesc;
	}

	public String getStopId() {
		return this.stopId;
	}

	public void setStopId(String stopId) {
		this.stopId = stopId;
	}

	public int getStopIndex() {
		return this.stopIndex;
	}

	public void setStopIndex(int stopIndex) {
		this.stopIndex = stopIndex;
	}

	public Double getStopLat() {
		return this.stopLat;
	}

	public void setStopLat(Double stopLat) {
		this.stopLat = stopLat;
	}

	public Double getStopLon() {
		return this.stopLon;
	}

	public void setStopLon(Double stopLon) {
		this.stopLon = stopLon;
	}

	public String getStopName() {
		return this.stopName;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public String getStopTimezone() {
		return this.stopTimezone;
	}

	public void setStopTimezone(String stopTimezone) {
		this.stopTimezone = stopTimezone;
	}

	public String getStopUrl() {
		return this.stopUrl;
	}

	public void setStopUrl(String stopUrl) {
		this.stopUrl = stopUrl;
	}

	public int getWheelchairBoarding() {
		return this.wheelchairBoarding;
	}

	public void setWheelchairBoarding(int wheelchairBoarding) {
		this.wheelchairBoarding = wheelchairBoarding;
	}

	public String getZoneId() {
		return this.zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public int getZoneIndex() {
		return this.zoneIndex;
	}

	public void setZoneIndex(int zoneIndex) {
		this.zoneIndex = zoneIndex;
	}

}