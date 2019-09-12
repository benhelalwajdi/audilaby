package com.audiolaby.view.activity;

import android.content.Intent;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.audiolaby.R;
import com.audiolaby.controller.AudioController;
import com.audiolaby.controller.model.Pagination;
import com.audiolaby.controller.request.PostDetailsRequest;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.controller.response.PostsListResponse;
import com.audiolaby.controller.response.UpdateWishlistResponse;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.AudioArticleMin;
import com.audiolaby.persistence.model.Part;
import com.audiolaby.persistence.model.User;
import com.audiolaby.view.adapter.AudioLibraryAdapter;
import com.audiolaby.view.adapter.decoration.EndlessRecyclerOnScrollListener;
import com.flipboard.bottomsheet.commons.MenuSheetView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;

import static com.audiolaby.Constants.POST_FIRST;
import static com.audiolaby.Constants.POST_MORE_SIZE;

@EActivity(R.layout.activity_wishlist)
public class MyWishlistActivity extends BasePlayerActivity implements AudioLibraryAdapter.OnClickListener {


    @Bean
    LibraryDAO libraryDAO;
    @Bean
    AudioController audioController;


    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout refresh;
    @ViewById(R.id.list)
    RecyclerView list;

    User user;
    AudioLibraryAdapter adapter;


    Boolean isRefresh = false;
    Boolean isVideo = false;


    int pos =1;

    MyWishlistActivity.EndlessScrollListener endlessScrollListener;


    private ArrayList<MediaMetadataCompat> mediaMetadataCompats = new ArrayList<MediaMetadataCompat>();



    @AfterViews
    void afterViewsInjection() {

        setSupportActionBar(this.toolbar);
        super.afterViewsInjection();
        this.user = this.libraryDAO.getUser();
        this.adapter = new AudioLibraryAdapter(MyWishlistActivity.this, new ArrayList());
        // viewHolder.adapter.setMenuItemClickListener(new onMenuItemClickListener(section, position));
        // this.list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        //  this.adapter.setMenuItemClickListener(new C07772());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //manager.setSpanSizeLookup(new spanSizeLookup(2));
        new MyWishlistActivity.EndlessScrollListener(manager);
        this.list.setLayoutManager(manager);
        this.list.setAdapter(new SlideInBottomAnimatorAdapter(this.adapter, this.list));
        refresh.setOnRefreshListener(new MyWishlistActivity.refreshLListener());
        endlessScrollListener = new MyWishlistActivity.EndlessScrollListener(manager);
        // this.list.addOnScrollListener(endlessScrollListener);

        wishlist(POST_FIRST);

    }


