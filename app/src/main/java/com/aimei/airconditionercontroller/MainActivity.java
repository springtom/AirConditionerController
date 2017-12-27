package com.aimei.airconditionercontroller;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.aimei.airconditionercontroller.util.DensityUtil;
import com.aimei.airconditionercontroller.widget.ArcBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArcBarView round_view;

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        round_view = findViewById(R.id.round_view);
//        List<String> list = new ArrayList<>();
//        list.add("17");
//        list.add("18");
//        list.add("19");
//        list.add("20");
//        list.add("21");
//        list.add("22");
//        list.add("23");
//        list.add("23");
//        list.add("25");
//        list.add("26");
//        list.add("27");
//        list.add("28");
//        list.add("29");
//        list.add("30");
//        list.add("31");
//        list.add("32");
//        list.add("33");
//        round_view.setListData(list);
//        round_view.setSelectedText(Color.parseColor("#ffffff"));
//        round_view.setTextColor(Color.parseColor("#4cffffff"));
//        round_view.setTextSize(DensityUtil.dip2px(this, 15));
//        round_view.setSelectedTextSize(DensityUtil.dip2px(this, 18));
//        round_view.setBorderColor(Color.parseColor("#4cffffff"));
//        round_view.setRingColor(Color.parseColor("#000000"));
//        round_view.setControllerColor(Color.parseColor("#BD0DE9"));
//        round_view.setStartColor(Color.parseColor("#fd4356"));
//        round_view.setEndColor(Color.parseColor("#456789"));
//        round_view.setControllerColor(Color.parseColor("#567432"));
//        round_view.setConfiguration(new ArcBarView.Configuration() {
//            @Override
//            public int getRingWidth() {
//                return DensityUtil.dip2px(MainActivity.this, 40f);
//            }
//
//            @Override
//            public int getBorderWidth() {
//                return DensityUtil.dip2px(MainActivity.this, 0.5f);
//
//            }
//
//            @Override
//            public int getInterval() {
//                return DensityUtil.dip2px(MainActivity.this, 0.5f);
//            }
//
//            @Override
//            public int getStartAngle() {
//                return 320;
//            }
//
//            @Override
//            public int getMaxAngle() {
//                return 580;
//            }
//        });
        text = findViewById(R.id.text);
        round_view.setChangeListener(new ArcBarView.ChangeListener() {
            @Override
            public void changed(String t) {
                text.setText(t + "℃");
            }
        });
    }
}
