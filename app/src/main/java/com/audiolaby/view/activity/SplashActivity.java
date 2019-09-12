package com.audiolaby.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.audiolaby.R;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.view.player.utils.timely.model.number.Null;

import java.io.File;

import static com.audiolaby.view.activity.AudioDetailsActivity.MY_PREFS_NAME;

public class SplashActivity extends Activity {

    LibraryDAO libraryDAO;
public static SharedPreferences prefs;
public static SharedPreferences.Editor editor ;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                libraryDAO = new LibraryDAO();
                prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("firsttime", null);

                if(restoredText != null) {
                    if (libraryDAO.getUser() != null)
                    {
                        MainActivity_.intent((Context) SplashActivity.this).start();
                        finish();
                    }
                    else{
                        IntroActivity_.intent((Context) SplashActivity.this).start();
                        finish();
                    }
                    //MainActivity_.intent((Context) SplashActivity.this).start();
                    try {
                        trimCache(getApplicationContext());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // close this activity
                    finish();
                }else{
                    if (libraryDAO.getUser() != null)
                    {
                        MainActivity_.intent((Context) SplashActivity.this).start();
                        finish();
                    }
                    else{
                        IntroActivity_.intent((Context) SplashActivity.this).start();
                        finish();
                    }
                    //MainActivity_.intent((Context) SplashActivity.this).start();
                    try {
                        trimCache(getApplicationContext());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
    @Override
    protected void onStart() {
        super.onStart();
        try {
            trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
                System.out.println("Cache vide");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }



  /*  class runnable implements Runnable {
        runnable() {
        }

        public void run() {

//            if (appPref.firstTime().get()) {
//                IntroductionActivity_.intent((Context) SplashActivity.this).start();
//                finish();
//                return;
//            }

            if (libraryDAO.getUser() != null)
            {
                MainActivity_.intent((Context) SplashActivity.this).start();
                finish();
            }

            else{
                IntroActivity_.intent((Context) SplashActivity.this).start();
                finish();
            }
        }
    }

    @AfterViews
    void afterViewsInjection() {
        super.afterViewsInjection();
        version.setText("Audiolaby "+getAppVersion(this) + "\n powered by : Mobisoft");
        copy.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                int sdk = Build.VERSION.SDK_INT;
                if (sdk < Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) SplashActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText("" + Utils.getKey(SplashActivity.this));
                    Toast.makeText(SplashActivity.this,
                           "copied",
                            Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) SplashActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData
                            .newPlainText("message", "" + Utils.getKey(SplashActivity.this));
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(SplashActivity.this,
                          "copied",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        version.setText(""+getAppVersion(this));
        if (Utils.verif(SplashActivity.this))
        {
            new CheckNewAppVersion(SplashActivity.this).setOnTaskCompleteListener(new CheckNewAppVersion.ITaskComplete() {
                @Override
                public void onTaskComplete(CheckNewAppVersion.Result result) {

                    final CheckNewAppVersion.Result result2=result;
                    if(result.hasNewVersion())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder
                                .setTitle("")
                                .setMessage(getString(R.string.new_version))
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new Handler().postDelayed(new runnable(), 2000);
                                    }
                                })
                                .setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        result2.openUpdateLink();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }

                    else
                        new Handler().postDelayed(new runnable(), 2000);
                }
            }).execute();
        }

        else
            //copy.setVisibility(View.VISIBLE);
            android.os.Process.killProcess(android.os.Process.myPid());
    }

   /* protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }*/


/*    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new runnable(), 2000);
    }

//    @Background(id = "longtask")
//    void getUserDetails() {
//        User user;
//        AccountResponse response = (AccountResponse) this.accountController.getUserDetails();
//
//        if (response != null && response.getResponseStatus() == ResponseStatus.SUCCESS) {
//            user = libraryDAO.getUser();
//            user.setFirstName(response.getUser().getFirstName());
//            user.setLastName(response.getUser().getLastName());
//            user.setImage(response.getUser().getImage());
//            user.save();
//            MainActivity_.intent((Context) this).start();
//            finish();
//
//        } else {
//            if (ConnectivityUtils.isConnected(this)) {
//              //  appPref.token().put("");
//               // this.libraryDAO.clearUserData();
//            }
//            MainActivity_.intent((Context) this).start();
//            finish();
//        }*/
//    }


}
