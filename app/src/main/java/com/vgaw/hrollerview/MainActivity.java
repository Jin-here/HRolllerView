package com.vgaw.hrollerview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HRollerView wv = (HRollerView) findViewById(R.id.wv);
        wv.setAdapter(new RollerAdapter() {
            @Override
            public int getItemCount() {
                return 17;
            }

            @Override
            public int getShowItemCount() {
                return 5;
            }

            @Override
            public View getItemView(int i) {
                TextView tv = new TextView(MainActivity.this);
                tv.setText("hello" + i);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(Color.BLUE);
                return tv;
            }

            @Override
            public void onItemSelected(int lst, int now, View itemLst, View itemNow) {
                itemLst.setBackgroundColor(Color.BLUE);
                itemNow.setBackgroundColor(Color.RED);
            }
        });
    }
}
