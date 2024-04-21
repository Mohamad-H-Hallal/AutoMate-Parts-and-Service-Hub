package com.example.project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.google.android.gms.maps.MapView;

public class CustomMapView extends MapView {

    private boolean scrollable = true;

    public CustomMapView(Context context) {
        super(context);
    }

    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (scrollable) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return super.dispatchTouchEvent(ev);
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }
    }
    @Override
    public boolean performClick() {
        super.performClick();
        // Handle click event if needed
        return true;
    }
}
