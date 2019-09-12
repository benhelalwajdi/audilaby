package com.audiolaby.util;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.lapism.searchview.SearchView;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;

import commons.validator.routines.AbstractNumberValidator;

public class ConnectivityUtils {
    private static final String HOST = "Www.google.com";

    private static class ResolveHostRunnable implements Runnable {
        private boolean canResolve;

        private ResolveHostRunnable() {
        }

        public void run() {
            try {
                boolean z;
                if (InetAddress.getByName(ConnectivityUtils.HOST).equals(StringUtils.EMPTY)) {
                    z = false;
                } else {
                    z = true;
                }
                this.canResolve = z;
            } catch (Exception e) {
                this.canResolve = false;
            }
        }

        public boolean canResolve() {
            return this.canResolve;
        }
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected() && info.isAvailable() && canResolveHost();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isConnected() && info.getType() == 1) {
            return true;
        }
        return false;
    }

    public static boolean isCellularConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected() && info.getType() == 0;
    }

    public static boolean isFastConnection(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected() && isFastConnection(info.getType(), info.getSubtype());
    }

    public static boolean isFastConnection(int type, int subType) {
        if (type == 1) {
            return true;
        }
        if (type != 0) {
            return false;
        }
        switch (subType) {
            case AbstractNumberValidator.CURRENCY_FORMAT /*1*/:
                return false;
            case AbstractNumberValidator.PERCENT_FORMAT /*2*/:
                return false;
            case SearchView.TEXT_STYLE_BOLD_ITALIC /*3*/:
            case CommonStatusCodes.INVALID_ACCOUNT /*5*/:
            case CommonStatusCodes.RESOLUTION_REQUIRED /*6*/:
            case CommonStatusCodes.INTERNAL_ERROR /*8*/:
            case ConnectionResult.SERVICE_INVALID /*9*/:
            case CommonStatusCodes.DEVELOPER_ERROR /*10*/:
            case StdKeyDeserializer.TYPE_UUID /*12*/:
            case CommonStatusCodes.ERROR /*13*/:
            case CommonStatusCodes.INTERRUPTED /*14*/:
            case CommonStatusCodes.TIMEOUT /*15*/:
                return true;
            case CommonStatusCodes.SIGN_IN_REQUIRED /*4*/:
                return false;
            case CommonStatusCodes.NETWORK_ERROR /*7*/:
                return false;
            case ConnectionResult.LICENSE_CHECK_FAILED /*11*/:
                return false;
            default:
                return false;
        }
    }

    private static boolean canResolveHost() {
        try {
            ResolveHostRunnable r = new ResolveHostRunnable();
            Thread t = new Thread(r);
            t.start();
            t.join();
            return r.canResolve();
        } catch (Exception e) {
            return true;
        }
    }
}
