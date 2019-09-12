package com.audiolaby.view.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;


import com.audiolaby.R;
import com.audiolaby.controller.enumeration.ResponseMap;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.util.ConnectivityUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.api.BackgroundExecutor;
import org.greenrobot.eventbus.EventBus;

@EFragment
public abstract class BaseFragment extends Fragment {

    @StringRes(R.string.error_connection_ops)
    String errorConnection;
    @StringRes(R.string.error_try_again)
    String tryAgain;


    @ViewById(R.id.progress)
    ProgressBar progress;

    abstract void afterViewsInjection();



    @AfterInject
    public void afterInjection() {
    }

    public void onDestroy() {
        super.onDestroy();
        BackgroundExecutor.cancelAll("longtask", true);

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }

    }

    public boolean isFragmentBeyingDiscarded() {
        if (getActivity() == null || getActivity().isFinishing() || isRemoving() || isDetached() || !isAdded()) {
            return true;
        }
        return false;
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

    @UiThread
    void showError(String message) {
        if(progress!=null)
            progress.setVisibility(View.GONE);
        Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }
}
