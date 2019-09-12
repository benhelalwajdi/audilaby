/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.audiolaby.view.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.audiolaby.R;
import com.audiolaby.otto.events.ConnectionChangedEvent;
import com.audiolaby.view.player.MusicService;
import com.audiolaby.view.player.playback.PlaybackControlsFragment;
import com.audiolaby.view.player.utils.FinishEvent;
import com.audiolaby.view.player.utils.SwipeDetector;
import com.audiolaby.view.player.utils.TrackEvent;
import com.audiolaby.view.player.utils.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;



@EActivity
public abstract class BasePlayerActivity extends BaseActivity {

    String mMediaId;

    @ViewById(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @ViewById(R.id.mini_player)
    RelativeLayout miniPlayer;
    @ViewById(R.id.player_header)
    RelativeLayout playerHeader;
    @ViewById(R.id.top_panel)
    FrameLayout topPanel;

    @Click({R.id.down})
    void onDownClicked() {
        if (this.slidingLayout == null) {
            return;
        }
        if (this.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || this.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED) {
            this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    @Click({R.id.close})
    public  void onCloseClicked() {
        try{
            this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            EventBus.getDefault().postSticky(new FinishEvent());
        }
        catch (Exception e)
        {

        }
    }


    @Click({R.id.close_frame})
    public  void onCloseMiniClicked() {
        try{
            this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            EventBus.getDefault().postSticky(new FinishEvent());
        }
        catch (Exception e)
        {

        }
    }


    @Click({R.id.back})
    void onBackClicked() {
        finish();
    }


    MediaBrowserCompat mMediaBrowser;
    PlaybackControlsFragment mControlsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    void afterViewsInjection() {
        super.afterViewsInjection();
        new SwipeDetector(this.miniPlayer).setOnSwipeListener(new C07232());
        this.slidingLayout.addPanelSlideListener(new C07254(getResources().getDimensionPixelSize(R.dimen.player_top_panel_height_difference)));

    }


    @Override
    protected void onStart() {
        super.onStart();

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
        }


        this.registerReceiver(mConnectivityChangeReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class), mConnectionCallback, null);

        mControlsFragment = (PlaybackControlsFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_playback_controls);
        if (mControlsFragment == null) {
            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
        }

        shouldShowControls();
//        if (shouldShowControls()) {
//            showPlaybackControls();
//        } else {
//            hidePlaybackControls();
//        }


        mMediaBrowser.connect();

        if (mMediaBrowser.isConnected()) {
            onConnected();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }

        MediaControllerCompat controllerCompat = MediaControllerCompat.getMediaController(this);
        if (controllerCompat != null) {
            controllerCompat.unregisterCallback(mMediaControllerCallback);
        }

        this.unregisterReceiver(mConnectivityChangeReceiver);


        if (mMediaBrowser != null && mMediaBrowser.isConnected() && mMediaId != null) {
            mMediaBrowser.unsubscribe(mMediaId);
        }

        mMediaBrowser.disconnect();
    }


    protected void onMediaControllerConnected() {
        // empty implementation, can be overridden by clients.
        onConnected();
    }


    protected void showPlaybackControls() {
      //  if (Utils.isOnline(this)) {
            if (this.slidingLayout.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
      //  }
    }


     void hidePlaybackControls() {
        this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @CallSuper
     boolean shouldShowControls() {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            hidePlaybackControls();
            return false;
        }
        switch (mediaController.getPlaybackState().getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                hidePlaybackControls();
                return false;
            default:
                showPlaybackControls();
                return true;
        }
    }

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(this, token);
        MediaControllerCompat.setMediaController(this, mediaController);
        mediaController.registerCallback(mMediaControllerCallback);

        shouldShowControls();
//        if (shouldShowControls()) {
//            showPlaybackControls();
//        } else {
//            hidePlaybackControls();
//        }


        if (mControlsFragment != null) {
            mControlsFragment.onConnected();
        }

        onMediaControllerConnected();
    }


    MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    shouldShowControls();
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    if (shouldShowControls()) {
                        showPlaybackControls();
                    } else {
                        hidePlaybackControls();
                    }
                }

            };

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        hidePlaybackControls();
                    }
                }
            };


    ///////mini player///////

    class C07254 implements SlidingUpPanelLayout.PanelSlideListener {
        final /* synthetic */ int val$difference;

        C07254(int i) {
            this.val$difference = i;
        }

        public void onPanelSlide(View panel, float slideOffset) {
            if (((double) slideOffset) == 1.0d) {
                BasePlayerActivity.this.miniPlayer.setVisibility(View.GONE);
            } else {
                BasePlayerActivity.this.miniPlayer.setVisibility(View.VISIBLE);
            }
            BasePlayerActivity.this.miniPlayer.setAlpha(1.0f - slideOffset);
            BasePlayerActivity.this.playerHeader.setAlpha(slideOffset);
            FrameLayout.LayoutParams miniPlayerParams = (FrameLayout.LayoutParams) BasePlayerActivity.this.miniPlayer.getLayoutParams();
            if (BasePlayerActivity.this.getResources().getConfiguration().orientation == 1) {
                try {
                    LinearLayout.LayoutParams topPanelParams = (LinearLayout.LayoutParams) BasePlayerActivity.this.topPanel.getLayoutParams();
                    topPanelParams.height = (int) (((float) miniPlayerParams.height) - (((float) this.val$difference) * slideOffset));
                    BasePlayerActivity.this.topPanel.setLayoutParams(topPanelParams);
                    return;
                } catch (ClassCastException e) {
                    RelativeLayout.LayoutParams topPanelParams2 = (RelativeLayout.LayoutParams) BasePlayerActivity.this.topPanel.getLayoutParams();
                    topPanelParams2.height = (int) (((float) miniPlayerParams.height) - (((float) this.val$difference) * slideOffset));
                    BasePlayerActivity.this.topPanel.setLayoutParams(topPanelParams2);
                    return;
                }
            }
            try {
                RelativeLayout.LayoutParams topPanelParams2 = (RelativeLayout.LayoutParams) BasePlayerActivity.this.topPanel.getLayoutParams();
                topPanelParams2.height = (int) (((float) miniPlayerParams.height) - (((float) this.val$difference) * slideOffset));
                BasePlayerActivity.this.topPanel.setLayoutParams(topPanelParams2);
            } catch (ClassCastException e2) {
                LinearLayout.LayoutParams topPanelParams = (LinearLayout.LayoutParams) BasePlayerActivity.this.topPanel.getLayoutParams();
                topPanelParams.height = (int) (((float) miniPlayerParams.height) - (((float) this.val$difference) * slideOffset));
                BasePlayerActivity.this.topPanel.setLayoutParams(topPanelParams);
            }
        }

        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//            if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && previousState == SlidingUpPanelLayout.PanelState.EXPANDED) {
//                BasePlayerActivity.this.updateStatusBarColor(ContextCompat.getColor(BasePlayerActivity.this, R.color.eStories_blue));
//            } else if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED && previousState == SlidingUpPanelLayout.PanelState.DRAGGING) {
//                BasePlayerActivity.this.interactionWasOnFullPlayer = false;
//                BasePlayerActivity.this.sendEvent(new RequestBookPagerUpdateEvent());
//            } else if (newState == SlidingUpPanelLayout.PanelState.DRAGGING && previousState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                int color;
//                if (BasePlayerActivity.this.getResources().getConfiguration().orientation == 1) {
//                    if (BasePlayerActivity.this.attachedFragment == null && BasePlayerActivity.this.booksAdapter != null) {
//                        BasePlayerActivity.this.attachedFragment = (FullPlayerBookFragment) BasePlayerActivity.this.booksAdapter.getRegisteredFragment(BasePlayerActivity.this.selectedBookIndex);
//                    }
//                    if (BasePlayerActivity.this.attachedFragment != null) {
//                        BasePlayerActivity.this.isDarkTheme = BasePlayerActivity.this.attachedFragment.isDarkTheme();
//                    }
//                }
//                BasePlayerActivity.this.updateUIColors();
//                BasePlayerActivity BasePlayerActivity = BasePlayerActivity.this;
//                if (BasePlayerActivity.this.isDarkTheme) {
//                    color = ContextCompat.getColor(BasePlayerActivity.this, R.color.eStories_orange_dark);
//                } else {
//                    color = ContextCompat.getColor(BasePlayerActivity.this, R.color.eStories_blue);
//                }
//                BasePlayerActivity.updateStatusBarColor(color);
//            }
        }
    }

    class C07232 implements SwipeDetector.OnSwipeEvent {
        C07232() {
        }

        public void swipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {

            if (swipeType == SwipeDetector.SwipeTypeEnum.TOUCH) {
                BasePlayerActivity.this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        }
    }

    public void onBackPressed() {
        if (this.slidingLayout == null || !(this.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || this.slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            super.onBackPressed();
        } else {
            this.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }


//    void checkForUserVisibleErrors(boolean forceError) {
//        if (!Utils.isOnline(this)) {
//            Toast.makeText(this, getString(R.string.error_no_connection), Toast.LENGTH_LONG);
//        } else {
//            MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
//            if (controller != null
//                    && controller.getMetadata() != null
//                    && controller.getPlaybackState() != null
//                    && controller.getPlaybackState().getState() == PlaybackStateCompat.STATE_ERROR
//                    && controller.getPlaybackState().getErrorMessage() != null) {
//                Toast.makeText(this, controller.getPlaybackState().getErrorMessage(), Toast.LENGTH_LONG);
//            } else if (forceError) {
//                Toast.makeText(this, getString(R.string.error_loading_media), Toast.LENGTH_LONG);
//            }
//        }
//    }

    public void onMediaItemSelected(String mMediaId) {
        try {
            MediaControllerCompat.getMediaController(this).getTransportControls()
                    .playFromMediaId(mMediaId, null);
        } catch (Exception e) {
            Log.i("", "" + e.toString());
        }

    }


    private final BroadcastReceiver mConnectivityChangeReceiver = new BroadcastReceiver() {
        private boolean oldOnline = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mMediaId != null) {
                boolean isOnline = Utils.isOnline(context);
                if (isOnline != oldOnline) {
                    oldOnline = isOnline;
                    //checkForUserVisibleErrors(false);
                }
            }
        }
    };

    public void onConnected() {

        if (mMediaId == null) {
            mMediaId = mMediaBrowser.getRoot();
        }
        //mMediaBrowser.unsubscribe(mMediaId);
        // mMediaBrowser.subscribe(mMediaId, mSubscriptionCallback);
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(this);
        if (controller != null) {
            controller.registerCallback(mMediaControllerCallback);
        }
    }


    public void putMusic(ArrayList<MediaMetadataCompat> mediaMetadataCompats) {
        EventBus.getDefault().postSticky(new TrackEvent(mediaMetadataCompats));
    }

    @org.greenrobot.eventbus.Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
   public void onConnectionChanged(ConnectionChangedEvent event) {
        if (event.getStatus() == ConnectionChangedEvent.Status.CONNECTED) {
           // this.libraryTask.syncCachedDataAsynchronously();
        } else {
            //this.libraryTask.stopSyncCachedData();
        }
    }
}
