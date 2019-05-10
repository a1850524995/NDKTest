package com.chinaso.ndktest;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

public class DetectionBasedTracker {
    private long mNativeObj = 0;
    public DetectionBasedTracker(String cascadeName, int minFaceSize){
        mNativeObj = nativeCreateObject(cascadeName, minFaceSize);

    }
    public void setMinFaceSize(int size) {
        nativeSetFaceSize(mNativeObj, size);
    }



    public void start(){
        nativeStart(mNativeObj);
    }
    public void stop() {
        nativeStop(mNativeObj);
    }
    public void detect(Mat imageGray, MatOfRect faces) {
        nativeDetect(mNativeObj, imageGray.getNativeObjAddr(), faces.getNativeObjAddr());
    }
    public void release() {
        nativeDestroyObject(mNativeObj);
        mNativeObj = 0;
    }

    private static native  void nativeDestroyObject(long nativeObj);

    private static native void nativeDetect(long nativeObj, long nativeObjAddr, long nativeObjAddr1);

    private static native void nativeStop(long nativeObj);

    private static native void nativeStart(long nativeObj);

    private static native long nativeCreateObject(String cascadeName, int minFaceSize);

    private static  native void nativeSetFaceSize(long nativeObj, int size) ;
}
