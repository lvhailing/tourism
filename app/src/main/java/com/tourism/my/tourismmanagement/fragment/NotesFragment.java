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
import com.tourism.my.tourismmanagement.activity.NotesAddActivity;
import com.tourism.my.tourismmanagement.activity.NotesDetailActivity;
import com.tourism.my.tourismmanagement.adapter.NotesAdapter;
import com.tourism.my.tourismmanagement.db.db.DBManager;
import com.tourism.my.tourismmanagement.db.db.model.Notes;
import com.tourism.my.tourismmanagement.utils.SPUtil;

import java.util.List;

/**
 * 底部导航---游记
 */
public class NotesFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tv_add, tv_no;
    private ListView lv;
    private List<Notes> list;
    private NotesAdapter adapter;
    private FragmentActivity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        context = getActivity();
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_no = (TextView) view.findViewById(R.id.tv_no);
        lv = (ListView) view.findViewById(R.id.lv);

        tv_add.setOnClickListener(this);

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
                Intent intent = new Intent(getActivity(), NotesDetailActivity.class);
                intent.putExtra("notes", list.get(arg2));
                startActivity(intent);
            }
        });

        //判断身份 1游客 2管理员
        String role = SPUtil.get(getActivity(), "role");

        if (role.equals("2")) {
            //"管理员";
            tv_add.setVisibility(View.GONE);
            tv_no.setText("暂无游客游记");
        }
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
        list = DBManager.getNotes(getActivity());
        if (list == null || list.size() <= 0) {
            tv_no.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            return;
        }
        tv_no.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new NotesAdapter(getActivity(), list);
            lv.setAdapter(adapter);
        } else {
            adapter.setData(list);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add: //添加
               Intent intent = new Intent(context, NotesAddActivity.class);
                startActivity(intent);
                break;

        }
    }

    protected void dialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认删除这篇游记记吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 从数据库删除这一项
                DBManager.delNotes(getActivity(), list.get(pos));
                // 刷新界面
                initData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}
