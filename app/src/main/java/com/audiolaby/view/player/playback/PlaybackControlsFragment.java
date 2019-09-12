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
package com.audiolaby.view.player.playback;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.audiolaby.persistence.model.AudioArticleMin;
import com.audiolaby.persistence.model.Part;
import com.audiolaby.view.dialog.PartsDialog_;
import com.audiolaby.view.player.MusicService;
import com.audiolaby.R;
import com.audiolaby.view.player.utils.CircularSeekBar;
import com.audiolaby.view.player.utils.PlayPauseButton;
import com.audiolaby.view.player.utils.PlayPauseDrawable;
import com.audiolaby.view.player.utils.Utils;
import com.audiolaby.view.player.utils.timely.TimelyView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;

import java.lang.reflect.Modifier;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.view.View.VISIBLE;

/**
 * A class that shows the Media Queue to the user.
 */

@EFragment
public class PlaybackControlsFragment extends Fragment {


    AudioArticleMin audioArticle;

    MediaMetadataCompat currentMetadata;
    private MediaBrowserCompat mMediaBrowser;


    private PlaybackStateCompat mLastPlaybackState;

    long currentPosition = 0;
    private List<Part> postParts = new ArrayList<Part>();


    TextView mTitle, mArtist;
    ProgressBar mProgress;
    View mWrapper;
    ImageView mPicture;
    PlayPauseButton mPlay;

    int part = 0;

    TextView close, down;
    AudioManager audioManager;
    SeekBar fVolume;
    RelativeLayout controls;
    LinearLayout parts;
    LinearLayout fTime;
    ImageView mBlurredArt;
    ImageView albumart;
    ImageView shuffle;
    ImageView repeat;
    ImageView previous, next;
    PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();
    FloatingActionButton playPauseFloating;
    TextView songtitle, songartist, songpart;


    CircularSeekBar mCircularProgress;
    TimelyView timelyView11, timelyView12, timelyView13, timelyView14, timelyView15;
    TextView hourColon;
    int[] timeArr = new int[]{-1, -1, -1, -1, -1};

