package com.app.rzm.ui.clip;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.rzm.R;
import com.rzm.commonlibrary.utils.TimeFormatUtil;

import java.io.File;
import java.util.ArrayList;


public class ChooseVideoAdapter extends RecyclerView.Adapter {

    private final Context mContext;
    private ArrayList<VideoInfo> mResultImageList;
    public ChooseVideoAdapter(Context context, ArrayList<VideoInfo> imageList) {
        this.mContext = context;
        this.mResultImageList = imageList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_chooser_item,parent,false);
        ImageHolder holder = new ImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageHolder viewHolder = (ImageHolder) holder;
        viewHolder.setData(mResultImageList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mResultImageList == null? 0:mResultImageList.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView time;

        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            time = (TextView) itemView.findViewById(R.id.time);
        }

        public void setData(final VideoInfo info, final int position) {
            String path = queryVideoThumbnailByPath(mContext,info.id);
            System.out.println("aaaaa info.path:"+info.path);
            System.out.println("aaaaa ,path:"+path);
            //本机拍摄的视频才会生成缩略图，如果是截取的或者从其他地方接收的视频是无法获取到的，所以需要手动获取
            if (path == null || path == ""){
                Glide.with(mContext).load(Uri.fromFile(new File(info.path)))
                        .centerCrop().into(imageView);
            }else{
                Glide.with(mContext).load(path)
                        .centerCrop().into(imageView);
            }
            time.setText(TimeFormatUtil.timeFormate(info.time));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        mListener.onImageSelect(itemView,info.path,position);
                    }
                }
            });
        }
    }

    public static String queryVideoThumbnailByPath(Context context, long mediaId) {

        Uri uri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
        String[] projection = new String[] { MediaStore.Video.Thumbnails.DATA };
        String selection = MediaStore.Video.Thumbnails.VIDEO_ID + " =  ? ";
        String[] selectionArgs = new String[] { String.valueOf(mediaId) };

        Cursor cursor = query(context, uri, projection, selection, selectionArgs);
        String thumbnail = null;
        if (cursor.moveToFirst()) {
            int idxData = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);
            thumbnail = cursor.getString(idxData);
        }
        cursor.close();
        return thumbnail;
    }

    private static Cursor query(Context context, Uri uri, String[] projection,
                                String selection, String[] selectionArgs) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, projection, selection, selectionArgs,
                null);
        return cursor;
    }

    public interface ChooseVideoListener {
        void onImageSelect(View view, String path, int position);
    }

    private ChooseVideoListener mListener;
    public void setOnSelectImageListener(ChooseVideoListener listener){
        this.mListener = listener;
    }

}
