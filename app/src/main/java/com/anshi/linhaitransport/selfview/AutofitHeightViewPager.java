package com.anshi.linhaitransport.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AutofitHeightViewPager extends ViewPager implements Serializable {
    public static final String POSITION = "position";

    private int mCurPosition;
    private int mHeight = 0;

    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    private boolean scrollble = true;

    public AutofitHeightViewPager(Context context) {
        super(context);
    }

    public AutofitHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChildrenViews.size() > mCurPosition) {
            View child = mChildrenViews.get(mCurPosition);
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mHeight = child.getMeasuredHeight();
            }
        }

        if (mHeight != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void updateHeight(int current) {
        this.mCurPosition = current;
        if (mChildrenViews.size() > current) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
            } else {
                layoutParams.height = mHeight;
            }

            setLayoutParams(layoutParams);
        }
    }

    public void setViewPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !scrollble || super.onTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
