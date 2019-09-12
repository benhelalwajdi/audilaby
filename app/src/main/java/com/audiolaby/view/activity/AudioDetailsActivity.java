package com.audiolaby.view.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.audiolaby.Audiolaby;
import com.audiolaby.Constants;
import com.audiolaby.R;
import com.audiolaby.controller.AccountController;
import com.audiolaby.controller.AudioController;
import com.audiolaby.controller.enumeration.ActionField;
import com.audiolaby.controller.enumeration.TypeField;
import com.audiolaby.controller.request.PostDetailsRequest;
import com.audiolaby.controller.request.RatingRequest;
import com.audiolaby.controller.request.SaveCommentRequest;
import com.audiolaby.controller.request.SavePaidAudioRequest;
import com.audiolaby.controller.response.CommentsListResponse;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.controller.response.PostDetailsResponse;
import com.audiolaby.controller.response.SaveOrderResponse;
import com.audiolaby.controller.response.SaveRatingResponse;
import com.audiolaby.controller.response.UpdateWishlistResponse;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.AudioArticle;
import com.audiolaby.persistence.model.AudioArticleMin;
import com.audiolaby.persistence.model.Comment;
import com.audiolaby.persistence.model.Download;
import com.audiolaby.persistence.model.Part;
import com.audiolaby.persistence.model.Tag;
import com.audiolaby.persistence.model.User;
import com.audiolaby.receiver.DownloadService_;
import com.audiolaby.util.TagGroup;
import com.audiolaby.util.Utils;
import com.audiolaby.view.adapter.AudioCommetnsAdapter;
import com.audiolaby.view.adapter.AudioPostAdapter;
import com.audiolaby.view.dialog.CommentsDialog_;
import com.audiolaby.view.layout.SmoothLinearLayoutManager;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import it.gmariotti.recyclerview.adapter.ScaleInAnimatorAdapter;


@EActivity(R.layout.activity_audio_details)
public class AudioDetailsActivity extends BasePlayerActivity implements BillingProcessor.IBillingHandler, RatingDialogListener {
    @Bean
    AccountController accountController;


    @Bean
    LibraryDAO libraryDAO;

    @Bean
    AudioController audioController;

    @App
    Audiolaby audiolaby;


    private BillingProcessor billingProcessor;
    private boolean readyToPurchase = false;
    private Boolean isBuying = false;


    int ind=1;

    private User user;

