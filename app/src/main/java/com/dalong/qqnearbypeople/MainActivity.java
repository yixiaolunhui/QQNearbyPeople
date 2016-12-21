package com.dalong.qqnearbypeople;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.dalong.qqnearbypeople.entity.People;
import com.dalong.qqnearbypeople.view.GalleryRecyclerView;
import com.dalong.qqnearbypeople.view.RadarViewLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int[] mImgs = {R.drawable.len, R.drawable.leo, R.drawable.lep,
            R.drawable.leq, R.drawable.ler, R.drawable.les, R.drawable.mln, R.drawable.mmz, R.drawable.mna,
            R.drawable.mnj, R.drawable.leo, R.drawable.leq};
    private String[] mNames = {"橘子", "花生", "菠菜", "萝卜", "豆角", "西红柿", "香蕉", "苹果",
            "小麦","大米","玉米","白菜"};
    private SparseArray<People> mDatas = new SparseArray<>();
    private List<People> mlist = new ArrayList<>();
    private RadarViewLayout mRadarViewLayout;
    private GalleryRecyclerView mGalleryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRadarViewLayout = (RadarViewLayout) findViewById(R.id.radarViewLayout);
        mGalleryRecyclerView = (GalleryRecyclerView) findViewById(R.id.gallery);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                mRadarViewLayout.setDatas(mDatas);
                mRadarViewLayout.setCurrentShowItem(0);

            }
        }, 1000);
        mGalleryRecyclerView.setCanAlpha(true);
        mGalleryRecyclerView.setCanScale(true);
        mGalleryRecyclerView.setBaseScale(0.25f);
        mGalleryRecyclerView.setBaseAlpha(0.1f);
        mGalleryRecyclerView.setAdapter(new CommonAdapter<People>(this, R.layout.item_gallery, mlist) {
            @Override
            public void convert(ViewHolder holder, final People s, int position) {
                holder.setText(R.id.name, s.getName());
                holder.setText(R.id.tv_distance, s.getAge()+" "+s.getDistance()+"km");
                holder.setImageResource(R.id.profile_image,s.getPortraitId());
                holder.getView(R.id.item_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, s.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mGalleryRecyclerView.setOnViewSelectedListener(new GalleryRecyclerView.OnViewSelectedListener() {
            @Override
            public void onSelected(View view, final int position) {
                mRadarViewLayout.setCurrentShowItem(position);
            }
        });

        mRadarViewLayout.setOnRadarClickListener(new RadarViewLayout.OnRadarClickListener() {
            @Override
            public void onRadarItemClick(final int position) {

                //待完善
            }
        });
    }


    private void initData() {
        mlist.clear();
        mDatas.clear();
        for (int i = 0; i < mImgs.length; i++) {
            People info = new People();
            info.setPortraitId(mImgs[i]);
            info.setAge(((int) Math.random() * 25 + 16) + "岁");
            info.setName(mNames[i]);
            info.setSex(i % 3 == 0 ? false : true);
            info.setDistance(Math.round((Math.random() * 10) * 100) / 100);
            mDatas.put(i, info);
            mlist.add(info);
        }
    }
}
