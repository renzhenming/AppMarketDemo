package com.rzm.commonlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rzm.commonlibrary.R;

/**
 * Created by renzhenming on 2017/6/1.
 * 无数据，加载异常，正在加载 状态显示
 * 使用方法：
 * mLoadState = (StatusSwitchLayout) getView.findViewById(R.id.status_layout);
 * mLoadState.setContentView(mThumbList);
 * mLoadState.showRequestLayout();
 * 网络状态
 * if (!NetWorkUtils.isNetWorkConn(mContext)) {
 * mLoadState.showFailureLayout(getString(R.string.ar_reload), R.drawable.empty_failed);
 * }
 * mLoadState.getFailureLayout().setOnClickListener(new View.OnClickListener() {
 * @Override
 * public void onClick(View v) {
 * mLoadState.getRequestLayout();
 * initData(1);
 * }
 * });
 * mLoadState.showNoDataLayout(getString(R.string.have_not_got_thumb), R.drawable.empty_like);
 * mLoadState.showFailureLayout(getString(R.string.ar_reload), R.drawable.empty_failed);
 */
public class StatusSwitchLayout extends RelativeLayout {
    private View vContentView;
    private LinearLayout vRequestLayout;
    private LinearLayout vFailureLayout;
    private LinearLayout vNoDataLayout;

    private ImageView vFailureImg;
    private ImageView vNoDataImg;
    private Button vNoDataBtn;
    private TextView vFailureText;
    private TextView vFailureReload;

    public StatusSwitchLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    public StatusSwitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public StatusSwitchLayout(Context context) {
        super(context);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.status_switch_layout, this);

        vRequestLayout = (LinearLayout) findViewById(R.id.request_layout);
        vFailureLayout = (LinearLayout) findViewById(R.id.loading_failure_layout);
        vNoDataLayout = (LinearLayout) findViewById(R.id.no_data_layout);

        //加载失败
        vFailureImg = (ImageView) findViewById(R.id.loading_failure_img);
        vFailureText = (TextView) findViewById(R.id.loading_failure_text);
        vFailureReload = (TextView) findViewById(R.id.loading_failure_reload);

        //加载为空
        vNoDataImg = (ImageView) findViewById(R.id.no_data_img);
        vNoDataBtn = (Button) findViewById(R.id.other_operate_button);

    }

    public View getItemView(int id) {
        View view = null;
        try {
            view = findViewById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public TextView getvFailureReload() {
        return vFailureReload;
    }

    public void setContentView(View vContentView) {
        this.vContentView = vContentView;
    }

    public LinearLayout getRequestLayout() {
        return vRequestLayout;
    }

    public LinearLayout getFailureLayout() {
        return vFailureLayout;
    }

    public Button getNoDataBtn() {
        return vNoDataBtn;
    }

    public void showContentLayout() {
        showWhichLayout(0,null,-1);
    }

    /**
     * 设置显示加载内容为空布局
     * @param data 加载内容为空文字提示
     * @param image 加载内容为空图片显示
     */
    public void showNoDataLayout(String data, int image) {
        showWhichLayout(1,data,image);
    }

    public void showRequestLayout() {
        showWhichLayout(2,null,-1);
    }

    /**
     * 设置显示加载失败布局
     * @param data 加载失败文字提示
     * @param image 加载失败图片显示
     */
    public void showFailureLayout(String data, int image) {
        showWhichLayout(3,data,image);
    }

    public void dismissAll() {
        showWhichLayout(4,null,-1);
    }

    /**
     * 0.代表显示content layout,1.代表显示无数据layout,2.代表显示请求layout,3.代表显示失败layout, 4.全部隐藏
     *
     * @param index
     */
    private void showWhichLayout(int index,String data,int image) {
        switch (index) {
            case 0:
                if (null != vContentView && vContentView.getVisibility() == View.GONE) {
                    showView(vContentView);
                }
                if (vNoDataLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vNoDataLayout);
                }
                if (vRequestLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vRequestLayout);
                }
                if (vFailureLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vFailureLayout);
                }
                break;
            case 1:
                if (null != vContentView && vContentView.getVisibility() == View.VISIBLE) {
                    dismissView(vContentView);
                }
                if (vNoDataLayout.getVisibility() == View.GONE) {
                    vNoDataBtn.setText(data);
                    vNoDataImg.setImageResource(image);
                    showView(vNoDataLayout);
                }
                if (vRequestLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vRequestLayout);
                }
                if (vFailureLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vFailureLayout);
                }
                break;
            case 2:
                if (null != vContentView && vContentView.getVisibility() == View.VISIBLE) {
                    dismissView(vContentView);
                }
                if (vNoDataLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vNoDataLayout);
                }
                if (vRequestLayout.getVisibility() == View.GONE) {
                    showView(vRequestLayout);
                }
                if (vFailureLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vFailureLayout);
                }
                break;
            case 3:
                if (null != vContentView && vContentView.getVisibility() == View.VISIBLE) {
                    dismissView(vContentView);
                }
                if (vNoDataLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vNoDataLayout);
                }
                if (vRequestLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vRequestLayout);
                }
                if (vFailureLayout.getVisibility() == View.GONE) {
                    vFailureImg.setImageResource(image);
                    vFailureReload.setText(data);
                    showView(vFailureLayout);
                }
                break;
            case 4:
                if (null != vContentView && vContentView.getVisibility() == View.VISIBLE) {
                    dismissView(vContentView);
                }
                if (vNoDataLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vNoDataLayout);
                }
                if (vRequestLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vRequestLayout);
                }
                if (vFailureLayout.getVisibility() == View.VISIBLE) {
                    dismissView(vFailureLayout);
                }
                break;
            default:
                break;
        }
    }

    private void showView(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        view.animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null);

    }

    private void dismissView(final View view) {
        view.setVisibility(View.GONE);
    }

}