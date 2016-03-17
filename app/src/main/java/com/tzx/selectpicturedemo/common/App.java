package com.tzx.selectpicturedemo.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Administrator
 * Date: 2016/3/11.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this, FrescoConfigConstants.getImagePipelineConfig(this));
    }
}
