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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.audiolaby.view.player.utils.QueueHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class QueueManager {

    private MusicProvider mMusicProvider;
    private MetadataUpdateListener mListener;
    private Resources mResources;
    private Context mContext;

    // "Now playing" queue:
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    private int mCurrentIndex;

    public QueueManager(Context context,
                        @NonNull MusicProvider musicProvider,
                        @NonNull Resources resources,
                        @NonNull MetadataUpdateListener listener) {
        this.mContext = context;
        this.mMusicProvider = musicProvider;
        this.mListener = listener;
        this.mResources = resources;

        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        mCurrentIndex = 0;
    }


    private void setCurrentQueueIndex(int index) {
        if (index >= 0 && index < mPlayingQueue.size()) {
            mCurrentIndex = index;
            mListener.onCurrentQueueIndexUpdated(mCurrentIndex);
        }
    }

    public boolean setCurrentQueueItem(long queueId) {
        // set the current index on queue from the queue Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, queueId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean setCurrentQueueItem(String mediaId) {
        // set the current index on queue from the music Id:
        int index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, mediaId);
        setCurrentQueueIndex(index);
        return index >= 0;
    }

    public boolean skipQueuePosition(int amount) {
        if(mPlayingQueue.size()==1)
            return false;
        int index = mCurrentIndex + amount;
        if (index < 0) {
            // skip backwards before the first song will keep you on the first song
            index = 0;
        } else {
            // skip forwards when in last song will cycle back to start of the queue
            index %= mPlayingQueue.size();
        }
        if (!QueueHelper.isIndexPlayable(index, mPlayingQueue)) {
            return false;
        }
        mCurrentIndex = index;
        return true;
    }




    public void setQueueFromMusic(String mediaId) {
        setCurrentQueue("currrent",
                QueueHelper.getPlayingQueue(mMusicProvider), mediaId);

        updateMetadata();
    }

    public MediaSessionCompat.QueueItem getCurrentMusic() {
        if (!QueueHelper.isIndexPlayable(mCurrentIndex, mPlayingQueue)) {
            return null;
        }
        return mPlayingQueue.get(mCurrentIndex);
    }

    public int getCurrentQueueSize() {
        if (mPlayingQueue == null) {
            return 0;
        }
        return mPlayingQueue.size();
    }

    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue) {
        setCurrentQueue(title, newQueue, null);
    }

    protected void setCurrentQueue(String title, List<MediaSessionCompat.QueueItem> newQueue,
                                   String initialMediaId) {
        mPlayingQueue = newQueue;
        int index = 0;
        if (initialMediaId != null) {
            index = QueueHelper.getMusicIndexOnQueue(mPlayingQueue, initialMediaId);
        }
        mCurrentIndex = Math.max(index, 0);
        mListener.onQueueUpdated(title, newQueue);
    }

    public void updateMetadata() {


        final MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
        if (currentMusic == null) {
            mListener.onMetadataRetrieveError();
            return;
        }
        MediaMetadataCompat metadata = mMusicProvider.getMusic(currentMusic.getDescription().getMediaId());
        if (metadata == null) {
            throw new IllegalArgumentException("Invalid musicId ");
        }

        mListener.onMetadataChanged(metadata);

        // Set the proper album artwork on the media session, so it can be shown in the
        // locked screen and in other places.
        if (metadata.getDescription().getIconBitmap() == null &&
                metadata.getDescription().getIconUri() != null) {
            String albumUri = metadata.getDescription().getIconUri().toString();

            Picasso.with(mContext)
                    .load(albumUri)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                           mMusicProvider.updateMusicArt(currentMusic.getDescription().getMediaId(), bitmap, bitmap);

                            // If we are still playing the same music, notify the listeners:
                            MediaSessionCompat.QueueItem currentMusic2 = getCurrentMusic();
                            if (currentMusic2 == null) {
                                return;
                            }
                            if (currentMusic.getDescription().getMediaId().equals(currentMusic2.getDescription().getMediaId())) {
                                mListener.onMetadataChanged(mMusicProvider.getMusic(currentMusic2.getDescription().getMediaId()));
                            }
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }
                    });

//            private static final int MAX_ART_WIDTH = 800;  // pixels
//            private static final int MAX_ART_HEIGHT = 480;  // pixels
//            private static final int MAX_ART_WIDTH_ICON = 128;  // pixels
//            private static final int MAX_ART_HEIGHT_ICON = 128;  // pixels
//
//            AlbumArtCache.getInstance().fetch(albumUri, new AlbumArtCache.FetchListener() {
//                @Override
//                public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
//                    mMusicProvider.updateMusicArt(musicId, bitmap, icon);
//
//                    // If we are still playing the same music, notify the listeners:
//                    MediaSessionCompat.QueueItem currentMusic = getCurrentMusic();
//                    if (currentMusic == null) {
//                        return;
//                    }
//                    String currentPlayingId = MediaIDHelper.extractMusicIDFromMediaID(
//                            currentMusic.getDescription().getMediaId());
//                    if (musicId.equals(currentPlayingId)) {
//                        mListener.onMetadataChanged(mMusicProvider.getMusic(currentPlayingId));
//                    }
//                }
//            });
        }
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);

        void onMetadataRetrieveError();

        void onCurrentQueueIndexUpdated(int queueIndex);

        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
