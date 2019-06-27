package com.sky.customviewstudy.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * @author: xuzhiyong
 * @date: 2018/10/23 18:12
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class BaseFragment extends Fragment {

    public Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
