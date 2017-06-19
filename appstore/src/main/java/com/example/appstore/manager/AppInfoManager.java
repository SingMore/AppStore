package com.example.appstore.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.appstore.bean.AppManageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SingMore on 2017/6/3.
 */

public class AppInfoManager {
    public static List<AppManageInfo> getAppInfos(Context context){
        List<AppManageInfo> appInfos = new ArrayList<AppManageInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);

        for(PackageInfo info: installedPackages){
            AppManageInfo appInfo = new AppManageInfo();
            appInfo.setPackageName(info.packageName);

            String name = info.applicationInfo.loadLabel(pm).toString();
            appInfo.setName(name);

            Drawable icon = info.applicationInfo.loadIcon(pm);
            appInfo.setIcon2(icon);

            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
                appInfo.setSystem(true);
            }else{
                appInfo.setSystem(false);
            }

            if((info.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                appInfo.setSDcard(true);
            }else{
                appInfo.setSDcard(false);
            }

            appInfos.add(appInfo);
        }

        return appInfos;
    }
}
