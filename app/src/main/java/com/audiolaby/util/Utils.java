package com.audiolaby.util;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.audiolaby.R;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.firebase.crash.FirebaseCrash;
import com.lapism.searchview.SearchView;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.minimize.com.seek_bar_compat.BuildConfig;
import commons.validator.routines.AbstractNumberValidator;

public final class Utils {


    public enum Format {
        SHORT_FORMAT,
        LONG_FORMAT
    }


    public static Drawable getDrawable(Context context, int icon, int color, float size) {

        TextDrawable d = new TextDrawable(context);
        d.setTypeface(Typeface.createFromAsset(context.getAssets(), "FontAwesome.otf"));
        d.setText(context.getString(icon));
        if (size > 0) d.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        d.setTextColor(ContextCompat.getColor(context, color));
        return d;
    }

    public static Drawable getDrawableMenu(Context context, int icon, int color, float size) {

        TextDrawable d = new TextDrawable(context);
        d.setTypeface(Typeface.createFromAsset(context.getAssets(), "FontAwesome.otf"));
        d.setText(context.getString(icon));
        if (size > 0) d.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        d.setTextColor(ContextCompat.getColor(context, color));
        d.setBounds(1, 0, 0, 0);
        return d;
    }

    public static void sendLog(String message) {

        FirebaseCrash.log("Time=" + getCurrentDate() + " *** os=android *** message=" + message);
    }

    public static File fileFromImage(ImageView image) {
        image.buildDrawingCache();
        Bitmap bmp = image.getDrawingCache();
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //context.getExternalFilesDir(null);
        File file = new File(storageLoc, "fanlive" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File fileFromBitmap(Bitmap bmp) {
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //context.getExternalFilesDir(null);
        File file = new File(storageLoc, "audioLaby" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(c.getTime());
        return date;
    }

    public static String formatSecondsToHoursMinutesAndSeconds(int seconds) {
        long milliseconds = (long) (seconds * SearchView.VERSION_TOOLBAR);
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        long second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
        if (TimeUnit.MILLISECONDS.toHours(milliseconds) > 0) {
            return String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(milliseconds)), Long.valueOf(minute), Long.valueOf(second)});
        }
        return String.format("%02d:%02d", new Object[]{Long.valueOf(minute), Long.valueOf(second)});
    }

