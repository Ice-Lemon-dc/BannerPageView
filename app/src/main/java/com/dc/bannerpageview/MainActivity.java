package com.dc.bannerpageview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dc.bannerpageview.banner.BannerAdapter;
import com.dc.bannerpageview.banner.BannerView;
import com.dc.bannerpageview.banner.BannerViewPager;

public class MainActivity extends AppCompatActivity {

    private int[] banner = new int[]{R.drawable.pic1, R.drawable.pic2, R.drawable.pic3};

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
    }
}
