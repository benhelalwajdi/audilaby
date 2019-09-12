package com.audiolaby.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Target;

public class ImageViewWithTarget extends ImageView {
    private Target target;

    public ImageViewWithTarget(Context context) {
        super(context);
    }

    public ImageViewWithTarget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewWithTarget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Target getTarget() {
        return this.target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
