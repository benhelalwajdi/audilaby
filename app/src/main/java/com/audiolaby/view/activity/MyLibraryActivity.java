package com.audiolaby.view.activity;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.audiolaby.R;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.AudioArticleMin;
import com.audiolaby.persistence.model.Part;
import com.audiolaby.persistence.model.User;
import com.audiolaby.view.adapter.AudioLibraryAdapter;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

@EActivity(R.layout.activity_library)
public class MyLibraryActivity extends BasePlayerActivity implements AudioLibraryAdapter.OnClickListener {


    @Bean
    LibraryDAO libraryDAO;


    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout refresh;
    @ViewById(R.id.list)
    RecyclerView list;

    User user;
    AudioLibraryAdapter adapter;


    private ArrayList<MediaMetadataCompat> mediaMetadataCompats = new ArrayList<MediaMetadataCompat>();

    @AfterViews
    void afterViewsInjection() {

        setSupportActionBar(this.toolbar);
        super.afterViewsInjection();
        this.user = this.libraryDAO.getUser();
        this.adapter = new AudioLibraryAdapter(MyLibraryActivity.this, new ArrayList());
        this.list.setLayoutManager(new LinearLayoutManager(this));
        this.list.setAdapter(new SlideInBottomAnimatorAdapter(this.adapter, this.list));

        // viewHolder.adapter.setMenuItemClickListener(new onMenuItemClickListener(section, position));
        // this.list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        updateList(libraryDAO.getAudioPost());
    }


