package com.applet.feature.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import io.dcloud.feature.sdk.Interface.IUniMP;

public class MPStack {

    private LinkedHashMap<String,IUniMP> mStack = new LinkedHashMap<>();

    private static MPStack instance;

    public static MPStack getInstance() {
        if (instance == null) {
            instance = new MPStack();
        }
        return instance;
    }

    public void push(IUniMP uniMP) {
        mStack.put(uniMP.getAppid(), uniMP);
    }

    public IUniMP pop() {
        if (mStack.size() > 0) {
            IUniMP uniMP = (IUniMP) mStack.values().toArray()[mStack.size() - 1];
            return mStack.remove(uniMP);
        }
        return null;
    }

    public void clear() {
        mStack.clear();
    }

    public int size() {
        return mStack.size();
    }

    public boolean isEmpty() {
        return mStack.isEmpty();
    }

    public IUniMP remove(String appid) {
        IUniMP remove = mStack.remove(appid);
        return remove;
    }

    public IUniMP remove(IUniMP uniMP) {
        IUniMP remove = mStack.remove(uniMP);
        return remove;
    }


    public IUniMP getCurrentUniMP(){
        if(mStack.size() == 0){
            return  null;
        }
        IUniMP uniMP = (IUniMP)( mStack.values().toArray()[mStack.size() - 1]);
        return uniMP;
    }


    public IUniMP getUniMP(String appid){
        return mStack.get(appid);
    }
}