    @Extra
    AudioArticle audioArticle;
    @Extra
    int position;
    @Extra
    String transition;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.wishlist)
    FloatingActionButton wishlist;
    @ViewById(R.id.rating)
    AppCompatRatingBar rating;
    @ViewById(R.id.progress_rating)
    LinearLayout progressRating;
    @ViewById(R.id.rating_dialog)
    LinearLayout ratingDialog;


    @ViewById(R.id.buy)
    AppCompatButton buy;
    @ViewById(R.id.free)
    LinearLayout free;


    @ViewById(R.id.info)
    LinearLayout info;
    @ViewById(R.id.cover)
    KenBurnsView cover;

    @ViewById(R.id.title)
    TextView title;

    @ViewById(R.id.date)
    TextView date;
    @ViewById(R.id.time)
    TextView time;
    @ViewById(R.id.category)
    TextView category;
    @ViewById(R.id.type)
    TextView type;
    @ViewById(R.id.author)
    TextView author;
    @ViewById(R.id.voiceover)
    TextView voiceover;
    @ViewById(R.id.rater)
    TextView rater;
    @ViewById(R.id.listner)
    TextView listner;
    @ViewById(R.id.downloader)
    TextView downloader;
    @ViewById(R.id.download)
    RelativeLayout download;
    @ViewById(R.id.description)
    TextView description;


    @ViewById(R.id.tags)
    TagGroup tags;
    @ViewById(R.id.tags_layout)
    LinearLayout tagsLayout;


    @ViewById(R.id.download_parcent)
    TextView downloadParcent;
    @ViewById(R.id.download_progress)
    ProgressBar covdownloadProgress;
    @ViewById(R.id.downloading_off)
    RelativeLayout downloadingOff;
    @ViewById(R.id.downloading_on)
    RelativeLayout downloadingOn;


    @ViewById(R.id.comments)
    RecyclerView comments;
    @ViewById(R.id.comments_layout)
    LinearLayout commentsLayout;
    @ViewById(R.id.comments_more)
    Button commentsMore;
    AudioCommetnsAdapter commentAdapter;


    @ViewById(R.id.category_layout)
    LinearLayout categoryLayout;

    @ViewById(R.id.download)
    RelativeLayout downloadBB;

    @ViewById(R.id.category_elements)
    RecyclerView categoryElements;
    AudioPostAdapter cAudioPostAdapter;


    @ViewById(R.id.author_layout)
    LinearLayout authorLayout;

    @ViewById(R.id.sliding_layout)
    SlidingUpPanelLayout sliding_layout;

    @ViewById(R.id.author_elements)
    RecyclerView authorElements;
    AudioPostAdapter aAudioPostAdapter;


    @ViewById(R.id.voiceover_layout)
    LinearLayout voiceoverLayout;
    @ViewById(R.id.voiceover_elements)
    RecyclerView voiceoverElements;
    AudioPostAdapter vAudioPostAdapter;


    AppRatingDialog appRatingDialog;

    @StringRes(R.string.runtime)
    String runtime;
    @StringRes(R.string.free)
    String freeString;
    @StringRes(R.string.not_free)
    String notFree;
    @StringRes(R.string.author_name)
    String authorName;
    @StringRes(R.string.voiceover_name)
    String voiceName;
    @StringRes(R.string.error_buy_not_availble)
    String notAvailble;
    @StringRes(R.string.error_buy_not_ready)
    String notReady;

    public static final String MY_PREFS_NAME = "Downloads";

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ArrayList<MediaMetadataCompat> mediaMetadataCompats = new ArrayList<MediaMetadataCompat>();


    int adsType = 0;

    @Click(R.id.category_more)
    void clickCategory() {
        MoreActivity_.intent(AudioDetailsActivity.this)
                .title(audioArticle.getCategory().getName())
                .type(TypeField.category.name())
                .id(audioArticle.getCategory().getCategory_id())
                .start();
    }

    @Click(R.id.author_more)
    void clickAuthor() {
        MoreActivity_.intent(AudioDetailsActivity.this)
                .title(audioArticle.getAuthor().getName())
                .type(TypeField.authors.name())
                .id(audioArticle.getAuthor().getAuthor_id())
                .start();
    }

    @Click(R.id.voiceover_more)
    void clickVoiceOver() {
        MoreActivity_.intent(AudioDetailsActivity.this)
                .title(audioArticle.getVoiceOver().getName())
                .type(TypeField.voiceovers.name())
                .id(audioArticle.getVoiceOver().getVoiceOver_id())
                .start();
    }

    @Click(R.id.comments_more)
    void clickComments() {
        CommentsDialog_.builder()
                .post(audioArticle)
                .build()
                .show(((AppCompatActivity) this).getSupportFragmentManager(), "comments");

    }


    @Click(R.id.wishlist)
    void clickWishlist() {
        wishlist.setImageDrawable(Utils.getDrawable(this, R.string.icon_spinner, R.color.white, 0));
        saveWishlist();
    }


    @Click(R.id.share)
    void share() {
        String url = "http://audiolaby.dev-fnode.com/api/share-post/" + audioArticle.getPost_id();

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_string));
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.share_string) + "\n" + url);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_string)));

    }



    @Click(R.id.listen)
    void clickListen() {
            saveListning();
            putMusic(mediaMetadataCompats);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    onMediaItemSelected(audioArticle.getPost_id());
                }
            }, 500);

    }

    @Click(R.id.buy)
    public void buy() {

        if (!readyToPurchase)
            showError(notReady);
        else {
            isBuying = true;
            billingProcessor.purchase(this, audioArticle.getCode());
            //billingProcessor.purchase(this, "android.test.purchased");

        }
    }


    @Click(R.id.download)
    public void download() {
        if (checkPermission()) {
                startDownload();
        } else {
            requestPermission();
        }

    }


    @Click(R.id.rating_dialog)
    public void rate() {
        appRatingDialog = new AppRatingDialog.Builder()
                .setPositiveButtonText("حفظ")
                .setNegativeButtonText("الغاء")
                .setNeutralButtonText("لاحقا")
                .setNumberOfStars(5)
                // .setNoteDescriptions(Arrays.asList("سيئ جدا", "سيء", "جيد", "جيد جدا", "رائع!!!"))
                .setDefaultRating(0)
                .setDescription("-")
                .setTitle("قم بالتقييم و التفاعل من خلال التعليق")
                // .setDefaultComment("استفدت كثيرا !!")
                .setStarColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.text_black)
                .setTitleTextColor(R.color.text_black)
                .setDescriptionTextColor(R.color.text_black)
                .setHint("قم باضفة تعليق هنا")
                .setHintTextColor(R.color.text_hint)
                .setCommentTextColor(R.color.text_black)
                .setCommentBackgroundColor(R.color.grey1)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(AudioDetailsActivity.this);
        appRatingDialog.show();
    }


    @AfterViews
    void afterViewsInjection() {
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(StringUtils.EMPTY);
        super.afterViewsInjection();



        if (Build.VERSION.SDK_INT >= 21) {
            //    ViewCompat.setTransitionName(this.cover, transition);
        }


        if (audioArticle.getDescription() != null) {
            description.setText(audioArticle.getDescription());
        }
        if (audioArticle.getTitle() != null) {
            title.setText(audioArticle.getTitle());
        }
        Picasso.with(this).load(audioArticle.getCover())
                .error((int) R.drawable.empty)
                .placeholder((int) R.drawable.empty)
                .into(this.cover);

        if (!BillingProcessor.isIabServiceAvailable(this)) {
            showError(notAvailble);
        }
        billingProcessor = new BillingProcessor(this, Constants.IN_APP_LICENSE_KEY, this);


        getAudio();
    }


    @Background
    void getAudio() {
        PostDetailsResponse response = (PostDetailsResponse) this.audioController.postDetails(
                new PostDetailsRequest(audioArticle.getPost_id())
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                getAudio();
                break;
            case "handler":
                if (response.getPost() != null)
                    updateUi(response.getPost());
                else
                    showError(emptyTable);
                break;
            default:
                showError(handlerResponse);
        }
    }


    @Background
    void saveListning() {
        CommonResponse response = (CommonResponse) this.audioController.saveListning(
                new PostDetailsRequest(audioArticle.getPost_id())
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                saveListning();
                break;
        }
    }


    @Background
    void saveDownload() {
        CommonResponse response = (CommonResponse) this.audioController.saveDownload(
                new PostDetailsRequest(audioArticle.getPost_id())
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                saveDownload();
                break;
        }
    }


    @Background
    void saveRaiting(int rate) {
        SaveRatingResponse response = (SaveRatingResponse) this.audioController.saveRating(
                new RatingRequest(audioArticle.getPost_id(), rate)
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                getAudio();
                break;
            case "handler":
                updateRating(response, false);
                break;
            default:
                showError(handlerResponse);
                updateRating(response, true);
        }
    }


    @Background
    void saveWishlist() {
        UpdateWishlistResponse response = (UpdateWishlistResponse) this.audioController.updateWishlist(
                new PostDetailsRequest(audioArticle.getPost_id())
        );
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                saveWishlist();
                break;
            case "handler":
                updateWishlist(response);
                break;
            default:
                showError(handlerResponse);
                setWishlist();
                break;
        }
    }


    @Background(id = "longtask.")
    void sendComment(String comment) {
        CommentsListResponse response = (CommentsListResponse) this.audioController.saveComments(new SaveCommentRequest(audioArticle.getPost_id(), comment));
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                sendComment(comment);
                break;
            case "handler":
                if (!response.getComments().isEmpty()) {
                    updateUi(response.getComments());
                }
                break;
        }
    }


    @UiThread
    <T> void updateUi(List<Comment> commentList) {
        audioArticle.setComments(commentList);
        generateComments();
    }


    @UiThread
    void updateUi(AudioArticle audioArticle) {
        dimissProgress();
        wishlist.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        this.audioArticle = audioArticle;
        registerReceiver();


        int y = 0;
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("firsttimeDetail", null);

        if(restoredText == null) {
            ViewTarget target = new ViewTarget(R.id.download, this);
            RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            int margin = ((Number) (getResources().getDisplayMetrics().density * 10)).intValue();
            lps.setMargins(margin, margin, margin, margin);
            ShowcaseView sv = new ShowcaseView.Builder(this)
                    .setTarget(target)
                    .setContentTitle("طريقة الإستعمال")
                    .setStyle(R.style.AppTheme)
                    .setContentText("إضغط على إستماع لتسمع اونلاين ، أو إضغط الى تحميل للستماع اوفلاين" )
                    .hideOnTouchOutside()
                    .build();

            sv.setButtonPosition(lps);
            sv.setButtonText("موافق");

            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("firsttimeDetail", String.valueOf(1));
            editor.apply();
        }
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        Gson gson = builder.create();

        AudioArticleMin audioArticleMin = new AudioArticleMin(audioArticle);


        mediaMetadataCompats.clear();
        if (audioArticle.getIs_parted()) {

            for (int i =  audioArticle.getPostParts().size()-1; i > -1; i--) {
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
                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, ind)
                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, ind)
                        .putString(com.audiolaby.view.player.utils.Utils.MEDIA_PART_NAME, part.getTitle())
                        .putString(com.audiolaby.view.player.utils.Utils.MEDIA_POST, gson.toJson(audioArticleMin))
                    .build();
                mediaMetadataCompats.add(media);
                ind++;
            }
        } else


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
                            .build());

        setWishlist();


        if (audioArticle.getIs_parted())
           time.setText(String.format(getString(R.string.parts_number), "" + audioArticle.getPostParts().size()));
        else
            time.setText(String.format(getString(R.string.runtime), "" + Utils.formatSecondsToHoursMinutesAndSeconds(audioArticle.getRuntime())));

        date.setText(Utils.getDate(audioArticle.getDate()));
        category.setText(audioArticle.getCategory().getName());
        author.setText(String.format(this.authorName, "" + audioArticle.getAuthor().getName()));
        voiceover.setText(String.format(this.voiceName, "" + audioArticle.getVoiceOver().getName()));

        downloader.setText("(" + audioArticle.getDownloads() + ")");
        listner.setText("(" + audioArticle.getViews() + ")");


        if (libraryDAO.getAudioPost() != null) ;
        {
            for (AudioArticle audioArticle1 : libraryDAO.getAudioPost()) {
                if (audioArticle1.getPost_id().equals(audioArticle.getPost_id())) {
                    download.setVisibility(View.GONE);
                    break;
                }

            }
        }

        if (!audioArticle.getWished()) {
            wishlist.setImageDrawable(Utils.getDrawable(this, R.string.icon_heart_empty, R.color.white, 0));
        } else {
            wishlist.setImageDrawable(Utils.getDrawable(this, R.string.icon_heart, R.color.white, 0));

        }

        rating.setRating(audioArticle.getRate());
        //rating.setProgressDrawable(Utils.getDrawable(this,R.string.icon_heart,R.color.icon_unselected,0));
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(this, R.color.colorPrimary));
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(this, R.color.black_25));
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(this, R.color.black_25));

        rater.setText("" + audioArticle.getN_rate());

        date.setText(Utils.getDate(audioArticle.getDate()));

        if (audioArticle.getFree()) {
            buy.setVisibility(View.GONE);
            free.setVisibility(View.VISIBLE);
            type.setText(freeString);
        } else {
            type.setText(notFree);
            if (audioArticle.getAuthPaid()) {
                buy.setVisibility(View.GONE);
                free.setVisibility(View.VISIBLE);
            } else {
                buy.setVisibility(View.VISIBLE);
                free.setVisibility(View.GONE);
            }
        }

        if (audioArticle.getDescription() != null) {
            description.setText(audioArticle.getDescription());
        }
        if (audioArticle.getTitle() != null) {
            title.setText(audioArticle.getTitle());
        }

        if (audioArticle.getTags() != null) {

            ArrayList<String> tagsList = new ArrayList<>();
            for (Tag tag : audioArticle.getTags())
                tagsList.add(tag.getName());

            if (tagsList.size() == 0)
                tagsLayout.setVisibility(View.GONE);
            else {
                tagsLayout.setVisibility(View.VISIBLE);
                tags.setTags(tagsList);
            }


        } else {
            tags.setTags(new String[]{audioArticle.getCategory().getName()});

        }

        tags.setOnTagClickListener(mTagClickListener);

        generateRelated(categoryLayout, categoryElements, cAudioPostAdapter, audioArticle.getRelatedCategoryPosts());
        generateRelated(authorLayout, authorElements, aAudioPostAdapter, audioArticle.getRelatedAuthorPosts());
        generateRelated(voiceoverLayout, voiceoverElements, vAudioPostAdapter, audioArticle.getRelatedVoiceOversPosts());
        generateComments();

    }


    @UiThread
    void updateRating(SaveRatingResponse saveRatingResponse, Boolean error) {


        progressRating.setVisibility(View.GONE);
        ratingDialog.setVisibility(View.VISIBLE);

        if (error)
            return;

        audioArticle.setRate(saveRatingResponse.getRate());
        audioArticle.setN_rate(saveRatingResponse.getN_rate());
        rating.setRating(audioArticle.getRate());
        //rating.setProgressDrawable(Utils.getDrawable(this,R.string.icon_heart,R.color.icon_unselected,0));
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(this, R.color.colorPrimary));
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(this, R.color.black_25));
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(this, R.color.black_25));

        rater.setText("" + audioArticle.getN_rate());
    }


    @UiThread
    void updateWishlist(UpdateWishlistResponse response) {

        if (response.getAction() != null) {
            if (response.getAction().equals(ActionField.attached))
                audioArticle.setWished(true);
            else
                audioArticle.setWished(false);

            setWishlist();
        } else
            setWishlist();

    }


    @UiThread
    void setWishlist() {
        if (audioArticle.getWished() != null && !audioArticle.getWished()) {
            wishlist.setImageDrawable(Utils.getDrawable(this, R.string.icon_heart_empty, R.color.white, 0));
        } else if (audioArticle.getWished() != null && audioArticle.getWished()) {
            wishlist.setImageDrawable(Utils.getDrawable(this, R.string.icon_heart, R.color.white, 0));
        } else if (audioArticle.getWished() == null) {
            wishlist.setImageDrawable(Utils.getDrawable(this, R.string.icon_heart_empty, R.color.white, 0));
        }
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }


    public void generateRelated(LinearLayout mainLayout, RecyclerView elements, AudioPostAdapter adapter, List<AudioArticle> posts) {

        if (posts.isEmpty()) {
            mainLayout.setVisibility(View.GONE);
        } else {
            adapter = new AudioPostAdapter(AudioDetailsActivity.this, layout, new ArrayList());
            ;
            elements.setNestedScrollingEnabled(false);
            elements.setLayoutManager(new LinearLayoutManager(AudioDetailsActivity.this, 0, false));
            elements.setAdapter(new ScaleInAnimatorAdapter(adapter, elements));
            adapter.setLibraryDAO(libraryDAO);
            adapter.setDummy(false);
            adapter.setSectionItemList(posts);
            adapter.notifyDataSetChanged();
        }
    }


    public void generateComments() {

        if (audioArticle.getComments().isEmpty()) {
            commentsLayout.setVisibility(View.GONE);
        } else {
            comments.setNestedScrollingEnabled(false);
            this.commentAdapter = new AudioCommetnsAdapter(this, true);
            comments.setLayoutManager(new SmoothLinearLayoutManager(this, 0, false));
            comments.setAdapter(new AlphaAnimatorAdapter(this.commentAdapter, comments));
            new Timer().schedule(new timerTask(), 3000, 3000);
            if (audioArticle.getComments().size() > 5) {
                commentsMore.setVisibility(View.VISIBLE);
                commentAdapter.setComments(audioArticle.getComments().subList(0, 4));
            } else {
                commentsMore.setVisibility(View.GONE);
                commentAdapter.setComments(audioArticle.getComments());

            }


            commentAdapter.setAudioArticle(audioArticle);
            commentAdapter.notifyDataSetChanged();
        }

    }

    private void startDownload() {
        DownloadService_.intent(getApplication())
                //.title(audioArticle.getTitle())
                .myAction(audioArticle).start();
        saveDownload();
        downloadingOn.setVisibility(View.VISIBLE);
        downloadingOff.setVisibility(View.GONE);

    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("download_progress");
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("download_progress")) {


                Download download = intent.getParcelableExtra("download");

                //covdownloadProgress.setMax((int) download.getTotalFileSize());
                covdownloadProgress.setProgress(download.getProgress());
                if (download.getProgress() == 100) {

                    downloadParcent.setText("تم التحميل ");
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("idAudio", Integer.parseInt(audioArticle.getPost_id()));
                    editor.apply();
                    Snackbar snackbar = Snackbar
                            .make(sliding_layout, "الرجاء زيارة مكتبتك", Snackbar.LENGTH_INDEFINITE)
                            .setAction("مكتبتي", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //MyLibraryActivity_.intent(AudioDetailsActivity.this).start();
                                    Intent myIntent = new Intent(AudioDetailsActivity.this, MyLibraryActivity_.class);
                                    startActivity(myIntent);
                                }
                            });
                    snackbar.show();
                    downloadBB.setVisibility(View.INVISIBLE);
                } else {
                    int parcent = (int) ((int) download.getCurrentFileSize()) * 100 / ((int) download.getTotalFileSize());
                    downloadParcent.setText("" + parcent + "%");

                }
            }
        }
    };


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    download();
                } else {

                    showError("Permission Denied, Please allow to proceed !");

                }
                break;
        }
    }

    @Override
    public void onPositiveButtonClicked(int i, String s) {

        if (i != audioArticle.getAuthrate()) {
            progressRating.setVisibility(View.VISIBLE);
            ratingDialog.setVisibility(View.GONE);
            saveRaiting(i);
        }

        if (!s.isEmpty())
            sendComment(s);

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    int mPosition;


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

        if (isBuying) {
            isBuying = false;

            Boolean consumed = billingProcessor.consumePurchase(audioArticle.getCode());

            //Boolean consumed = billingProcessor.consumePurchase("android.test.purchased");

            if (consumed) {
                Utils.sendLog("onProductPurchased Consumed productId=" + productId);
                showError(".");

                savePaidAudio(audioArticle.getPost_id(),
                        details.purchaseInfo.purchaseData.packageName,
                        details.purchaseInfo.purchaseData.productId,
                        details.purchaseInfo.purchaseData.purchaseToken);
            } else {
                Utils.sendLog("onProductPurchased not consumed productId=" + productId);
            }

        } else {
            Utils.sendLog("onProductPurchased not not Buying productId=" + productId);
            showError(getString(R.string.error_try_again));

        }

    }

    @Override
    public void onPurchaseHistoryRestored() {
        Utils.sendLog("onProductPurchased not not Buying productId");

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        isBuying = false;
        showError(getString(R.string.error_try_again));
    }

    @Override
    public void onBillingInitialized() {
        isBuying = false;
        readyToPurchase = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (billingProcessor != null)
            billingProcessor.release();
        super.onDestroy();
    }


    @Background(id = "longtask.")
    void savePaidAudio(String audio_id, String packageName, String orderId, String purchaseToken) {
        SaveOrderResponse response = (SaveOrderResponse) this.accountController.savePaidAudio(new SavePaidAudioRequest("android", audio_id, packageName, orderId, purchaseToken));
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                savePaidAudio(audio_id, packageName, orderId, purchaseToken);
                break;
            case "handler":
                updateUser(response);
                break;
            default:
                showError(handlerResponse);
        }
    }

    @UiThread
    <T> void updateUser(SaveOrderResponse response) {


        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        Gson gson = builder.create();

        AudioArticleMin audioArticleMin = new AudioArticleMin(audioArticle);


        mediaMetadataCompats.clear();


        if (audioArticle.getIs_parted()) {

            for (int i =  audioArticle.getPostParts().size()-1; i > -1; i--) {
                Part part = audioArticle.getPostParts().get(i);
                part.setAudioUrl(response.getPostParts().get(i).getAudioUrl());


                MediaMetadataCompat media = new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, audioArticle.getPost_id() + "-" + i)
                        .putString(com.audiolaby.view.player.utils.Utils.CUSTOM_METADATA_TRACK_SOURCE, part.getAudioUrl())
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, audioArticle.getAuthor().getName())
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, audioArticle.getVoiceOver().getName())
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, Long.parseLong("" + part.getDuration()) * 1000)
                        .putString(MediaMetadataCompat.METADATA_KEY_GENRE, audioArticle.getCategory().getName())
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, audioArticle.getCover())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, audioArticle.getTitle())
                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, ind)
                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, ind)
                        .putString(com.audiolaby.view.player.utils.Utils.MEDIA_PART_NAME, part.getTitle())
                        .putString(com.audiolaby.view.player.utils.Utils.MEDIA_POST, gson.toJson(audioArticleMin))
                        .build();
                mediaMetadataCompats.add(media);
                ind++;
            }
        }

        else
        {

            audioArticle.setAudioUrl(response.getAudioUrl());
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
                            .build());


        }

        buy.setVisibility(View.GONE);
        free.setVisibility(View.VISIBLE);
    }


    class timerTask extends TimerTask {
        timerTask() {
        }

        public void run() {
            if (mPosition + 1 >= commentAdapter.getItemCount()) {
                mPosition = -1;
            }
            comments.smoothScrollToPosition(getPos());
        }
    }

    int getPos() {
        int i = mPosition + 1;
        mPosition = i;
        return i;
    }

    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String tag) {

            MoreActivity_.intent(AudioDetailsActivity.this)
                    .title(tag)
                    .type(TypeField.tag.name())
                    .id(tag)
                    .start();

        }
    };


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
