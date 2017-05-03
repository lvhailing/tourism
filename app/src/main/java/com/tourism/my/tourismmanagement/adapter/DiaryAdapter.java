package com.tourism.my.tourismmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.activity.DiaryImageDetailActivity;
import com.tourism.my.tourismmanagement.db.db.model.Diary;

import java.util.List;

public class DiaryAdapter extends BaseAdapter {
    private List<Diary> dataList;
    private Context context;

    public DiaryAdapter(Context context, List<Diary> dataList) {
        super();
        this.dataList = dataList;
        this.context = context;
    }

    public void setData(List<Diary> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;

        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_diary_item, parent, false);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.iv_point2 = (ImageView) convertView.findViewById(R.id.iv_point2);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(dataList.get(position).getName());
        holder.tv_jianjie.setText(dataList.get(position).getJianjie());
        holder.tv_addr.setText(dataList.get(position).getAddr());

        Options options = new Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 10;
        Bitmap bitmap = BitmapFactory.decodeFile(dataList.get(position).getFilePath(), options);
        holder.iv.setImageBitmap(bitmap);

        holder.tv_num.setText(position + "");
        if (position == dataList.size() - 1) {
            holder.iv_point2.setVisibility(View.VISIBLE);
        }

        holder.iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ���ͼƬȥ����ҳ
                Intent intent = new Intent(context, DiaryImageDetailActivity.class);
                intent.putExtra("filePath", dataList.get(position).getFilePath());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_addr;
        private ImageView iv;
        private ImageView iv_point2;
        private TextView tv_num;
        private TextView tv_name;
        private TextView tv_jianjie;
    }
}
