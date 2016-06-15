package com.wuwind.corelibrary.network.download;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wuwind.corelibrary.network.download.interfaces.IDListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wuwind.corelibrary.network.download.DLCons.DEBUG;
import static com.wuwind.corelibrary.network.download.DLError.ERROR_INVALID_URL;
import static com.wuwind.corelibrary.network.download.DLError.ERROR_NOT_NETWORK;
import static com.wuwind.corelibrary.network.download.DLError.ERROR_REPEAT_URL;

/**
 * 下载管理器
 * Download manager
 * 执行具体的下载操作
 * <p/>
 * 开始一个下载任务只需调用{@link #dlStart}方法即可
 * 停止某个下载任务需要调用{@link #dlStop}方法 停止下载任务仅仅会将对应下载任务移除下载队列而不删除相应数据 下次启动相同任务时会自动根据上一次停止时保存的数据重新开始下载
 * 取消某个下载任务需要调用{@link #dlCancel}方法 取消下载任务会删除掉相应的本地数据库数据但文件不会被删除
 * 相同url的下载任务视为相同任务
 * Use {@link #dlStart} for a new download task.
 * Use {@link #dlStop} to stop a download task base on url.
 * Use {@link #dlCancel} to cancel a download task base on url.
 * By the way, the difference between {@link #dlStop} and {@link #dlCancel} is whether the data in database would be deleted or not,
 * for example, the state of download like local file and data in database will be save when you use {@link #dlStop} stop a download task,
 * if you use {@link #dlCancel} cancel a download task, anything related to download task would be deleted.
 * 日志开关及日志记录，开关方法为setDebugEnable，日志TAG为DLManager；方便调试;
 * 修改{@link #setDebugEnable(boolean)}方法
 * 新增多个不同的{@link #dlStart}方法便于回调
 * 新增{@link #setMaxTask(int)}方法限制多个下载任务的并发数
 * 新增{@link #getDLInfo(String)}方法获取瞬时下载信息
 * 新增{@link #getDLDBManager()}方法获取数据库管理对象
 */
public final class DLManager {
    private static final String TAG = DLManager.class.getSimpleName();

    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final int POOL_SIZE = CORES + 1;
    private static final int POOL_SIZE_MAX = CORES * 2 + 1;

    private static final BlockingQueue<Runnable> POOL_QUEUE_TASK = new LinkedBlockingQueue<>(56);
    private static final BlockingQueue<Runnable> POOL_QUEUE_THREAD = new LinkedBlockingQueue<>(256);

