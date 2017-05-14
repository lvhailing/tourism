package com.tourism.my.tourismmanagement.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.tourism.my.tourismmanagement.activity.ForumsAddActivity;
import com.tourism.my.tourismmanagement.activity.ForumsDetailActivity;
import com.tourism.my.tourismmanagement.adapter.ForumsAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Forums;
import com.tourism.my.tourismmanagement.utils.SPUtil;

import java.util.List;

/**
 * 底部导航---论坛（管理员身份登录时可见）
 */
public class ForumFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tv_add, tv_no;
    private ListView lv;
    private List<Forums> list;
    private ForumsAdapter adapter;
    private FragmentActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmetn_forum, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        context = getActivity();
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_no = (TextView) view.findViewById(R.id.tv_no);
        lv = (ListView) view.findViewById(R.id.lv);

        tv_add.setOnClickListener(this);

        //判断身份 1游客 2管理员
        String role = SPUtil.get(getActivity(), "role");

        if (role.equals("2")) {
            //2管理员
            tv_add.setVisibility(View.VISIBLE);
        } else {
            tv_add.setVisibility(View.GONE);
        }

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                // 长安删除
                dialog(pos);
                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 去详情
                Intent intent = new Intent(getActivity(), ForumsDetailActivity.class);
                intent.putExtra("forumDetail", list.get(arg2));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        list = DBManager.getForums(getActivity());
        if (list == null || list.size() <= 0) {
            tv_no.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return;
        }
        tv_no.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new ForumsAdapter(context, list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add: //发布
                Intent intent = new Intent(context, ForumsAddActivity.class);
                startActivity(intent);
                break;

        }
    }

    protected void dialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认删除此条论坛吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delForums(getActivity(), list.get(pos));
                // 刷新界面
                initData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
