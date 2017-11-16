package com.example.renzhenming.appmarket;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mylibrary.view.indicator.banner.BannerAdapter;
import com.example.mylibrary.view.indicator.banner.BannerView;
import com.example.mylibrary.view.indicator.banner.BannerViewPager;
import com.example.mylibrary.view.indicator.recyclerview.adapter.MultiTypeSupport;
import com.example.mylibrary.view.indicator.recyclerview.adapter.OnItemClickListener;
import com.example.mylibrary.view.indicator.recyclerview.adapter.OnLongClickListener;
import com.example.mylibrary.view.indicator.recyclerview.view.DefaultLoadCreator;
import com.example.mylibrary.view.indicator.recyclerview.view.DefaultRefreshCreator;
import com.example.mylibrary.view.indicator.recyclerview.view.LoadRefreshRecyclerView;
import com.example.mylibrary.view.indicator.recyclerview.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestCustomRecyclerViewActivity extends AppCompatActivity{

    private LoadRefreshRecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private List<String> mBannerList = new ArrayList<>();
    private TestCommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_recycler_view);
        initView();
    }

    private void initView() {
        mRecyclerView = (LoadRefreshRecyclerView) findViewById(R.id.recyclerview);

        adapter = new TestCommonAdapter(this, mList, new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position == 0) {
                    return R.layout.item_head;
                } else if (position == 1) {
                    return R.layout.test_item_1;
                } else if (position == 2) {
                    return R.layout.test_item_2;
                } else {
                    return R.layout.item_center;
                }
            }
        });
        View view = findViewById(R.id.view_empty);
        View loadingView = findViewById(R.id.view_loading);


        mRecyclerView.addEmptyView(view);
        mRecyclerView.addLoadingView(loadingView);
        mRecyclerView.addRefreshViewCreator(new DefaultRefreshCreator());
        mRecyclerView.addLoadViewCreator(new DefaultLoadCreator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(adapter);
        // 这个就不多解释了，就这么attach
        //itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.onStopRefresh();
                    }
                }, 1000);
            }
        });

        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(int position) {
                Toast.makeText(getApplicationContext(), "long click " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        mRecyclerView.setOnLoadMoreListener(new LoadRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoad() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.onStopLoad();
                        for (int i = 0; i < 20; i++) {
                            mList.add("测试数据" + i);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    mList.add("测试数据" + i);
                }
                adapter.notifyDataSetChanged();

                /**
                 * ************************************
                 *
                 * 加载失败显示失败布局
                 * View loadingFailedView = findViewById(R.id.view_load_failed);
                 * mRecyclerView.showFailureView(loadingFailedView);
                 *
                 * ************************************
                 */

            }
        }, 1000);



        //---------------    头部轮播   ----------------------//
        mBannerList.add("http://img5.imgtn.bdimg.com/it/u=2617197204,2648632269&fm=15&gp=0.jpg");
        mBannerList.add("http://img1.imgtn.bdimg.com/it/u=4157134066,1594146451&fm=15&gp=0.jpg");
        mBannerList.add("http://img4.imgtn.bdimg.com/it/u=561664270,3411990450&fm=15&gp=0.jpg");
        mBannerList.add("http://img0.imgtn.bdimg.com/it/u=3791883986,637837532&fm=27&gp=0.jpg");
        mBannerList.add("http://img4.imgtn.bdimg.com/it/u=1931147940,1655865016&fm=15&gp=0.jpg");
        mBannerList.add("http://img5.imgtn.bdimg.com/it/u=363482050,555400723&fm=15&gp=0.jpg");

        BannerView bannerView = (BannerView) LayoutInflater.from(this)
                .inflate(R.layout.layout_banner_view, mRecyclerView, false);
        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(),R.layout.item_test_bannerview,null);
                }
                ImageView imageView = (ImageView) convertView.findViewById(R.id.item_banner);
                Glide.with(getApplicationContext()).load(mBannerList.get(position)).into(imageView);
                return convertView;
            }

            @Override
            public int getCount() {
                return mBannerList.size();
            }
        });
        bannerView.setOnBannerItemClickListener(new BannerViewPager.BannerItemClickListener() {
            @Override
            public void click(int position) {
                Toast.makeText(getApplicationContext(), position+"", Toast.LENGTH_SHORT).show();
            }
        });
        // 开启滚动
        bannerView.startRoll();
        mRecyclerView.addHeaderView(bannerView);
        //---------------    头部轮播   ----------------------//


    }

    // 实现左边侧滑删除 和 拖动排序
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 获取触摸响应的方向   包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
            // 代表只能是向左侧滑删除，当前可以是这样ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
            int swipeFlags = ItemTouchHelper.LEFT;


            // 拖动
            int dragFlags = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                // GridView 样式四个方向都可以
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.LEFT |
                        ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT;
            } else {
                // ListView 样式不支持左右，只支持上下
                dragFlags = ItemTouchHelper.UP |
                        ItemTouchHelper.DOWN;
            }

            return makeMovementFlags(dragFlags, swipeFlags);
        }

        /**
         * 拖动的时候不断的回调方法
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // 获取原来的位置
            int fromPosition = viewHolder.getAdapterPosition();
            // 得到目标的位置
            int targetPosition = target.getAdapterPosition();
            if (fromPosition > targetPosition) {
                for (int i = fromPosition; i < targetPosition; i++) {
                    Collections.swap(mList, i, i + 1);// 改变实际的数据集
                }
            } else {
                for (int i = fromPosition; i > targetPosition; i--) {
                    Collections.swap(mList, i, i - 1);// 改变实际的数据集
                }
            }
            adapter.notifyItemMoved(fromPosition, targetPosition);
            return true;
        }

        /**
         * 侧滑删除后会回调的方法
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // 获取当前删除的位置
            int position = viewHolder.getAdapterPosition();
            mList.remove(position);
            // adapter 更新notify当前位置删除
            adapter.notifyItemRemoved(position);
        }

        /**
         * 拖动选择状态改变回调
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                // ItemTouchHelper.ACTION_STATE_IDLE 看看源码解释就能理解了
                // 侧滑或者拖动的时候背景设置为灰色
                viewHolder.itemView.setBackgroundColor(Color.GRAY);
            }
        }

        /**
         * 回到正常状态的时候回调
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            // 正常默认状态下背景恢复默认
            viewHolder.itemView.setBackgroundColor(0);
            viewHolder.itemView.setTranslationX(0);
        }
    });

}
