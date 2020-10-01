package be.wethinkonline.test;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import be.wethinkonline.util.Firebase;

public class FirebaseTest {
	
	private static Firebase firebase;
	
	@BeforeClass
	public static void authorizeFirebase() throws IOException {
	firebase = new Firebase();
		
	}
	

	@Test
	public void send() throws FirebaseMessagingException {
		String tokenMacbookPro ="fchrVia7rJijH8JnwDuRzx:APA91bG3t5zKXGqv8UH6oz9SHWOW_v_gdkYDfnLybmg99V-O6yhKWoeWMfpwfh_T0pmq3EothsEnnjuozNVmhbBgF3w31n0iBlyjAjWWZs4ZTl-cwejqCd3a0PPenb0czVm1CWU7FNmD";// See documentation on defining a message payload.
		String tokenAndroid ="cAc8pSEsOoPIM9EC-QXJO0:APA91bFI25gRObG1nVkqcHbtIz214IysxWGtR3hjxtVBi8FdKBshAtfXztD4KK-FqpHz8LdaOAJAQH-Kxp-txSkfxsTer7JAtD0orIu8_iD_NXeMQqZoUE3DDCZdh7GPH9z9o5Df4Z-Z";
		String tokenChromeMacPro = "f0PYvAij7-Pe6_iY8bb6L_:APA91bH8b-h-bqjTHgjkZ1XQIU60DJ6ywMjmoAmqd91tSWNLONbgU0Ntk7V6S9jrWOj79cIdb5VT0AoXuFaKQq9136m7MHYN9YER1rC7F0P8hfIJ-ZePX6DWjYz_IcmTqvEBWJ3up1u9";
		firebase.send("Trein vertraging","+ 1 uur",tokenChromeMacPro);
		
	}
}
