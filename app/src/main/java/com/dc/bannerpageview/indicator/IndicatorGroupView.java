package com.dc.bannerpageview.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IndicatorGroupView extends FrameLayout {

    /**
     * 指示器条目容器
     */
    private LinearLayout mIndicatorGroup;

    private View mBottomTrackView;

    /**
     * 一个条目的宽度
     */
    private int mItemWidth;

    /**
     * 底部指示器的params
     */
    private FrameLayout.LayoutParams mTrackParams;

    private int mLeftMargin;

    public IndicatorGroupView(@NonNull Context context) {
        this(context, null);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroupView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mIndicatorGroup = new LinearLayout(context);
        addView(mIndicatorGroup);
    }

    /**
     * 添加当前View
     *
     * @param itemView View
     */
    public void addItemView(View itemView) {
        mIndicatorGroup.addView(itemView);
    }

    /**
     * 获取当前View
     *
     * @param position int
     * @return View
     */
    public View getItemAt(int position) {
        return mIndicatorGroup.getChildAt(position);
    }

    /**
     * 添加底部跟踪指示器
     *
     * @param view      View
     * @param itemWidth int
     */
    public void addBottomTrackView(View view, int itemWidth) {
        if (view == null) {
            return;
        }
        mBottomTrackView = view;

        this.mItemWidth = itemWidth;

        addView(mBottomTrackView);

        mTrackParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        mTrackParams.gravity = Gravity.BOTTOM;

        int trackWidth = mTrackParams.width;

        // 没有设置宽度
        if (mTrackParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            trackWidth = mItemWidth;
        }
        // 设置宽度过大
        if (trackWidth > mItemWidth) {
            trackWidth = mItemWidth;
        }

        mLeftMargin = (mItemWidth - trackWidth) / 2;
        mTrackParams.leftMargin = mLeftMargin ;

        mTrackParams.width = trackWidth;
        mBottomTrackView.setLayoutParams(mTrackParams);
    }

    /**
     * 滑动时滚动底部的指示器
     *
     * @param position       int
     * @param positionOffset float
     */
    public void scrollBottomTrack(int position, float positionOffset) {
        if (mBottomTrackView == null) {
            return;
        }
        mTrackParams.leftMargin = (int) ((position + positionOffset) * mItemWidth) + mLeftMargin;
        mBottomTrackView.setLayoutParams(mTrackParams);
    }

    /**
     * 点击移动指示器
     *
     * @param position int
     */
    public void scrollBottomTrack(int position) {
        if (mBottomTrackView == null) {
            return;
        }

        // 最终要移动的位置
        int finalLeftMargin = position * mItemWidth + mLeftMargin;
        // 当前的距离
        int currentLeftMargin = mTrackParams.leftMargin;

        // 移动的距离
        int distance = finalLeftMargin - currentLeftMargin;

        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin).setDuration((long) (Math.abs(distance) * 0.3f));

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float leftMargin = (float) animation.getAnimatedValue();
                mTrackParams.leftMargin = (int) leftMargin;
                mBottomTrackView.setLayoutParams(mTrackParams);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();

    }
}
