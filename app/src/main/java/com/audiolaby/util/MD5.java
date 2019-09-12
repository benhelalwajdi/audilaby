package com.audiolaby.util;

import android.util.Log;

import com.facebook.internal.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static final String TAG = "MD5";

    public static String calculateMD5(File updateFile) {
        String str = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(TAG);
            try {
                InputStream is = new FileInputStream(updateFile);
                byte[] buffer = new byte[Utility.DEFAULT_STREAM_BUFFER_SIZE];
                while (true) {
                    try {
                        int read = is.read(buffer);
                        if (read <= 0) {
                            break;
                        }
                        digest.update(buffer, 0, read);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to process file for MD5", e);
                    } catch (Throwable th) {
                        try {
                            is.close();
                        } catch (IOException e2) {
                            Log.e(TAG, "Exception on closing MD5 input stream", e2);
                        }
                    }
                }
                str = String.format("%32s", new Object[]{new BigInteger(1, digest.digest()).toString(16)}).replace(' ', '0');
                try {
                    is.close();
                } catch (IOException e22) {
                    Log.e(TAG, "Exception on closing MD5 input stream", e22);
                }
            } catch (FileNotFoundException e3) {
                Log.e(TAG, "Exception while getting FileInputStream", e3);
            }
        } catch (NoSuchAlgorithmException e4) {
            Log.e(TAG, "Exception while getting digest", e4);
        }
        return str;
    }
}
