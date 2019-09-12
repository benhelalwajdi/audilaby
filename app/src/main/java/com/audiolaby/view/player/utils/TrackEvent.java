package com.audiolaby.view.player.utils;


import android.support.v4.media.MediaMetadataCompat;

import java.util.ArrayList;

public class TrackEvent {

    private ArrayList<MediaMetadataCompat> mediaMetadataCompats;

    public TrackEvent(ArrayList<MediaMetadataCompat> mediaMetadataCompats) {
        this.mediaMetadataCompats = mediaMetadataCompats;
    }

    public ArrayList<MediaMetadataCompat> getMediaMetadataCompats() {
        return mediaMetadataCompats;
    }

    public void setMediaMetadataCompats(ArrayList<MediaMetadataCompat> mediaMetadataCompats) {
        this.mediaMetadataCompats = mediaMetadataCompats;
    }
}
