package com.wuwind.viewapplication.module.download;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.wuwind.corelibrary.network.download.DLInfo;
import com.wuwind.corelibrary.network.download.DLManager;
import com.wuwind.corelibrary.network.download.interfaces.SimpleDListener;
import com.wuwind.viewapplication.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DownActivity extends Activity {
    private static final String[] URLS = {
            "https://raw.githubusercontent.com/wuwind/FApplication/master/app/pointCamera/apoint.apk",
            "",
            "",
            "",
            ""
    };

    private static final int[] RES_ID_BTN_START = {
            R.id.main_dl_start_btn1,
            R.id.main_dl_start_btn2,
            R.id.main_dl_start_btn3,
            R.id.main_dl_start_btn4,
            R.id.main_dl_start_btn5,
            R.id.main_dl_start_btn6};
    private static final int[] RES_ID_BTN_STOP = {
            R.id.main_dl_stop_btn1,
            R.id.main_dl_stop_btn2,
            R.id.main_dl_stop_btn3,
            R.id.main_dl_stop_btn4,
            R.id.main_dl_stop_btn5,
            R.id.main_dl_stop_btn6};
    private static final int[] RES_ID_PB = {
            R.id.main_dl_pb1,
            R.id.main_dl_pb2,
            R.id.main_dl_pb3,
            R.id.main_dl_pb4,
            R.id.main_dl_pb5,
            R.id.main_dl_pb6};
    private static final int[] RES_ID_NOTIFY = {
            R.id.main_notify_btn1,
            R.id.main_notify_btn2,
            R.id.main_notify_btn3,
            R.id.main_notify_btn4,
            R.id.main_notify_btn5,
            R.id.main_notify_btn6};

    private String saveDir;

    private ProgressBar[] pbDLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        DLManager.getInstance(DownActivity.this).setMaxTask(2);

        Button[] btnStarts = new Button[RES_ID_BTN_START.length];
        for (int i = 0; i < btnStarts.length; i++) {
            btnStarts[i] = (Button) findViewById(RES_ID_BTN_START[i]);
            final int finalI = i;
            btnStarts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DLManager.getInstance(DownActivity.this).dlStart(URLS[finalI], saveDir,
                            new SimpleDListener() {
                                @Override
                                public void onStart(String fileName, String realUrl, int fileLength) {
                                    pbDLs[finalI].setMax(fileLength);
                                }

                                @Override
                                public void onProgress(int progress) {
                                    pbDLs[finalI].setProgress(progress);
                                    EventBus.getDefault().post(new Message(progress));
                                }

                                @Override
                                public void onError(int status, String error) {
                                    super.onError(status, error);
                                    Log.e("DownActivity", error);
                                }
                            }, true);
                }
            });
        }

        btnStops = new Button[RES_ID_BTN_STOP.length];
        for (int i = 0; i < btnStops.length; i++) {
            btnStops[i] = (Button) findViewById(RES_ID_BTN_STOP[i]);
            final int finalI = i;
            btnStops[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DLManager.getInstance(DownActivity.this).dlStop(URLS[finalI]);
                }
            });
        }

        pbDLs = new ProgressBar[RES_ID_PB.length];
        for (int i = 0; i < pbDLs.length; i++) {
            pbDLs[i] = (ProgressBar) findViewById(RES_ID_PB[i]);
            pbDLs[i].setMax(100);
        }

        DLInfo dlInfo = DLManager.getInstance(this).getDLInfo(URLS[0]);
        if (null != dlInfo) {
            int totalBytes = dlInfo.totalBytes;
            int currentBytes = dlInfo.currentBytes;

            pbDLs[0].setMax(totalBytes);
            pbDLs[0].setProgress(currentBytes);

        }

        Button[] btnNotifys = new Button[RES_ID_NOTIFY.length];
        for (int i = 0; i < btnNotifys.length; i++) {
            btnNotifys[i] = (Button) findViewById(RES_ID_NOTIFY[i]);
            final int finalI = i;
            btnNotifys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationUtil.notificationForDLAPK(DownActivity.this, URLS[finalI]);
                }
            });
        }

        saveDir = Environment.getExternalStorageDirectory() + "/AigeStudio/";
    }

        Button[] btnStops;
    @Override
    protected void onDestroy() {
        for (String url : URLS) {
            DLManager.getInstance(this).dlStop(url);
        }
        super.onDestroy();
//        long result = 0;
//        for(int i=1; i<=50; i++) {
//            long m = 1;
//            for(int j = 2; j<=i; j++) {
//                m *= j;
//            }
//            if(i % 2 != 0) {
//                result += m;
//            } else {
//                result -= m;
//            }
//        }
//        printf("%ld", result);
//        Log.e("mian", result+"");

//        for (int i=1; i<101; i++) {
//            for(int j=2; j<=i; j++) {
//                if(i == j)
//                    printf("%d", i);
//                if(i % j == 0)
//                    break;
//            }
//        }
//
//        int n, m;
//        for (int i = 2; i < 101; i++) {
//            m = 1;
//            n = i / 2;
//            for (int j = 2; j <= n; j++) {
//                if (i % j == 0) {
//                    m = 0;
//                    break;
//                }
//            }
//            if (m == 1)
//                Log.e("mian", i+"");
//        }

//        void main()
//        {
//            int num;
//            printf("input\t:");
//            scanf("%d",&num);
//            if(num < 0 )
//                num =
//            printf("%d\t%d\n",num,abs(num));
//        }

        new ThreadLocal<Integer>(){
            @Override
            protected Integer initialValue() {
                return super.initialValue();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(Message o) {
        btnStops[0].setText(o.progress+"");
    }

    class Message {
        public int progress;

        public Message(int progress) {
            this.progress = progress;
        }
    }

}
