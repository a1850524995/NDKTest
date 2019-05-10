package com.chinaso.ndktest.dlib1;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;

/***
 * 将取回的预览画面转化成bitmap，交由dlib处理
 */
public class OnGetImageListener implements ImageReader.OnImageAvailableListener {
    private static  final boolean SAVE_PREVIEW_BITMAP = false;
    private static final int INPUT_SIZE = 224;
    private static final  String TAG = "OnGetImageListener";
    private int mScreenRotation = 90;
    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;
    private byte[][] mYUVBytes;
    private int[] mRGBBytes = null;
    private Bitmap mRGBframeBitmap = null;
    private Bitmap mCroppedBitmap = null;

    private boolean mIsComputing = false;
    private Handler mInferenceHandler;
    private Context mContext;
    private FaceDet mFaceDet;
    private TrasparentTitleView mTrasparentTitleView;
    private FloatingCameraWindow mWindow;
    private Paint mFaceLandmarkPaint;

    public void initialize(
            final Context context,
            final AssetManager assetManager,
            final TrasparentTitleView scoreView,
            final Handler handler) {
        this.mContext = context;
        this.mTrasparentTitleView = scoreView;
        this.mInferenceHandler = handler;
        mFaceDet = new FaceDet(Constants.getFaceShapeModelPath());
        mWindow = new FloatingCameraWindow(mContext);

        mFaceLandmarkPaint = new Paint();
        mFaceLandmarkPaint.setColor(Color.GREEN);
        mFaceLandmarkPaint.setStrokeWidth(2);
        mFaceLandmarkPaint.setStyle(Paint.Style.STROKE);
    }
    public void deInitialize() {
        synchronized (OnGetImageListener.class) {
            if (mFaceDet != null) {
                mFaceDet.release();
            }
            if (mWindow != null) {
                mWindow.release();
            }
        }
    }

    private void drawResizeBitmap(final Bitmap src, final Bitmap dst) {
        Display getOrient = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        Point point = new Point();
        getOrient.getSize(point);
        int screen_width = point.x;
        int screen_height = point.y;
        Log.d(TAG, String.format("screen size (%d,%d)", screen_width, screen_height));
        if (screen_width < screen_height) {
            orientation = Configuration.ORIENTATION_PORTRAIT;
            mScreenRotation = 90;
        } else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
            mScreenRotation = 0;
        }
        if (SAVE_PREVIEW_BITMAP) {
           ImageUtils.saveBitmap(mCroppedBitmap);
        }
        mInferenceHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }


    @Override
    public void onImageAvailable(final ImageReader reader) {
        Image image = null;
        try {
            image = reader.acquireLatestImage();
            if (image == null) {
                return;
            }
            if (mIsComputing) {
                image.close();
                return;
            }
            mIsComputing = true;
            Trace.beginSection("imageAvailable");
            final Image.Plane[] planes = image.getPlanes();//todo 关于图片数组格式相关，获取像素平面数组,与图像格式相关
            if (mPreviewWidth != image.getWidth() || mPreviewHeight != image.getHeight()) {
                mPreviewHeight = image.getHeight();
                mPreviewWidth = image.getWidth();
                Log.d(TAG, String.format("Initializing at size %dx%d", mPreviewWidth, mPreviewHeight));
                mRGBBytes = new int[mPreviewWidth * mPreviewHeight];
                mRGBframeBitmap = Bitmap.createBitmap(mPreviewWidth, mPreviewHeight, Bitmap.Config.ARGB_8888);
                mCroppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);
                mYUVBytes = new byte[planes.length][];//其中一种存储方式，吧Y、U、V分别存到不同的数组中
                for (int i = 0;i < planes.length; i++ ) {
                    mYUVBytes[i] = new byte[planes[i].getBuffer().capacity()];
                }
            }//todo 此处的范围限定似有问题，可能会出现mYUVBytes 没初始化的情况。
            for (int i = 0; i < planes.length; i++) {
                planes[i].getBuffer().get(mYUVBytes[i]);
            }
            final int yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();
            ImageUtils.convertYUV420ToARGB8888(
                    mYUVBytes[0],
                    mYUVBytes[1],
                    mYUVBytes[2],
                    mRGBBytes,
                    mPreviewWidth,
                    mPreviewHeight,
                    yRowStride,
                    uvRowStride,
                    uvPixelStride,
                    false);
            image.close();
        } catch (Exception e) {
            if (image != null) {
                image.close();
            }
            Log.e(TAG, "Exception!", e);
            e.printStackTrace();
            return;
        }
        mRGBframeBitmap.setPixels(mRGBBytes, 0, mPreviewWidth, 0, 0,mPreviewWidth, mPreviewHeight);
        drawResizeBitmap(mRGBframeBitmap, mCroppedBitmap);



    }
}
