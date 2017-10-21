package com.example.renzhenming.appmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rzm.commonlibrary.general.hook.HookActivityUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
public class TestProxyActivity extends Activity {

    private IBank bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_proxy);
        /*Man man = new Man();
        bank = (IBank) Proxy.newProxyInstance(IBank.class.getClassLoader(), new Class<?>[] {IBank.class}, new BankInvocationHandler(man));*/

        HookActivityUtil hookActivityUtil = new HookActivityUtil(this,TestHookActivity_Registered.class);
        try {
            hookActivityUtil.hookStartActivity();
            hookActivityUtil.hookLaunchActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void proxy(View v){
        bank.makeCard();
        bank.takeMoney();
    }
    public void hook(View v){
        startActivity(new Intent(getApplicationContext(),TestHookActivity.class));
    }
    private class BankInvocationHandler implements InvocationHandler{

        private final Man man;

        public BankInvocationHandler(Man man) {
            this.man = man;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            Object invoke = method.invoke(man, objects);
            return invoke;
        }
    }

    public interface IBank{
        void takeMoney();
        void makeCard();
    }

    public class Man implements IBank{

        @Override
        public void takeMoney() {
            Toast.makeText(TestProxyActivity.this, "取钱", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void makeCard() {
            Toast.makeText(TestProxyActivity.this, "办卡", Toast.LENGTH_SHORT).show();
        }
    }
}
