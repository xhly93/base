package sharing.school.ctq.util.log;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import sharing.school.ctq.model.data.Contants;
import sharing.school.ctq.util.CalendarUtil;

public class LogUtil {

    private static final String TAG = LogUtil.class.getName();

    /**
     * Define the log priority.
     */
    private static LogType logType;

    private static HashMap<Integer, String> level = new HashMap<>();

    static {
        level.put(2, "verbose");
        level.put(3, "debug");
        level.put(4, "info");
        level.put(5, "warn");
        level.put(6, "error");
        level.put(7, "asset");
        setLogType(LogType.verbose);
      /*  if (Url.BASE_URL.startsWith("https://t")) {
            setLogType(LogType.asset);
        } else {
            setLogType(LogType.verbose);
        }*/
    }

    private LogUtil() {
    }

    /**
     * Get the log level.
     *
     * @return log level
     */
    public static LogType getLogType() {
        return logType;
    }

    /**
     * Set the log level for the application.
     *
     * @param logType the value to be set.
     */
    public static void setLogType(LogType logType) {
        LogUtil.logType = logType;
        i(TAG, "logType: " + logType);
    }

    /**
     * Send a  log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int v(String tag, String msg) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.verbose.value(), tag, msg, stackTraceElement);
    }

    /**
     * Send a  log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int v(String tag, String msg, Throwable tr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.verbose.value(), tag, msg + '\n' + getStackTraceString(tr), stackTraceElement);
    }


    /**
     * Send a log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int d(String tag, String msg) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.debug.value(), tag, msg, stackTraceElement);
    }

    /**
     * Send a  log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int d(String tag, String msg, Throwable tr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.debug.value(), tag, msg + '\n' + getStackTraceString(tr), stackTraceElement);
    }

    /**
     * Send an log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int i(String tag, String msg) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.info.value(), tag, msg, stackTraceElement);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int i(String tag, String msg, Throwable tr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.info.value(), tag, msg + '\n' + getStackTraceString(tr), stackTraceElement);
    }

    /**
     * Send a log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int w(String tag, String msg) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.warn.value(), tag, msg, stackTraceElement);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int w(String tag, String msg, Throwable tr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.warn.value(), tag, msg + '\n' + getStackTraceString(tr), stackTraceElement);
    }


    /*
     * Send a {@link #WARN} log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     * identifies the class or activity where the log call occurs.
     *
     * @param tr An exception to log
     */
    public static int w(String tag, Throwable tr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.warn.value(), tag, getStackTraceString(tr), stackTraceElement);
    }

    /**
     * Send an log message.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static int e(String tag, String msg) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.error.value(), tag, msg, stackTraceElement);
    }

    /**
     * Send a log message and log the exception.
     *
     * @param tag Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public static int e(String tag, String msg, Throwable tr) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        return ElectronicBrandPrintln(LogType.error.value(), tag, msg + "\n" + getStackTraceString(tr), stackTraceElement);
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     *
     * @param tr An exception to log
     */
    private static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    private static int ElectronicBrandPrintln(int priority, String tag, String msg, StackTraceElement stackTraceElement) {
        if (priority >= logType.value()) {
            if (/*!Contants.FOR_PUBLISH &&*/ priority >= 4) {
                LogThreadPool.addTask(new LogSaveUtils(getLogInfo(priority, tag, msg, stackTraceElement)));
            }
            return android.util.Log.println(priority, tag, msg);
        } else {
            return -1;
        }
    }

    /**
     * May be verbose, debug, info, warn, error, asset.
     * 只保存info及以上级别的日志
     *
     * @author coleman
     */
    public static enum LogType {
        verbose(2), debug(3), info(4), warn(5), error(6), asset(7);

        private final int type;

        private LogType(int type) {
            this.type = type;
        }

        public int value() {
            return type;
        }

        public static LogType instanse(int i) {
            LogType type = verbose;
            switch (i) {
                case 2:
                    type = verbose;
                    break;
                case 3:
                    type = debug;
                    break;
                case 4:
                    type = info;
                    break;
                case 5:
                    type = warn;
                    break;
                case 6:
                    type = error;
                    break;
                case 7:
                    type = asset;
                    break;
                default:
                    type = null;
                    break;
            }
            return type;
        }
    }

    /**
     * 保存日志所包含的信息
     *
     * @param tag
     * @param priority
     * @return
     */
    private static String getLogInfo(int priority, String tag, String msg, StackTraceElement stackTraceElement) {
        StringBuilder logInfoStringBuilder = new StringBuilder();
        // 获取线程名
        String threadName = Thread.currentThread().getName();
        // 获取类名.即包名+类名
        String className = stackTraceElement.getClassName();
      /*  // 获取方法名称
        String methodName = stackTraceElement.getMethodName();
        // 获取生日输出行数
        int lineNumber = stackTraceElement.getLineNumber();*/
        logInfoStringBuilder.append(CalendarUtil.getFormatedTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        logInfoStringBuilder.append("  [");
        logInfoStringBuilder.append(threadName).append("]   ");
        logInfoStringBuilder.append(className).append("   ");
        logInfoStringBuilder.append(msg);
        return logInfoStringBuilder.toString();
    }

}
