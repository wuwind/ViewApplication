package com.wuwind.viewapplication.base;

import com.wuwind.corelibrary.base.CoreApplication;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Wuhf on 2016/6/8.
 * Description ：
 */
public class BaseApplication extends CoreApplication {

    @Override
    protected void init() {
        EventBus.builder().installDefaultEventBus();
    }
}
