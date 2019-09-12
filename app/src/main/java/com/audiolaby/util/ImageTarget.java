package com.audiolaby.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.audiolaby.R;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

public class ImageTarget implements Target {
    private final Context context;
    private final ImageView imageView;
    private final ImageButton play;

    public ImageTarget(Context context, ImageView imageView, ImageButton play) {
        this.context = context;
        this.imageView = imageView;
        this.play = play;
    }

    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
        this.imageView.setImageBitmap(bitmap);
        this.imageView.clearAnimation();
        this.imageView.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.fade_in));
    }

    public void onBitmapFailed(Drawable errorDrawable) {
        this.imageView.setImageDrawable(errorDrawable);
    }

    public void onPrepareLoad(Drawable placeHolderDrawable) {
        this.imageView.setImageDrawable(placeHolderDrawable);
    }
}
