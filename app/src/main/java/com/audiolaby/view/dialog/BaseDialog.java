package com.audiolaby.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.audiolaby.R;
import com.audiolaby.controller.enumeration.ResponseMap;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.util.ConnectivityUtils;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.BackgroundExecutor;

@EFragment
public abstract class BaseDialog extends DialogFragment {

    @StringRes(R.string.error_connection_ops)
    String errorConnection;
    @StringRes(R.string.error_try_again)
    String tryAgain;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onDestroy() {
        super.onDestroy();
        BackgroundExecutor.cancelAll("longtask", true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setLayout(-1, -2);
    }


    String  responseHandler(CommonResponse response) {

        if (response != null ) {
            if (response.getThread())
                return "again";
            else {

                if(response.getResponseStatus() == ResponseStatus.SUCCESS )
                    return "handler";
                else
                    return ResponseMap.getError(response.getResponseText(), getActivity());
            }
        }
        else {
            if (!ConnectivityUtils.isConnected(getActivity()))  return errorConnection;
            else return tryAgain;
        }

    }
}
