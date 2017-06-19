package com.example.appstore.factory;


import com.example.appstore.base.BaseFragment;
import com.example.appstore.fragment.AppFragment;
import com.example.appstore.fragment.GameFragment;
import com.example.appstore.fragment.HomeFragment;
import com.example.appstore.fragment.PersonFragment;
import com.example.appstore.fragment.RankFragment;
import com.example.appstore.fragment.RecommendFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SingMore on 2017/2/7.
 * ViewPager里面的Fragment的制造工厂
 */

public class FragmentFactory {
    public static final int FRAGMENT_HOME = 0;//首页
    public static final int FRAGMENT_RANK = 1;//排行
    public static final int FRAGMENT_APP = 2;//应用
    public static final int FRAGMENT_GAME = 3;//游戏
    public static final int FRAGMENT_RECOMMEND = 4;//推荐
    public static final int FRAGMENT_PERSON = 5;//我的

    //Fragment缓存集合
    public static Map<Integer, BaseFragment> cachesFragmentMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment getFragment(int position) {
        BaseFragment fragment = null;
        //如果缓存集合里面有对应的fragment，就直接取出来
        if(cachesFragmentMap.containsKey(position)) {
            fragment = cachesFragmentMap.get(position);
            return fragment;
        }

        switch (position) {
            case FRAGMENT_HOME://首页
                fragment = new HomeFragment();
                break;
            case FRAGMENT_RANK://排行
                fragment = new RankFragment();
                break;
            case FRAGMENT_APP://应用
                fragment = new AppFragment();
                break;
            case FRAGMENT_GAME://游戏
                fragment = new GameFragment();
                break;
            case FRAGMENT_RECOMMEND://推荐
                fragment = new RecommendFragment();
                break;
            case FRAGMENT_PERSON://我的
                fragment = new PersonFragment();
                break;
        }
        //保存对应的fragment
        cachesFragmentMap.put(position, fragment);
        return fragment;
    }
}
