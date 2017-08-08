package com.example.renzhenming.appmarket.vm.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.renzhenming.appmarket.R;
import com.rzm.commonlibrary.stack.fragment.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private FrameLayout mContainer;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    showError();
                    break;
                case 200:
                    showEmpty();
                    break;
                case 300:
                    showSuccessView();
                    break;
            }
        }
    };

    private void showSuccessView() {
        View view = View.inflate(mContext,R.layout.pager_success,null);
        mContainer.removeAllViews();
        mContainer.addView(view);
    }

    public void showProgress(){
        View view = View.inflate(mContext,R.layout.pager_loading,null);
        mContainer.removeAllViews();
        mContainer.addView(view);
    }

    public void showEmpty(){
        View view = View.inflate(mContext,R.layout.pager_empty,null);
        mContainer.removeAllViews();
        mContainer.addView(view);
    }

    public void showError(){
        View view = View.inflate(mContext,R.layout.pager_error,null);
        view.findViewById(R.id.error_btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
        mContainer.removeAllViews();
        mContainer.addView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContainer = (FrameLayout) view.findViewById(R.id.home_container);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        showProgress();
        loadData();
    }

    private void loadData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(1000);
                mHandler.sendEmptyMessage(300);
            }
        }.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
