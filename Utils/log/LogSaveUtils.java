package sharing.school.ctq.util.log;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import sharing.school.ctq.util.CalendarUtil;

public class LogSaveUtils implements Runnable {
    private String content;

    public LogSaveUtils(String content) {
        this.content = content;
    }

    /**
     * 文件最大个数
     */
    private static final int MAX_SIZE = 3;

    /**
     * 文件名后缀
     */
    private static String FILE_NAME = ".log";

    /**
     * 文件
     */
    private File file;

    /**
     * 输出流
     */
    private RandomAccessFile randomAccessFile;

    private static final String LOG_NAME_FORMAT = "yyyy-MM-dd";

    private SimpleDateFormat dataFormat = new SimpleDateFormat(LOG_NAME_FORMAT);// 日志名称格式

    @Override
    public void run() {
        saveToFile();
    }

    /**
     * 保存到本地文件
     */
    private synchronized void saveToFile() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            deleteFile();
            String path = Environment.getExternalStorageDirectory() + "/" + "ctq/log/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = path + CalendarUtil.getFormatedTime(System.currentTimeMillis(), LOG_NAME_FORMAT) + FILE_NAME;
            file = new File(fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    return;
                }
            }
            randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(content.getBytes("utf-8"));
            randomAccessFile.write("\r\n".getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != randomAccessFile) {
                    randomAccessFile.close();
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 文件超过最大个数则删除最旧的文件
     */
    private void deleteFile() {
        String path = Environment.getExternalStorageDirectory() + "/" + "ctq/log/";
        File root = new File(path);
        if (root.isDirectory()) {
            File[] listFiles = root.listFiles();
            if (listFiles != null && listFiles.length >= MAX_SIZE) {
                Arrays.sort(listFiles, new FileComparator());
                listFiles[0].delete();
            }
        }
    }

    class FileComparator implements Comparator<File> {
        public int compare(File file1, File file2) {
            String createInfo1 = getFileNameWithoutExtension(file1.getName());
            String createInfo2 = getFileNameWithoutExtension(file2.getName());

            try {
                Date create1 = dataFormat.parse(createInfo1);
                Date create2 = dataFormat.parse(createInfo2);
                if (create1.before(create2)) {
                    return -1;
                } else {
                    return 1;
                }
            } catch (ParseException e) {
                return 0;
            }
        }
    }

    /**
     * 去除文件的扩展类型（.log）
     *
     * @param fileName
     * @return
     */
    private String getFileNameWithoutExtension(String fileName) {
        try {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } catch (Exception e) {
            return "";
        }
    }
}
