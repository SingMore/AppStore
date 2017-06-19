package com.example.appstore.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by SingMore on 2017/3/7.
 */

public class CarouselPic extends BmobObject {
    public BmobFile pic; //首页轮播图片

    public BmobFile getPic() {
        return pic;
    }

    public void setPic(BmobFile pic) {
        this.pic = pic;
    }
}
