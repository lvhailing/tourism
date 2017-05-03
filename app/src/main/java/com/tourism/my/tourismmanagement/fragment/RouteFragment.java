package com.tourism.my.tourismmanagement.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourism.my.tourismmanagement.R;

/**
 * 底部导航---路线
 */
public class RouteFragment extends Fragment implements View.OnClickListener {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spot, null);
        initView(view);
        return view;
    }


    private void initView(View view) {


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
