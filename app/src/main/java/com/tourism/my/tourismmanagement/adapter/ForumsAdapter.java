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
import com.tourism.my.tourismmanagement.db.db.model.Forums;
import com.tourism.my.tourismmanagement.db.db.model.Notes;

import java.util.List;

public class ForumsAdapter extends BaseAdapter {
	private List<Forums> dataList;
	private Context context;

	public ForumsAdapter(Context context, List<Forums> dataList) {
		super();
		this.dataList = dataList;
		this.context = context;
	}

	public void setData(List<Forums> dataList) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_forums, parent, false);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_time.setText("创建时间：" + dataList.get(position).getTime());
		holder.tv_title.setText(dataList.get(position).getTitle());
		holder.tv_content.setText(dataList.get(position).getCotent());

		return convertView;
	}

	private static class ViewHolder {
		private TextView tv_time;
		private TextView tv_title;
		private TextView tv_content;
	}
}
