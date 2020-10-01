CREATE TABLE gtfs_rt_alerts (alert_id INTEGER, header TEXT, description TEXT, cause INTEGER, effect INTEGER, recorded INTEGER);
CREATE TABLE gtfs_rt_alerts_timeranges (alert_id INTEGER, start INTEGER, finish INTEGER);
CREATE TABLE gtfs_rt_alerts_entities (alert_id INTEGER, agency_id TEXT, route_id TEXT, route_type INTEGER, stop_id TEXT, trip_rship INTEGER, trip_start_date TEXT, trip_start_time TEXT, trip_id TEXT);
CREATE INDEX gtfs_rt_alerts_entities_agency_id ON gtfs_rt_alerts_entities (agency_id);
CREATE INDEX gtfs_rt_alerts_entities_route_id ON gtfs_rt_alerts_entities (route_id);
CREATE INDEX gtfs_rt_alerts_entities_stop_id ON gtfs_rt_alerts_entities (stop_id);
CREATE INDEX gtfs_rt_alerts_entities_trip_id ON gtfs_rt_alerts_entities (trip_id);
CREATE TABLE gtfs_rt_vehicles (congestion INTEGER, status INTEGER, sequence INTEGER, bearing REAL, odometer REAL, speed REAL, latitude REAL, longitude REAL, stop_id TEXT, ts INTEGER, trip_sr INTEGER, trip_date TEXT, trip_time TEXT, trip_id TEXT, route_id TEXT, vehicle_id TEXT, vehicle_label TEXT, vehicle_plate TEXT, recorded INTEGER);
CREATE INDEX gtfs_rt_vehicles_stop_id ON gtfs_rt_vehicles (stop_id);
CREATE INDEX gtfs_rt_vehicles_trip_id ON gtfs_rt_vehicles (trip_id);
CREATE INDEX gtfs_rt_vehicles_route_id ON gtfs_rt_vehicles (route_id);
CREATE TABLE gtfs_rt_trip_updates (update_id INTEGER, ts INTEGER, trip_sr INTEGER, trip_date TEXT, trip_time TEXT, trip_id TEXT, route_id TEXT, vehicle_id TEXT, vehicle_label TEXT, vehicle_plate TEXT, recorded INTEGER);
CREATE INDEX gtfs_rt_trip_updates_update_id ON gtfs_rt_trip_updates (update_id);
CREATE INDEX gtfs_rt_trip_updates_trip_id ON gtfs_rt_trip_updates (trip_id);
CREATE INDEX gtfs_rt_trip_updates_route_id ON gtfs_rt_trip_updates (route_id);
CREATE TABLE gtfs_rt_trip_updates_stoptimes (update_id INTEGER, arrival_time INTEGER, arrival_uncertainty INTEGER, arrival_delay INTEGER, departure_time INTEGER, departure_uncertainty INTEGER, departure_delay INTEGER, rship INTEGER, stop_id TEXT, stop_sequence INTEGER);
CREATE INDEX gtfs_rt_trip_updates_stoptimes_stop_id ON gtfs_rt_trip_updates_stoptimes (stop_id);
CREATE INDEX gtfs_rt_trip_updates_stoptimes_update_id ON gtfs_rt_trip_updates_stoptimes (update_id);
CREATE TABLE IF NOT EXISTS "alert_trips" (
	trip_index INTEGER,
	trips_to_alert_id INTEGER NOT NULL,
	trip_id TEXT(46) NOT NULL, trips_to_alert_group_id INTEGER,
	CONSTRAINT trips_to_alert_PK PRIMARY KEY (trips_to_alert_id)
);
CREATE TABLE IF NOT EXISTS "alert" (
	alert_id INTEGER NOT NULL,
	"e-mail" TEXT(254),
	stop_index_a INTEGER,
	stop_index_b INTEGER, 
	stop_a TEXT,
	stop_b TEXT,
	departure_time TEXT,
	arrival_time TEXT,
	alert_trip_group_id INTEGER, stop_id_a INTEGER, stop_id_b INTEGER, firebase_token TEXT,
	CONSTRAINT alert_PK PRIMARY KEY (alert_id)
);
CREATE TABLE IF NOT EXISTS "alert_trip_group" (
	alert_trip_group_id INTEGER,
 stop_index_a INTEGER, stop_index_b INTEGER,
 
  departure_time TEXT, arrival_time TEXT , stop_id_a INTEGER, stop_id_b INTEGER,
	CONSTRAINT alert_trip_group_id_PK PRIMARY KEY (alert_trip_group_id));
