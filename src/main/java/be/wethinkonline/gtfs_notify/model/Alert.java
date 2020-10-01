package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the "alert" database table.
 * * field arrival_time is deprecated and not used anymore
 * 
 */
@Entity
@Table(name = "\"alert\"")
@NamedQueries({
@NamedQuery(name = "Alert.findAll", query = "SELECT a FROM Alert a"),
@NamedQuery(name = "Alert.findById", query = "SELECT a FROM Alert a WHERE a.alertId = ?1")}
)


@NamedNativeQueries({
	@NamedNativeQuery(name = "AlertTrips.findAll", query = "SELECT * FROM alert_trips ORDER BY trip_index ASC"),
	@NamedNativeQuery(name = "AlertTrips.findTripGroupTrips", query = "SELECT * FROM alert_trips "
			+ "INNER JOIN alert_trip_group on alert_trip_group.alert_trip_group_id = alert_trips.trips_to_alert_group_id "
			+ "WHERE stop_id_a = ? AND stop_id_b = ? AND departure_time = ?  "
			+ "ORDER BY trip_index ASC"),
		@NamedNativeQuery(name = "Alert.clearTestData", query = "delete from alert;"),

		@NamedNativeQuery(name = "Alert.createTestData1", query = "INSERT INTO alert"
				+ "(alert_id, \"e-mail\",stop_id_a,stop_id_b, stop_a, stop_b, departure_time, arrival_time, alert_trip_group_id)"
				+ "VALUES(null, 'alert@example.com', 8891140,8813003 ,'Aalter', 'Bruxelles-Central', '7:23:00', '8:14:00', null) "),
		@NamedNativeQuery(name = "Alert.createTestData2", query = "INSERT INTO alert"
				+ "(alert_id, \"e-mail\", stop_id_a,stop_id_b, stop_a, stop_b, departure_time, arrival_time, alert_trip_group_id)"
				+ "VALUES(null, 'alert@example.com',  8891140,8813003 ,'Aalter', 'Bruxelles-Central', '8:23:00', '9:15:00', null) ;"),
		@NamedNativeQuery(name = "Alert.createTestData3", query = "INSERT INTO alert"
				+ "(alert_id, \"e-mail\",stop_id_a,stop_id_b, stop_a, stop_b, departure_time, arrival_time, alert_trip_group_id)"
				+ "VALUES(null, 'alert@example.com' ,8813003,8891140,'Bruxelles-Central', 'Aalter', '17:16:00', '8:14:00', null); "),
		@NamedNativeQuery(name = "Alert.createTestData4", query = "INSERT INTO alert"
				+ "(alert_id, \"e-mail\",stop_id_a,stop_id_b, stop_a, stop_b, departure_time, arrival_time, alert_trip_group_id)"
				+ "VALUES(null, 'alert@example.com', 8891140,8891405 ,'Aalter', 'Blankenberge', '8:37:00', '9:18:00', null); "
				+ ""),
		@NamedNativeQuery(name="Alert.deleteAlertUpdatesProcessed",query="delete from alert_updates_processed;"),
		// -- create alert groups based on alert stop_a_id and stop_b_id
		// subscriptions in alert table

		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep1",
				// -- create alert groups based on alert stop_a_id and stop_b_id
				// subscriptions in alert table
				// -- update the alert group id in alert table
				// -- create the alert_trips tripids to alert from the trip
				// planning based on the created trip groups
				query = "delete from 	alert_trip_group;"),
		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep2", query = "INSERT into alert_trip_group"
				+ "( alert_trip_group_id, stop_id_a, stop_id_b, departure_time) " //, arrival_time 
				+ "select distinct null, alert.stop_id_a, alert.stop_id_b , departure_time FROM alert;"),//, arrival_time 

		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep2aa",
				query = "UPDATE alert SET stop_index_a = (SELECT stop_index FROM stops WHERE stop_id = alert.stop_id_a);"),
		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep2ab", 
		query = "UPDATE alert_trip_group SET stop_index_a = (SELECT stop_index FROM stops WHERE stop_id = alert_trip_group.stop_id_a);"
				+ ""),
		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep2ba",
				query = "UPDATE alert SET stop_index_b = (SELECT stop_index FROM stops WHERE stop_id = alert.stop_id_b);"),

		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep2bb", 
		query = "UPDATE alert_trip_group "
				+ "SET stop_index_b = (SELECT stop_index FROM stops WHERE stop_id = alert_trip_group.stop_id_b);"),

		// -- update the alert group id in alert table
		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep3",
				//
				query = "UPDATE alert  set 	alert_trip_group_id = "
						+ "(select 	alert_trip_group_id from alert_trip_group where alert_trip_group.stop_id_a = alert.stop_id_a "
						+ "and alert_trip_group.departure_time = alert.departure_time "
						+ " and alert_trip_group.stop_id_b = alert.stop_id_b);"), //and alert_trip_group.arrival_time = alert.arrival_time
		// -- create the alert_trips tripids to alert from the trip planning
		// based on the created trip groups
		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep4",
				//
				query = "delete from alert_trips;"),
		@NamedNativeQuery(name = "Alert.updateAlertGroupsStep5", query = "INSERT into alert_trips"
				+ "(trips_to_alert_id, trip_id, trip_index, trips_to_alert_group_id) "
				+ "select null, trips.trip_id, stop_times.trip_index, alert_trip_group.alert_trip_group_id "
				+ "FROM alert_trip_group  inner join stop_times on stop_times.departure_time = alert_trip_group.departure_time "
				+ "AND stop_times.stop_index = alert_trip_group.stop_index_a "
				+ "inner join stop_times stop_times_b on stop_times_b.stop_index = alert_trip_group.stop_index_b "
				+ "AND stop_times_b.trip_index = stop_times.trip_index "
				+ "inner join trips on trips.trip_index  = stop_times.trip_index "
				+ " where stop_times.stop_sequence < stop_times_b.stop_sequence ;"),

		@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep1a", query = "DELETE from alert_trips WHERE trips_to_alert_group_id IN"
				+ "(SELECT alert_trip_group_ID FROM alert_trip_group "
				+ "WHERE stop_id_a = ? AND stop_id_b = ? AND departure_time = ?  );"), //AND arrival_time = ?
		@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep1b", query = "DELETE from 	alert_trip_group "
				+ "WHERE stop_id_a = ? AND stop_id_b = ? AND departure_time = ? ;"), //AND arrival_time = ? 
		@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep2", query = "INSERT into alert_trip_group"
				+ "( alert_trip_group_id, stop_id_a, stop_id_b, departure_time) " //, arrival_time
				+ "SELECT distinct null, alert.stop_id_a, 	alert.stop_id_b,departure_time	"//, arrival_time 	
				+ "FROM alert "
				+ "WHERE stop_id_a = ? AND stop_id_b = ? AND departure_time = ?  ;"),//AND arrival_time = ?
		
		@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep2aa",
		query = "UPDATE alert SET stop_index_a = (SELECT stop_index FROM stops WHERE stop_id = alert.stop_id_a) WHERE alert.stop_id_a= ?;"),
