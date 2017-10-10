package com.example.renzhenming.appmarket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestProxyActivity extends AppCompatActivity {

    private IBank bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_proxy);
        Man man = new Man();
        bank = (IBank) Proxy.newProxyInstance(IBank.class.getClassLoader(), new Class<?>[] {IBank.class}, new BankInvocationHandler(man));

    }
    public void proxy(View v){
        bank.makeCard();
        bank.takeMoney();
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
