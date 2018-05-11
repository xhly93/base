package com.ssj.user.Mode.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.ssj.user.R;
import com.ssj.user.SSApplication;
import com.ssj.user.Utils.BitmapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 王矩龙 on 2018/3/7.
 * 图片下载，提供双缓存策略
 */

public class ImageLoader {
    private static final String TAG = "ImageLoader";

    private static int maxMemory = (int) (Runtime.getRuntime().maxMemory());
    private static int cacheSize = maxMemory / 8;
    private static LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };


    //内部类，在装载该内部类时才会去创建单利对象
    private static class SingletonHolder {
        public static ImageLoader instance = new ImageLoader();
    }

    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 加载图片到对应组件
     *
     * @param key      所需加载的路径
     * @param view     被加载的组件
     */
    @SuppressWarnings("deprecation")
    public void loadImage(String key, ImageView view, Context context) {
        Drawable drawable=context.getDrawable(R.drawable.ic_launcher);
        synchronized (view) {
            // 检查缓存中是否已有
            Bitmap bitmap = getFromCache(context, key);
            if (bitmap != null) {
                // 如果有了就从缓存中取出显示
                view.setImageBitmap(bitmap);
                view.setBackground(null);
            } else {
                // 软应用缓存中不存在，磁盘中也不存在，只能下载
                // 下载之前应该先放一张默认图，用来友好显示
                view.setBackgroundDrawable(drawable);
                // 用异步任务去下载
                new CastielAsyncImageLoaderTask(view).execute(key);
            }
        }
    }

    /**
     * 判断缓存中是否已经有了，如果有了就从缓存中取出
     * @param key
     * @return
     */
    public Bitmap getFromCache(Context context, String key) {
        // 检查内存软引中是否存在
        String md5Key = hashKeyForDisk(key);
        Bitmap bitmap = mMemoryCache.get(md5Key);
        if (null != bitmap) {
            return bitmap;
        }
        Bitmap bitmap2 = getFromLocalSD(context, md5Key);
        if (bitmap2 != null) {// 硬盘中有
            mMemoryCache.put(md5Key, bitmap2);
            return bitmap2;
        }
        return null;
    }



    /**
     * 添加到缓存中去
     *
     * @param key
     * @param result
     */
    public void addFirstCache(String key, Bitmap result) {
        String md5Key = hashKeyForDisk(key);
        if (result != null) {
           mMemoryCache.put(md5Key, result);
            diskCache(md5Key, new SoftReference<Bitmap>(result));
        }
    }


    /**
     * 无缓存，下载
     * @param context
     * @param key
     * @return
     */
    public Bitmap downIfNoCache(Context context, String key,ImageView imageView) {
        Bitmap result = getFromCache(context, key);
        if (null == result) {
            new CastielAsyncImageLoaderTask(imageView).execute(key);
            return null;
        }
        return result;
    }

    ;

    /**
     * 判断本地磁盘中是否已经有了该图片，如果有了就从本地磁盘中取出
     *
     * @param key
     * @return
     */
    private Bitmap getFromLocalSD(Context context, String key) {
        String fileName = key;
        if (fileName == null) {// 如果文件名为Null，直接返回null
            return null;
        } else {
            String filePath = context.getCacheDir().getAbsolutePath() + File.separator + fileName;
            InputStream is = null;
            try {
                is = new FileInputStream(new File(filePath));
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e){
                Log.e(TAG,"getFromLocalSD OutOfMemoryError e = "+e.getMessage().toString());
                System.gc();
            }finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 把图片缓存到本地磁盘，拿到图片，写到SD卡中
     *
     * @param key   图片的URL
     * @param value Bitmap
     */
    private static void diskCache(String key, SoftReference<Bitmap> value) {
        // 把写入SD的图片名字改为基于MD5加密算法加密后的名字
        String fileName;
        fileName = key;
        String filePath = SSApplication.getInstance().getApplicationContext().getCacheDir().getAbsolutePath() + File.separator + fileName;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            if (value.get() != null) {
                value.get().compress(Bitmap.CompressFormat.JPEG, 60, os);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @author
     * @ClassName: MyAsyncImageLoaderTask
     * @Description: 异步加载图片
     */
    class CastielAsyncImageLoaderTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;// 图片组件
        private String key;//图片路径

        public CastielAsyncImageLoaderTask(ImageView imageView) {
            if (imageView != null) {
                this.imageView = imageView;
            }
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            this.key = params[0];// 图片的路径
            Bitmap bitmap = castielDownload(key, imageView);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {// 说明已经下载下来了
                addFirstCache(key, result);
                if (imageView != null) {
                    imageView.setBackground(null);
                    imageView.setImageBitmap(result);// 加载网络中的图片
                }
            }
        }
    }

    /**
     * 根据图片路径执行图片下载
     *
     * @param key
     * @return
     */
    public Bitmap castielDownload(String key, ImageView imageView) {
        InputStream is = null;
        try {
            Bitmap bitmap = null;
            is = castielDownLoad(key);
            if (null == imageView) {
                bitmap = BitmapFactory.decodeStream(is);
            } else {
                int reqWidth = imageView.getWidth();
                int reqHeight = imageView.getHeight();
                bitmap = BitmapUtils.get(reqHeight, reqWidth, key);
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
    下载图片
     */
    public static InputStream castielDownLoad(String key) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(key).openConnection();
        InputStream in = conn.getInputStream();
        //conn.getInputStream().close();
        return in;
    }


    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


}
