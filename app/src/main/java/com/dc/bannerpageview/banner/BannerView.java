package com.dc.bannerpageview.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.dc.bannerpageview.R;

public class BannerView extends RelativeLayout {

    private Context mContext;

    /**
     * 轮播的ViewPager
     */
    private BannerViewPager mBannerViewPager;

    /**
     * 轮播的描述
     */
    private TextView mBannerDesc;

    /**
     * 点的容器
     */
    private LinearLayout mDotContainer;

    private RelativeLayout mBannerBottom;

    private BannerAdapter mAdapter;

    /**
     * 选中的Drawable
     */
    private Drawable mIndicatorFocusDrawable;

    /**
     * 默认的Drawable
     */
    private Drawable mIndicatorNormalDrawable;

    /**
     * 当前的位置
     */
    private int mCurrentPosition;

    /**
     * 点的位置 默认左边
     */
    private int mDotGravity = -1;

    /**
     * 点的大小
     */
    private int mDotSize = 8;

    /**
     * 点的间距
     */
    private int mDotDistance = 8;

    /**
     * 底部颜色
     */
    private int mBottomColor = Color.TRANSPARENT;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflate(context, R.layout.view_banner, this);
        initAttribute(attrs);
        initView();

    }

    private void initView() {
        mBannerViewPager = findViewById(R.id.banner);

        mBannerDesc = findViewById(R.id.banner_desc);

        mDotContainer = findViewById(R.id.dot_container);

        mBannerBottom = findViewById(R.id.banner_bottom);
        mBannerBottom.setBackgroundColor(mBottomColor);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mDotGravity = typedArray.getInt(R.styleable.BannerView_dotGravity, mDotGravity);

        mIndicatorFocusDrawable = typedArray.getDrawable(R.styleable.BannerView_dotIndicatorFocus);
        mIndicatorNormalDrawable = typedArray.getDrawable(R.styleable.BannerView_dotIndicatorNormal);
        if (mIndicatorFocusDrawable == null) {
            mIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        }
        if (mIndicatorNormalDrawable == null) {
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        mDotSize = (int) typedArray.getDimension(R.styleable.BannerView_dotSize, dip2px(mDotSize));
        mDotDistance = (int) typedArray.getDimension(R.styleable.BannerView_dotDistance, dip2px(mDotDistance));
        mBottomColor = typedArray.getColor(R.styleable.BannerView_bottomColor, mBottomColor);
        typedArray.recycle();
    }

    public void setAdapter(BannerAdapter adapter) {
        mAdapter = adapter;
        mBannerViewPager.setAdapter(adapter);

        initDotIndicator();

        mBannerViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }
        });

        // 初始化时获取第一张描述
        String firstDesc = mAdapter.getBannerDesc(0);
        mBannerDesc.setText(firstDesc);
    }

    /**
     * 页面切换回调
     *
     * @param position int
     */
    private void pageSelected(int position) {
        // 当前亮着的点设为默认
        DotIndicatorView oldIndicatorView = (DotIndicatorView) mDotContainer.getChildAt(mCurrentPosition);
        oldIndicatorView.setDrawable(mIndicatorNormalDrawable);

        // 当前位置的点 position   0 --> 2的31次方
        mCurrentPosition = position % mAdapter.getCount();

        DotIndicatorView currentIndicatorView = (DotIndicatorView) mDotContainer.getChildAt(mCurrentPosition);
        currentIndicatorView.setDrawable(mIndicatorFocusDrawable);

        String bannerDesc = mAdapter.getBannerDesc(mCurrentPosition);
        mBannerDesc.setText(bannerDesc);
    }

    private void initDotIndicator() {
        int count = mAdapter.getCount();
        mDotContainer.setGravity(getDotGravity());
        for (int i = 0; i < count; i++) {
            DotIndicatorView indicatorView = new DotIndicatorView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            params.leftMargin = mDotDistance;
            indicatorView.setLayoutParams(params);
            if (i == 0) {
                indicatorView.setDrawable(mIndicatorFocusDrawable);
            } else {
                indicatorView.setDrawable(mIndicatorNormalDrawable);
            }
            mDotContainer.addView(indicatorView);
        }
    }

    private int getDotGravity() {
        switch (mDotGravity) {
            case 0:
                return Gravity.CENTER;
            case -1:
                return Gravity.END;
            case 1:
                return Gravity.START;
        }
        return mDotGravity;
    }

    private int dip2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    public void startRoll() {
        mBannerViewPager.startRoll();
    }
}
