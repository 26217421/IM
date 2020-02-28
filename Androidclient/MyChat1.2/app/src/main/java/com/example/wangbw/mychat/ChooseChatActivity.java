package com.example.wangbw.mychat;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.lihanxiao.mychat.R;

public class ChooseChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_chat);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content, new com.example.lihanxiao.mychat.com.example.wangbw.mychat.ChooseChatFragment(),"ChooseChatFragment");
        transaction.commit();
    }
}