    private static final ThreadFactory TASK_FACTORY = new ThreadFactory() {

        private final AtomicInteger COUNT = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "DLTask #" + COUNT.getAndIncrement());
        }
    };

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger COUNT = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "DLThread #" + COUNT.getAndIncrement());
        }
    };

    private static final ExecutorService POOL_TASK = new ThreadPoolExecutor(POOL_SIZE,
            POOL_SIZE_MAX, 3, TimeUnit.SECONDS, POOL_QUEUE_TASK, TASK_FACTORY);
    private static final ExecutorService POOL_Thread = new ThreadPoolExecutor(POOL_SIZE * 5,
            POOL_SIZE_MAX * 5, 1, TimeUnit.SECONDS, POOL_QUEUE_THREAD, THREAD_FACTORY);

    private static final ConcurrentHashMap<String, DLInfo> TASK_DLING = new ConcurrentHashMap<>();
    private static final List<DLInfo> TASK_PREPARE =
            Collections.synchronizedList(new ArrayList<DLInfo>());
    private static final ConcurrentHashMap<String, DLInfo> TASK_STOPPED = new ConcurrentHashMap<>();

    private static DLManager sManager;

    private Context context;

    private int maxTask = 10;

    private DLManager(Context context) {
        this.context = context;
    }

    public static DLManager getInstance(Context context) {
        if (null == sManager) {
            sManager = new DLManager(context);
        }
        return sManager;
    }

    /**
     * 设置并发下载任务最大值
     * The max task of DLManager.
     *
     * @param maxTask ...
     */
    public DLManager setMaxTask(int maxTask) {
        this.maxTask = maxTask;
        return sManager;
    }

    /**
     * 设置是否开启Debug模式 默认不开启
     * Is debug mode, default is false.
     *
     * @param isDebug ...
     * @return ...
     */
    public DLManager setDebugEnable(boolean isDebug) {
        DLCons.DEBUG = isDebug;
        return sManager;
    }

    /**
     * @see #dlStart(String, String, String, List, IDListener, boolean)
     */
    public void dlStart(String url) {
        dlStart(url, "", "", null, null, false);
    }

    public void dlStart(String url, boolean loadAnyway) {
        dlStart(url, "", "", null, null, loadAnyway);
    }

    public void dlStart(String url, String dir) {
        dlStart(url, dir, "", null, null, false);
    }

    public void dlStart(String url, String dir, boolean loadAnyway) {
        dlStart(url, dir, "", null, null, loadAnyway);
    }

    public void dlStart(String url, IDListener listener) {
        dlStart(url, "", "", null, listener, false);
    }

    public void dlStart(String url, IDListener listener, boolean loadAnyway) {
        dlStart(url, "", "", null, listener, loadAnyway);
    }

    public void dlStart(String url, String dir, IDListener listener) {
        dlStart(url, dir, "", null, listener, false);
    }

    public void dlStart(String url, String dir, IDListener listener, boolean loadAnyway) {
        dlStart(url, dir, "", null, listener, loadAnyway);
    }

    public void dlStart(String url, String dir, String name, IDListener listener) {
        dlStart(url, dir, name, null, listener, false);
    }

    public void dlStart(String url, String dir, String name, List<DLHeader> headers, IDListener listener) {
        dlStart(url, dir, name, null, listener, false);
    }

    /**
     * 开始一个下载任务
     * Start a download task.
     *
     * @param url      文件下载地址
     *                 Download url.
     * @param dir      文件下载后保存的目录地址，该值为空时会默认使用应用的文件缓存目录作为保存目录地址
     *                 The directory of download file. This parameter can be null, in this case we
     *                 will use cache dir of app for download path.
     * @param name     文件名，文件名需要包括文件扩展名，类似“AigeStudio.apk”的格式。该值可为空，为空时将由程
     *                 序决定文件名。
     *                 Name of download file, include extension like "AigeStudio.apk". This
     *                 parameter can be null, in this case the file name will be decided by program.
     * @param headers  请求头参数
     *                 Request header of http.
     * @param listener 下载监听器
     *                 Listener of download task.
     */
    public void dlStart(String url, String dir, String name, List<DLHeader> headers, IDListener listener, boolean loadAnyway) {
        if (DEBUG) Log.d(TAG, "dlStart");
        boolean hasListener = listener != null;
        if (TextUtils.isEmpty(url)) {
            if (hasListener) listener.onError(ERROR_INVALID_URL, "Url can not be null.");
            return;
        }
        if (!DLUtil.isNetworkAvailable(context)) {
            if (hasListener) listener.onError(ERROR_NOT_NETWORK, "Network is not available.");
            return;
        }
        if (TASK_DLING.containsKey(url)) {
            if (null != listener) listener.onError(ERROR_REPEAT_URL, url + " is downloading.");
        } else {
            DLInfo info;
            if (TASK_STOPPED.containsKey(url)) {
                if (DEBUG) Log.d(TAG, "Resume task from memory.");
                info = TASK_STOPPED.remove(url);
            } else {
                if (DEBUG) Log.d(TAG, "Resume task from database.");
                info = DLDBManager.getInstance(context).queryTaskInfo(url);
                if (null != info) {
                    if (DEBUG) Log.d(TAG, "baseUrl:" + info.baseUrl);
                    info.threads.clear();
                    info.threads.addAll(DLDBManager.getInstance(context).queryAllThreadInfo(url));
                }
            }
            if (null == info) {
                if (DEBUG) Log.d(TAG, "New task will be start.");
                info = new DLInfo();
                info.baseUrl = url;
                info.realUrl = url;
                if (TextUtils.isEmpty(dir)) dir = context.getCacheDir().getAbsolutePath();
                info.dirPath = dir;
                info.fileName = name;
            } else {
                info.isResume = true;
                for (DLThreadInfo threadInfo : info.threads) {
                    threadInfo.isStop = false;
                }
            }

            info.redirect = 0;
            info.loadAnyway = loadAnyway;
            info.requestHeaders = DLUtil.initRequestHeaders(headers, info);
            info.listener = listener;
            info.hasListener = hasListener;
            if (TASK_DLING.size() >= maxTask) {
                if (DEBUG) Log.w(TAG, "Downloading urls is out of range.");
                TASK_PREPARE.add(info);
            } else {
                if (DEBUG) Log.d(TAG, "Prepare download from " + info.baseUrl);
                if (hasListener) listener.onPrepare();
                TASK_DLING.put(url, info);
                POOL_TASK.execute(new DLTask(context, info));
            }
        }
    }

    /**
     * 根据Url暂停一个下载任务
     * Stop a download task according to url.
     *
     * @param url 文件下载地址
     *            Download url.
     */
    public void dlStop(String url) {
        if (DEBUG) Log.d(TAG, "dlStop");
        if (TASK_DLING.containsKey(url)) {
            DLInfo info = TASK_DLING.get(url);
            info.isStop = true;
            TASK_DLING.remove(url);
            if (!info.threads.isEmpty()) {
                for (DLThreadInfo threadInfo : info.threads) {
                    threadInfo.isStop = true;
                }
            }
        }
    }

    /**
     * 根据Url取消一个下载任务
     * Cancel a download task according to url.
     *
     * @param url 文件下载地址
     *            Download url.
     */
    public void dlCancel(String url) {
        dlStop(url);
        DLInfo info;
        if (TASK_DLING.containsKey(url)) {
            info = TASK_DLING.get(url);
        } else {
            info = DLDBManager.getInstance(context).queryTaskInfo(url);
        }
        if (null != info) {
            File file = new File(info.dirPath, info.fileName);
            if (file.exists()) file.delete();
        }
        DLDBManager.getInstance(context).deleteTaskInfo(url);
        DLDBManager.getInstance(context).deleteAllThreadInfo(url);
    }

    public DLInfo getDLInfo(String url) {
        return DLDBManager.getInstance(context).queryTaskInfo(url);
    }

    @Deprecated
    public DLDBManager getDLDBManager() {
        return DLDBManager.getInstance(context);
    }

    synchronized DLManager removeDLTask(String url) {
        TASK_DLING.remove(url);
        return sManager;
    }

    synchronized DLManager addDLTask() {
        if (!TASK_PREPARE.isEmpty()) {
            POOL_TASK.execute(new DLTask(context, TASK_PREPARE.remove(0)));
        }
        return sManager;
    }

    synchronized DLManager addStopTask(DLInfo info) {
        TASK_STOPPED.put(info.baseUrl, info);
        return sManager;
    }

    synchronized DLManager addDLThread(DLThread thread) {
        POOL_Thread.execute(thread);
        return sManager;
    }

}