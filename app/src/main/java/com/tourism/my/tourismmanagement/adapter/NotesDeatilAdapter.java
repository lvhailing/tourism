package com.tourism.my.tourismmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.model.NotesDetail;

import java.util.List;

public class NotesDeatilAdapter extends BaseAdapter {
	private List<NotesDetail> dataList;
	private Context context;

	public NotesDeatilAdapter(Context context, List<NotesDetail> dataList) {
		super();
		this.dataList = dataList;
		this.context = context;
	}

	public void setData(List<NotesDetail> dataList) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_notes_detail, parent, false);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.iv = (ImageView) convertView.findViewById(R.id.iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_time.setText("上传时间：" + dataList.get(position).getTime());
		holder.tv_name.setText(dataList.get(position).getFileName());

		Options options = new Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 6;
		Bitmap bitmap = BitmapFactory.decodeFile(dataList.get(position).getFilePath(), options);
		holder.iv.setImageBitmap(bitmap);
		return convertView;
	}

	private static class ViewHolder {
		private TextView tv_time;
		private TextView tv_name;
		private ImageView iv;
	}
}
