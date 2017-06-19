package com.example.appstore.conf;

import com.example.appstore.utils.LogUtils;

/**
 * Created by SingMore on 2016/12/6.
 *
 */

public class Constants {
    public static int DEBUGLEVEL = LogUtils.LEVEL_ALL;//显示所有的日志

    public static final int	PAGESIZE = 5;//加载数据一页的数量

    //Bmob
    public static final String BMOB_APPID = "c5af5ecb3c10feeb7868471641f2f52b";

    //图灵
    public static final String TURING_API = "http://www.tuling123.com/openapi/api";
    public static final String TURING_KEY = "7f04bd28ad88481280cedfd5d8ecaaca";
    public static final String TURING_SECRET = "15f39b266412810a";


    /**
     * http://127.0.0.1:8090/-->服务器在手机上
     * http://192.168.1.3:8080/GooglePlayServer/-->服务器在电脑上,直接ip访问
     * http://10.0.2.2:8080/GooglePlayServer/-->服务器在电脑上,android模拟器访问
     * http://10.0.3.2:8080/GooglePlayServer/-->服务器在电脑上,genymotion模拟器访问
     */
    public static final class URLS {
        public static String BASEURL = "http://10.0.2.2:8080/GooglePlayServer/";
        public static String IMAGEBASEURL = BASEURL + "image?name=";
        public static String DOWNLOADBASEURL = BASEURL + "download";
        public static String HTTP = "HTTP";
    }

}