    public static String formatSecondsToMinutesAndSeconds(int seconds) {
        long minute = TimeUnit.SECONDS.toMinutes((long) seconds) - (TimeUnit.SECONDS.toHours((long) seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds((long) seconds) - (TimeUnit.SECONDS.toMinutes((long) seconds) * 60);
        return String.format("%02d:%02d", new Object[]{Long.valueOf(minute), Long.valueOf(second)});
    }

    public static String formatMillisecondsToMinutesAndSeconds(long milliseconds) {
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        long second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
        return String.format("%02d:%02d", new Object[]{Long.valueOf(minute), Long.valueOf(second)});
    }

    public static String formatMillisecondsToHoursMinutesAndSeconds(long milliseconds) {
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        long second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
        if (TimeUnit.MILLISECONDS.toHours(milliseconds) > 0) {
            return String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(milliseconds)), Long.valueOf(minute), Long.valueOf(second)});
        }
        return String.format("%02d:%02d", new Object[]{Long.valueOf(minute), Long.valueOf(second)});
    }

    public static String formatMillisecondsToHoursMinutesAndSeconds(long milliseconds, Format format) {
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds));
        long second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds));
        if (format == Format.LONG_FORMAT) {
            return String.format("%02d hours %02d min %02d seconds", new Object[]{Long.valueOf(hours), Long.valueOf(minute), Long.valueOf(second)});
        } else if (hours > 0) {
            return String.format("%02dh %02dm", new Object[]{Long.valueOf(hours), Long.valueOf(minute)});
        } else {
            return String.format("%02dm", new Object[]{Long.valueOf(minute)});
        }
    }

    public static long getPercentageCompleted(long current, long total) {
        if (total == 0) {
            return 0;
        }
        return 100 - ((current * 100) / total);
    }

    public static String getDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static String getDeviceVersion(Context context) {

        return "" + Build.VERSION.SDK_INT;
    }


    public static String getCountry(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return "" + locale.getDisplayCountry().toString();
    }


    public static String getUserCountry(Context context) {

        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) {
                Log.i("", "");
// SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) {
                    Log.i("", "");
// network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
            Log.i("", "");
        }
        return getCountry(context);
    }


    public static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return BuildConfig.VERSION_NAME;
        }
    }

    public static void setFont(TextView textView, String fontName) {
        if (textView != null) {
            textView.setTypeface(Typeface.createFromAsset(textView.getContext().getAssets(), fontName));
        }
    }

    public static int daysBetween(Date begin, Date end) {
        Calendar start = DateUtils.toCalendar(begin);
//        start.set(14, 0);
//        start.set(13, 0);
//        start.set(12, 0);
//        start.set(11, 0);
        Calendar finish = DateUtils.toCalendar(end);
//        finish.set(14, 999);
//        finish.set(13, 59);
//        finish.set(12, 59);
//        finish.set(11, 23);
        return (int) Math.ceil(((double) (finish.getTimeInMillis() - start.getTimeInMillis())) / 8.64E7d);
    }

    public static String getNetworkInfo(Context context) {
        try {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == 1) {
                    return "Connection Type: WIFI";
                }
                if (info.getType() == 0) {
                    String connectionType = "Not able to gather Network type";
                    switch (info.getSubtype()) {
                        case AbstractNumberValidator.STANDARD_FORMAT /*0*/:
                            connectionType = "Unknow";
                            break;
                        case AbstractNumberValidator.CURRENCY_FORMAT /*1*/:
                            connectionType = "GPRS";
                            break;
                        case AbstractNumberValidator.PERCENT_FORMAT /*2*/:
                            connectionType = "EDGE";
                            break;
                        case SearchView.TEXT_STYLE_BOLD_ITALIC /*3*/:
                            connectionType = "UMTS";
                            break;
                        case CommonStatusCodes.SIGN_IN_REQUIRED /*4*/:
                            connectionType = "CDMA";
                            break;
                        case CommonStatusCodes.INVALID_ACCOUNT /*5*/:
                            connectionType = "EVDO-0";
                            break;
                        case CommonStatusCodes.RESOLUTION_REQUIRED /*6*/:
                            connectionType = "EVDO-A";
                            break;
                        case CommonStatusCodes.NETWORK_ERROR /*7*/:
                            connectionType = "1xRTT";
                            break;
                        case CommonStatusCodes.INTERNAL_ERROR /*8*/:
                            connectionType = "HSDPA";
                            break;
                        case ConnectionResult.SERVICE_INVALID /*9*/:
                            connectionType = "HSUPA";
                            break;
                        case CommonStatusCodes.DEVELOPER_ERROR /*10*/:
                            connectionType = "HSPA";
                            break;
                        case ConnectionResult.LICENSE_CHECK_FAILED /*11*/:
                            connectionType = "IDEN";
                            break;
                        case StdKeyDeserializer.TYPE_UUID /*12*/:
                            connectionType = "EVDO-8";
                            break;
                        case CommonStatusCodes.ERROR /*13*/:
                            connectionType = "LTE";
                            break;
                        case CommonStatusCodes.INTERRUPTED /*14*/:
                            connectionType = "EHRPD";
                            break;
                        case CommonStatusCodes.TIMEOUT /*15*/:
                            connectionType = "HSPAP";
                            break;
                    }
                    return String.format("Connection Type: %s", new Object[]{connectionType});
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return "Not able to gather Network info";
    }

    public static String getDate(Long timeStampStr) {

        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date netDate = (new Date(timeStampStr * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "" + timeStampStr;
        }
    }


    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    private int calculateScrollXForTab(int position, TableLayout tableLayout) {
        final View selectedChild = tableLayout.getChildAt(position);
        return (int) selectedChild.getLeft();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void clearPicasso(Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        LruCache picassoCache = new LruCache(context);
        builder.memoryCache(picassoCache);
        Picasso.setSingletonInstance(builder.build());

        picassoCache.clear();

    }


    public static String getKey(Context context) {

        PackageInfo info;
        String something = "";
        try {
            info = context.getPackageManager().getPackageInfo("com.audiolaby",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                something = new String(Base64.encode(md.digest(), 0));


                String something2 = "zY00cczs2SG28RTn1bZQvsCftvI=";
                String something3 = "XE42iFhT3yFSsxnIoUMq+Fhx/j4=";
                String something4 = "2gBno6jzrE3BKXEagvuVVdgXnB8=";
                String something5 = "Ujhnuv5cMpdZXGUiGAAN07wMePl=";
                String something6 = "2jmj7l5rSw0yVb/vlWAYkK/YBwk=";


                something = something.substring(0, 28);
               return  something;
            }
        } catch (Exception e) {
            return  "false";
        }

        return  "false";

    }


    public static boolean verif(Context context) {

        PackageInfo info;
        String something = "";
        try {
            info = context.getPackageManager().getPackageInfo("com.audiolaby",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                something = new String(Base64.encode(md.digest(), 0));


                String something2 = "zY00cczs2SG28RTn1bZQvsCftvI=";
                String something3 = "XE42iFhT3yFSsxnIoUMq+Fhx/j4=";
                String something4 = "2gBno6jzrE3BKXEagvuVVdgXnB8=";
                String something5 = "Ujhnuv5cMpdZXGUiGAAN07wMePl=";
                String something6 = "2jmj7l5rSw0yVb/vlWAYkK/YBwk=";
                String something7 = "Ujhnuv5cMpdZXGUiGAAN07wMePI=";





                        something = something.substring(0, 28);
                Log.e("hash key", something);
                if (!something.equals(something2) &&
                        !something.equals(something3)
                        && !something.equals(something4)
                        && !something.equals(something5)
                        && !something.equals(something6)
                        && !something.equals(something7))
                    return false;

                else
                    return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;

    }
}
