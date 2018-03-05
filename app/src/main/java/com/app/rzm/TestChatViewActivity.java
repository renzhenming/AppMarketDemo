package com.app.rzm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rzm.commonlibrary.views.chatview.ChatInput;
import com.rzm.commonlibrary.views.chatview.ChatView;

import java.util.List;

public class TestChatViewActivity extends AppCompatActivity implements ChatView {

    private ChatInput input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat_view);
        initView();
    }

    private void initView() {
        input = (ChatInput) findViewById(R.id.chat_input);
        input.setChatView(this);
    }

    @Override
    public void showMessage(Object message) {

    }

    @Override
    public void showMessage(List<Object> messages) {

    }

    @Override
    public void clearAllMessage() {

    }

    @Override
    public void onSendMessageSuccess(Object message) {

    }

    @Override
    public void onSendMessageFail(int code, String desc, Object message) {

    }

    @Override
    public void sendImage() {

    }

    @Override
    public void sendPhoto() {

    }

    @Override
    public void sendText() {

    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {

    }

    @Override
    public void endSendVoice() {

    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void sending() {

    }

    @Override
    public void showDraft(Object draft) {

    }
}
