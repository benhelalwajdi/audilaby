package com.audiolaby.util;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.widget.Toast;

import com.activeandroid.Cache;
import com.audiolaby.Audiolaby;

import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import app.minimize.com.seek_bar_compat.BuildConfig;

@EBean(scope = Scope.Singleton)
public class Logger {
    @App
    Audiolaby audiolaby;

    public File getLogs() {
        Date date = new Date();
        File file = new File(this.audiolaby.getExternalCacheDir(), "fanlive-".concat(new SimpleDateFormat("yyyy-MM-dd").format(date)) + ".log");
        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(String.format("logcat -d -v threadtime *:*", new Object[0])).getInputStream()));
            StringBuilder result = new StringBuilder();
            while (true) {
                String currentLine = reader.readLine();
                if (currentLine == null) {
                    break;
                }
                result.append(currentLine);
                result.append(StringUtils.LF);
            }
            FileWriter out = new FileWriter(file);
            out.write(result.toString());
            out.close();
        } catch (IOException e) {
            Toast.makeText(this.audiolaby, e.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e2) {
            Toast.makeText(this.audiolaby, e2.toString(), Toast.LENGTH_LONG).show();
        }
        return file;
    }

    public File zipFile(File file) {
        try {
            File zippedFile = new File(this.audiolaby.getExternalCacheDir().getAbsolutePath().concat("/").concat(file.getName().substring(0, file.getName().indexOf(".log")).concat(".zip")));
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zippedFile)));
                byte[] data = new byte[Cache.DEFAULT_CACHE_SIZE];
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file), Cache.DEFAULT_CACHE_SIZE);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                while (true) {
                    int count = bufferedInputStream.read(data, 0, Cache.DEFAULT_CACHE_SIZE);
                    if (count != -1) {
                        zipOutputStream.write(data, 0, count);
                    } else {
                        bufferedInputStream.close();
                        zipOutputStream.close();
                        return zippedFile;
                    }
                }
            } catch (Exception e) {
                return zippedFile;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public String getAndroidVersion() {
        return VERSION.RELEASE;
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return StringUtils.capitalize(model);
        }
        return StringUtils.capitalize(manufacturer) + StringUtils.SPACE + model;
    }

    public String getAppVersion() {
        try {
            return this.audiolaby.getPackageManager().getPackageInfo(this.audiolaby.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return BuildConfig.VERSION_NAME;
        }
    }
}
