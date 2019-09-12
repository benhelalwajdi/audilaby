package com.audiolaby.receiver;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;


import com.audiolaby.R;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.Download;
import com.audiolaby.persistence.model.Part;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EIntentService;
import org.androidannotations.annotations.ServiceAction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;


@EIntentService
public class DownloadService extends IntentService {


    @Bean
    LibraryDAO libraryDAO;

    public DownloadService() {
        super("Download Service");
    }

    String url;
    String title;
    String path;
    AudioArticle audioArticle;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;

    private int part_position = 0;

    @ServiceAction
    void myAction(AudioArticle audioArticle) {

        this.audioArticle = audioArticle;

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (audioArticle.getIs_parted()) {
            initDownloadParts();
        } else {
            initDownload();
        }


    }


    private void initDownload() {

        this.url = audioArticle.getAudioUrl();
        this.title = audioArticle.getTitle();

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("" + title)
                .setContentText("جاري التحميل")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());


        
        int slashPos = url.lastIndexOf('/');
        String file = url.substring(slashPos + 1, url.length());
        String path = url.substring(0, slashPos + 1);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(path)
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);


        Call<ResponseBody> request = retrofitInterface.downloadFile(file);
        try {
            downloadFile(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    private void initDownloadParts() {


        this.url = audioArticle.getPostParts().get(part_position).getAudioUrl();
        this.title = audioArticle.getTitle() + "-" + audioArticle.getPostParts().get(part_position).getTitle();

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("" + title)
                .setContentText("جاري التحميل")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());


        int slashPos = url.lastIndexOf('/');
        String file = url.substring(slashPos + 1, url.length());
        String path = url.substring(0, slashPos + 1);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(path)
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);


        Call<ResponseBody> request = retrofitInterface.downloadFile(file);
        try {
            downloadFiles(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }


    private void downloadFiles(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);


        String audioLabyPath = Environment.getExternalStorageDirectory() + "/.Audiolaby";
        File sdIconStorageDir = new File(audioLabyPath);
        if (!sdIconStorageDir.isDirectory()) {
            sdIconStorageDir.mkdirs();
        }
        File outputFile = new File(audioLabyPath, ".audio_" + audioArticle.getPost_id() + "_" + part_position + ".mp3");
        path = outputFile.getPath();
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }

        if (part_position == audioArticle.getPostParts().size() - 1) {
            audioArticle.getPostParts().get(part_position).setLocalAudioUrl(path);
            onDownloadComplete();
        } else {
            audioArticle.getPostParts().get(part_position).setLocalAudioUrl(path);
            part_position++;
            initDownloadParts();
        }

        output.flush();
        output.close();
        bis.close();

    }


    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);


        String audioLabyPath = Environment.getExternalStorageDirectory() + "/.Audiolaby";
        File sdIconStorageDir = new File(audioLabyPath);
        if (!sdIconStorageDir.isDirectory()) {
            sdIconStorageDir.mkdirs();
        }
        File outputFile = new File(audioLabyPath, ".audio_" + audioArticle.getPost_id() + ".mp3");
        path = outputFile.getPath();
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download) {

        sendIntent(download);
        int parcent = (int) ((int) download.getCurrentFileSize()) * 100 / ((int) download.getTotalFileSize());
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText("" + parcent + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendIntent(Download download) {

        Intent intent = new Intent("download_progress");
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }


    private void onDownloadComplete() {

        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText(" تمت اضافته الى مكتبتك ");
        notificationManager.notify(0, notificationBuilder.build());


        audioArticle.setLocalAudioUrl(path);
        audioArticle.setLibrary(true);
        audioArticle.save();
        if (audioArticle.getIs_parted()) {
            for (Part part : audioArticle.getPostParts()) {
                part.audioArticle = audioArticle;
                part.save();
            }
        }
        audioArticle.getAuthor().audioArticle = audioArticle;
        audioArticle.getAuthor().save();
        audioArticle.getVoiceOver().audioArticle = audioArticle;
        audioArticle.getVoiceOver().save();
        audioArticle.getCategory().audioArticle = audioArticle;
        audioArticle.getCategory().save();


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    public interface RetrofitInterface {
        @GET("{file}")
        @Streaming
        Call<ResponseBody> downloadFile(@Path("file") String file);
    }


}
