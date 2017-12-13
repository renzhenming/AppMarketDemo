package com.rzm.commonlibrary.general.videoeditor.filter.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.rzm.commonlibrary.R;
import com.rzm.commonlibrary.general.videoeditor.filter.basefilter.GPUImageFilter;
import com.rzm.commonlibrary.general.videoeditor.utils.GLUtils;

/**
 * 美白的filter
 */
public class MagicBeautyFilter extends GPUImageFilter {
    private int mSingleStepOffsetLocation;
    private int mParamsLocation;
    private int mLevel;

    public MagicBeautyFilter(Context context){
        super(NO_FILTER_VERTEX_SHADER ,
                GLUtils.readShaderFromRawResource(context, R.raw.beauty));
    }

    protected void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        setBeautyLevel(3);//beauty Level
    }

    private void setTexelSize(final float w, final float h) {
        setFloatVec2(mSingleStepOffsetLocation, new float[] {2.0f / w, 2.0f / h});
    }

    @Override
    public void onInputSizeChanged(final int width, final int height) {
        super.onInputSizeChanged(width, height);
        setTexelSize(width, height);
    }

    public void setBeautyLevel(int level){
        mLevel=level;
        switch (level) {
            case 1:
                setFloat(mParamsLocation, 1.0f);
                break;
            case 2:
                setFloat(mParamsLocation, 0.8f);
                break;
            case 3:
                setFloat(mParamsLocation,0.6f);
                break;
            case 4:
                setFloat(mParamsLocation, 0.4f);
                break;
            case 5:
                setFloat(mParamsLocation,0.33f);
                break;
            default:
                break;
        }
    }
    public int getBeautyLevel(){
        return mLevel;
    }
    public void onBeautyLevelChanged(){
        setBeautyLevel(3);//beauty level
    }
}
