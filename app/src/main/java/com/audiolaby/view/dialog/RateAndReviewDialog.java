package com.audiolaby.view.dialog;

import android.app.ProgressDialog;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.audiolaby.R;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.util.ConnectivityUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.Date;
import java.util.Random;



// RateAndReviewDialog_.builder()
// .titleStr(libraryAudiobook.getAudiobook().getTitle())
// .libraryAudiobook(libraryAudiobook)
// .subtitleStr(subtitle)
// .build()
// .show(((AppCompatActivity) this.context).getSupportFragmentManager(), "rate_and_review");

@EFragment(R.layout.dialog_rate_and_review)
public class RateAndReviewDialog extends BaseDialog {
//    @ViewById(R.id.review)
//    EditText body;
//    @Bean
//    LibraryController controller;
//    @ViewById(R.id.discard)
//    Button discard;
//    @ViewById(R.id.headline)
//    EditText headline;
//    @ViewById(R.id.headline_label)
//    TextView headlineLabel;
//    @FragmentArg
//    LibraryAudiobook libraryAudiobook;
//    private ProgressDialog progressDialog;
//    @ViewById(R.id.rate)
//    AppCompatRatingBar rate;
//    @ViewById(R.id.rate_label)
//    TextView rateLabel;
//    @FragmentArg
//    Float rating;
//    @ViewById(R.id.review_label)
//    TextView reviewLabel;
//    @StringRes(R.string.reviewing_book)
//    String reviewingBook;
//    @ViewById(R.id.save)
//    Button save;
//    @ViewById(R.id.subtitle)
//    TextView subtitle;
//    @FragmentArg
//    String subtitleStr;
//    @ViewById(R.id.title)
//    TextView title;
//    @FragmentArg
//    String titleStr;
//
//    @Click({R.id.discard})
//    void onClickDiscard() {
//        this.bus.post(new ReviewCancelledEvent(this.libraryAudiobook));
//        dismiss();
//    }
//
//    @Click({R.id.save})
//    void onClickSave() {
//        sendData();
//    }
//
//    @AfterViews
//    void afterViewsInjection() {
//        getDialog().requestWindowFeature(1);
//        getDialog().setCanceledOnTouchOutside(false);
//        getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.rate_and_review_popup_size_width), -2);
//        LayerDrawable stars = (LayerDrawable) this.rate.getProgressDrawable();
//        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(getActivity(), R.color.eStories_orange));
//        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(getActivity(), R.color.black_25));
//        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(getActivity(), R.color.black_25));
//        this.title.setText(this.titleStr);
//        this.subtitle.setText(this.subtitleStr);
//        if (this.libraryAudiobook.getReview() != null) {
//            this.update = true;
//            this.headline.setText(this.libraryAudiobook.getReview().getHeadline());
//            this.rate.setRating((this.rating != null ? this.rating : this.libraryAudiobook.getReview().getRating()).floatValue());
//            this.body.setText(this.libraryAudiobook.getReview().getBody());
//        } else if (this.rating != null) {
//            this.rate.setRating(this.rating.floatValue());
//        }
//    }
//
//    @Background
//    void sendData() {
//        showProgress();
//
//        if (ConnectivityUtils.isConnected(getContext())) {
//            ReviewAudiobookResponse response = (ReviewAudiobookResponse) this.controller.reviewAudiobook(new ReviewAudiobookRequest(this.libraryAudiobook.getAudiobook().getCode(), this.headline.getText().toString(), Float.valueOf(this.rate.getRating()), this.body.getText().toString()));
//            if (response == null || response.getResponseStatus() != ResponseStatus.SUCCESS) {
//                String format;
//                if (response != null) {
//                    format = String.format(this.unexpectedErrorWithCode, new Object[]{response.getResponseText().toString()});
//                } else {
//                    format = this.unexpectedError;
//                }
//                showError(format);
//                return;
//            }
//            review.setCode(response.getReviewId());
//            review.setRating(Float.valueOf(this.rate.getRating()));
//            review.setUpdateDate(new Date());
//            review.setBody(this.body.getText().toString());
//            review.setHeadline(this.headline.getText().toString());
//            review.save();
//        } else {
//            Random random = new Random();
//            random.setSeed(System.currentTimeMillis());
//            review.setCode(Integer.valueOf(0 - random.nextInt()));
//            review.setRating(Float.valueOf(this.rate.getRating()));
//            review.setUpdateDate(new Date());
//            review.setBody(this.body.getText().toString());
//            review.setHeadline(this.headline.getText().toString());
//            review.setAudiobookCode(this.libraryAudiobook.getAudiobook().getCode());
//            review.setActionType(ActionType.ACTION_SAVE);
//            review.setShouldSyncWithServer(true);
//            review.save();
//        }
//        updateUI(review);
//    }
//
//    @UiThread
//    void updateUI(Review review) {
//        this.localyticsReporter.reportFeedback(review);
//        this.libraryAudiobook.setReview(review);
//        this.libraryAudiobook.save();
//        this.bus.post(new ReviewUpdatedEvent(this.libraryAudiobook, review, this.update ? ReviewType.UPDATE : ReviewType.NEW));
//        this.progressDialog.dismiss();
//        dismiss();
//    }
//
//    @UiThread
//    void showProgress() {
//        this.progressDialog = new ProgressDialog(getActivity());
//        this.progressDialog.setIndeterminate(true);
//        this.progressDialog.setMessage(this.reviewingBook);
//        this.progressDialog.setCancelable(false);
//        this.progressDialog.show();
//    }
//
//    @UiThread
//    void showError(String message) {
//        if (this.progressDialog != null) {
//            this.progressDialog.dismiss();
//        }
//        Snackbar.make(getActivity().getWindow().findViewById(android.R.id.content), (CharSequence) message, Snackbar.LENGTH_LONG).show();
//    }
//
//    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
//        if (VERSION.SDK_INT >= 21) {
//            DrawableCompat.setTint(drawable, color);
//        } else {
//            drawable.setColorFilter(color, Mode.SRC_IN);
//        }
//    }
}
