package com.rzm.commonlibrary.general.videoeditor.recorder;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;


/**
 * Created by liugang on 2017/11/17.
 */

public class VideoEncoder {

    private static final String TAG = "EncodeAndMuxTest";
    private static final boolean VERBOSE = true;           // lots of logging

    // where to put the output file (note: /sdcard requires WRITE_EXTERNAL_STORAGE permission)
    private static final File OUTPUT_DIR = Environment.getExternalStorageDirectory();

    // parameters for the encoder
    private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
    private static final int FRAME_RATE = 15;               // 15fps
    private static final int IFRAME_INTERVAL = 10;          // 10 seconds between I-frames
    private static final int NUM_FRAMES = 150;               // two seconds of video

    // size of a frame, in pixels
    private int mWidth = -1;
    private int mHeight = -1;
    // bit rate, in bits per second
    private int mBitRate = 2000000;

    // encoder / muxer state
    private MediaCodec mEncoder;
    public CodecInputSurface mInputSurface;
    private MediaMuxer mMuxer;
    private int mTrackIndex;
    private boolean mMuxerStarted;

    // allocate one of these up front so we don't need to do it every time
    private MediaCodec.BufferInfo mBufferInfo;

    private Context mContext;

    public VideoEncoder(Context context, int width, int height) {
        mWidth = width;
        mHeight = height;
        mContext = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void generateSurfaceFrame(int frameIndex) {
        Random rnd = new Random();
        mInputSurface.makeCurrent();
        GLES20.glClearColor(rnd.nextInt(256) / 255.0f, rnd.nextInt(256) / 255.0f, rnd.nextInt(256) / 255.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //Log.d(TAG, "generateSurfaceFrame,textureID=" + textureID);
        square.draw(textureID);
    }


    private Square square;
    public int textureGround;
    public boolean stop = false;
    private int textureID;
    private boolean newFrameIsAvailable;

    public void pushFrame(int textureID) {
        this.textureID = textureID;
        newFrameIsAvailable = true;
    }

    public void stop() {
        stop = true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void start() {
        try {
            prepareEncoder();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {

                try {
                    mInputSurface.makeCurrent();
                    square = new Square();
                    //textureGround = TextureHelper.loadTexture(mContext, R.drawable.ground);
                    int i = 0;
                    //for (int i = 0; i < NUM_FRAMES; i++)
                    while (!stop) {
                        if (newFrameIsAvailable) {
                            // Feed any pending encoder output into the muxer.
                            drainEncoder(false);

                            // Generate a new frame of input.
                            generateSurfaceFrame(i);
                            mInputSurface.setPresentationTime(computePresentationTimeNsec(i));

                            // Submit it to the encoder.  The eglSwapBuffers call will block if the input
                            // is full, which would be bad if it stayed full until we dequeued an output
                            // buffer (which we can't do, since we're stuck here).  So long as we fully drain
                            // the encoder before supplying additional input, the system guarantees that we
                            // can supply another frame without blocking.
                            if (VERBOSE) Log.d(TAG, "sending frame " + i + " to encoder");
                            mInputSurface.swapBuffers();
                            i++;
                            newFrameIsAvailable = false;
                        }

                    }

                    // send end-of-stream to encoder, and drain remaining output
                    drainEncoder(true);
                } finally {
                    // release encoder, muxer, and input Surface
                    releaseEncoder();
                }

                // To test the result, open the file with MediaExtractor, and get the format.  Pass
                // that into the MediaCodec decoder configuration, along with a SurfaceTexture surface,
                // and examine the output with glReadPixels.
            }
        }).start();

    }


    /**
     * Configures encoder and muxer state, and prepares the input Surface.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void prepareEncoder() throws IOException {
        Log.d(TAG, "prepareEncoder");

        mBitRate = 2000000;

        mBufferInfo = new MediaCodec.BufferInfo();

        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);
        Log.d(TAG, "createVideoFormat");

        // Set some properties.  Failing to specify some of these can cause the MediaCodec
        // configure() call to throw an unhelpful exception.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
        Log.d(TAG, "format: " + format);

        // Create a MediaCodec encoder, and configure it with our format.  Get a Surface
        // we can use for input and wrap it with a class that handles the EGL work.
        //
        // If you want to have two EGL contexts -- one for display, one for recording --
        // you will likely want to defer instantiation of CodecInputSurface until after the
        // "display" EGL context is created, then modify the eglCreateContext call to
        // take eglGetCurrentContext() as the share_context argument.
        mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
        Log.d(TAG, "createEncoderByType");

        mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mInputSurface = new CodecInputSurface(mEncoder.createInputSurface());

        mEncoder.start();
        Log.d(TAG, "mEncoder.start");
        // Output filename.  Ideally this would use Context.getFilesDir() rather than a
        // hard-coded output directory.
        String outputPath = new File(OUTPUT_DIR,
                "test." + mWidth + "x" + mHeight + ".mp4").toString();
        Log.d(TAG, "output file is " + outputPath);

        //Toast.makeText(this,"output file is " + outputPath,Toast.LENGTH_SHORT).show();

        // Create a MediaMuxer.  We can't add the video track and start() the muxer here,
        // because our MediaFormat doesn't have the Magic Goodies.  These can only be
        // obtained from the encoder after it has started processing data.
        //
        // We're not actually interested in multiplexing audio.  We just want to convert
        // the raw H.264 elementary stream we get from MediaCodec into a .mp4 file.
        try {
            mMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException ioe) {
            throw new RuntimeException("MediaMuxer creation failed", ioe);
        }

        mTrackIndex = -1;
        mMuxerStarted = false;
    }

    /**
     * Releases encoder resources.  May be called after partial / failed initialization.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void releaseEncoder() {
        if (VERBOSE) Log.d(TAG, "releasing encoder objects");
        if (mEncoder != null) {
            mEncoder.stop();
            mEncoder.release();
            mEncoder = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mMuxer != null) {
            mMuxer.stop();
            mMuxer.release();
            mMuxer = null;
        }
    }

    /**
     * Extracts all pending data from the encoder.
     * <p>
     * If endOfStream is not set, this returns when there is no more data to drain.  If it
     * is set, we send EOS to the encoder, and then iterate until we see EOS on the output.
     * Calling this with endOfStream set should be done once, right before stopping the muxer.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void drainEncoder(boolean endOfStream) {
        final int TIMEOUT_USEC = 10000;
        if (VERBOSE) Log.d(TAG, "drainEncoder(" + endOfStream + ")");

        if (endOfStream) {
            if (VERBOSE) Log.d(TAG, "sending EOS to encoder");
            mEncoder.signalEndOfInputStream();
        }

        ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
        while (true) {
            int index = mEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
            if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // no output available yet
                if (!endOfStream) {
                    break;      // out of while
                } else {
                    if (VERBOSE) Log.d(TAG, "no output available, spinning to await EOS");
                }
            } else if (index == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                // not expected for an encoder
                encoderOutputBuffers = mEncoder.getOutputBuffers();
            } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only happen once
                if (mMuxerStarted) {
                    throw new RuntimeException("format changed twice");
                }
                MediaFormat newFormat = mEncoder.getOutputFormat();
                Log.d(TAG, "encoder output format changed: " + newFormat);

                // now that we have the Magic Goodies, start the muxer
                mTrackIndex = mMuxer.addTrack(newFormat);
                mMuxer.start();
                mMuxerStarted = true;
            } else if (index < 0) {
                Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " +
                        index);
                // let's ignore it
            } else {
                ByteBuffer encodedData = encoderOutputBuffers[index];
                if (encodedData == null) {
                    throw new RuntimeException("encoderOutputBuffer " + index +
                            " was null");
                }

                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    // The codec config data was pulled out and fed to the muxer when we got
                    // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                    if (VERBOSE) Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    mBufferInfo.size = 0;
                }

                if (mBufferInfo.size != 0) {
                    if (!mMuxerStarted) {
                        throw new RuntimeException("muxer hasn't started");
                    }

                    // adjust the ByteBuffer values to match BufferInfo (not needed?)
                    encodedData.position(mBufferInfo.offset);
                    encodedData.limit(mBufferInfo.offset + mBufferInfo.size);

                    mMuxer.writeSampleData(mTrackIndex, encodedData, mBufferInfo);
                    if (VERBOSE) Log.d(TAG, "sent " + mBufferInfo.size + " bytes to muxer");
                }

                mEncoder.releaseOutputBuffer(index, false);

                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    if (!endOfStream) {
                        Log.w(TAG, "reached end of stream unexpectedly");
                    } else {
                        if (VERBOSE) Log.d(TAG, "end of stream reached");
                    }
                    break;      // out of while
                }
            }
        }
    }


    /**
     * Generates the presentation time for frame N, in nanoseconds.
     */
    private long computePresentationTimeNsec(int frameIndex) {
        final long ONE_BILLION = 1000000000;
        return frameIndex * ONE_BILLION / FRAME_RATE;
    }


}
