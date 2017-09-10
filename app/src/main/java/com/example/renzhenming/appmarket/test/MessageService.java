package com.example.renzhenming.appmarket.test;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by renzhenming on 2017/9/3.
 */

public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IMessageAidl.Stub stub = new IMessageAidl.Stub() {
        @Override
        public String getUserName() throws RemoteException {
            return "renzhenming";
        }

        @Override
        public String getPassword() throws RemoteException {
            return "123456";
        }
    };
}
