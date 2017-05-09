package com.tourism.my.tourismmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.model.Spot;

import java.util.ArrayList;
import java.util.List;

public class RouteChooseSpotAdapter extends BaseAdapter {
    private List<Spot> dataList;
    private List<Spot> addList = new ArrayList<>();
    private Context context;

    public RouteChooseSpotAdapter(Context context, List<Spot> dataList) {
        super();
        this.dataList = dataList;
        this.context = context;
    }

    public List<Spot> getAddList() {
        return addList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_route_choose_spot_item, parent, false);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_jianjie = (TextView) convertView.findViewById(R.id.tv_jianjie);
            holder.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(dataList.get(position).getName());
        holder.tv_jianjie.setText(dataList.get(position).getJianjie());
        holder.tv_addr.setText(dataList.get(position).getAddr());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addList.add(dataList.get(position));
                } else {
                    addList.remove(dataList.get(position));
                }
            }
        });

        Options options = new Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 6;
        Bitmap bitmap = BitmapFactory.decodeFile(dataList.get(position).getFilePath(), options);
        holder.iv.setImageBitmap(bitmap);
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_addr;
        private ImageView iv;
        private TextView tv_name;
        private TextView tv_jianjie;
        private CheckBox checkBox;
    }
}
