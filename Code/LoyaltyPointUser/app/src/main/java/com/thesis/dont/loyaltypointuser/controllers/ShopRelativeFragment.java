package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thesis.dont.loyaltypointuser.R;

import butterknife.ButterKnife;

public class ShopRelativeFragment extends Fragment {
    private static final String ARG_POSITION = "position";

    //    @InjectView(R.id.textView)
    TextView textView;

    private int position;

    Activity mParentActivity;

    public ShopRelativeFragment() {}

    public ShopRelativeFragment(int position, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        this.setArguments(b);
    }

//    public static ShopDetailFragment newInstance(int position) {
//        ShopDetailFragment f = new ShopDetailFragment();
//        Bundle b = new Bundle();
//        b.putInt(ARG_POSITION, position);
//        f.setArguments(b);
//        return f;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_relative,container,false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();

        textView = (TextView)getActivity().findViewById(R.id.textView4);
        textView.setText("CARD " + position);

    }

}
