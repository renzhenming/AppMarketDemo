package com.example.renzhenming.appmarket;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.renzhenming.appmarket.vm.fragment.HomeFragment;
import com.rzm.commonlibrary.stack.BaseActivity;
import com.rzm.commonlibrary.stack.BaseFragment;

public class MainActivity extends BaseActivity implements HomeFragment.OnFragmentInteractionListener{

    @NonNull
    @Override
    protected BaseFragment getRootFragment() {
        return new HomeFragment();
    }

    @Override
    public void onCreateNow(Bundle savedInstanceState) {
        super.onCreateNow(savedInstanceState);
        setAnim(R.anim.next_in, R.anim.next_out, R.anim.quit_in, R.anim.quit_out);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
