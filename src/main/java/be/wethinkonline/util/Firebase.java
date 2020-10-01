package be.wethinkonline.util;

import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class Firebase {
	
	static  {
		
		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
				    .setCredentials(GoogleCredentials.getApplicationDefault())
				    .setDatabaseUrl("https://gtfs-alert.firebaseio.com/")
				    .build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Firebase() {
		

			
	}
	
	public void send(String title, String msg, String tokenClient) {
		// This registration token comes from the client FCM SDKs.
		Message message = Message.builder()
		    .setNotification(new Notification(title,msg))
		    .setToken(tokenClient)
		    .build();

		// Send a message to the device corresponding to the provided
		// registration token.
		String response ;  
		try {
			response = FirebaseMessaging.getInstance().send(message);
			// Response is a message ID string.
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
