package com.bansalankit.sandesh;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class is utility for logging messages and showing toast notifications.
 * <br><br>Created by <b>Ankit Bansal</b> on <i>25 Feb 2016</i>.
 */
public final class Logger {
    private static final String TAG = Logger.class.getSimpleName();
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String LOG_FILE_NAME = "Logs.txt";
    private static final int LOG_FILE_SIZE = 1024 * 1024;
    private static boolean sIsDebuggable;
    private static Context sAppContext;
    private static String sLogFile;

    private Logger() {/*Prevent object creation*/}

    /**
     * Initialize the logger to use application context (via supplied context) for various operations.
     *
     * @param appDebuggable true if application is in DEBUG mode and logs containing sensitive information
     *                      (printed by {@code info()} method) can be shown, false otherwise.
     * @param enableFileLog true if logs must be saved on device using file with size cap of 1MB.
     * @throws IllegalStateException If supplied context is {@code null}.
     */
    public static void init(Context context, boolean appDebuggable, boolean enableFileLog) {
        if (context == null) throw new IllegalStateException("Context cannot be null");
        sAppContext = context.getApplicationContext();
        if (enableFileLog) initLogFile();
        sIsDebuggable = appDebuggable;
    }

    /**
     * Create log file on device's secondary internal storage.
     */
    private static void initLogFile() {
        File root = sAppContext.getExternalFilesDir(null);
        if (root != null && (root.mkdirs() || root.isDirectory()))
            sLogFile = new File(root, LOG_FILE_NAME).getPath();
    }

    /**
     * @return true if logger is initialized and application's context is present to show logs/toasts.
     */
    private static boolean isInitialized() {
        if (sAppContext != null) return true;
        throw new IllegalStateException("Logger not initialized");
    }

    /**
     * @param tag     TAG under which DEBUG message will be shown.
     * @param message Message to be shown under DEBUG tag.
     */
    public static void debug(String tag, String message) {
        if (TextUtils.isEmpty(tag)) tag = TAG;
        if (message == null) message = "";
        Log.d(tag, message);
        writeInFile(message);
    }

    /**
     * NOTE: INFO Logs will only be shown if application is debuggable, as set in {@code init()}.
     *
     * @param tag     TAG under which INFO message will be shown.
     * @param message Message to be shown under INFO tag.
     */
    public static void info(String tag, String message) {
        if (TextUtils.isEmpty(tag)) tag = TAG;
        if (message == null) message = "";
        if (sIsDebuggable) Log.i(tag, message);
        writeInFile(message);
    }

    /**
     * @param tag     TAG under which WARN message will be shown.
     * @param message Message to be shown under WARN tag.
     */
    public static void warn(String tag, String message) {
        if (TextUtils.isEmpty(tag)) tag = TAG;
        if (message == null) message = "";
        Log.w(tag, message);
        writeInFile(message);
    }

    /**
     * @param tag     TAG under which WARN message will be shown.
     * @param message Message to be shown under WARN tag.
     * @param error   Error occurred for which log is shown.
     */
    public static void warn(String tag, String message, Throwable error) {
        if (TextUtils.isEmpty(tag)) tag = TAG;
        if (message == null) message = "";
        Log.w(tag, message, error);
        writeInFile(message);
    }

    /**
     * Write the supplied message in application's log file, in separate worker thread, only if
     * file logging is enabled.
     */
    private static void writeInFile(final String message) {
        // Start a synchronized thread to access log file if needed
        if (!TextUtils.isEmpty(sLogFile)) synchronized (LOG_FILE_NAME) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Append if log file have valid size
                        final File logFile = new File(sLogFile);
                        boolean append = (logFile.length() < LOG_FILE_SIZE);

                        // Append/Write the log message in file
                        FileWriter writer = new FileWriter(logFile, append);
                        writer.write(getTimeStamp() + "\t" + message + "\r\n");
                        writer.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * @return Human readable form "{@value #TIMESTAMP_FORMAT}" of current timestamp.
     */
    private static String getTimeStamp() {
        return new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.getDefault()).format(new Date());
    }

    /**
     * Display a toast with supplied message resource for {@code LONG} period of time.
     *
     * @throws IllegalStateException If {@code Logger} not yet initialized.
     */
    public static void toastLong(@StringRes int msgRes) {
        if (isInitialized()) toastLong(sAppContext.getString(msgRes));
    }

    /**
     * Display a toast with supplied message for {@code LONG} period of time.
     *
     * @param msg The message to be shown. Can be formatted text.
     * @throws IllegalStateException If {@code Logger} not yet initialized.
     */
    public static void toastLong(CharSequence msg) {
        if (isInitialized()) Toast.makeText(sAppContext, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Display a toast with supplied message resource for {@code SHORT} period of time.
     *
     * @throws IllegalStateException If {@code Logger} not yet initialized.
     */
    public static void toastShort(@StringRes int msgRes) {
        if (isInitialized()) toastShort(sAppContext.getString(msgRes));
    }

    /**
     * Display a toast with supplied message for {@code SHORT} period of time.
     *
     * @param msg The message to be shown. Can be formatted text.
     * @throws IllegalStateException If {@code Logger} not yet initialized.
     */
    public static void toastShort(CharSequence msg) {
        if (isInitialized()) Toast.makeText(sAppContext, msg, Toast.LENGTH_SHORT).show();
    }
}