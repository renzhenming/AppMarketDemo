package com.example.renzhenming.appmarket.ui.clip;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by renzhenming on 2017/12/5.
 */

public class ThreeColumnItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public ThreeColumnItemDecoration(int spacing) {
        this.space = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        switch (position % 3) {
            case 0:
                outRect.right = this.space;
                outRect.bottom = this.space;
                break;
            case 1:
                outRect.bottom = this.space;
                break;
            case 2:
                outRect.left = this.space;
                outRect.bottom = this.space;
                break;

        }

    }
}