    @Override
    public void onClick(final AudioArticle audioArticle, int position) {
        // if (adapter.getSelected().isEmpty())
        //    putMusic(mediaMetadataCompats);

            saveListning(audioArticle);
            if(audioArticle.getIs_parted())

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //checkForUserVisibleErrors(false);
                        onMediaItemSelected(audioArticle.getPost_id()+"-0");
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
    public void onLongClick(final AudioArticle audioArticle, final int position) {
        Menu menu;
        MenuSheetView menuSheetView = new MenuSheetView(this, MenuSheetView.MenuType.LIST, null, new onMenuItemClickListener(audioArticle,position));
        menu = menuSheetView.getMenu();
        menuSheetView.inflateMenu(R.menu.audio_post_wishlist);
        //menu.getItem(0).setIcon(Utils.getDrawableMenu(this, R.string.icon_heart_empty, R.color.text_black, 0));
        menu.findItem(R.id.pause).setVisible(false);
       // menu.findItem(R.id.share).setVisible(false);

        menuSheetView.updateMenu();
        this.layout.showWithSheetView(menuSheetView);
    }

    class onMenuItemClickListener implements MenuSheetView.OnMenuItemClickListener {
        AudioArticle audioArticle;
        int position;
        onMenuItemClickListener(AudioArticle audioArticle, int position) {
            this.audioArticle = audioArticle;
            this.position=position;
        }

        public boolean onMenuItemClick(MenuItem item) {
            if (MyWishlistActivity.this.layout.isSheetShowing()) {
                MyWishlistActivity.this.layout.dismissSheet();
            }
            switch (item.getItemId()) {
                case R.id.preview:
                    AudioDetailsActivity_.intent(MyWishlistActivity.this)
                            .audioArticle(audioArticle)
                            .position(position)
                            .start();
                    return true;
                case  R.id.share:

                    String url = "http://audiolaby.dev-fnode.com/api/share-post/" + audioArticle.getPost_id() ;
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_string));
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_string) + "\n" + url);
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_string)));

                    return true;
                case R.id.remove:
                    remove(audioArticle,position);
                    return true;
                case R.id.play:
                    onClick(audioArticle,position);
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


    @Background
    void saveListning(AudioArticle audioArticle) {
        CommonResponse response = (CommonResponse) this.audioController.saveListning(
                new PostDetailsRequest(audioArticle.getPost_id())
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                saveListning(audioArticle);
                break;
        }
    }

    @Background(id = "longtask")
    void wishlist(final int page) {
        if (!isRefresh)
            showProgress();
        isRefresh = false;
        Pagination pagination = new Pagination(page, POST_MORE_SIZE);
        PostsListResponse response = (PostsListResponse) this.audioController.wishlist(pagination);
        String handlerResponse = responseHandler(response);
        dimissProgress();
        switch (handlerResponse) {
            case "again":
                wishlist(page);
                break;
            case "handler":
                if (response.getPosts() != null && !response.getPosts().isEmpty())
                    updateList(page, response.getPosts());
                break;
            default:
                showError(handlerResponse);
        }
    }

    @UiThread
    <T> void updateList(int page, List<AudioArticle> postList) {
        refresh.setRefreshing(false);

        if (page == POST_FIRST) {
            endlessScrollListener.reset();
            this.adapter.setAudioArticles(postList);
            this.adapter.notifyDataSetChanged();
        } else {
            List<AudioArticle> posts = this.adapter.getAudioArticles();
            posts.addAll(postList);
            this.adapter.setAudioArticles(posts);
            this.adapter.notifyDataSetChanged();
        }

        mediaMetadataCompats.clear();





        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        Gson gson = builder.create();



        for (AudioArticle audioArticle : adapter.getAudioArticles()) {
            AudioArticleMin audioArticleMin = new AudioArticleMin(audioArticle);



            if(audioArticle.getIs_parted())

            {

                for (int i = 0; i < audioArticle.getPostParts().size(); i++) {
                    Part part = audioArticle.getPostParts().get(i);
                    MediaMetadataCompat media = new MediaMetadataCompat.Builder()
                            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audioArticle.getPost_id() + "-" + i)
                            .putString(com.audiolaby.view.player.utils.Utils.CUSTOM_METADATA_TRACK_SOURCE, part.getAudioUrl())
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



                }
            }

            else
            {

                mediaMetadataCompats.add(
                        new MediaMetadataCompat.Builder()
                                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audioArticle.getPost_id())
                                .putString(com.audiolaby.view.player.utils.Utils.CUSTOM_METADATA_TRACK_SOURCE, audioArticle.getAudioUrl())
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

            }
        }





        //if (adapter.getSelected().isEmpty())
        putMusic(mediaMetadataCompats);
    }


    class EndlessScrollListener extends EndlessRecyclerOnScrollListener {
        EndlessScrollListener(LinearLayoutManager linearLayoutManager) {
            super(linearLayoutManager);
        }

        public void onLoadMore(int currentPage) {
            wishlist(currentPage);
        }
    }

    class refreshLListener implements SwipeRefreshLayout.OnRefreshListener {
        refreshLListener() {
        }

        @Override
        public void onRefresh() {
            isRefresh = true;
            wishlist(POST_FIRST);
        }

    }

    @Background
    void remove(final AudioArticle audioArticle, final  int position) {
        UpdateWishlistResponse response = (UpdateWishlistResponse) this.audioController.updateWishlist(
                new PostDetailsRequest(audioArticle.getPost_id())
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                remove(audioArticle,position);
                break;
            case "handler":
                updateWishlist(audioArticle,position);
                break;
            default:
                showError(handlerResponse);
                break;
        }
    }

    @UiThread
    void updateWishlist(final AudioArticle audioArticle, final  int position) {
        if(audioArticle.getPost_id().equals(adapter.getSelected()))
            onCloseClicked();
        List<AudioArticle> posts = this.adapter.getAudioArticles();
        posts.remove(position);
        updateList(POST_FIRST,posts);
    }





}
