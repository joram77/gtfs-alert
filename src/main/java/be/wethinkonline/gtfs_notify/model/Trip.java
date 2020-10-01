package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the "trips" database table.
 * 
 */
// todo make proper int cnversion for trip_index and ,calender_dates.\"date\" in
// StopTimesAToBResult
@Entity
@Table(name = "\"trips\"")
@NamedQuery(name = "Trip.findAll", query = "SELECT t FROM Trip t")
@NamedNativeQueries({
		@NamedNativeQuery(name = "tripPlan", query = "select trips.trip_id,trips.trip_headsign,routes.route_long_name,stops.stop_name,stops_b.stop_name as b_stop_name,stops.stop_id, stops_b.stop_id as stop_id_b, "
				+ "stop_times.departure_time,stop_times.arrival_time,stop_times_b.arrival_time as b_arrival_time,stop_times_b.departure_time as b_departure_time,calendar_dates.\"date\" "
				+ "from trips " + "inner join stop_times on stop_times.trip_index = trips.trip_index "
				+ "inner join stop_times stop_times_b on stop_times.trip_index = stop_times_b.trip_index "
				+ "inner join stops on stops.stop_index = stop_times.stop_index "
				+ "inner join stops stops_b on stops_b.stop_index = stop_times_b.stop_index "
				+ "inner join calendar_dates on calendar_dates.service_index = trips.service_index "
				+ "inner join routes on trips.route_index = routes.route_index "
				+ "where  stops.stop_name like ? AND (stops_b.stop_name like ?) "
				+ "AND stop_times.departure_time like ? AND (calendar_dates.\"date\"like ? ) "
				+ "AND stop_times.stop_sequence < stop_times_b.stop_sequence "
				+ "order by trip_id desc;", resultSetMapping = "stopTimesAToBResult"),

		@NamedNativeQuery(name = "tripPlanStopId", query = "select trips.trip_id,trips.trip_headsign,routes.route_long_name,stops.stop_name,stops_b.stop_name as b_stop_name, stops.stop_id, stops_b.stop_id as stop_id_b, "
				+ "stop_times.departure_time,stop_times.arrival_time,stop_times_b.arrival_time as b_arrival_time,stop_times_b.departure_time as b_departure_time,calendar_dates.\"date\" "
				+ "from trips " + "inner join stop_times on stop_times.trip_index = trips.trip_index "
				+ "inner join stop_times stop_times_b on stop_times.trip_index = stop_times_b.trip_index "
				+ "inner join stops on stops.stop_index = stop_times.stop_index "
				+ "inner join stops stops_b on stops_b.stop_index = stop_times_b.stop_index "
				+ "inner join calendar_dates on calendar_dates.service_index = trips.service_index "
				+ "inner join routes on trips.route_index = routes.route_index "
				+ "where  stops.stop_id = ? AND (stops_b.stop_id = ?) "
				+ "AND stop_times.departure_time like ? AND (calendar_dates.\"date\"like ? ) "
				+ "AND stop_times.stop_sequence < stop_times_b.stop_sequence "
				+ "order by trip_id desc;", resultSetMapping = "stopTimesAToBResult"),

		@NamedNativeQuery(name = "tripPlanStopIndex", query = "select trips.trip_id,trips.trip_headsign,routes.route_long_name,stops.stop_name,stops_b.stop_name as b_stop_name, stops.stop_id, stops_b.stop_id as stop_id_b, "
				+ "stop_times.departure_time,stop_times.arrival_time,stop_times_b.arrival_time as b_arrival_time,stop_times_b.departure_time as b_departure_time,"
				+ "calendar_dates.\"date\" " + "from trips "
				+ "inner join stop_times on stop_times.trip_index = trips.trip_index "
				+ "inner join stop_times stop_times_b on stop_times.trip_index = stop_times_b.trip_index "
				+ "inner join stops on stops.stop_index = stop_times.stop_index "
				+ "inner join stops stops_b on stops_b.stop_index = stop_times_b.stop_index "
				+ "inner join calendar_dates on calendar_dates.service_index = trips.service_index "
				+ "inner join routes on trips.route_index = routes.route_index "
				+ "where  stops.stop_index = ? AND (stops_b.stop_index = ?) "
				+ "AND stop_times.departure_time like ? AND (calendar_dates.\"date\"like ? ) "
				+ "AND stop_times.stop_sequence < stop_times_b.stop_sequence "
				+ "order by trip_id desc;", resultSetMapping = "stopTimesAToBResult"),

		@NamedNativeQuery(name = "trip", query = "select trips.trip_id,trips.trip_headsign,routes.route_long_name,stops.stop_name,stops_b.stop_name as b_stop_name, stops.stop_id, stops_b.stop_id as stop_id_b, "
				+ "stop_times.departure_time,stop_times.arrival_time,stop_times_b.arrival_time as b_arrival_time,stop_times_b.departure_time as b_departure_time,calendar_dates.\"date\" "
				+ "from trips " + "inner join stop_times on stop_times.trip_index = trips.trip_index "
				+ "inner join stop_times stop_times_b on stop_times.trip_index = stop_times_b.trip_index "
				+ "inner join stops on stops.stop_index = stop_times.stop_index "
				+ "inner join stops stops_b on stops_b.stop_index = stop_times_b.stop_index "
				+ "inner join calendar_dates on calendar_dates.service_index = trips.service_index "
				+ "inner join routes on trips.route_index = routes.route_index "
				+ "where  stops.stop_index = ? AND (stops_b.stop_index = ?) " + "AND stop_times.departure_time like ?  "
				+ "AND stop_times.stop_sequence < stop_times_b.stop_sequence "
				+ "order by trip_id desc;", resultSetMapping = "trip"),
		// group bytrip_group_id and maxtu.update_id and max(trip_id)) because
		// of
		// split trains have multiple trip ids and alerts , we only need one
		// alert per trip_group_id(eg genk-brussel-blankenberge split in bruges)
		@NamedNativeQuery(name = "realtime_update_unprocessed", query = "select s.stop_name,sa.stop_name as stop_b_name,departure_delay,trip_headsign,"
				+ "(select departure_time from stop_times where stop_times.stop_index = s.stop_index AND trip_index = t.trip_index)  as planned_departure_time ,"
				+ "time(cast(stu.departure_time as varchar), 'unixepoch', 'localtime') as real_departure_time_string "
				+ ",(select arrival_time from stop_times where stop_times.stop_index = s.stop_index AND trip_index = t.trip_index)  as planned_arrival_time "
				+ ",datetime(cast(stu.arrival_time as varchar), 'unixepoch', 'localtime') as real_arrival_time_string ,"
				+ " max(tu.update_id) as update_id,s.stop_id,max(t.trip_id) as trip_id,alert_trip_group.alert_trip_group_id,trip_date "
				+ "from alert_trips " + "inner join gtfs_rt_trip_updates tu on tu.trip_id  = alert_trips.trip_id "
				+ "inner join gtfs_rt_trip_updates_stoptimes stu on tu.update_id   = stu.update_id "
				+ "inner join trips t on tu.trip_id = t.trip_id " + "inner join stops s on stu.stop_id = s.stop_id "
				+ "inner join alert_trip_group on alert_trip_group.alert_trip_group_id = alert_trips.trips_to_alert_group_id  "
				+ " inner join stops sa on alert_trip_group.stop_index_b = sa.stop_index  "
				+ " where stu.stop_id = alert_trip_group.stop_id_a 	"
				+ " AND NOT EXISTS (SELECT processed_id FROM alert_updates_processed ap WHERE update_id = (s.stop_name||planned_departure_time||real_departure_time_string)  AND trip_date = tu.trip_date  ) "
				+ "	group by alert_trip_group_id"
				+ " order by real_departure_time_string desc", resultSetMapping = "realtime_update"

		),
		/*
		 * raltimeupdate constructor: String top_name, String stop_b_name
		 * ,Integer departure_delay, String trip_headsign, String
		 * planned_departure_time, String real_departure_time_string, String
		 * planned_arrival_time, String real_arrival_time_string, Integer
		 * update_id, String top_id, String trip_id, Integer alertTripGroupId,
		 * String tripDate
		 */
		@NamedNativeQuery(name = "realtime_update_all", query = "select s.stop_name as stop_name,'' as stop_b_name,departure_delay,trip_headsign"
				+ ",(select departure_time from stop_times where stop_times.stop_index = s.stop_index AND trip_index = t.trip_index)  as planned_departure_time "
				+ ",datetime(cast(stu.departure_time as varchar), 'unixepoch', 'localtime') as real_departure_time_string "
				+ ",(select arrival_time from stop_times where stop_times.stop_index = s.stop_index AND trip_index = t.trip_index)  as planned_arrival_time "
				+ ",datetime(cast(stu.arrival_time as varchar), 'unixepoch', 'localtime') as real_arrival_time_string "
				+ ", (tu.update_id) as update_id,s.stop_id,(t.trip_id) as trip_id, 0 as alert_trip_group_id, trip_date "

				// +",tu.update_id,datetime(cast(tu.recorded as varchar),
				// 'unixepoch', 'localtime') as recorded "
				+ "from gtfs_rt_trip_updates_stoptimes stu "
				+ "inner join gtfs_rt_trip_updates tu on tu.update_id   = stu.update_id "
				+ "inner join trips t on tu.trip_id = t.trip_id " + "inner join stops s on stu.stop_id = s.stop_id "
				+ " where (departure_delay > 0 or arrival_delay > 0)  "
				+ "order by real_departure_time_string desc", resultSetMapping = "realtime_update"),
		
		@NamedNativeQuery(name = "realtime_update_stop_min_delay", query = "select s.stop_name as stop_name,'' as stop_b_name,departure_delay,trip_headsign"
				+ ",(select departure_time from stop_times where stop_times.stop_index = s.stop_index AND trip_index = t.trip_index)  as planned_departure_time "
				+ ",datetime(cast(stu.departure_time as varchar), 'unixepoch', 'localtime') as real_departure_time_string "
				+ ",(select arrival_time from stop_times where stop_times.stop_index = s.stop_index AND trip_index = t.trip_index)  as planned_arrival_time "
				+ ",datetime(cast(stu.arrival_time as varchar), 'unixepoch', 'localtime') as real_arrival_time_string "
				+ ", (tu.update_id) as update_id,s.stop_id,(t.trip_id) as trip_id, 0 as alert_trip_group_id, trip_date "

				// +",tu.update_id,datetime(cast(tu.recorded as varchar),
				// 'unixepoch', 'localtime') as recorded "
				+ "from gtfs_rt_trip_updates_stoptimes stu "
				+ "inner join gtfs_rt_trip_updates tu on tu.update_id   = stu.update_id "
				+ "inner join trips t on tu.trip_id = t.trip_id " + "inner join stops s on stu.stop_id = s.stop_id "
				+ " where (departure_delay > ?)  AND s.stop_name LIKE  ? || '%' "
				+ "order by real_departure_time_string desc", resultSetMapping = "realtime_update")
}) // end NamedNativeQueries
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "stopTimesAToBResult", classes = {
				@ConstructorResult(targetClass = StopTimesAToBResult.class, columns = {
						@ColumnResult(name = "trip_id"), @ColumnResult(name = "trip_headsign"),
						@ColumnResult(name = "route_long_name"), @ColumnResult(name = "stop_name"),
						@ColumnResult(name = "b_stop_name"), @ColumnResult(name = "stop_id"),
						@ColumnResult(name = "stop_id_b"), @ColumnResult(name = "departure_time"),
						@ColumnResult(name = "arrival_time"), @ColumnResult(name = "b_arrival_time"),
						@ColumnResult(name = "b_departure_time"), @ColumnResult(name = "date")
				}

				)
		}), @SqlResultSetMapping(name = "trip", classes = {
				@ConstructorResult(targetClass = StopTimesAToBResult.class, columns = {
						@ColumnResult(name = "trip_id"), @ColumnResult(name = "trip_headsign"),
						@ColumnResult(name = "route_long_name"), @ColumnResult(name = "stop_name"),
						@ColumnResult(name = "b_stop_name"), @ColumnResult(name = "stop_id"),
						@ColumnResult(name = "stop_id_b"), @ColumnResult(name = "departure_time"),
						@ColumnResult(name = "arrival_time"), @ColumnResult(name = "b_arrival_time"),
						@ColumnResult(name = "b_departure_time"), @ColumnResult(name = "date")
				})
		}), @SqlResultSetMapping(name = "realtime_update", classes = {
				@ConstructorResult(targetClass = RealtimeUpdate.class, columns = {
						@ColumnResult(name = "stop_name"), @ColumnResult(name = "stop_b_name"),
						@ColumnResult(name = "departure_delay"), @ColumnResult(name = "trip_headsign"),
						@ColumnResult(name = "planned_departure_time"),
						@ColumnResult(name = "real_departure_time_string"),
						@ColumnResult(name = "planned_arrival_time"), @ColumnResult(name = "real_arrival_time_string"),
						@ColumnResult(name = "update_id"), @ColumnResult(name = "stop_id"),
						@ColumnResult(name = "trip_id"), @ColumnResult(name = "alert_trip_group_id"),
						@ColumnResult(name = "trip_date")
				})
		})

})
public class Trip implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "\"arrival_time\"")
	private String arrivalTime;

	@Column(name = "\"arrival_time_secs\"")
	private int arrivalTimeSecs;

	@Column(name = "\"block_id\"")
	private String blockId;

	@Column(name = "\"block_index\"")
	private int blockIndex;

	@Column(name = "\"departure_time\"")
	private String departureTime;

	@Column(name = "\"departure_time_secs\"")
	private int departureTimeSecs;

	@Column(name = "\"direction_id\"")
	private int directionId;

	@Column(name = "\"route_index\"")
	private int routeIndex;

	@Column(name = "\"service_id\"")
	private String serviceId;

	@Column(name = "\"service_index\"")
	private int serviceIndex;

	@Column(name = "\"shape_id\"")
	private String shapeId;

	@Column(name = "\"shape_index\"")
	private int shapeIndex;

	@Column(name = "\"trip_headsign\"")
	private String tripHeadsign;

	@Id
	@Column(name = "\"trip_id\"")
	private String tripId;

	@Column(name = "\"trip_index\"")
	private int tripIndex;

	@Column(name = "\"trip_short_name\"")
	private String tripShortName;

	@Column(name = "\"wheelchair_accessible\"")
	private int wheelchairAccessible;

	public Trip() {
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

	public String getBlockId() {
		return this.blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public int getBlockIndex() {
		return this.blockIndex;
	}

	public void setBlockIndex(int blockIndex) {
		this.blockIndex = blockIndex;
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

	public int getDirectionId() {
		return this.directionId;
	}

	public void setDirectionId(int directionId) {
		this.directionId = directionId;
	}

	public int getRouteIndex() {
		return this.routeIndex;
	}

	public void setRouteIndex(int routeIndex) {
		this.routeIndex = routeIndex;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getServiceIndex() {
		return this.serviceIndex;
	}

	public void setServiceIndex(int serviceIndex) {
		this.serviceIndex = serviceIndex;
	}

	public String getShapeId() {
		return this.shapeId;
	}

	public void setShapeId(String shapeId) {
		this.shapeId = shapeId;
	}

	public int getShapeIndex() {
		return this.shapeIndex;
	}

	public void setShapeIndex(int shapeIndex) {
		this.shapeIndex = shapeIndex;
	}

	public String getTripHeadsign() {
		return this.tripHeadsign;
	}

	public void setTripHeadsign(String tripHeadsign) {
		this.tripHeadsign = tripHeadsign;
	}

	public String getTripId() {
		return this.tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public int getTripIndex() {
		return this.tripIndex;
	}

	public void setTripIndex(int tripIndex) {
		this.tripIndex = tripIndex;
	}

	public String getTripShortName() {
		return this.tripShortName;
	}

	public void setTripShortName(String tripShortName) {
		this.tripShortName = tripShortName;
	}

	public int getWheelchairAccessible() {
		return this.wheelchairAccessible;
	}

	public void setWheelchairAccessible(int wheelchairAccessible) {
		this.wheelchairAccessible = wheelchairAccessible;
	}

}