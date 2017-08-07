package com.example.renzhenming.appmarket;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;

import com.example.renzhenming.appmarket.vm.fragment.HomeFragment;
import com.example.renzhenming.appmarket.vm.fragment.MainFragment;
import com.example.renzhenming.appmarket.vm.fragment.Other1Fragment;
import com.example.renzhenming.appmarket.vm.fragment.Other2Fragment;
import com.example.renzhenming.appmarket.vm.fragment.Other3Fragment;
import com.rzm.commonlibrary.stack.BaseActivity;
import com.rzm.commonlibrary.stack.BaseFragment;

public class MainActivity extends BaseActivity implements MainFragment.OnFragmentInteractionListener,HomeFragment.OnFragmentInteractionListener,Other1Fragment.OnFragmentInteractionListener,Other2Fragment.OnFragmentInteractionListener,Other3Fragment.OnFragmentInteractionListener{

    @NonNull
    @Override
    public BaseFragment getRootFragment() {
        return new MainFragment();
    }

    @Override
    public void onCreateNow(Bundle savedInstanceState) {
        super.onCreateNow(savedInstanceState);
        setAnim(R.anim.next_in, R.anim.next_out, R.anim.quit_in, R.anim.quit_out);
    }
/*
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
