package com.dc.bannerpageview.banner;

import android.view.View;

public abstract class BannerAdapter {

    /**
     * 根据位置获取ViewPager的子View
     *
     * @param position int
     * @param convertView 复用View
     *
     * @return View
     */
    public abstract View getView(int position, View convertView);

    /**
     * 获取轮播的数量
     *
     * @return int
     */
    public abstract int getCount();

    /**
     * 根据位置获取广告位描述
     *
     * @param position int
     */
    public String getBannerDesc(int position) {
        return "";
    }
}
