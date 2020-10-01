GTFS-ALERT README
===

Introduction
---
GTFS-alert is a Java app that sends Web browser or e-mail notifications when delays occur on certain train journeys. The app was mainly tested with the Belgian railways open data. However, it can very easily be adapted to be used for any (train, metro, bus) agency that publishes data in the (Realtime) GTFS open format. It also saves the downloaded GTFS data in separate folders per timestamp for archiving purposes.
The application can be operated by a REST interface.


I started this in 2017, and worked bit by bit on it as an experimental project to learn more about  the GTFS and realtime GTFS data formats. The app is not finished nor fit for production in terms of quality standards, and contains a lot of hardcoded configuration. However, the application prototype is functional and has been running stable for over 6 months on our servers. 

Data is downloaded and checked for alerts on the following intervals (in seconds):
	realtimeUpdateThreadIntervalSeconds = 30;
	plannedUpdateThreadIntervalSeconds = 3600;
These intervals can easily be adapted in the GtfsAlertContextListener which schedules the import threads on webapp startup.

Usage, building / running
---
Before building and running: check section  PREREQUISITES before building & running
You can add an alert configuration via a  REST call like this:
http://127.0.0.1/gtfs-alert/alert/add?a=Bruxelles-Central&b=Courtrai&departureTime=20:39:00&firebaseToken=client_token_id_to_alert

Libraries / stack overview
---
- Eclipse Jersey for JAX-RS REST endpoints implementation
- Eclipselink libraries
- SQLite persistence layer
- Google Firebase SDK

Prerequisites before building & running (Important!)
---

### NMBS (or other agency) open data GTFS download URLs
* planned data:
hardcoded in  PlannedService, property strPlannedUrl = "https://sncb-opendata-personalized-url";

* realtime data:
hardcoded in 	 String strNmbsUrl = "https://sncb-opendata-url";

You can request these open data URLS by contacting NMBS via the form on their website: https://www.belgiantrain.be/nl/support/forms/public-data
Alternatively,for testing, you can mock these url's so they point to a sample file on our own server, we can use the planned/realtime data for tests:
eg.
* planned data:
hardcoded in  PlannedService, property strPlannedUrl = "";

* realtime data:
hardcoded in 	 String strNmbsUrl = "";


### Server time zone
Should be set to : -Duser.timezone=GMT

eg. in startup script  tomcat8/bin/setenv.sh
variable CATALINA_OPTS

### Webapp server context path
Tested in tomcat 8, JDK 1.8.
It is expected that the tomcat server listens on 127.0.0.1:8080 and the app is served under gtfs-alert. Reason: hardcoded app URL in:
RealtimeUpdateService 
	localHttpProxyDownloadNmbsUrl = "http://127.0.0.1:8080/gtfs-alert/import/realtime/download";
	
If you want to change the port or context name, you should also adapt this variable before building and deploying the app.

### Change the Google Firebase database URL
The application relies on Google Firebase to send web browser notifications.
You should create a firebase database with google, and then change the variable here:

* Firebase/setDatabaseUrl() 
public class Firebase {
	
