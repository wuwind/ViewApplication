package com.wuwind.corelibrary.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.wuwind.corelibrary.R;

import butterknife.ButterKnife;
import rx.Subscription;


/**
 * Activity基类，写activity必须继承
 * Created by Wuhf on 2016/4/1.
 * Description ：
 */
public abstract class CoreActivity extends AppCompatActivity {

    private ExitReceiver exitReceiver;
    protected Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        beforeOnCreate();
        super.onCreate(savedInstanceState);
        afterOnCreate();
        int contentLayoutId = setContentLayoutId();
        if (contentLayoutId != 0)
            setContentView(contentLayoutId);
        else
            setContentView(setContentView());
        ButterKnife.bind(this);
        registerExitReceiver();
        initData();
        initView();
        initMonitor();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void registerExitReceiver() {
        exitReceiver = new ExitReceiver();
        IntentFilter filter = new IntentFilter();
        registerReceiver(exitReceiver, filter);
    }

    protected void beforeOnCreate() {
    }

    protected void afterOnCreate() {
    }

    protected abstract int setContentLayoutId();

    protected View setContentView() {
        return null;
    }

    protected abstract void initView();

    protected void initData() {
    }

    protected abstract void initMonitor();

    protected void unsubscribe() {
        if(null != subscription && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        if (null != exitReceiver) {
            unregisterReceiver(exitReceiver);
            exitReceiver = null;
        }
        unsubscribe();
        super.onDestroy();
    }

    class ExitReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}
