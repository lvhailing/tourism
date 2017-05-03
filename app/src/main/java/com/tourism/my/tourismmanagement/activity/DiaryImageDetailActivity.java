package com.tourism.my.tourismmanagement.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;

import com.tourism.my.tourismmanagement.widget.DragImageView;

public class DiaryImageDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DragImageView mView = new DragImageView(this);

		String filePath = getIntent().getStringExtra("filePath");
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, null);
		Drawable mDrawable = new BitmapDrawable(bitmap);
		mView.setmDrawable(mDrawable);
		setContentView(mView);
	}

}
