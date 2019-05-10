package com.chinaso.ndktest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.chinaso.ndktest.dlib1.FileUtils;
import com.chinaso.ndktest.utils.ScreenUtil;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageView;
    private Bitmap mBitmap;
    private File mFile;
    TextView mTextView;
    Button mButton;
    Button mDetectionButton;
    private FaceDet mFaceDet;
    private static final int RESULT_LOAD_IMG = 1;
    private String mTestImgPath;
    private ProgressDialog mDialog;
    private String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textview);
        mButton = findViewById(R.id.button);
        mDetectionButton = findViewById(R.id.face_detect_button);
        mImageView = findViewById(R.id.face_image);

        mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.face_image);
        mImageView.setImageBitmap(mBitmap);




        mDetectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);



            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       /* Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                                R.mipmap.ic_launcher_round)).getBitmap();*/
                        int w = mBitmap.getWidth(), h = mBitmap.getHeight();
                        int[] pix = new int[w * h];
                        mBitmap.getPixels(pix, 0, w, 0, 0, w, h);
                        int[] resultPixels = JNIUtils.getGrayImage(pix,w,h);
                        Bitmap result = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
                        result.setPixels(resultPixels, 0, w, 0, 0,w, h);
                        mImageView.setImageBitmap(result);
                    }
                },2000);




            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};//令人疑惑的引用，此处换为String变量也可以
            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mTestImgPath = cursor.getString(columnIndex);
            cursor.close();
            if (mTestImgPath != null) {
                demoFaceDet(mTestImgPath);
                Toast.makeText(this, "Img Path:" + mTestImgPath, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 开新线程处理图片，静态内部类防止内存泄漏
     */
    static class FaceDetectAsyncTask extends AsyncTask<String, Void, List<VisionDetRet>>{
        private final WeakReference<MainActivity> mMainActivityWeakReference;
        FaceDetectAsyncTask(MainActivity mainActivity){
            mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = mMainActivityWeakReference.get();
            if (activity.mDialog != null) {
                activity.mDialog.dismiss();
                activity.mDialog = null;
            }
           activity.mDialog = ProgressDialog.show(activity, "FaceDetect", "process...", true);
        }

        @Override
        protected List<VisionDetRet> doInBackground(String ... imgPath) {
            Log.e("haha", "doInBackground:1 " );
            MainActivity activity = mMainActivityWeakReference.get();
            if (activity.mFaceDet == null) {
                activity.mFaceDet = new FaceDet(Constants.getFaceShapeModelPath());
            }
            final String targetPath = Constants.getFaceShapeModelPath();
            if (!new File(targetPath).exists()) {
                FileUtils.copyFileFromRawToOthers(activity, R.raw.shape_predictor_68_face_landmarks, targetPath);

            }
            activity.imagePath =imgPath[0];
            Log.e("haha", "doInBackground: 2" );
            List<VisionDetRet> faceList = activity.mFaceDet.detect(imgPath[0]);
            Log.e("haha", "doInBackground: 3" );
            return faceList;
        }

        @Override
        protected void onPostExecute(List<VisionDetRet> visionDetRets) {
            super.onPostExecute(visionDetRets);
            MainActivity activity = mMainActivityWeakReference.get();
            activity.mImageView.setImageBitmap(drawRect(activity.imagePath, visionDetRets, Color.BLUE, mMainActivityWeakReference.get()));
           activity.mDialog.dismiss();
        }
    }


    private static Bitmap drawRect(String path, List<VisionDetRet> results, int color, Activity activity) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        android.graphics.Bitmap.Config bitmapConfig = bm.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bm = bm.copy(bitmapConfig, true);
        int width = bm.getWidth();
        int height = bm.getHeight();
        // By ratio scale
        float aspectRatio = bm.getWidth() / (float) bm.getHeight();
        float screenWidth = new ScreenUtil(activity).getScreenSize("WIDTH");
        float screenHeight = new ScreenUtil(activity).getScreenSize("HEIGHT");
        float screenRatio = screenWidth/screenHeight;
        int newHeight = height;
        int newWidth = width;
        if (screenRatio > aspectRatio) {
            newHeight = height;
            newWidth = (int) (height * aspectRatio);
        }else {
            newWidth = width;
            newHeight = (int) (width/aspectRatio);
        }



        /*final int MAX_SIZE = 512;
        int newWidth = MAX_SIZE;
        int newHeight = MAX_SIZE;*/
        float resizeRatio = 1;
        newHeight = Math.round(newWidth / aspectRatio);
        if (bm.getWidth() > screenWidth && bm.getHeight() > screenWidth) {

            bm = (Bitmap) getResizedBitmap(bm, newWidth, newHeight);
            resizeRatio = (float) bm.getWidth() / (float) width;

        }

        // Create canvas to draw
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        // Loop result list
        for (VisionDetRet ret : results) {
            Rect bounds = new Rect();
            bounds.left = (int) (ret.getLeft() * resizeRatio);
            bounds.top = (int) (ret.getTop() * resizeRatio);
            bounds.right = (int) (ret.getRight() * resizeRatio);
            bounds.bottom = (int) (ret.getBottom() * resizeRatio);
            canvas.drawRect(bounds, paint);
            // Get landmark
            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            for (Point point : landmarks) {
                int pointX = (int) (point.x * resizeRatio);
                int pointY = (int) (point.y * resizeRatio);
                canvas.drawCircle(pointX, pointY, 2, paint);
            }
        }

        return new BitmapDrawable(activity.getResources(), bm).getBitmap();
    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return resizedBitmap;
    }
    private void demoFaceDet(String testImgPath) {
        new FaceDetectAsyncTask(MainActivity.this).execute(testImgPath);


    }
}
