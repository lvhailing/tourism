package com.tourism.my.tourismmanagement.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.fragment.ForumFragment;
import com.tourism.my.tourismmanagement.fragment.MeFragment;
import com.tourism.my.tourismmanagement.fragment.NotesFragment;
import com.tourism.my.tourismmanagement.fragment.RouteFragment;
import com.tourism.my.tourismmanagement.fragment.SpotFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager vp;
    private LinearLayout ll_tab_spot, ll_tab_route, ll_tab_notes, ll_tab_forums, ll_tab_me;
    private FragmentPagerAdapter mAdapter;
    private ArrayList<Fragment> mFragments;
    private SpotFragment tab_spot; //景点
    private RouteFragment tab_route; //路线
    private NotesFragment tab_notes; // 游记
    private ForumFragment tab_forum; // 论坛
    private MeFragment tab_me; // 我的
    private TextView tv_spot, tv_route, tv_notes, tv_forums, tv_me;
    private ImageView iv_spot, iv_route, iv_notes, iv_forums, iv_me;
    private String role;
    private String result;
    private String zh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        setSelect(0);
    }

    private void setSelect(int i) {
        setTab(i);
        vp.setCurrentItem(i);
    }

    private void initView() {
        zh = getIntent().getStringExtra("accout");
        vp = (ViewPager) findViewById(R.id.vp);
        ll_tab_spot = (LinearLayout) findViewById(R.id.ll_tab_spot);
        ll_tab_route = (LinearLayout) findViewById(R.id.ll_tab_route);
        ll_tab_notes = (LinearLayout) findViewById(R.id.ll_tab_notes);
        ll_tab_forums = (LinearLayout) findViewById(R.id.ll_tab_forums);
        ll_tab_me = (LinearLayout) findViewById(R.id.ll_tab_me);

        tv_spot = (TextView) findViewById(R.id.tv_spot);
        tv_route = (TextView) findViewById(R.id.tv_route);
        tv_notes = (TextView) findViewById(R.id.tv_notes);
        tv_forums = (TextView) findViewById(R.id.tv_forums);
        tv_me = (TextView) findViewById(R.id.tv_me);

        iv_spot = (ImageView) findViewById(R.id.iv_spot);
        iv_route = (ImageView) findViewById(R.id.iv_route);
        iv_notes = (ImageView) findViewById(R.id.iv_notes);
        iv_forums = (ImageView) findViewById(R.id.iv_forums);
        iv_me = (ImageView) findViewById(R.id.iv_me);

        iv_spot.setImageResource(R.mipmap.icon_spot_selected);

        mFragments = new ArrayList<Fragment>();
        tab_spot = new SpotFragment();
        tab_route = new RouteFragment();
        tab_notes = new NotesFragment();
        tab_forum = new ForumFragment();
        tab_me = new MeFragment();

        mFragments.add(tab_spot);
        mFragments.add(tab_route);
        if (DBManager.getUserByCount(this,zh) == null) {
            return;
        }
        role = DBManager.getUserByCount(this,zh).getRole();

        if (role.equals("1")) {
            result = "游客";
            ll_tab_notes.setVisibility(View.VISIBLE);
            mFragments.add(tab_notes);
        } else {
            result = "管理员";
            ll_tab_forums.setVisibility(View.VISIBLE);
            mFragments.add(tab_forum);
        }
        mFragments.add(tab_me);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragments.get(position);
            }
        };
        vp.setAdapter(mAdapter);
        vp.setOffscreenPageLimit(4);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                setTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    private void setTab(int currentItem) {
        resetImgs();
        switch (currentItem) {
            case 0:
                if (result .equals("游客")) {
                    iv_notes.setImageResource(R.mipmap.icon_travel_notes_normal);
                } else {
                    iv_forums.setImageResource(R.mipmap.icon_forums_normal);
                }
                tv_spot.setTextColor(Color.parseColor("#04a915"));
                iv_spot.setImageResource(R.mipmap.icon_spot_selected);
                iv_route.setImageResource(R.mipmap.icon_route_normal);
                iv_me.setImageResource(R.mipmap.icon_me_normal);
                break;
            case 1:
                if (result .equals("游客")) {
                    iv_notes.setImageResource(R.mipmap.icon_travel_notes_normal);
                } else {
                    iv_forums.setImageResource(R.mipmap.icon_forums_normal);
                }
                tv_route.setTextColor(Color.parseColor("#04a915"));
                iv_spot.setImageResource(R.mipmap.icon_spot_normal);
                iv_route.setImageResource(R.mipmap.icon_route_seleced);
                iv_me.setImageResource(R.mipmap.icon_me_normal);
                break;
            case 2:
                if (result.equals("游客")) {
                    tv_notes.setTextColor(Color.parseColor("#04a915"));
                    iv_spot.setImageResource(R.mipmap.icon_spot_normal);
                    iv_route.setImageResource(R.mipmap.icon_route_normal);
                    iv_notes.setImageResource(R.mipmap.icon_travel_notes_selected);
                    iv_me.setImageResource(R.mipmap.icon_me_normal);
                } else {
                    tv_forums.setTextColor(Color.parseColor("#04a915"));
                    iv_spot.setImageResource(R.mipmap.icon_spot_normal);
                    iv_route.setImageResource(R.mipmap.icon_route_normal);
                    iv_forums.setImageResource(R.mipmap.icon_forums_selected);
                    iv_me.setImageResource(R.mipmap.icon_me_normal);
                }
                break;
            case 3:
                if (result.equals("游客")) {
                    iv_notes.setImageResource(R.mipmap.icon_travel_notes_normal);
                } else {
                    iv_forums.setImageResource(R.mipmap.icon_forums_normal);
                }
                tv_me.setTextColor(Color.parseColor("#04a915"));
                iv_spot.setImageResource(R.mipmap.icon_spot_normal);
                iv_route.setImageResource(R.mipmap.icon_route_normal);
                iv_me.setImageResource(R.mipmap.icon_me_seleced);
                break;
        }
    }

    private void initEvent() {
        ll_tab_spot.setOnClickListener(this);
        ll_tab_route.setOnClickListener(this);
        if (result .equals("游客")) {
            ll_tab_notes.setOnClickListener(this);
        } else {
            ll_tab_forums.setOnClickListener(this);
        }
        ll_tab_me.setOnClickListener(this);
    }

    private void resetImgs() {
        tv_spot.setTextColor(Color.parseColor("#999999"));
        tv_route.setTextColor(Color.parseColor("#999999"));
        if (result.equals("游客")) {
            tv_notes.setTextColor(Color.parseColor("#999999"));
        } else {
            tv_forums.setTextColor(Color.parseColor("#999999"));
        }
        tv_me.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_tab_spot: // 景点
                setSelect(0);
                break;
            case R.id.ll_tab_route: // 路线
                setSelect(1);
                break;
            case R.id.ll_tab_notes: // 游记
                setSelect(2);
                break;
            case R.id.ll_tab_forums: // 论坛
                setSelect(2);
                break;
            case R.id.ll_tab_me: // 我的
                setSelect(3);
                break;

        }
    }
}
