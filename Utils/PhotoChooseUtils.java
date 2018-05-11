package com.ssj.user.Utils;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.ssj.user.R;
import com.ssj.user.Student.Activity.BaseActivity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by 17258 on 2018/3/27.
 */

public class PhotoChooseUtils {


    private static final String TAG = "PhotoChooseUtils";

    /**
     * 选择相册图片
     * @param activityContext
     * @param requestCode
     */
    public static void selectPhoto(BaseActivity activityContext,int requestCode){
        Intent intentPhoto = new Intent(Intent.ACTION_PICK, null);//返回被选中项的URI
        intentPhoto.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//得到所有图片的URI
//                Log.d("MediaStore.Images.Media.EXTERNAL_CONTENT_URI  ------------>   "
//                        + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//   content://media/external/images/media
        activityContext.startActivityForResult(intentPhoto, requestCode);
    }

    /**
     * 选择相机拍照
     * @param activityContext
     * @param requestCode
     */
    public static void selectCamera(BaseActivity activityContext,int requestCode){
        try {
            boolean cameraPermission = ContextCompat.checkSelfPermission(activityContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission){
                startCamera(activityContext,requestCode);
            }else {
                ActivityCompat.requestPermissions(activityContext
                        , new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            }
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            Toast.makeText(activityContext, R.string.carmera_start_error, Toast.LENGTH_LONG).show();
        }
    }

    public static void startCamera(BaseActivity activity,int requestCode) {
        Intent intentCarera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//开启相机应用程序获取并返回图片（capture：俘获）
         /*获取当前系统的android版本号*/
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Log.e("currentapiVersion", "currentapiVersion====>" + currentapiVersion);
        File file = new File(Environment.getExternalStorageDirectory(), "head.jpg");
        if (currentapiVersion < 24) {
            intentCarera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(intentCarera, requestCode);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intentCarera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intentCarera, requestCode);

        }
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public static void cropPhoto(Uri  uri,boolean camera,BaseActivity context,int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri imageUri = null;
        if (camera){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //通过FileProvider创建一个content类型的Uri
                try {
                    imageUri = getImageContentUri(new File(new URI(uri.toString())),context);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                //TODO:outputUri不需要ContentUri,否则失败
                //outputUri = FileProvider.getUriForFile(activity, "com.solux.furniture.fileprovider", new File(crop_image));
            } else {
                imageUri = uri;
            }
        }else {
            imageUri = uri;
        }

        File outputFile = new File(Environment.getExternalStorageDirectory(), "head.jpg");

        //找到指定URI对应的资源图片
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        //输入图片的Uri，指定以后，可以在这个uri获得图片
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        //进入系统裁剪图片的界面
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 转换 content:// uri
     *
     * @param imageFile
     * @return
     */
    private static Uri getImageContentUri(File imageFile,Context context) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
