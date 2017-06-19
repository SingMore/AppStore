package com.example.appstore.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * Created by SingMore on 2017/2/13.
 */
public class AppUtils {

    /**
     * 判断包是否安装
     */
    public static boolean isInstalled(Context context, String packageName) {
//        PackageManager manager = PackageManagerFactory.getPackageManager(context);
//        try {
//            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
//            return true;
//        } catch (NameNotFoundException e) {
//            return false;
//        }


        if (TextUtils.isEmpty(packageName)) {
            return false;
        }

        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 安装应用程序
     */
    public static void installApp(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开应用程序
     */
    public static void openApp(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if(intent != null){
            context.startActivity(intent);
        }else{
            Toast.makeText(UIUtils.getContext(), "此应用不能打开", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 卸载应用程序
     */

    public static void uninstallApp(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }
}
