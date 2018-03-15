package com.app.rzm.test;

import java.util.Observable;

/**
 * Created by rzm on 2017/12/18.
 */

public class TextObserverClass extends Observable{

    private volatile static TextObserverClass observer = new TextObserverClass();
    private TextObserverClass(){}
    public static TextObserverClass getInstance(){
        if (observer == null) {
            synchronized (TextObserverClass.class) {
                if (observer == null) {
                    observer = new TextObserverClass();
                }
            }
        }
        return observer;
    }


    public void changeInfo(String value){
        setChanged();//修改是否更新的标示
        notifyObservers(value);//将数据传递给每一个观察者
    }

    public void clear(){
        observer = null;
    }
}
