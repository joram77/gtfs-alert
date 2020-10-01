package be.wethinkonline.gtfs_notify.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;

import org.eclipse.persistence.internal.jpa.querydef.CriteriaBuilderImpl;

import be.wethinkonline.gtfs_notify.model.Alert;
import be.wethinkonline.gtfs_notify.model.AlertUpdatesProcessed;
import be.wethinkonline.gtfs_notify.model.RealtimeUpdate;
import be.wethinkonline.gtfs_notify.model.Stop;
import be.wethinkonline.util.Firebase;

public class AlertService {
	private EntityManager em = TripService.getEmf().createEntityManager();
	private static final Logger LOGGER = Logger.getLogger(RealtimeUpdateService.class.getName());
	String host = "localhost";// or IP address
	String port = "25";
	StopService sv = new StopService();
	Firebase firebase = new Firebase();
	private static final int minDepartureDelayInSecondsBeforeAlert = 60*2;
	
	public AlertService() {
		
		em = TripService.emf.createEntityManager();

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public String composeMessage(RealtimeUpdate realtimeUpdate) {
		String msg = "Train to " + realtimeUpdate.getStopBName() + " from " + realtimeUpdate.getStopName() + " "
		+ " Delay departure : "+ realtimeUpdate.getPlannedDepartureTime().toString() + " -> " + realtimeUpdate.getRealDepartureTime()+ " "
		+ " Delay arrival   : "+ realtimeUpdate.getPlannedArrivalTime().toString() + " -> " + realtimeUpdate.getRealArrivalTime() ;

		return msg;
	}
	
	public String composeTitle(RealtimeUpdate realtimeUpdate) {
		String title = "Train >"+ realtimeUpdate.getStopBName() + " +"+ realtimeUpdate.getDepartureDelayInMin() + " min" 
	+ " from " + realtimeUpdate.getStopName();
		return title;
		
	}

	public void alert(List<RealtimeUpdate> lsRtu) {

		for (RealtimeUpdate realtimeUpdate : lsRtu) {
			
			if(realtimeUpdate.getDepartureDelay() > minDepartureDelayInSecondsBeforeAlert) {
	

			Integer alertTripGroupId = realtimeUpdate.getAlertTripGroupId();
			Query query = em.createQuery("SELECT a FROM Alert a WHERE a.alertTripGroupId = " + alertTripGroupId);
			List<Alert> emailsToAlert;
			emailsToAlert = (List<Alert>) query.getResultList();
			for (Alert alert : emailsToAlert) {

				if(alert.getEmail()!=null) email(alert.getEmail(), composeMessage(realtimeUpdate), composeTitle(realtimeUpdate));
				if(alert.getFirebaseToken()!=null) { 
					String title = composeTitle(realtimeUpdate);
					firebase.send(title, composeMessage(realtimeUpdate), alert.getFirebaseToken());;
					LOGGER.log(Level.INFO, "Alert sent firebase message  with title " + title + " to "+alert.getFirebaseToken());

				}

			}
			//
			AlertUpdatesProcessed aProcessed = new AlertUpdatesProcessed();
			aProcessed.setTripDate(realtimeUpdate.getTripDate());
			/*
			 * register already processed alert so we don't send multiple mails
			 * for the same delay/alert update NMBS update_id not registered by
			 * gtfsrealtimetosql: eg. entity { id:
			 * "88____:046::8865300:8865003:2:1051:20171208" } We will construct
			 * a unique update identifier on the fly by concatenation of values
			 * identifying an update as unique: updateId = stopName +
			 * PlannedDepartureTime + realDepartureTime:
			 */
			aProcessed.setUpdateId(realtimeUpdate.getStopName() + realtimeUpdate.getPlannedDepartureTime()
					+ realtimeUpdate.getRealDepartureTime());
			em.getTransaction().begin();
			em.persist(aProcessed);
			em.getTransaction().commit(); // flush needed?
		}
		//em.close();
			}

	}

	public boolean addAlert(Alert a) {
		if (a.getStopIdA() == null) {
			Stop stopA = sv.stopBeginningByName(a.getStopA()).get(0); //todo check if array has elements
			Stop stopB = sv.stopBeginningByName(a.getStopB()).get(0);
			a.setStopIdA(stopA.getStopId());
			a.setStopIdB(stopB.getStopId());

		}

		em.getTransaction().begin();
		em.persist(a);
		em.getTransaction().commit();
		this.updateAlertGroup(a.getStopIdA(),a.getStopIdB(), a.getDepartureTime()
				);
		return true;

	}

	public void updateAlertGroups() {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Query
		 query = em.createNamedQuery("Alert.updateAlertGroupsStep1");
		query.executeUpdate();
		
	
		
		query = em.createNamedQuery("Alert.updateAlertGroupsStep2");
		query.executeUpdate();
		
		query = em.createNamedQuery("Alert.updateAlertGroupsStep2aa");
		query.executeUpdate();
		query = em.createNamedQuery("Alert.updateAlertGroupsStep2ab");
		query.executeUpdate();
		query = em.createNamedQuery("Alert.updateAlertGroupsStep2ba");
		query.executeUpdate();
		query = em.createNamedQuery("Alert.updateAlertGroupsStep2bb");
		query.executeUpdate();
		query = em.createNamedQuery("Alert.updateAlertGroupsStep3");
		query.executeUpdate();
		query = em.createNamedQuery("Alert.updateAlertGroupsStep4");
		query.executeUpdate();
		query = em.createNamedQuery("Alert.updateAlertGroupsStep5");
		query.executeUpdate();

		tx.commit();

	}

	public void updateAlertGroup(String stop_id_a, String stop_id_b, String departure_time) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		// stop_index_a = ? AND stop_index_b = ? AND departure_time = ? AND
		// arrival_time = ?

		Consumer<String> qry = (String q) -> {
			Query query = em.createNamedQuery(q);
			query.setParameter(1, stop_id_a);
			query.setParameter(2, stop_id_b);
			query.setParameter(3, departure_time);
			query.executeUpdate();
		};
		qry.accept("Alert.updateAlertGroupsSpecificStopTimeStep1a");
		qry.accept("Alert.updateAlertGroupsSpecificStopTimeStep1b");
		qry.accept("Alert.updateAlertGroupsSpecificStopTimeStep2");
		
		Query query2aa = em.createNamedQuery("Alert.updateAlertGroupsStep2aa");
		query2aa.setParameter(1,stop_id_a);
		query2aa.executeUpdate();
		Query query2ab = em.createNamedQuery("Alert.updateAlertGroupsStep2ab");
		query2ab.setParameter(1,stop_id_a);
		query2ab.executeUpdate();
		
		Query query2ba = em.createNamedQuery("Alert.updateAlertGroupsStep2ba");
		query2ba.setParameter(1,stop_id_b);
		query2ba.executeUpdate();
		Query query2bb = em.createNamedQuery("Alert.updateAlertGroupsStep2bb");
		query2bb.setParameter(1,stop_id_b);
		query2bb.executeUpdate();
		
		qry.accept("Alert.updateAlertGroupsSpecificStopTimeStep3");
		qry.accept("Alert.updateAlertGroupsSpecificStopTimeStep5");
		tx.commit();

	}

	public void email(String to, String msg, String subject) {

		// Get the session object
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", port);

		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("alert@example.com"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setText(msg);
			message.setSubject(subject);
			// Send message
			Transport.send(message);
			System.out.println("message sent successfully....");
			LOGGER.log(Level.INFO, "Alert sent mail " + to + " with title " + subject);

		} catch (MessagingException mex) {
			LOGGER.log(Level.WARNING, "Cannot send mail " + message, mex);
		}
	}

}