	static  {
		
		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
				    .setCredentials(GoogleCredentials.getApplicationDefault())
				    .setDatabaseUrl("https://gtfs-alert.firebaseio.com/")




### Data Persistence: create sqlite db, import structure & assure writable folders/files

* The SQLite SQL db exist in the following path, as defined in persistence.xml.
- Create an sqlite database in file /Users/user/Documents/projects/gtfs-alert/db/gtfs_alert.sqlite. Then fill the db  with table & index structure by importing the sql file from the project folder's src/db/gtfs_alert_db_structure.sql
- Make sure you have a writable path fot the sqlite file to be modified by the web application.
		<property name="javax.persistence.jdbc.url"
				value="jdbc:sqlite:/Users/user/Documents/projects/gtfs-alert/db/gtfs_alert.sqlite" />

* The downloaded GTFS and Realtime GTFS files from the Agency (eg. NMBS) are stored in this path:
Make sure this folder exists and is writable:					If you want to change the path, it is hardcoded in:
/Users/user/Documents/projects/gtfs-alert/sample_data/planned			PlannedImportService
/Users/user/Documents/projects/gtfs-alert/sample_data/realtime			RealTimeImportService

### Service keys needed by firebase admin sdk to contact google firebase servers
To generate a private key file for your service account:
In the Firebase console, open project Settings > Service Accounts.
Click Generate New Private Key, then confirm by clicking Generate Key.

Securely store the JSON file containing the key.
on dev machine create env variable:
```console
export GOOGLE_APPLICATION_CREDENTIALS="/path-to-firebase-json/gtfs-alert-firebase-adminsdk-x1xyz.json"```
On production server, set environment variable for tomcat via 
```console
nano /etc/default/tomcat8
```
eg:

```

 # If you run Tomcat on port numbers that are all higher than 1023, then you
 # do not need authbind.  It is used for binding Tomcat to lower port numbers.
 # (yes/no, default: no)
 #AUTHBIND=no
 GOOGLE_APPLICATION_CREDENTIALS="/path-to-firebase-json/gtfs-alert-firebase-adminsdk-x1xyz.json"

```


### Create file system folder for test data and webapp download data
Test data should exist in the following file system paths you will need to create before running.
The same paths are also used by the main application to store realtime/planned data for now:
* /Users/user/Documents/projects/gtfs-alert/sample_data/planned/
* /Users/user/Documents/projects/gtfs-alert/sample_data/realtime/

Make sure these paths are writable by your tomcat user.
Then copy the folder content in this project called sample_data to the above location before running tests.

Known errors in application
---
### Error preallocating sequence numbers
okt 06, 2017 9:21:28 AM $ lambda$0
SEVERE: Scheduled import job error 
Local Exception Stack: 
Exception [EclipseLink-4011] (Eclipse Persistence Services - 2.6.4.v20160829-44060b6): org.eclipse.persistence.exceptions.DatabaseException
Exception Description: Error preallocating sequence numbers.  The sequence table information is not complete.
	
	==> execute in sql:
	INSERT INTO alert_updates_processed
	(processed_id, update_id, trip_date)
	VALUES(0, 0, '');


###Bug realtime update 18/10/2017 08:23->06:23 ???

INFO: Starting scheduled checking for realtime alerts (2/2)
[EL Fine]: sql: 2017-10-19 08:42:25.788--ServerSession(490089862)--Connection(580585491)--test@example.com | Trein Aalter Blankenberge 8:37:00 -> 06:38:00
message sent successfully....

==> reason is server timezone tempfix: -Duser.timezone=GMT. Todo permanent fix code should not depend on serv er timezone: set timezone in agent service.(agent operates in certain country)



:20:24 AM be.wethinkonline.gtfs_notify.boot.GtfsAlertContextListener lambda$0
INFO: 0 alerts found
Oct 25, 2017 10:20:24 AM org.apache.tomcat.util.net.NioEndpoint$Acceptor run
SEVERE: Socket accept failed
java.io.IOException: Too many open files
	at sun.nio.ch.ServerSocketChannelImpl.accept0(Native Method)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:422)
	at sun.nio.ch.ServerSocketChannelImpl.accept(ServerSocketChannelImpl.java:250)
	at org.apache.tomcat.util.net.NioEndpoint$Acceptor.run(NioEndpoint.java:688)
	at java.lang.Thread.run(Thread.java:748)
	
	
	
### bug: certain ? international trains non existent in planning?

    {
        "stopName": "Liers",
        "departureDelay": 420,
        "tripHeadsign": "Maastricht (NL)",
        "plannedDepartureTime": "09:14:00",
        "realDepartureTime": "2020-01-09 09:21:00",
        "plannedArrivalTime": "09:14:00",
        "realArrivalTime": "2020-01-09 09:20:00",
        "updateId": 105,
        "stopId": "8841673",
        "tripId": "88____:L72::8831005:8400219:15:1002:20201211",
        "alertTripGroupId": 0,
        "stopBName": "",
        "tripDate": "20200109",
        "departureDelayInMin": 7
    },
	==> fix: error in service add alert
	using % % on stopname
	instead of ... % on stopname filter 
	
	==> + other bug in data : maastricht is not in stop_time sof trip        "tripId": "88____:L72::8831005:8400219:15:1002:20201211", see debug_lier_maastricht_realtime.sql
	
	

Features todo:
---
* realtime_updates query: do not include later arrival time ? flag in alert table?
* more strictly validate/clean all input on resources
* correct 0 prefixed departure time h(remove leading0)
* translate station names to nl (currently all stops are in French, or the original NL name if no translation in stops.stop_name) translations.txt from planned GTFS data should be parsed

Copyright & licensing
---
* Code in be.wethinkonline packages License: GNU Lesser General Public License version 3 see file LICENSE
* Code in other packages, check for licensing and ownership with respective owners

Project by wethinkonline.be - https://www.wethinkonline.be
