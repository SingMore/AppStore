package com.example.appstore.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by SingMore on 2017/6/15.
 */

public class AppManageInfo {
    public String name; //应用名称
    public String packageName; //应用包名

    //以下为应用管理模块新增字段
    private Drawable icon2;
    private boolean isSystem;
    private boolean isSDcard;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon2() {
        return icon2;
    }

    public void setIcon2(Drawable icon2) {
        this.icon2 = icon2;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public boolean isSDcard() {
        return isSDcard;
    }

    public void setSDcard(boolean SDcard) {
        isSDcard = SDcard;
    }
}
