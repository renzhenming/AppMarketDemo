package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.renzhenming.appmarket.test.IMessageAidl;

public class MainActivity extends AppCompatActivity {

    private IMessageAidl messageAidl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Android5.0中service的intent一定要显性声明
        //final Intent intent = new Intent(this,BindService.class);
        //bindService(intent,coon,Service.BIND_AUTO_CREATE)

        /*Intent intent = new Intent();
        intent.setAction("com.renzhenming.getmessage");
        bindService(intent,connection, Context.BIND_AUTO_CREATE);*/
    }

    public void getData(View view){
        try {
            Toast.makeText(getApplicationContext(),messageAidl.getUserName()+","+messageAidl.getPassword(),Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageAidl = IMessageAidl.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
