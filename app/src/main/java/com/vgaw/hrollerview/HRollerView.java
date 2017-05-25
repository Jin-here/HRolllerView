package com.vgaw.hrollerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

/**
 * Created by caojin on 2017/5/24.
 */

public class HRollerView extends HorizontalScrollView {
    private static final int CHECK_STOP_DELAY = 10;
    private int itemCount = 5;
    private int showItemCount = 5;
    private int itemPadding = 10;

    private FrameLayout root;
    private int width;
    private int itemWidth;

    private RollerAdapter adapter;

    public HRollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        root = new FrameLayout(getContext());
        addView(root, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setAdapter(RollerAdapter adapter) {
        if (adapter != null) {
            this.adapter = adapter;
            showItemCount = adapter.getShowItemCount();
            if (showItemCount % 2 == 0) {
                throw new RuntimeException("show item count show be odd");
            }
            itemCount = adapter.getItemCount();
            lstI = showItemCount / 2;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (adapter == null) {
            return;
        }
        width = getWidth();
        if (width < itemPadding) {
            throw new RuntimeException("bad item padding");
        }

        root.removeAllViews();
        int startX = 0;
        itemWidth = (width - (showItemCount - 1) * itemPadding) / showItemCount;
        for (int i = 0;i < itemCount;i++) {
            View item = adapter.getItemView(i);
            item.setLayoutParams(new LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT));
            ((MarginLayoutParams)item.getLayoutParams()).leftMargin = startX;
            startX += (itemWidth + itemPadding);
            root.addView(item);
        }
        adapter.onItemSelected(lstI, lstI, root.getChildAt(lstI), root.getChildAt(lstI));
    }

    private int lstScrollX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            lstScrollX = getScrollX();
            checkStop();
        }
        return super.onTouchEvent(ev);
    }

    private void checkStop() {
        getHandler().removeCallbacks(checkStopRunnable);
        postDelayed(checkStopRunnable, CHECK_STOP_DELAY);
    }

    private Runnable checkStopRunnable = new Runnable() {
        @Override
        public void run() {
            int crtScrollX = getScrollX();
            if (crtScrollX == lstScrollX) {
                calStop();
            } else {
                lstScrollX = crtScrollX;
                checkStop();
            }
        }
    };

    private int lstI;

    private void calStop() {
        if (adapter != null) {
            int crtScrollX = getScrollX();
            int i = crtScrollX / (itemWidth + itemPadding) - 1;
            int rest = crtScrollX % (itemWidth + itemPadding);
            if (rest > (itemWidth + itemPadding) / 2) {
                i++;
                smoothScrollBy(itemWidth + itemPadding - rest, 0);
            } else {
                smoothScrollBy(-rest, 0);
            }
            int now = i + (showItemCount / 2) + 1;
            Log.d("HRollerView", "calStop: " + now);
            adapter.onItemSelected(lstI, now, root.getChildAt(lstI), root.getChildAt(now));
            lstI = now;
        }
    }
}