CREATE TABLE sqlite_sequence(name,seq);
CREATE TABLE IF NOT EXISTS "alert_updates_processed" (
	processed_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,
	update_id INTEGER,
	trip_date TEXT
);
CREATE TABLE stops_translations (
	trans_id TEXT,
	lang TEXT,
	"translation" TEXT
);
CREATE TABLE translations (trans_id TEXT,lang TEXT,translation TEXT);
CREATE INDEX translations_trans_id ON translations (trans_id);
CREATE TABLE agency (agency_id TEXT, agency_name TEXT, agency_timezone TEXT, agency_url TEXT, agency_lang TEXT, agency_phone TEXT, agency_fare_url TEXT);
CREATE TABLE stops (stop_index INTEGER, stop_id TEXT, stop_code TEXT, stop_name TEXT, stop_desc TEXT, zone_index INTEGER, zone_id TEXT, stop_lat REAL, stop_lon REAL, location_type INTEGER, parent_station TEXT, parent_station_index INTEGER, wheelchair_boarding INTEGER, stop_url TEXT, stop_timezone TEXT);
CREATE TABLE routes (route_index INTEGER, route_id TEXT, agency_id TEXT, route_short_name TEXT, route_long_name TEXT, route_desc TEXT, route_type INTEGER, route_color TEXT, route_text_color TEXT, route_url TEXT);
CREATE TABLE trips (trip_index INTEGER, route_index INTEGER, service_index INTEGER, service_id TEXT, shape_index INTEGER, shape_id TEXT, trip_id TEXT, trip_headsign TEXT, trip_short_name TEXT, direction_id INTEGER, block_index INTEGER, block_id TEXT, wheelchair_accessible INTEGER, departure_time TEXT, departure_time_secs INTEGER, arrival_time TEXT, arrival_time_secs INTEGER);
CREATE TABLE stop_times (stop_index INTEGER, trip_index INTEGER, arrival_time TEXT, arrival_time_secs INTEGER, departure_time TEXT, departure_time_secs INTEGER, stop_sequence INTEGER, last_stop INTEGER, shape_dist_traveled REAL, stop_headsign TEXT, pickup_type INTEGER, drop_off_type INTEGER);
CREATE TABLE calendar (service_index INTEGER, service_id TEXT, monday INTEGER, tuesday INTEGER, wednesday INTEGER, thursday INTEGER, friday INTEGER, saturday INTEGER, sunday INTEGER, start_date TEXT, end_date TEXT);
CREATE TABLE calendar_dates (service_index INTEGER, service_id TEXT, date TEXT, exception_type INTEGER);
CREATE TABLE shapes (shape_index INTEGER, shape_id TEXT, shape_pt_lat REAL, shape_pt_lon REAL, shape_pt_sequence INTEGER, shape_dist_traveled REAL);
CREATE TABLE fare_attributes (fare_index INTEGER, fare_id TEXT, price TEXT, currency_type TEXT, payment_method TEXT, transfers TEXT, transfer_duration TEXT);
CREATE TABLE fare_rules (fare_index INTEGER, route_index INTEGER, origin_index INTEGER, destination_index INTEGER, contains_index INTEGER);
CREATE TABLE frequencies (trip_index INTEGER, start_time TEXT, end_time TEXT, headway_secs TEXT, exact_times TEXT);
CREATE TABLE transfers (from_stop_index INTEGER, to_stop_index INTEGER, transfer_type TEXT, min_transfer_time TEXT);
CREATE TABLE feed_info (feed_publisher_name TEXT, feed_publisher_url TEXT, feed_lang TEXT, feed_start_date TEXT, feed_end_date TEXT, feed_version TEXT);
CREATE INDEX agency_agency_id ON agency (agency_id);
CREATE INDEX stops_stop_index ON stops (stop_index);
CREATE INDEX stops_stop_id ON stops (stop_id);
CREATE INDEX stops_stop_code ON stops (stop_code);
CREATE INDEX stops_zone_id ON stops (zone_id);
CREATE INDEX stops_zone_index ON stops (zone_index);
CREATE INDEX routes_route_index ON routes (route_index);
CREATE INDEX routes_route_id ON routes (route_id);
CREATE INDEX routes_agency_id ON routes (agency_id);
CREATE INDEX trips_trip_index ON trips (trip_index);
CREATE INDEX trips_route_index ON trips (route_index);
CREATE INDEX trips_service_index ON trips (service_index);
CREATE INDEX trips_shape_index ON trips (shape_index);
CREATE INDEX trips_trip_id ON trips (trip_id);
CREATE INDEX trips_block_index ON trips (block_index);
CREATE INDEX stop_times_stop_index ON stop_times (stop_index);
CREATE INDEX stop_times_trip_index ON stop_times (trip_index);
CREATE INDEX calendar_service_index ON calendar (service_index);
CREATE INDEX calendar_service_id ON calendar (service_id);
CREATE INDEX calendar_dates_service_index ON calendar_dates (service_index);
CREATE INDEX shapes_shape_index ON shapes (shape_index);
CREATE INDEX shapes_shape_id ON shapes (shape_id);
CREATE INDEX fare_attributes_fare_index ON fare_attributes (fare_index);
CREATE INDEX fare_attributes_fare_id ON fare_attributes (fare_id);
CREATE INDEX fare_rules_fare_index ON fare_rules (fare_index);
CREATE INDEX frequencies_trip_index ON frequencies (trip_index);
CREATE INDEX transfers_from_stop_index ON transfers (from_stop_index);
CREATE INDEX transfers_to_stop_index ON transfers (to_stop_index);
