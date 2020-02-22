package com.dc.bannerpageview.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.viewpager.widget.ViewPager;

import com.dc.bannerpageview.R;

public class TrackIndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private IndicatorAdapter mAdapter;

    /**
     * 指示器条目容器
     */
    private IndicatorGroupView mIndicatorGroupView;

    /**
     * 一屏幕可显示多少个
     */
    private int mTabVisibleNums;

    private int mItemWidth;

    private ViewPager mViewPager;

    /**
     * 当前位置
     */
    private int mCurrentPosition = 0;

    /**
     * 解决抖动的问题
     */
    private boolean mIsExecuteScroll = false;

    private boolean mSmoothScrool;

    public TrackIndicatorView(Context context) {
        this(context, null);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorGroupView = new IndicatorGroupView(context);
        addView(mIndicatorGroupView);

        initAttribute(context, attrs);
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrackIndicatorView);
        mTabVisibleNums = typedArray.getInt(R.styleable.TrackIndicatorView_tabVisibleNums, 0);
        typedArray.recycle();
    }

    public void setAdapter(IndicatorAdapter adapter) {

        this.mAdapter = adapter;

        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View itemView = mAdapter.getView(i, mIndicatorGroupView);
            mIndicatorGroupView.addItemView(itemView);

            if (mViewPager != null) {
                switchItemClick(itemView, i);
            }
        }

        mAdapter.highLightIndicator(mIndicatorGroupView.getItemAt(0));


    }

    private void switchItemClick(View itemView, final int position) {
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position, mSmoothScrool);

                smoothScrollCurrentIndicator(position);

                mIndicatorGroupView.scrollBottomTrack(position);
            }
        });
    }

    /**
     * 点击移动 带动画
     *
     * @param position int
     */
    private void smoothScrollCurrentIndicator(int position) {
        // 当前总共的位置
        float totalScroll = position * mItemWidth;

        // 左边的偏移
        int offsetScroll = (getWidth() - mItemWidth) / 2;

        int finalScroll = (int) (totalScroll - offsetScroll);

        smoothScrollTo(finalScroll, 0);
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager) {
        setAdapter(adapter, viewPager, true);
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager, boolean smoothScrool) {
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);

        this.mSmoothScrool = smoothScrool;

        setAdapter(adapter);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            mItemWidth = getItemWidth();

            for (int i = 0; i < mAdapter.getCount(); i++) {
                View view = mIndicatorGroupView.getItemAt(i);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = mItemWidth;
                view.setLayoutParams(params);
            }

            mIndicatorGroupView.addBottomTrackView(mAdapter.getBottomTrackView(), mItemWidth);
        }
    }

    private int getItemWidth() {
        int parentWidth = getWidth();
        if (mTabVisibleNums != 0) {
            return parentWidth / mTabVisibleNums;
        }

        int itemWidth = 0;
        int maxItemWidth = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            int currentItemWidth = mIndicatorGroupView.getItemAt(i).getWidth();
            maxItemWidth = Math.max(currentItemWidth, maxItemWidth);
        }

        itemWidth = maxItemWidth;
        int allWidth = mAdapter.getCount() * itemWidth;

        if (allWidth < parentWidth) {
            itemWidth = parentWidth / mAdapter.getCount();
        }
        return itemWidth;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mIsExecuteScroll) {

            scrollCurrentIndicator(position, positionOffset);

            mIndicatorGroupView.scrollBottomTrack(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        // 上一个位置重置
        mAdapter.restoreIndicator(mIndicatorGroupView.getItemAt(mCurrentPosition));

        mCurrentPosition = position;

        // 高亮当前位置
        mAdapter.highLightIndicator(mIndicatorGroupView.getItemAt(mCurrentPosition));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 1) {
            mIsExecuteScroll = true;
        }
        if (state == 0) {
            mIsExecuteScroll = false;
        }
    }

    /**
     * 滑动移动
     *
     * @param position int
     * @param positionOffset float
     */
    private void scrollCurrentIndicator(int position, float positionOffset) {
        // 当前总共的位置
        float totalScroll = (position + positionOffset) * mItemWidth;

        // 左边的偏移
        int offsetScroll = (getWidth() - mItemWidth) / 2;

        int finalScroll = (int) (totalScroll - offsetScroll);

        scrollTo(finalScroll, 0);

    }
}
