package com.vgaw.hrollerview;

import android.view.View;

/**
 * Created by caojin on 2017/5/24.
 */

public interface RollerAdapter<V extends View> {
    int getItemCount();

    int getShowItemCount();

    V getItemView(int i);

    void onItemSelected(int lst, int now, View itemLst, View itemNow);
}
