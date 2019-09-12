package com.audiolaby.view.layout;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

public class SmoothLinearLayoutManager extends LinearLayoutManager {
    private static final float MILLISECONDS_PER_INCH = 100.0f;
    private Context context;

    class C12511 extends LinearSmoothScroller {
        C12511(Context x0) {
            super(x0);
        }

        public PointF computeScrollVectorForPosition(int targetPosition) {
            return SmoothLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return SmoothLinearLayoutManager.MILLISECONDS_PER_INCH / ((float) displayMetrics.densityDpi);
        }
    }

    public SmoothLinearLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    public SmoothLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.context = context;
    }

    public SmoothLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
        LinearSmoothScroller scroller = new C12511(this.context);
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }
}