    private final View.OnClickListener mFLoatingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            PlaybackStateCompat state = MediaControllerCompat.getMediaController(getActivity()).getPlaybackState();
            if (state != null) {
                switch (state.getState()) {
                    case PlaybackStateCompat.STATE_PLAYING: // fall through
                    case PlaybackStateCompat.STATE_BUFFERING:
                        pauseMedia();
                        stopSeekbarUpdate();
                        break;
                    case PlaybackStateCompat.STATE_PAUSED:
                    case PlaybackStateCompat.STATE_STOPPED:
                        playMedia();
                        scheduleSeekbarUpdate();
                        break;
                }
            }


//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    PlaybackStateCompat state = MediaControllerCompat.getMediaController(getActivity()).getPlaybackState();
//                    if (state.getState() == PlaybackStateCompat.STATE_PAUSED ||
//                            state.getState() == PlaybackStateCompat.STATE_STOPPED ||
//                            state.getState() == PlaybackStateCompat.STATE_NONE) {
//                        playMedia();
//                    } else if (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
//                            state.getState() == PlaybackStateCompat.STATE_BUFFERING ||
//                            state.getState() == PlaybackStateCompat.STATE_CONNECTING) {
//                        pauseMedia();
//                    }
//                }
//            }, 250);


        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);


        close = (TextView) rootView.findViewById(R.id.close_img);
        down = (TextView) rootView.findViewById(R.id.down_img);


        fTime = (LinearLayout) rootView.findViewById(R.id.full_time);
        fVolume = (SeekBar) rootView.findViewById(R.id.full_volume);
        parts = (LinearLayout) rootView.findViewById(R.id.parts);
        controls = (RelativeLayout) rootView.findViewById(R.id.controls);
        mPicture = (ImageView) rootView.findViewById(R.id.mini_picture);
        mTitle = (TextView) rootView.findViewById(R.id.mini_title);
        mArtist = (TextView) rootView.findViewById(R.id.mini_artist);
        mPlay = (PlayPauseButton) rootView.findViewById(R.id.mini_play);
        mWrapper = rootView.findViewById(R.id.mini_wrapper);
        mProgress = (ProgressBar) rootView.findViewById(R.id.mini_progress);


        mBlurredArt = (ImageView) rootView.findViewById(R.id.album_art_blurred);
        albumart = (ImageView) rootView.findViewById(R.id.album_art);
        shuffle = (ImageView) rootView.findViewById(R.id.shuffle);
        repeat = (ImageView) rootView.findViewById(R.id.repeat);
        next = (ImageView) rootView.findViewById(R.id.next);
        previous = (ImageView) rootView.findViewById(R.id.previous);
        playPauseFloating = (FloatingActionButton) rootView.findViewById(R.id.playpausefloating);

        songtitle = (TextView) rootView.findViewById(R.id.song_title);
        songartist = (TextView) rootView.findViewById(R.id.song_artist);
        songpart = (TextView) rootView.findViewById(R.id.song_part);


        timelyView11 = (TimelyView) rootView.findViewById(R.id.timelyView11);
        timelyView12 = (TimelyView) rootView.findViewById(R.id.timelyView12);
        timelyView13 = (TimelyView) rootView.findViewById(R.id.timelyView13);
        timelyView14 = (TimelyView) rootView.findViewById(R.id.timelyView14);
        timelyView15 = (TimelyView) rootView.findViewById(R.id.timelyView15);
        hourColon = (TextView) rootView.findViewById(R.id.hour_colon);

        mCircularProgress = (CircularSeekBar) rootView.findViewById(R.id.song_progress_circular);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("");
        }


        try {
            MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
            MediaMetadataCompat metadata = controller.getMetadata();

            if (metadata != null) {
                currentMetadata = metadata;
                updateInfo(metadata);
            }
        } catch (Exception e) {
        }


        mMediaBrowser = new MediaBrowserCompat(getActivity(),
                new ComponentName(getActivity(), MusicService.class), mConnectionCallback, null);


        return rootView;
    }


    public void updateSongDetails(final MediaMetadataCompat metadata) {


      /*  try {
            String partsString = (String) metadata.getString(com.audiolaby.view.player.utils.Utils.MEDIA_PARTS);

            JSONObject jsonObject = new JSONObject(partsString);
            JSONArray jarray = jsonObject.getJSONArray("postParts");

            for (int i = 0; i < jarray.length(); ++i) {
                JSONObject itemObject = (JSONObject) jarray.get(i);
                Part part = new Part();
                part.setTitle(itemObject.getString("title"));
                part.setStart_time(itemObject.getLong("start_time"));
                part.setEnd_time(itemObject.getLong("end_time"));
                part.setPosition(itemObject.getInt("position"));
                postParts.add(part);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < postParts.size(); i++) {
            if (postParts.get(i).getEnd_time() > currentPosition) {
                part = i;
                break;
            }

        }

        if (!postParts.isEmpty())
            mCircularProgress.setMax((int) postParts.get(part).getEnd_time().longValue());
        else
            mCircularProgress.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
*/

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        Gson gson = builder.create();
        String partsString = (String) metadata.getString(Utils.MEDIA_POST);



        audioArticle   = gson.fromJson(partsString, AudioArticleMin.class);


      if(audioArticle!=null)
      {

          if(audioArticle.getIs_parted())
          {
              parts.setVisibility(VISIBLE);
              parts.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                      PartsDialog_.builder()
                              .audioArticle(audioArticle)
                              .build()
                              .show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "parts");

                  }
              });
          }
          else
          {
              parts.setVisibility(View.GONE);
              Log.i("","");
          }
      }


        mCircularProgress.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));


        mTitle.setText(metadata.getDescription().getTitle());
        mArtist.setText(metadata.getDescription().getSubtitle());


        try {
            audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            fVolume.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            fVolume.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            fVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            controls.setVisibility(View.GONE);
        }


        mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlaybackStateCompat state = MediaControllerCompat.getMediaController(getActivity()).getPlaybackState();
                if (state.getState() == PlaybackStateCompat.STATE_PAUSED ||
                        state.getState() == PlaybackStateCompat.STATE_STOPPED ||
                        state.getState() == PlaybackStateCompat.STATE_NONE) {
                    playMedia();
                } else if (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                        state.getState() == PlaybackStateCompat.STATE_BUFFERING ||
                        state.getState() == PlaybackStateCompat.STATE_CONNECTING) {
                    pauseMedia();
                }
            }
        });


        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlaybackStateCompat state = MediaControllerCompat.getMediaController(getActivity()).getPlaybackState();
                if (state.getState() == PlaybackStateCompat.STATE_PAUSED ||
                        state.getState() == PlaybackStateCompat.STATE_STOPPED ||
                        state.getState() == PlaybackStateCompat.STATE_NONE) {
                    playMedia();
                } else if (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                        state.getState() == PlaybackStateCompat.STATE_BUFFERING ||
                        state.getState() == PlaybackStateCompat.STATE_CONNECTING) {
                    pauseMedia();
                }
            }
        });

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mProgress.getLayoutParams();
        mProgress.measure(0, 0);
        layoutParams.setMargins(0, -(mProgress.getMeasuredHeight() / 2), 0, 0);
        mProgress.setLayoutParams(layoutParams);
        mProgress.setMax((int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
//                controller.getTransportControls().fastForward();

                int lastposition = mCircularProgress.getProgress();
                int duration = (int) metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
                if (lastposition + 30000 < duration)
                    MediaControllerCompat.getMediaController(getActivity()).getTransportControls().seekTo(lastposition + 30000);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
//                controller.getTransportControls().rewind();

                int lastposition = mCircularProgress.getProgress();
                if (lastposition - 30000 > 0)
                    MediaControllerCompat.getMediaController(getActivity()).getTransportControls().seekTo(lastposition - 30000);
            }
        });

        updateRepeatState();


        songtitle.setText(metadata.getDescription().getTitle());

        songartist.setText(metadata.getDescription().getSubtitle());




        if (metadata.getString(Utils.MEDIA_PART_NAME)!=null) {
            songartist.setText(metadata.getString(Utils.MEDIA_PART_NAME));
            songpart.setVisibility(View.VISIBLE);
        } else {
            songpart.setVisibility(View.GONE);
        }


        playPauseFloating.setOnClickListener(mFLoatingButtonListener);
        playPauseDrawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);
        playPauseFloating.setImageDrawable(playPauseDrawable);


        PlaybackStateCompat state = MediaControllerCompat.getMediaController(getActivity()).getPlaybackState();
        if (state.getState() == PlaybackStateCompat.STATE_PAUSED ||
                state.getState() == PlaybackStateCompat.STATE_STOPPED ||
                state.getState() == PlaybackStateCompat.STATE_NONE) {
            playPauseDrawable.transformToPlay(false);
            mPlay.setPlayed(false);
            mPlay.startAnimation();

        } else if (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING ||
                state.getState() == PlaybackStateCompat.STATE_CONNECTING) {
            playPauseDrawable.transformToPause(false);
            mPlay.setPlayed(true);
            mPlay.startAnimation();

        }


        if (mCircularProgress != null) {


            mCircularProgress.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        MediaControllerCompat.getMediaController(getActivity()).getTransportControls().seekTo(progress);
                    }

                    fTime.setVisibility(VISIBLE);
                    String time = Utils.makeShortTimeString(getActivity(), progress / 1000);
                    if (time.length() < 5) {
                        timelyView11.setVisibility(View.GONE);
                        timelyView12.setVisibility(View.GONE);
                        hourColon.setVisibility(View.GONE);
                        tv13(time.charAt(0) - '0');
                        tv14(time.charAt(2) - '0');
                        tv15(time.charAt(3) - '0');
                    } else if (time.length() == 5) {
                        timelyView12.setVisibility(View.VISIBLE);
                        tv12(time.charAt(0) - '0');
                        tv13(time.charAt(1) - '0');
                        tv14(time.charAt(3) - '0');
                        tv15(time.charAt(4) - '0');
                    } else {
                        timelyView11.setVisibility(View.VISIBLE);
                        hourColon.setVisibility(View.VISIBLE);
                        tv11(time.charAt(0) - '0');
                        tv12(time.charAt(2) - '0');
                        tv13(time.charAt(3) - '0');
                        tv14(time.charAt(5) - '0');
                        tv15(time.charAt(6) - '0');
                    }
                }

                @Override
                public void onStopTrackingTouch(CircularSeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(CircularSeekBar seekBar) {

                }
            });

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowser != null) {
            mMediaBrowser.connect();
        }
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
        }
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.unregisterCallback(mCallback);
        }
    }

    public void onConnected() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            onMetadataChanged(controller.getMetadata());
            onPlaybackStateChanged(controller.getPlaybackState());
            controller.registerCallback(mCallback);
        }
    }

    private void onMetadataChanged(MediaMetadataCompat metadata) {
        if (getActivity() == null) {
            return;
        }
        if (metadata == null) {
            return;
        }

        fetchImageAsync(metadata.getDescription());
        updateSongDetails(metadata);

    }


    private void onPlaybackStateChanged(PlaybackStateCompat state) {
        if (getActivity() == null) {
        }
        if (state == null) {
            return;
        }
        boolean enablePlay = false;
        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PAUSED:
            case PlaybackStateCompat.STATE_STOPPED:
                enablePlay = true;
                break;
            case PlaybackStateCompat.STATE_ERROR:
                Toast.makeText(getActivity(), state.getErrorMessage(), Toast.LENGTH_LONG).show();
                break;
        }

        if (enablePlay) {

            playPauseDrawable.transformToPlay(false);
            mPlay.setPlayed(false);
            mPlay.startAnimation();
        } else {
            playPauseDrawable.transformToPause(false);
            mPlay.setPlayed(true);
            mPlay.startAnimation();
        }

        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        String extraInfo = null;
        if (controller != null && controller.getExtras() != null) {
            String castName = controller.getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);
            if (castName != null) {
                extraInfo = getResources().getString(R.string.casting_to_device, castName);
            }
        }
    }

    private void playMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().play();
        }
    }

    private void pauseMedia() {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
        if (controller != null) {
            controller.getTransportControls().pause();
        }
    }


    //full//

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = new MediaControllerCompat(
                getActivity(), token);
        if (mediaController.getMetadata() == null) {
            //getActivity().finish();
            return;
        }
        MediaControllerCompat.setMediaController(getActivity(), mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackStateCompat state = mediaController.getPlaybackState();
        updatePlaybackState(state);
        MediaMetadataCompat metadata = mediaController.getMetadata();
        if (metadata != null) {
            updateInfo(metadata);
        }
        updateProgress();
        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
            scheduleSeekbarUpdate();
        }
    }

    private void updateProgress() {
        if (mLastPlaybackState == null) {
            return;
        }
        currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }

        if (mCircularProgress != null) {
            mCircularProgress.setProgress((int) currentPosition);
            mProgress.setProgress((int) currentPosition);
        }
    }


    private final Runnable mUpdateProgressTask = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private final Handler mHandler = new Handler();
    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> mScheduleFuture;

    private void scheduleSeekbarUpdate() {
        stopSeekbarUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mUpdateProgressTask);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopSeekbarUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSeekbarUpdate();
        mExecutorService.shutdown();
    }

    private void fetchImageAsync(@NonNull MediaDescriptionCompat description) {
        if (description.getIconUri() == null) {
            return;
        }
        String artUrl = description.getIconUri().toString();

        Picasso.with(getActivity())
                .load(artUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        setBlurredAlbumArt blurredAlbumArt = new setBlurredAlbumArt();
                        blurredAlbumArt.execute(bitmap);
                        albumart.setImageBitmap(bitmap);
                        mPicture.setImageBitmap(bitmap);
                        checkImageColor(bitmap);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                });
    }


    private void updateInfo(MediaMetadataCompat metadata) {
        if (metadata == null) {
            return;
        }
        fetchImageAsync(metadata.getDescription());
        updateSongDetails(metadata);
    }

    private void updatePlaybackState(PlaybackStateCompat state) {
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        MediaControllerCompat controllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (controllerCompat != null && controllerCompat.getExtras() != null) {
            String castName = controllerCompat.getExtras().getString(MusicService.EXTRA_CONNECTED_CAST);

        }

        switch (state.getState()) {
            case PlaybackStateCompat.STATE_PLAYING:
                scheduleSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_NONE:
                Log.i("", "");
            case PlaybackStateCompat.STATE_STOPPED:
                stopSeekbarUpdate();
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                stopSeekbarUpdate();
                break;
        }

        // mSkipNext.setVisibility((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) == 0? INVISIBLE : VISIBLE);
        // mSkipPrev.setVisibility((state.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) == 0? INVISIBLE : VISIBLE);
    }


    ////newFull/////

    private class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Bitmap... loadedImage) {
            Drawable drawable = null;
            try {
                drawable = Utils.createBlurredImageFromBitmap(loadedImage[0], getActivity(), 6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                if (mBlurredArt.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    mBlurredArt.getDrawable(),
                                    result
                            });
                    mBlurredArt.setImageDrawable(td);
                    td.startTransition(200);

                } else {
                    mBlurredArt.setImageDrawable(result);
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }


    public void changeDigit(TimelyView tv, int end) {
        ObjectAnimator obja = tv.animate(end);
        obja.setDuration(400);
        obja.start();
    }

    public void changeDigit(TimelyView tv, int start, int end) {
        try {
            ObjectAnimator obja = tv.animate(start, end);
            obja.setDuration(400);
            obja.start();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    public void tv11(int a) {
        if (a != timeArr[0]) {
            changeDigit(timelyView11, timeArr[0], a);
            timeArr[0] = a;
        }
    }

    public void tv12(int a) {
        if (a != timeArr[1]) {
            changeDigit(timelyView12, timeArr[1], a);
            timeArr[1] = a;
        }
    }

    public void tv13(int a) {
        if (a != timeArr[2]) {
            changeDigit(timelyView13, timeArr[2], a);
            timeArr[2] = a;
        }
    }

    public void tv14(int a) {
        if (a != timeArr[3]) {
            changeDigit(timelyView14, timeArr[3], a);
            timeArr[3] = a;
        }
    }

    public void tv15(int a) {
        if (a != timeArr[4]) {
            changeDigit(timelyView15, timeArr[4], a);
            timeArr[4] = a;
        }
    }


    public void updateRepeatState() {
        if (repeat != null && getActivity() != null) {
//            MaterialDrawableBuilder builder = MaterialDrawableBuilder.with(getActivity()).setSizeDp(30);
//
//
//            if (MediaControllerCompat.getMediaController(getActivity()).getRepeatMode() == 0) {
//                builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT);
//            } else if (MediaControllerCompat.getMediaController(getActivity()).getRepeatMode() == 1) {
//                builder.setIcon(MaterialDrawableBuilder.IconValue.REPEAT_ONCE);
//            }


            //  repeat.setImageDrawable(builder.build());
            repeat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaControllerCompat controller = MediaControllerCompat.getMediaController(getActivity());
                    if (controller != null) {
                        controller.getTransportControls().setRepeatMode(1);
                    }
                    updateRepeatState();
                }
            });
        }
    }

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            PlaybackControlsFragment.this.onPlaybackStateChanged(state);
            updatePlaybackState(state);

        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            if (metadata == null) {
                return;
            }

            PlaybackControlsFragment.this.onMetadataChanged(metadata);
            updateInfo(metadata);
        }
    };

    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                    }
                }
            };


    @Background
    void checkImageColor(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                int color = p.getDominantColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
                if (1.0d - ((((0.299d * ((double) Color.red(color))) + (0.587d * ((double) Color.green(color)))) + (0.114d * ((double) Color.blue(color)))) / 255.0d) > 0.5d) {
                    close.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    down.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                } else {
                    close.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    down.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                }
            }
        });
    }




}