    @UiThread
    <T> void updateList(List<AudioArticle> postList) {
        if (postList != null && !postList.isEmpty()) {

            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            Gson gson = builder.create();


            for (Iterator<AudioArticle> iterator = postList.iterator(); iterator.hasNext(); ) {
                AudioArticle audioArticle = iterator.next();

                AudioArticleMin audioArticleMin = new AudioArticleMin(audioArticle);

                if (audioArticle.getIs_parted())

                {

                    for (int i = 0; i < audioArticle.getPostParts().size(); i++) {
                        Part part = audioArticle.getPostParts().get(i);
                        File file = new File(part.getLocalAudioUrl());
                        if (file.exists()) {

                            MediaMetadataCompat media = new MediaMetadataCompat.Builder()
                                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audioArticle.getPost_id() + "-" + i)
                                    .putString(com.audiolaby.view.player.utils.Utils.CUSTOM_METADATA_TRACK_SOURCE, part.getLocalAudioUrl())
                                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, audioArticle.getAuthor().getName())
                                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audioArticle.getVoiceOver().getName())
                                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Long.parseLong("" + part.getDuration()) * 1000)
                                    .putString(MediaMetadataCompat.METADATA_KEY_GENRE, audioArticle.getCategory().getName())
                                    .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, audioArticle.getCover())
                                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audioArticle.getTitle())
                                    .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 1)
                                    .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, 1)
                                    .putString(com.audiolaby.view.player.utils.Utils.MEDIA_PART_NAME, part.getTitle())
                                    .putString(com.audiolaby.view.player.utils.Utils.MEDIA_POST, gson.toJson(audioArticleMin))
                                    .build();
                            mediaMetadataCompats.add(media);


                        } else {
                            audioArticle.delete();
                            iterator.remove();
                            break;
                        }
                    }
                } else {
                    File file = new File(audioArticle.getLocalAudioUrl());
                    if (file.exists()) {
                        mediaMetadataCompats.add(
                                new MediaMetadataCompat.Builder()
                                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audioArticle.getPost_id())
                                        .putString(com.audiolaby.view.player.utils.Utils.CUSTOM_METADATA_TRACK_SOURCE, audioArticle.getLocalAudioUrl())
                                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, audioArticle.getAuthor().getName())
                                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audioArticle.getVoiceOver().getName())
                                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Long.parseLong("" + audioArticle.getRuntime()) * 1000)
                                        .putString(MediaMetadataCompat.METADATA_KEY_GENRE, audioArticle.getCategory().getName())
                                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, audioArticle.getCover())
                                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audioArticle.getTitle())
                                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 1)
                                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, 1)
                                        .putString(com.audiolaby.view.player.utils.Utils.MEDIA_POST, gson.toJson(audioArticleMin))
                                        .build()
                        );
                    } else {
                        audioArticle.delete();
                        iterator.remove();
                    }
                }


            }


            this.adapter.setAudioArticles(postList);
            this.adapter.notifyDataSetChanged();
        } else {
            this.adapter.setAudioArticles(postList);
            this.adapter.notifyDataSetChanged();
            showError(getString(R.string.error_empty_table));
        }
    }


    @Override
    public void onClick(final AudioArticle audioArticle, int position) {
        if (adapter.getSelected().isEmpty())
            putMusic(mediaMetadataCompats);


        if (audioArticle.getIs_parted())

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //checkForUserVisibleErrors(false);
                    onMediaItemSelected(audioArticle.getPost_id() + "-0");
                }
            }, 500);
        else
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    //checkForUserVisibleErrors(false);
                    onMediaItemSelected(audioArticle.getPost_id());
                }
            }, 500);


        adapter.setSelected(audioArticle.getPost_id());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLongClick(AudioArticle audioArticle, int position) {
        Menu menu;
        MenuSheetView menuSheetView = new MenuSheetView(this, MenuSheetView.MenuType.LIST, null, new onMenuItemClickListener(audioArticle, position));
        menu = menuSheetView.getMenu();
        menuSheetView.inflateMenu(R.menu.audio_post_library);
        menu.findItem(R.id.pause).setVisible(false);
        menu.findItem(R.id.preview).setVisible(false);
        menu.findItem(R.id.share).setVisible(false);
        menuSheetView.updateMenu();
        this.layout.showWithSheetView(menuSheetView);
    }

    class onMenuItemClickListener implements MenuSheetView.OnMenuItemClickListener {
        AudioArticle audioArticle;
        int position;

        onMenuItemClickListener(AudioArticle audioArticle, int position) {
            this.audioArticle = audioArticle;
            this.position = position;
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (MyLibraryActivity.this.layout.isSheetShowing()) {
                MyLibraryActivity.this.layout.dismissSheet();
            }
            switch (item.getItemId()) {
                case R.id.preview:
                    AudioDetailsActivity_.intent(MyLibraryActivity.this)
                            .audioArticle(audioArticle)
                            .position(position)
                            .start();
                    return true;
                case R.id.remove:
                    remove(audioArticle, position);
                    return true;
                case R.id.play:
                    onClick(audioArticle, position);
                    return true;
                default:
                    return false;
            }
        }
    }


    boolean shouldShowControls() {
        super.shouldShowControls();
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController == null ||
                mediaController.getMetadata() == null ||
                mediaController.getPlaybackState() == null) {
            adapter.setSelected("");
            adapter.notifyDataSetChanged();
            return false;
        }
        switch (mediaController.getPlaybackState().getState()) {
            case PlaybackStateCompat.STATE_ERROR:
            case PlaybackStateCompat.STATE_NONE:
            case PlaybackStateCompat.STATE_STOPPED:
                adapter.setSelected("");
                adapter.notifyDataSetChanged();
                return false;
            default:
                adapter.setSelected(mediaController.getMetadata().getDescription().getMediaId());
                adapter.notifyDataSetChanged();
                return true;
        }
    }

    @UiThread
    void remove(final AudioArticle audioArticle, final int position) {
        if (audioArticle.getPost_id().equals(adapter.getSelected()))
            onCloseClicked();
        audioArticle.delete();
        try {
            new File(audioArticle.getLocalAudioUrl()).delete();
        } catch (Exception e) {
        }
        List<AudioArticle> posts = this.adapter.getAudioArticles();
        posts.remove(position);
        updateList(posts);
    }
}
