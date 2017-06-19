package com.example.appstore.factory;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by SingMore on 2017/3/17.
 */

public class PackageManagerFactory {
    static PackageManager packageManager;

    public static PackageManager getPackageManager(Context context) {
        if(packageManager == null) {
            synchronized (PackageManagerFactory.class) {
                if(packageManager == null) {
                    packageManager = context.getPackageManager();
                }
            }
        }
        return packageManager;
    }
}
