package com.rzm.commonlibrary.general.videoeditor.drawer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.rzm.commonlibrary.general.videoeditor.filter.basefilter.GPUImageFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.AFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.GroupFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.MagicBeautyFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.NoFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.ProcessFilter;
import com.rzm.commonlibrary.general.videoeditor.filter.filter.RotationOESFilter;
import com.rzm.commonlibrary.general.videoeditor.player.VideoInfo;
import com.rzm.commonlibrary.general.videoeditor.utils.GLUtils;
import com.rzm.commonlibrary.general.videoeditor.utils.MatrixUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 添加水印和美白效果
 */

public class VideoDrawer implements GLSurfaceView.Renderer {
    public static final String TAG="VideoDrawer";
    /**
     * 用于后台绘制的变换矩阵
     */
    private float[] OM;
    /**
     * 用于显示的变换矩阵
     */
    private float[] SM = new float[16];
    private SurfaceTexture surfaceTexture;
    /**
     * 可选择画面的滤镜
     */
    private RotationOESFilter mPreFilter;
    /**
     * 显示的滤镜
     */
    private AFilter mShow;
    /**
     * 美白的filter
     */
    private MagicBeautyFilter mBeautyFilter;
    private AFilter mProcessFilter;
    /**
     * 绘制水印的滤镜
     */
    private final GroupFilter mBeFilter;


    /**
     * 绘制其他样式的滤镜
     */
    private GPUImageFilter mGroupFilter;
    /**
     * 控件的长宽
     */
    private int viewWidth;
    private int viewHeight;

    /**
     * 创建离屏buffer
     */
    private int[] fFrame = new int[1];
    private int[] fTexture = new int[1];
    /**
     * 用于视频旋转的参数
     */
    private int rotation;
    /**
     * 是否开启美颜
     */
    private boolean isBeauty = false;


    public VideoDrawer(Context context, Resources res) {
        mPreFilter = new RotationOESFilter(res);//旋转相机操作
        mShow = new NoFilter(res);
        mBeFilter = new GroupFilter(res);
        mBeautyFilter = new MagicBeautyFilter(context);

        mProcessFilter = new ProcessFilter(res);

        OM = MatrixUtils.getOriginalMatrix();
        MatrixUtils.flip(OM, false, true);//矩阵上下翻转
//        mShow.setMatrix(OM);

//        WaterMarkFilter waterMarkFilter = new WaterMarkFilter(res);
//        waterMarkFilter.setWaterMark(BitmapFactory.decodeResource(res, R.mipmap.watermark));
//
//        waterMarkFilter.setPosition(0, 70, 0, 0);
//        mBeFilter.addFilter(waterMarkFilter);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        int texture[] = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        surfaceTexture = new SurfaceTexture(texture[0]);
        mPreFilter.create();
        mPreFilter.setTextureId(texture[0]);

        mBeFilter.create();
        mProcessFilter.create();
        mShow.create();
        mBeautyFilter.init();
        mBeautyFilter.setBeautyLevel(3);//默认设置3级的美颜
    }

    public void onVideoChanged(VideoInfo info) {
        setRotation(info.rotation);
        if (info.rotation == 0 || info.rotation == 180) {
            MatrixUtils.getShowMatrix(SM, info.width, info.height, viewWidth, viewHeight);
        } else {
            MatrixUtils.getShowMatrix(SM, info.height, info.width, viewWidth, viewHeight);
        }

        mPreFilter.setMatrix(SM);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        viewWidth = width;
        viewHeight = height;
        GLES20.glDeleteFramebuffers(1, fFrame, 0);
        GLES20.glDeleteTextures(1, fTexture, 0);

        GLES20.glGenFramebuffers(1, fFrame, 0);
        GLUtils.genTextures(1, fTexture, 0, GLES20.GL_RGBA, viewWidth, viewHeight);

        mBeFilter.setSize(viewWidth, viewHeight);
        mProcessFilter.setSize(viewWidth, viewHeight);
        mBeautyFilter.onDisplaySizeChanged(viewWidth, viewHeight);
        mBeautyFilter.onInputSizeChanged(viewWidth, viewHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        surfaceTexture.updateTexImage();
        GLUtils.bindFrameTexture(fFrame[0], fTexture[0]);
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mPreFilter.draw();//draw video surface texture to fbo
        GLUtils.unBindFrameBuffer();

 /*       mBeFilter.setTextureId(fTexture[0]);
        mBeFilter.draw();//draw mPreFilter to a mBeFilter fbo

        int[] drawFboId=new int[1];
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING,drawFboId,0);
        Log.i(TAG,"after mBeFilter draw fbo id is "+drawFboId[0]);


        if (mBeautyFilter != null && isBeauty && mBeautyFilter.getBeautyLevel() != 0) {
            GLUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            GLES20.glViewport(0, 0, viewWidth, viewHeight);
            mBeautyFilter.onDrawFrame(mBeFilter.getOutputTexture());
            GLUtils.unBindFrameBuffer();
            mProcessFilter.setTextureId(fTexture[0]);
        } else {
            mProcessFilter.setTextureId(mBeFilter.getOutputTexture());
        }
        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mProcessFilter.draw();

        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING,drawFboId,0);
        Log.i(TAG,"after mProcessFilter draw fbo id is "+drawFboId[0]);*/

        //mSlideFilterGroup.onDrawFrame(mProcessFilter.getOutputTexture());
      /*  if (mGroupFilter != null) {
            GLUtils.bindFrameTexture(fFrame[0], fTexture[0]);
            GLES20.glViewport(0, 0, viewWidth, viewHeight);
            //  mGroupFilter.onDrawFrame(mSlideFilterGroup.getOutputTexture());
            GLUtils.unBindFrameBuffer();
            mProcessFilter.setTextureId(fTexture[0]);
        } else {
            //mProcessFilter.setTextureId(mSlideFilterGroup.getOutputTexture());
        }
        mProcessFilter.draw();

        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mShow.setTextureId(mProcessFilter.getOutputTexture());
        mShow.draw();*/

        GLES20.glViewport(0, 0, viewWidth, viewHeight);
        mShow.setTextureId(fTexture[0]);
        mShow.draw();
    }

    public SurfaceTexture getSurfaceTexture() {
        return surfaceTexture;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        if (mPreFilter != null) {
            mPreFilter.setRotation(this.rotation);
        }
    }

    /**
     * 切换开启美白效果
     */
    public void switchBeauty() {
        isBeauty = !isBeauty;
    }

    /**
     * 是否开启美颜功能
     */
    public void isOpenBeauty(boolean isBeauty) {
        this.isBeauty = isBeauty;
    }

    /**
     * 触摸事件监听
     */
    public void onTouch(MotionEvent event) {
        //mSlideFilterGroup.onTouchEvent(event);
    }

    /**
     * 滤镜切换的监听
     */
//    public void setOnFilterChangeListener(SlideGpuFilterGroup.OnFilterChangeListener listener){
//        mSlideFilterGroup.setOnFilterChangeListener(listener);
//    }
    public void checkGlError(String s) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(s + ": glError " + error);
        }
    }

    public void setGpuFilter(GPUImageFilter filter) {
        if (filter != null) {
            mGroupFilter = filter;
            mGroupFilter.init();
            mGroupFilter.onDisplaySizeChanged(viewWidth, viewWidth);
            mGroupFilter.onInputSizeChanged(viewWidth, viewHeight);
        }

    }
}
