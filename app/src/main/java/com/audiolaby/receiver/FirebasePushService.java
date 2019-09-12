package com.audiolaby.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
//import android.support.v7.app.NotificationCompat;
import android.support.v4.app.NotificationCompat;

import com.audiolaby.R;
import com.audiolaby.persistence.AppPref_;
import com.audiolaby.view.activity.MainActivity_;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@EService
public class FirebasePushService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Gson gson = new Gson();
    String data = "", title = "Fanlive", body = "";

    @Pref
    AppPref_ appPref;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if(appPref.allowNotification().get())
        {
            if (remoteMessage.getNotification() != null) {
                body = remoteMessage.getNotification().getBody();
                title = remoteMessage.getNotification().getTitle();
            }
            if (remoteMessage.getData().size() > 0) {
                data = remoteMessage.getData().toString();
                try {
                    parse(data);
                } catch (Exception e) {
                    sendNotification();
                }
            } else {
                sendNotification();
            }
        }

    }

    void parse(String msg) throws JSONException {
        JSONObject msgObject = new JSONObject(msg);
        switch (msgObject.getString("type")) {
            case "new_post":
                parsePost(msgObject);
                break;
            default:
                sendNotification();
        }
    }

//{post_details={"featured":false,"author":{"image":null,"name":"مجلة زان","nPost":4,"author_id":7},
// "voiceOver":{"voiceOver_id":5,"image":"http:\/\/audiolaby.dev-fnode.com\/img\/voiceover\/1514484621575274.jpeg","name":"عائشة عبد الله","nPost":1},
// "runtime":931,"created_at":1514989320,"posts_id":56,"title":"5 نصائح تحفيزية للطلاب.. اتبعها لتحصل على التحفيز الكافي للدراسة","tags":[],"coverUrl":"http:\/\/audiolaby.dev-fnode.com\/files\/cover\/1514989320130105237756.png","isFree":true,"n_rate":0,"downloads":0,"rate":0,"postWished":null,"views":0}, type=new_post}
    void parsePost(JSONObject msgObject) {

        try {
           // AudioArticle audioArticle = gson.fromJson(msgObject.getJSONObject("post_details").toString(), AudioArticle.class);

            JSONObject details = msgObject.getJSONObject("post_details");
            title="مقال صوتي جديد";
            body= details.getString("title");
            String image = details.getString("coverUrl");
            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("response", ((Serializable) details.getString("posts_id")));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);



            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            final NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

        } catch (Exception e) {
            sendNotification();
        }


    }


    private void sendNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(),
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder)
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    //String msg = "{NOTIF_ID=NOTIF_QUIZ_DUO, status=2, winner=1, first_game=1, points_to_win=2, type=dual_quiz, created_at=1507036770, details={\"correct_g2\":3,\"time_g2\":9,\"time_g1\":8,\"created_at\":1507036770,\"gamer1\":{\"user_id\":116,\"favorite_team\":{\"name\":\"HANOVRE 96\",\"logo\":\"http:\\/\\/v2.fan-live.com\\/img\\/team\\/\\/112.png\",\"team_id\":112},\"last_name\":\"cx\",\"photo\":\"http:\\/\\/v2.fan-live.com\\/uploads\\/116\\/profile\\/116.jpeg\",\"first_name\":\"cc\",\"pseudo\":\"bachoula\",\"email\":\"coucou@gmail.com\"},\"correct_g1\":0,\"gamer2\":{\"user_id\":64,\"favorite_team\":{\"name\":\"BAYERN MUNICH\",\"logo\":\"http:\\/\\/v2.fan-live.com\\/img\\/team\\/\\/8.png\",\"team_id\":8},\"last_name\":\"tt\",\"photo\":\"http:\\/\\/v2.fan-live.com\\/uploads\\/64\\/profile\\/64.jpeg\",\"first_name\":\"tt\",\"pseudo\":\"testing\",\"email\":\"testing@gmail.com\"}}}";
    //String msg = "{status=2, winner=1, first_game=1, points_to_win=2, type=dual_quiz, created_at=1507036770, details={\"correct_g2\":3,\"time_g2\":9,\"time_g1\":8,\"created_at\":1507036770,\"gamer1\":{\"user_id\":116,\"favorite_team\":{\"name\":\"HANOVRE 96\",\"logo\":\"http:\\/\\/v2.fan-live.com\\/img\\/team\\/\\/112.png\",\"team_id\":112},\"last_name\":\"cx\",\"photo\":\"http:\\/\\/v2.fan-live.com\\/uploads\\/116\\/profile\\/116.jpeg\",\"first_name\":\"cc\",\"pseudo\":\"bachoula\",\"email\":\"coucou@gmail.com\"},\"correct_g1\":0,\"gamer2\":{\"user_id\":64,\"favorite_team\":{\"name\":\"BAYERN MUNICH\",\"logo\":\"http:\\/\\/v2.fan-live.com\\/img\\/team\\/\\/8.png\",\"team_id\":8},\"last_name\":\"tt\",\"photo\":\"http:\\/\\/v2.fan-live.com\\/uploads\\/64\\/profile\\/64.jpeg\",\"first_name\":\"tt\",\"pseudo\":\"testing\",\"email\":\"testing@gmail.com\"}}}";

}



