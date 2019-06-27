package com.sky.customviewstudy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sky.customviewstudy.bean.SizeModel;

import org.jcodec.api.android.AndroidSequenceEncoder;

import java.io.File;
import java.io.IOException;

import VideoHandle.OnEditorListener;

/**
 * @author: xuzhiyong
 * @date: 2018/10/24 15:39
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class BitmapUtil {

    public static SizeModel getImageSize(String url){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        return new SizeModel(opts.outWidth,opts.outHeight);
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath,
                                                     int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static void checkArgs(String filePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static void Image2Mp4(String videoin, String outPath, int w, int h, OnEditorListener onEditorListener){
        try {
            Log.e("performJcodec: ", "执行开始");
            AndroidSequenceEncoder se   = null;
            File file = new File(videoin);


            File out = new File(outPath);
            se =AndroidSequenceEncoder.createSequenceEncoder(out,1);

            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].exists()) { break; }
                Bitmap frame = BitmapUtil.decodeSampledBitmapFromFile(files[i].getAbsolutePath() , w , h);

                se.encodeImage(frame);
                Log.e("performJcodec: ", "执行到的图片是 " + i);
            }
            se.finish();
            Log.e("performJcodec: ", "执行完成");
            if(onEditorListener != null){
                onEditorListener.onSuccess();
            }

        } catch (IOException e) {
            Log.e("performJcodec: ", "执行异常 " + e.toString());
            if(onEditorListener != null){
                onEditorListener.onFailure();
            }
        }
    }
}