@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep2ab", 
query = "UPDATE alert_trip_group SET stop_index_a = (SELECT stop_index FROM stops WHERE stop_id = alert_trip_group.stop_id_a) WHERE alert.stop_id_a= ?;;"
		+ ""),
@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep2ba",
		query = "UPDATE alert SET stop_index_b = (SELECT stop_index FROM stops WHERE stop_id = alert.stop_id_b) WHERE alert.stop_id_b= ?;"),

@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep2bb", 
query = "UPDATE alert_trip_group SET stop_index_b = (SELECT stop_index FROM stops WHERE stop_id = alert_trip_group.stop_id_b) WHERE alert.stop_id_b= ?;"),

		
		@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep3",
				//
				query = "UPDATE 	alert  SET 	alert_trip_group_id = (select 		alert_trip_group_id 	from 		alert_trip_group 	"
						+ "where 		alert_trip_group.stop_index_a = alert.stop_index_a 		and alert_trip_group.departure_time = alert.departure_time  "
						+ "  		and alert_trip_group.stop_index_b = alert.stop_index_b) "//and alert_trip_group.arrival_time = alert.arrival_time
						+ "WHERE stop_id_a = ? AND stop_id_b = ? AND departure_time = ?   " + "" //AND arrival_time = ?
						+ ";"),
		@NamedNativeQuery(name = "Alert.updateAlertGroupsSpecificStopTimeStep5", query = ""
				+ "INSERT into alert_trips(trips_to_alert_id, trip_id, trip_index, trips_to_alert_group_id) "
				+ "SELECT null, trips.trip_id, stop_times.trip_index, alert_trip_group.alert_trip_group_id "
				+ "FROM alert_trip_group " + " inner join stop_times "
				+ "ON stop_times.departure_time = alert_trip_group.departure_time "
				+ "AND stop_times.stop_index= alert_trip_group.stop_index_a inner "
				+ "join stop_times stop_times_b on stop_times_b.stop_index = alert_trip_group.stop_index_b "
				+ "AND stop_times_b.trip_index = stop_times.trip_index "
				+ "inner join trips on trips.trip_index  = stop_times.trip_index  "
		+ " WHERE alert_trip_group.alert_trip_group_id = "
		+ "(SELECT alert_trip_group_id from alert_trip_group 	where  alert_trip_group.stop_id_a = ? and alert_trip_group.stop_id_b = ?		"
		+ "and alert_trip_group.departure_time = ?  			) " //and alert_trip_group.arrival_time = ?  	
		+ "AND stop_times.stop_sequence < stop_times_b.stop_sequence "
		+ ""
),
})
public class Alert implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "\"alert_id\"")

	@GeneratedValue(generator = "sqlite_alert_generator")
	@TableGenerator(name = "sqlite_alert_generator", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "alert")
	private int alertId;

	@Column(name = "\"alert_trip_group_id\"")
	private int alertTripGroupId;

	@Column(name = "\"arrival_time\"")
	private String arrivalTime;

	@Column(name = "\"departure_time\"")
	private String departureTime;

	@Column(name = "\"e-mail\"")
	private String email;

	@Column(name = "\"stop_a\"")
	private String stopA;

	@Column(name = "\"stop_b\"")
	private String stopB;

	@Column(name = "\"stop_id_a\"")
	private String stopIdA;

	@Column(name = "\"stop_id_b\"")
	private String stopIdB;

	@Column(name = "\"stop_index_a\"")
	private int stopIndexA;

	@Column(name = "\"stop_index_b\"")
	private int stopIndexB;

	@Column(name = "\"firebase_token\"")
	private String firebaseToken;
	
	public Alert() {
	}

	public int getAlertId() {
		return this.alertId;
	}

	public void setAlertId(int alertId) {
		this.alertId = alertId;
	}

	public int getAlertTripGroupId() {
		return this.alertTripGroupId;
	}

	public void setAlertTripGroupId(int alertTripGroupId) {
		this.alertTripGroupId = alertTripGroupId;
	}

	public String getArrivalTime() {
		return this.arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartureTime() {
		return this.departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getEmail() {
		return this.email;
	}

	public void setE_mail(String e_mail) {
		this.email = e_mail;
	}

	public String getStopA() {
		return this.stopA;
	}

	public void setStopA(String stopA) {
		this.stopA = stopA;
	}

	public String getStopB() {
		return this.stopB;
	}

	public void setStopB(String stopB) {
		this.stopB = stopB;
	}

	public String getStopIdA() {
		return this.stopIdA;
	}

	public void setStopIdA(String stopIdA) {
		this.stopIdA = stopIdA;
	}

	public String getStopIdB() {
		return this.stopIdB;
	}

	public void setStopIdB(String stopIdB) {
		this.stopIdB = stopIdB;
	}

	public int getStopIndexA() {
		return this.stopIndexA;
	}

	public void setStopIndexA(int stopIndexA) {
		this.stopIndexA = stopIndexA;
	}

	public int getStopIndexB() {
		return this.stopIndexB;
	}

	public void setStopIndexB(int stopIndexB) {
		this.stopIndexB = stopIndexB;
	}

	public String getFirebaseToken() {
		return firebaseToken;
	}

	public void setFirebaseToken(String firebaseToken) {
		this.firebaseToken = firebaseToken;
	}

}