package com.example.john.medic;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by Akshay on 3/8/2018.
 */


public class TopicsPushNotification extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String table_number = "";
        String user_uid = "";
        if (!remoteMessage.getData().isEmpty()) {
            table_number = remoteMessage.getData().get("table_number");
            user_uid = remoteMessage.getData().get("user_uid");
        }

        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),
                table_number, user_uid);

        // Check if message contains a notification payload.


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */


    private void sendNotification(String title, String messageBody, String table_number, String user_uid) {
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationChannelBuild();
        }
        Intent notification_click = null;
       // if (table_number.equals("Empty")) {
            notification_click = new Intent(this, MainActivity.class);

      //  } else {
           // notification_click = new Intent(this, AcceptActivity.class);
          //  notification_click.putExtra("table_number", table_number);
          //  notification_click.putExtra("accepted_mode", messageBody);
          //  notification_click.putExtra("user_uid", user_uid);
       // }

        PendingIntent notification_pi = PendingIntent.getActivity(this, 12345,
                notification_click, PendingIntent.FLAG_UPDATE_CURRENT);


        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setLargeIcon(icon)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (!table_number.equals("Empty")) {
          //  notificationBuilder
            //        .addAction(R.drawable.ic_check_black_24dp, "Accept", notification_pi);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId("topic_ch_1");
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Random random = new Random();

// generate a random integer from 0 to 899, then add 100
        int x = random.nextInt(900) + 100;


        notificationManager.notify(x /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_launcher_background : R.mipmap.ic_launcher;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificationChannelBuild() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel chan1 = new NotificationChannel("topic_ch_1",
                "Notification", NotificationManager.IMPORTANCE_DEFAULT);
        chan1.setLightColor(Color.BLUE);
        chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager.createNotificationChannel(chan1);
    }
}
