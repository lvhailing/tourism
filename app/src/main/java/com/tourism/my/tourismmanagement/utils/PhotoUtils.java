package com.tourism.my.tourismmanagement.utils;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class PhotoUtils {

    public static Map<String, String> getData(Context context, Intent data) {

        Map<String, String> map = new HashMap<>();
        Uri uri = getPictureUri(context, data);
        String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        // 获取指定列的索引
        int namePos = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int pathPos = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int bucketPos = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        String name = cursor.getString(namePos);
        String path = cursor.getString(pathPos);
        String bucketName = cursor.getString(bucketPos);

        cursor.close();

        Log.i("aaa", "name -- " + name);
        Log.i("aaa", "path -- " + path);
        Log.i("aaa", "bucketName -- " + bucketName);

        map.put("name", name);
        map.put("path", path);
        map.put("bucketName", bucketName);

        return map;
    }

    public static Uri getPictureUri(Context context, Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            //为了适配小米
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID}, buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                }
                if (index == 0) {
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    public static void startImageActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(intent, 100);

        //下面的也行
//        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        activity.startActivityForResult(i, 100);
    }
}
