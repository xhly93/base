package com.ssj.user.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ssj.user.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class BitmapUtils {
    private static final String TAG ="BitmapUtils" ;

    public static Bitmap loadBitmap(String path) {
        Bitmap bm = null;
        bm = BitmapFactory.decodeFile(path);
        return bm;
    }

    public static Bitmap loadBitmap(byte[] data, int width, int height) {
        Bitmap bm = null;
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        bm = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        int x = opts.outWidth / width;
        int y = opts.outHeight / height;
        opts.inSampleSize = x > y ? x : y;
        opts.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        return bm;
    }

    public static void save(Bitmap bm, File file) throws Exception {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(file);
        bm.compress(CompressFormat.JPEG, 100, stream);
    }

    //dstWidth��dstHeight�ֱ�ΪĿ��ImageView�Ŀ��
    public static int calSampleSize(BitmapFactory.Options options, int dstWidth, int dstHeight) {
        int rawWidth = options.outWidth;
        int rawHeight = options.outHeight;
        int inSampleSize = 1;
        if (rawWidth > dstWidth || rawHeight > dstHeight) {
            float ratioHeight = (float) rawHeight / dstHeight;
            float ratioWidth = (float) rawWidth / dstHeight;
            inSampleSize = (int) Math.min(ratioWidth, ratioHeight);
        }
        return inSampleSize;
    }

    public static Bitmap get(int reqHeight,int reqWidth,String key){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
            BitmapFactory.decodeStream(new URL(key).openStream(), null, options);
            int width = options.outWidth, height = options.outHeight;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = inSampleSize;
            opt.inPreferredConfig =  Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(new URL(key).openStream(), null, opt);
        } catch (IOException e) {
            Log.e(TAG,"get e = "+e.getMessage().toString());
        }catch (OutOfMemoryError e) {
            Log.e(TAG,"get OutOfMemoryError e = "+e.getMessage().toString());
            System.gc();
        }

        return bitmap;
    }

    public static Bitmap getBitmapFromFile(String filePath,int reqHeight,int reqWidth){
        Options options = new Options();
        options.inJustDecodeBounds = true;

        Bitmap bitmap = null;
        try {
            FileInputStream fs = null;
            fs = new FileInputStream(filePath);
            BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
            Options opt = new Options();
            opt.inSampleSize = calSampleSize(options, reqWidth, reqHeight);
            Log.i(TAG,"getBitmapFromFile inSampleSize = "+ opt.inSampleSize);
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, opt);
        } catch (IOException e) {
            Log.e(TAG, "get IOException e = " + e.getMessage());
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "get OutOfMemoryError e = " + e.getMessage());
            System.gc();
        }
        return bitmap;
    }

    public static Bitmap getBitmapFromRes(Context context, int resId, ImageView imageView){
        if (null==imageView){
            Log.i(TAG,"getBitmapFromRes null==imageView ");
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inSampleSize = calSampleSize(options, imageView.getWidth(), imageView.getHeight());
        Log.i(TAG,"getBitmapFromRes inSampleSize = "+ opt.inSampleSize);
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static void loadBitmapToImage(Context mContext,String url,ImageView imageview){
        Glide.with(mContext).load(url)
                .asBitmap()
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .into(imageview);
    }


}
