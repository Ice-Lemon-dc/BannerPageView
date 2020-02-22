package com.dc.bannerpageview.indicator;

import android.view.View;
import android.view.ViewGroup;

public abstract class IndicatorAdapter<T extends View> {

    /**
     * 获取总共有多少条数据
     *
     * @return int
     */
    public abstract int getCount();

    /**
     * 根据当前位置获取条目View
     *
     * @param position int
     * @param parent   ViewGroup
     * @return View
     */
    public abstract View getView(int position, ViewGroup parent);

    /**
     * 高亮当前位置
     *
     * @param view View
     */
    public void highLightIndicator(T view) {

    }

    /**
     * 重置当前位置
     *
     * @param view View
     */
    public void restoreIndicator(T view) {

    }

    /**
     * 添加底部跟踪的指示器
     *
     * @return View
     */
    public View getBottomTrackView() {
        return null;
    }
}
