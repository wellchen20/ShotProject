package com.jpkh.cnpc.activity.utils;

public class CVUtils {
    static {
        System.loadLibrary("CVUtils");
    }

    public static native void img2gray(String imgPath);

    //仪表指针识别, -1:识别失败  其他值: 识别出的指针百分比，需要自己再乘以量程得到具体值
    //public static native float GetMeterCalibrate(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2);
    public static native float GetMeterValue(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2, float iMinValue, float iMaxValue);
    public static native UPSStatus GetUpsStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2, UPSStatus buttonStatus);
    public float getZHvaules(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2)
    {
        //UPSStatus UPS = new UPSStatus();
        return GetMeterValue(data,width,height,screenWidth,screenHeight,x1,y1,x2,y2, (float)0, (float)120);

        //GetUpsStatus(data,width,height,screenWidth,screenHeight,x1,y1,x2,y2, UPS);
        //return (float) 0.0;
    }

    //指示灯是否亮:  错误:-1 0:红不亮 1:红亮 2:绿亮 3:绿不亮
    public static native int GetLightColor(String imgPath);

    //切断阀状态: open:1 close:0
    public static native int GetValveStatus(byte[] data,int width, int height,int x1, int y1, int x2, int y2);

    public int getValues(byte[] data,int width, int height,int x1, int y1, int x2, int y2)
    {
        return  GetValveStatus(data,width,height,x1,y1,x2,y2);
    }



    // 仪控室旋钮状态
    public static native int GetKnobStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2);
    public int getKnobStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2) {
        return GetKnobStatus(data, width, height, screenWidth,screenHeight,x1,y1,x2,y2);
    }

    // 仪控室红灯状态
    public static native int GetHistRedStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2);
    public int getRedLightStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2) {
        return GetHistRedStatus(data, width, height, screenWidth,screenHeight,x1,y1,x2,y2);
    }

    // 仪控室绿灯状态
    public static native int GetHistGreenStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2);
    public int getGreenLightStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2) {
        return GetHistGreenStatus(data, width, height, screenWidth,screenHeight,x1,y1,x2,y2);
    }

    // 排液切断阀
    public static native int GetDrainageValveStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2);
    public int getValveStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2) {
        return GetDrainageValveStatus(data, width, height, screenWidth,screenHeight,x1,y1,x2,y2);
    }

    // 缓释液
    public static native int GetBTAStatus(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2);
    public int getBTAState(byte[] data,int width, int height,int screenWidth, int screenHeight,int x1, int y1, int x2, int y2) {
        return GetBTAStatus(data, width, height, screenWidth,screenHeight,x1,y1,x2,y2);
    }
}
