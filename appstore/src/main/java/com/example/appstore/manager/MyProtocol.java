package com.example.appstore.manager;

import com.example.appstore.bean.AppInfo;
import com.example.appstore.bean.CarouselPic;
import com.example.appstore.bean.RecommendInfo;
import com.example.appstore.conf.Constants;
import com.example.appstore.utils.LogUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by SingMore on 2017/2/10.
 * 从后端云服务器获取数据
 */

public class MyProtocol {
    public List<AppInfo> mDataList;
    public AppInfo mData;
    public List<CarouselPic> picList;
    public List<RecommendInfo> wordList;

    /**
     * 从Bmob后端云获取数据,查询轮播图数据
     */
    public List<CarouselPic> loadCarouselPics() {
        BmobQuery<CarouselPic> bmobQuery = new BmobQuery<CarouselPic>();
        bmobQuery.findObjects(new FindListener<CarouselPic>() {
            @Override
            public void done(List<CarouselPic> list, BmobException e) {
                picList = list;
            }
        });

        return picList;
    }

    /**
     * 从Bmob后端云获取数据,查询推荐热点词
     */
    public List<RecommendInfo> loadRecommendWords() {
        BmobQuery<RecommendInfo> bmobQuery = new BmobQuery<RecommendInfo>();
        bmobQuery.findObjects(new FindListener<RecommendInfo>() {
            @Override
            public void done(List<RecommendInfo> list, BmobException e) {
                wordList = list;
            }
        });
        return wordList;
    }

    /**
     * 从Bmob后端云获取数据, 分页查询,设置一次查询10条数据
     */
    public List<AppInfo> loadPartData(int index) {
        final BmobQuery<AppInfo> bmobQuery = new BmobQuery<AppInfo>();
        bmobQuery.setLimit(Constants.PAGESIZE);
        bmobQuery.setSkip(index);
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = bmobQuery.hasCachedResult(AppInfo.class);
        if(isCache){
            // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
            bmobQuery.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
        }else{
            // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
            bmobQuery.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
        }

        bmobQuery.findObjects(new FindListener<AppInfo>() {
            @Override
            public void done(List<AppInfo> list, BmobException e) {
                mDataList = list;
            }
        });

        return mDataList;
    }

    /**
     * 根据包名查询
     */
    public AppInfo queryByPackageName(final String packageName) {
        BmobQuery<AppInfo> bmobQuery = new BmobQuery<AppInfo>();
        bmobQuery.addWhereEqualTo("packageName", packageName);
        bmobQuery.findObjects(new FindListener<AppInfo>() {
            @Override
            public void done(List<AppInfo> list, BmobException e) {
                mData = list.get(0);
            }
        });
        return mData;
    }

    /**
     * 根据类型查询
     */
    public List<AppInfo> queryByType(final String type, final int index) {
        BmobQuery<AppInfo> bmobQuery = new BmobQuery<AppInfo>();
        bmobQuery.setLimit(Constants.PAGESIZE);
        bmobQuery.setSkip(index);
        bmobQuery.addWhereEqualTo("type", type);
        bmobQuery.findObjects(new FindListener<AppInfo>() {
            @Override
            public void done(List<AppInfo> list, BmobException e) {
                mDataList = list;
            }
        });
        return mDataList;
    }

    /**
     * 根据应用下载量以及评价进行排序
     */
    public List<AppInfo> sortByDownloadCount(final int index) {
        BmobQuery<AppInfo> bmobQuery = new BmobQuery<AppInfo>();
        bmobQuery.setLimit(Constants.PAGESIZE);
        bmobQuery.setSkip(index);
        bmobQuery.order("-downloadCount, -stars");
        bmobQuery.findObjects(new FindListener<AppInfo>() {
            @Override
            public void done(List<AppInfo> list, BmobException e) {
                mDataList = list;
            }
        });
        return mDataList;
    }

    /**
     * 模糊搜索
     */
    public List<AppInfo> searchByName(final String searchName) {
        BmobQuery<AppInfo> bmobQuery = new BmobQuery<AppInfo>();
        //查询name字段的值含有“searchName”的数据
        //bmobQuery.addWhereContains("name", searchName);

        //根据搜索的名字进行查询
        bmobQuery.addWhereEqualTo("name", searchName);
        bmobQuery.findObjects(new FindListener<AppInfo>() {
            @Override
            public void done(List<AppInfo> list, BmobException e) {
                mDataList = list;
                LogUtils.e("wqz", "搜索到的结果有：");
                for (AppInfo info : list) {
                    LogUtils.e("wqz", info.getName() + " ##### " + info.getDes());
                }
            }
        });
        return mDataList;
    }

}
