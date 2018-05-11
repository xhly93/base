package com.ssj.user.Mode.network;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.ssj.user.Mode.interfac.CommenCallBack;
import com.ssj.user.Mode.interfac.DownPhotoCallBack;
import com.ssj.user.Utils.Contants;

import java.io.InputStream;

/**
 * Created by 王矩龙 on 2018/3/9.
 */

public class OSSNetWork {
    private static final String TAG = "OSSNetWork";
    private Context mContext;
    private OSS oss;
    public static final String bucket = "ssj-problem-test";

    public OSSNetWork(Context context,String accessKeyId,String secretKeyId,String securityToken) {
        this.mContext = context;
        setNetWork(accessKeyId,secretKeyId,securityToken);
    }

    private String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";

    private void setNetWork(String accessKeyId,String secretKeyId,String securityToken) {
        // 在移动端建议使用STS的方式初始化OSSClient，更多信息参考：[访问控制]
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, secretKeyId, securityToken);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(mContext, endpoint, credentialProvider, conf);
    }


    public void upload(String objectKey, String path, CommenCallBack callBack) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, objectKey, path);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d(TAG, "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d(TAG, "UploadSuccess");
                callBack.onResult(Contants.CommenValues.SUCCESS,null);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    Log.e(TAG, "clientExcepion = "+clientExcepion.getMessage().toString());
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e(TAG, "upload errorCode = "+serviceException.getErrorCode());
                    Log.e(TAG, "upload requestId = "+serviceException.getRequestId());
                    Log.e(TAG, "upload hostId = "+serviceException.getHostId());
                    Log.e(TAG, "upload rawMsg = "+serviceException.getRawMessage());
                }
                callBack.onResult(Contants.CommenValues.ERROR,null);
            }
        });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
    }

    public void download(String key, DownPhotoCallBack callBack) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(bucket, key);

        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                Log.d(TAG, "Content-Length = " + result.getContentLength());
                InputStream inputStream = result.getObjectContent();
                callBack.onCallBack(inputStream);

            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e(TAG, "errorCode = "+serviceException.getErrorCode());
                    Log.e(TAG, "requestId = "+serviceException.getRequestId());
                    Log.e(TAG, "hostId = "+serviceException.getHostId());
                    Log.e(TAG, "rawMsg = "+serviceException.getRawMessage());
                }
            }
        });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 如果需要等待任务完成
    }

}
