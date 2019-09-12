package com.audiolaby.view.player.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class SwipeDetector implements OnTouchListener {
    private float downX;
    private float downY;
    private int minDistance;
    private OnSwipeEvent swipeEventListener;
    private float upX;
    private float upY;
    private View f189v;

    public interface OnSwipeEvent {
        void swipeEventDetected(View view, SwipeTypeEnum swipeTypeEnum);
    }

    public enum SwipeTypeEnum {
        RIGHT_TO_LEFT,
        LEFT_TO_RIGHT,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP,
        TOUCH
    }

    public SwipeDetector(View v) {
        this.minDistance = 100;
        this.f189v = v;
        v.setOnTouchListener(this);
    }

    public void setOnSwipeListener(OnSwipeEvent listener) {
        try {
            this.swipeEventListener = listener;
        } catch (ClassCastException e) {
        }
    }

    public void onRightToLeftSwipe() {
        if (this.swipeEventListener != null) {
            this.swipeEventListener.swipeEventDetected(this.f189v, SwipeTypeEnum.RIGHT_TO_LEFT);
        }
    }

    public void onLeftToRightSwipe() {
        if (this.swipeEventListener != null) {
            this.swipeEventListener.swipeEventDetected(this.f189v, SwipeTypeEnum.LEFT_TO_RIGHT);
        }
    }

    public void onTopToBottomSwipe() {
        if (this.swipeEventListener != null) {
            this.swipeEventListener.swipeEventDetected(this.f189v, SwipeTypeEnum.TOP_TO_BOTTOM);
        }
    }

    public void onBottomToTopSwipe() {
        if (this.swipeEventListener != null) {
            this.swipeEventListener.swipeEventDetected(this.f189v, SwipeTypeEnum.BOTTOM_TO_TOP);
        }
    }

    public void onTouch() {
        if (this.swipeEventListener != null) {
            this.swipeEventListener.swipeEventDetected(this.f189v, SwipeTypeEnum.TOUCH);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.downX = event.getX();
                this.downY = event.getY();
                return true;
            case 1:
                this.upX = event.getX();
                this.upY = event.getY();
                float deltaX = this.downX - this.upX;
                float deltaY = this.downY - this.upY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) <= ((float) this.minDistance)) {
                        onTouch();
                        return true;
                    } else if (deltaX < 0.0f) {
                        onLeftToRightSwipe();
                        return true;
                    } else if (deltaX <= 0.0f) {
                        return true;
                    } else {
                        onRightToLeftSwipe();
                        return true;
                    }
                } else if (Math.abs(deltaY) <= ((float) this.minDistance)) {
                    onTouch();
                    return true;
                } else if (deltaY < 0.0f) {
                    onTopToBottomSwipe();
                    return true;
                } else if (deltaY <= 0.0f) {
                    return true;
                } else {
                    onBottomToTopSwipe();
                    return true;
                }
            default:
                return false;
        }
    }

    public SwipeDetector setMinDistanceInPixels(int minDistance) {
        this.minDistance = minDistance;
        return this;
    }
}
