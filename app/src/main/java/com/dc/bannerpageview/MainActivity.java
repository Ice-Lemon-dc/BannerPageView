package com.dc.bannerpageview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dc.bannerpageview.banner.BannerAdapter;
import com.dc.bannerpageview.banner.BannerView;
import com.dc.bannerpageview.indicator.ColorTrackTextView;
import com.dc.bannerpageview.indicator.IndicatorAdapter;
import com.dc.bannerpageview.indicator.ItemFragment;
import com.dc.bannerpageview.indicator.TrackIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int[] banner = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3};
    private String[] items = {"直播", "推荐", "视频", "段友秀","图片", "段子", "精华", "同城", "游戏"};
    private TrackIndicatorView mTrackIndicatorView;
    private List<ColorTrackTextView> mIndicators;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BannerView bannerView = findViewById(R.id.bannerView);

        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView imageView;
                if (convertView == null) {
                    imageView = new ImageView(MainActivity.this);
                } else {
                    imageView = (ImageView) convertView;
                }
                imageView.setBackgroundResource(banner[position]);
                return imageView;
            }

            @Override
            public int getCount() {
                return banner.length;
            }
        });
        bannerView.startRoll();


        mIndicators = new ArrayList<>();
        mTrackIndicatorView = findViewById(R.id.indicator_view);
        mViewPager = findViewById(R.id.view_pager);
        initIndicator();
        initViewPager();

    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), 0) {
            @Override
            public Fragment getItem(int position) {
                return ItemFragment.newInstance(items[position]);
            }

            @Override
            public int getCount() {
                return items.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ColorTrackTextView left = mIndicators.get(position);
                    left.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                    left.setCurrentProgress(1 - positionOffset);

                    if (position == items.length - 1) {
                        return;
                    }
                    ColorTrackTextView right = mIndicators.get(position + 1);
                    right.setDirection(ColorTrackTextView.Direction.LEFT_TO_RIGHT);
                    right.setCurrentProgress(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initIndicator() {

        mTrackIndicatorView.setAdapter(new IndicatorAdapter<ColorTrackTextView>() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public View getView(int position, ViewGroup parent) {

                ColorTrackTextView colorTrackTextView = new ColorTrackTextView(MainActivity.this);
                colorTrackTextView.setTextSize(20);
                colorTrackTextView.setChangeColor(Color.RED);
                colorTrackTextView.setText(items[position]);
                mIndicators.add(colorTrackTextView);
                return colorTrackTextView;
            }

            @Override
            public void highLightIndicator(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                view.setCurrentProgress(1);
            }

            @Override
            public void restoreIndicator(ColorTrackTextView view) {
                view.setDirection(ColorTrackTextView.Direction.RIGHT_TO_LEFT);
                view.setCurrentProgress(0);
            }

            @Override
            public View getBottomTrackView() {
                View view = new View(MainActivity.this);
                view.setBackgroundColor(Color.RED);
                view.setLayoutParams(new ViewGroup.LayoutParams(88, 8));
                return view;
            }
        }, mViewPager, false);
    }
}
