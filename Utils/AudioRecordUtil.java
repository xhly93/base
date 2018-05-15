package com.ssj.user.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 录音 播放
 * Created by 17258 on 2018/4/4.
 */

public class AudioRecordUtil {

    private static final String TAG = "AudioRecordUtil";
    public MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    public File mAudioFile;
    private volatile boolean isPlaying;
    private Handler mHandler = new Handler();
    private AnimationDrawable mAnimationDrawable;
    //录音文件保存位置
    public static String mFilePath = Environment.getExternalStorageDirectory() + "/sishujia/msg/voice/";

    private long mStartTime, mEndTime, mDuration;//录音开始结束时间
    private boolean mRecord;//是否再录音

    public boolean ismRecord() {
        return mRecord;
    }

    public void setmRecord(boolean mRecord) {
        this.mRecord = mRecord;
    }

    public long getmDuration() {
        return mDuration;
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }


    static {
        File file = new File(mFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 录音操作
     */
    public boolean startRecord(String path) {
        Log.i(TAG, "startRecord");
        boolean result = false;

        try {
            //创建录音文件,.m4a为MPEG-4音频标准的文件的扩展名
            File mDirFile = new File(mFilePath);
            if (!mDirFile.exists()) {
                mDirFile.mkdirs();
            }
            mAudioFile = new File(path);
            if (!mAudioFile.exists()){
                mAudioFile.createNewFile();
            }
            mMediaRecorder = new MediaRecorder();
            //配置mMediaRecorder相应参数
            //从麦克风采集声音数据
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置保存文件格式为MP4
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            //设置采样频率,44100是所有安卓设备都支持的频率,频率越高，音质越好，当然文件越大
            mMediaRecorder.setAudioSamplingRate(44100);
            //设置声音数据编码格式,音频通用格式是AAC
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //设置编码频率
            mMediaRecorder.setAudioEncodingBitRate(96000);
            //设置录音保存的文件
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            mMediaRecorder.setMaxDuration(60 * 1000);
            //开始录音
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mRecord = true;
//            updateMicStatus();
            //记录开始录音时间
            mStartTime = System.currentTimeMillis();
            result = true;
        } catch (Exception e) {
            Log.i(TAG, "Exception e = " + e.getMessage().toString());
            mRecord = false;
            result = false;
            recordFail();
        }
        return result;
    }

    /**
     * 更新话筒状态
     */
    private static final int BASE = 1;
    private static final int SPACE = 100;// 间隔取样时间

    private void updateMicStatus() {
        Log.i(TAG, "updateMicStatus");
        if (mMediaRecorder != null) {
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            Log.i(TAG, "分贝值：" + db);
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        } else {
            Log.i(TAG, "mMediaRecorder = null");
        }
    }

    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    /**
     * 录音失败
     */
    private void recordFail() {
        mAudioFile = null;
    }

    /**
     * 停止录音
     */
    public void stopRecord(boolean saveRecord) {
        Log.i(TAG, "mMediaRecorder.stopRecord");
        mHandler.removeCallbacksAndMessages(null);

        mRecord = false;
        if (!saveRecord) {
            if (FileUtil.exist(mAudioFile)) {
                mAudioFile.delete();
            }
        }

        //记录停止时间
        mEndTime = System.currentTimeMillis();
        //录音时间处理，比如只有大于2秒的录音才算成功
        mDuration = (int) ((mEndTime - mStartTime) / 1000);
        if (mDuration < 1) {
//            if (FileUtil.exist(mAudioFile3)) {
//                mAudioFile3.delete();
//            } else if (FileUtil.exist(mAudioFile2)) {
//                mAudioFile2.delete();
//            } else if (FileUtil.exist(mAudioFile1)) {
//                mAudioFile1.delete();
//            }
        } else {
//            mAudioFile = null;
//            mHandler.sendEmptyMessage(Constant.RECORD_TOO_SHORT);
        }
        //录音完成释放资源
        releaseRecorder();
    }

    /**
     * 播放录音
     */
    public void startPlay(File mFile) {
        try {
            //初始化播放器
            mMediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mMediaPlayer.setDataSource(mFile.getAbsolutePath());
            mAnimationDrawable.start();
            //设置播放监听事件
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mMediaPlayer.setVolume(1, 1);
            //是否循环播放
            mMediaPlayer.setLooping(false);
            //准备及播放
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }


    /**
     * 播放完毕处理
     *
     * @param isEnd
     */
    public void playEndOrFail(boolean isEnd) {
        if (null != mAnimationDrawable) {
            if (mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
            }
        }
        Log.i(TAG, "mMediaRecorder.isEnd = " + isEnd);
        isPlaying = false;
        if (isEnd) {
//            mHandler.sendEmptyMessage(Constant.PLAY_COMPLETION);
        } else {
//            mHandler.sendEmptyMessage(Constant.PLAY_ERROR);
        }
        if (null != mMediaPlayer) {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 释放
     */
    private void releaseRecorder() {
        if (null != mMediaRecorder) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }


    /*******6.0以上版本手机权限处理***************************/
    /**
     */
    public boolean permissionForM(Activity context) {
        Log.i(TAG, "permissionForM");
        boolean hasPermmision = false;
        boolean writePermmision = ContextCompat
                .checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean readPermmision = ContextCompat
                .checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean audioPermmision = ContextCompat
                .checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        if (!writePermmision || !audioPermmision || !readPermmision) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    Contants.ActivityCode.REQUEST_AUDIO);
        } else {
            hasPermmision = true;
        }
        return hasPermmision;
    }



}
