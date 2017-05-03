package com.tourism.my.tourismmanagement.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.adapter.DiaryAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Diary;

import java.util.List;

public class DiaryActivity extends Activity implements View.OnClickListener {
    private Intent intent;
    private ListView lv;
    private DiaryAdapter adapter;
    private TextView tv_no;
    private List<Diary> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        TextView tv_add = (TextView) findViewById(R.id.tv_add);
        tv_no = (TextView) findViewById(R.id.tv_no);
        lv = (ListView) findViewById(R.id.lv);

        tv_add.setOnClickListener(this);

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 去详情
                Intent intent = new Intent(DiaryActivity.this, DiaryDetailActivity.class);
                intent.putExtra("diary", list.get(arg2));
                startActivity(intent);
            }
        });
    }

    public void initData() {
        list = DBManager.getDiary(this);
        if (list == null || list.size() <= 0) {
            tv_no.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return;
        }
        tv_no.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new DiaryAdapter(this, list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add: // 添加
//                intent = new Intent(this, DiaryAddActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
