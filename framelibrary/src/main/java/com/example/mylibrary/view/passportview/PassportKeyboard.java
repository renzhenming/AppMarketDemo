package com.example.mylibrary.view.passportview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mylibrary.R;

import java.util.ArrayList;

/**
 * Created by renzhenming on 2018/3/12.
 * 自定义键盘
 */

public class PassportKeyboard  extends LinearLayout implements AdapterView.OnItemClickListener {

    private ArrayList<String> mNumList = new ArrayList<>();

    private PasswordEditText mPasswordEditText;

    public PassportKeyboard(Context context) {
        this(context, null);
    }

    public PassportKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassportKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_password_keyboard, this);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 10; i++) {
            if (i == 9){
                mNumList.add(" ");
                break;
            }
            mNumList.add((i+1)+"");
        }
        mNumList.add("0");
        mNumList.add("x");
        LineGridView mGrid = (LineGridView)findViewById(R.id.num_grid);
        mGrid.setOnItemClickListener(this);
        mGrid.setAdapter(new NumAdapter(mNumList));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        View clickView = view;
        if (position < 11 && position != 9) {
            String number = ((TextView) clickView).getText().toString();
            if (!TextUtils.isEmpty(number)) {
                if (mPasswordEditText != null){
                    mPasswordEditText.addPassword(number);
                }
            }
        } else if (position == 11) {
            if (mPasswordEditText != null){
                mPasswordEditText.deleteLastPassword();
            }
        }
    }

    public void bindPasswordEditText(PasswordEditText passwordEditText){
        this.mPasswordEditText = passwordEditText;
    }

    class NumAdapter extends BaseAdapter{

        private final ArrayList<String> numList;

        public NumAdapter(ArrayList<String> numList) {
            this.numList = numList;
        }

        @Override
        public int getCount() {
            return numList == null? 0:numList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(getContext(),R.layout.item_password_keyboard,null);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.item);
            textView.setText(numList.get(position));
            return textView;
        }
    }
}
