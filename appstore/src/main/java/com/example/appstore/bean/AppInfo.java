package com.example.appstore.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SingMore on 2017/3/6.
 */

public class AppInfo extends BmobObject implements Serializable{
    public Float stars; //应用评分
    public String des; //应用描述
    public BmobFile icon; //应用图标
    public String name; //应用名称
    public String packageName; //应用包名
    public BmobFile screen0; //应用截图
    public BmobFile screen1; //
    public BmobFile screen2; //
    public BmobFile screen3; //
    public BmobFile screen4; //
    public Long size; //应用大小
    public BmobFile apk; //应用apk

    public String version; //应用版本号
    public String author; //应用作者
    public String date; //应用更新日期
    public Integer downloadCount; //应用下载量

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

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

    public BmobFile getScreen0() {
        return screen0;
    }

    public void setScreen0(BmobFile screen0) {
        this.screen0 = screen0;
    }

    public BmobFile getScreen1() {
        return screen1;
    }

    public void setScreen1(BmobFile screen1) {
        this.screen1 = screen1;
    }

    public BmobFile getScreen2() {
        return screen2;
    }

    public void setScreen2(BmobFile screen2) {
        this.screen2 = screen2;
    }

    public BmobFile getScreen3() {
        return screen3;
    }

    public void setScreen3(BmobFile screen3) {
        this.screen3 = screen3;
    }

    public BmobFile getScreen4() {
        return screen4;
    }

    public void setScreen4(BmobFile screen4) {
        this.screen4 = screen4;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public BmobFile getApk() {
        return apk;
    }

    public void setApk(BmobFile apk) {
        this.apk = apk;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

}
