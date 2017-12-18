package com.example.renzhenming.appmarket;

import java.util.Observable;

/**
 * Created by renzhenming on 2017/12/18.
 */

public class TextObserverClass extends Observable{

    private static TextObserverClass observer = new TextObserverClass();
    private TextObserverClass(){}
    public static TextObserverClass getObserver(){
        return observer;
    }


    public void changeInfo(String value){
        setChanged();//修改是否更新的标示
        notifyObservers(value);//将数据传递给每一个观察者
    }
}
