package com.tourism.my.tourismmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.activity.SpotsAddActivity;
import com.tourism.my.tourismmanagement.activity.SpotsDetailActivity;
import com.tourism.my.tourismmanagement.adapter.SpotAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Spot;

import java.util.List;

/**
 * 底部导航---景点
 */
public class SpotFragment extends Fragment implements View.OnClickListener {
    private FragmentActivity context;
    private View view;
    private Intent intent;
    private ListView lv;
    private SpotAdapter adapter;
    private TextView tv_no;
    private List<Spot> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spots, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        context = getActivity();
        TextView tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_no = (TextView) view.findViewById(R.id.tv_no);
        lv = (ListView) view.findViewById(R.id.lv);

        tv_add.setOnClickListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 去详情
                Intent intent = new Intent(context, SpotsDetailActivity.class);
                intent.putExtra("diary", list.get(arg2));
                startActivity(intent);
            }
        });
    }

    public void initData() {
        //选routeId等于空的，因为非空的属于线路 会重复
        list = DBManager.getSpotByRouteId(context,"");
        if (list == null || list.size() <= 0) {
            tv_no.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return;
        }
        tv_no.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new SpotAdapter(context, list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add: // 添加
                intent = new Intent(getActivity(), SpotsAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }


}
