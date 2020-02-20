package com.dc.bannerpageview.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BannerViewPager extends ViewPager {

    private BannerAdapter mAdapter;

    private static final int SCROLL_MSG = 0x0001;

    private int mTime = 3500;

    /**
     * 改变ViewPager切换速率的Scroller
     */
    private BannerScroller mBannerScroller;

    /**
     * 界面复用Views-- 如果只创建一个view,快速滑动会出现该View未从父布局移除而产生异常
     */
    private List<View> mConvertViews;

    /**
     * 当前Activity
     */
    private Activity mActivity;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            setCurrentItem(getCurrentItem() + 1);
            startRoll();
        }
    };

    public BannerViewPager(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");

            mBannerScroller = new BannerScroller(context);

            field.setAccessible(true);
            // 设置参数 第一个参数:当前属性在哪个类 第二个参数:设置的值
            field.set(this, mBannerScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mConvertViews = new ArrayList<>();
    }

    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        setAdapter(new BannerPagerAdapter());

        // 管理Activity生命周期
        mActivity.getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    /**
     * 设置切换页面动画持续的时间
     *
     * @param scrollerDuration
     */
    public void setScrollerDuration(int scrollerDuration) {
        mBannerScroller.setScrollerDuration(scrollerDuration);
    }

    public void startRoll() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null;
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        super.onDetachedFromWindow();
    }

    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // 无限轮播
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            Log.e("TAG", "" + position);
            View bannerItemView = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            container.addView(bannerItemView);
            return bannerItemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }

    /**
     * 获取复用界面
     *
     * @return View
     */
    private View getConvertView() {
        for (int i = 0; i < mConvertViews.size(); i++) {
            View convertView = mConvertViews.get(i);
            // 获取没有添加在ViewPager的View
            if (convertView != null && convertView.getParent() == null) {
                return convertView;
            }
        }
        return null;
    }

    /**
     * 管理Activity生命周期
     */
    Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new DefaultActivityLifecycleCallbacks() {

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            if (activity == mActivity) {
                // 开始轮播
                startRoll();
            }
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
            if (activity == mActivity) {
                // 停止轮播
                mHandler.removeMessages(SCROLL_MSG);
            }
        }
    };
}
