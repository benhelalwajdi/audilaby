

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

import android.graphics.Bitmap;
import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MusicProvider {


    public ConcurrentMap<String, MediaMetadataCompat> mMusicListById;


    enum State {
        NON_INITIALIZED, INITIALIZING, INITIALIZED
    }

    private volatile State mCurrentState = State.NON_INITIALIZED;



    public MusicProvider() {
        mMusicListById = new ConcurrentHashMap<>();
    }


    public Iterable<MediaMetadataCompat> getShuffledMusic() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        List<MediaMetadataCompat> shuffled = new ArrayList<>(mMusicListById.size());
        for (MediaMetadataCompat mediaMetadataCompat : mMusicListById.values()) {
            shuffled.add(mediaMetadataCompat);
        }
        Collections.shuffle(shuffled);
        return shuffled;
    }

    public MediaMetadataCompat getMusic(String musicId) {
        return mMusicListById.containsKey(musicId) ? mMusicListById.get(musicId) : null;
    }

    public synchronized void updateMusicArt(String musicId, Bitmap albumArt, Bitmap icon) {
        MediaMetadataCompat metadata = getMusic(musicId);
        metadata = new MediaMetadataCompat.Builder(metadata)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, icon)
                .build();

        metadata = mMusicListById.get(musicId);
    }


    public ConcurrentMap<String, MediaMetadataCompat> getmMusicListById() {
        return mMusicListById;
    }

    public void putMusic(ArrayList<MediaMetadataCompat> mediaMetadataCompats ) {
        mMusicListById.clear();
        for(MediaMetadataCompat mediaMetadataCompat:mediaMetadataCompats )
            mMusicListById.putIfAbsent(mediaMetadataCompat.getDescription().getMediaId(),mediaMetadataCompat);
    }

}


