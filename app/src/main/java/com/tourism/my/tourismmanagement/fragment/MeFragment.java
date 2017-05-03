package com.tourism.my.tourismmanagement.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tourism.my.tourismmanagement.R;
import com.tourism.my.tourismmanagement.db.db.model.Me;
import com.tourism.my.tourismmanagement.widget.CircleImageView;

/**
 * 底部导航---我的
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView iv_back;
    private TextView tv_add;
    private CircleImageView civ_photo;
    private EditText et1;
    private EditText et2;
    private ImageView iv_sex;
    private RelativeLayout rl_sex;

    private TextView tv_man;
    private TextView tv_female;
    private ProgressDialog progDialog;
    private Me me;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        civ_photo = (CircleImageView) view.findViewById(R.id.civ_photo);
        et1 = (EditText) view.findViewById(R.id.et1);
        et2 = (EditText) view.findViewById(R.id.et2);
        iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        tv_man = (TextView) view.findViewById(R.id.tv_man);
        tv_female = (TextView) view.findViewById(R.id.tv_female);
        rl_sex = (RelativeLayout) view.findViewById(R.id.rl_sex);

        tv_add.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        civ_photo.setOnClickListener(this);

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.iv_asset_chosse_my_num: //总资产后的眼睛图标
                onclickAssetData();
                break;
            case R.id.rl_fragment_asset_fixed:  //固定收益类
                Intent i_fixed = new Intent(mActivity, AssetFixedActivity.class);
                mActivity.startActivityForResult(i_fixed, ASSET_REQUEST_CODE);

                break;
            case R.id.rl_fragment_asset_float: //浮动收益类
                Intent i_float = new Intent(mActivity, AssetFloatActivity.class);
                mActivity.startActivityForResult(i_float, ASSET_REQUEST_CODE);

                break;
            case R.id.rl_fragment_asset_insurance: //保险类
                Intent i_insurance = new Intent(mActivity, AssetInsuranceActivity.class);
                mActivity.startActivityForResult(i_insurance, ASSET_REQUEST_CODE);

                break;
            case R.id.iv_fragment_asset_message:  //消息列表
                Intent i_messgae = new Intent(mActivity, MessageActivity.class);
                mActivity.startActivityForResult(i_messgae, ASSET_REQUEST_CODE);
                break;*/

        }
    }


}
