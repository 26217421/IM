package com.example.wangbw.mychat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lihanxiao.mychat.R;
import com.example.lihanxiao.mychat.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText editUser;
    private EditText editPassword;
    private Button buttonLogin;
    private Button buttonResgister;

    private MessageReceiver messageReceiver;

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String content = intent.getStringExtra("jsonString");
            try {
                JSONObject root = new JSONObject(content);
                boolean isLoginSuccess = root.getBoolean("result");
                if(isLoginSuccess)
                {
                    Intent i = new Intent(LoginActivity.this, ChooseChatActivity.class);
                    startActivity(i);
                    UserInfo.setID(editUser.getText().toString());
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "login fail", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initMessageReceiver()
    {
        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("SocketClient");
        registerReceiver(messageReceiver,filter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUser = (EditText) findViewById(R.id.User);
        editPassword = (EditText) findViewById(R.id.Password);
        buttonLogin = (Button) findViewById(R.id.Login);
        buttonResgister = (Button) findViewById(R.id.Resgister);

        buttonLogin.setOnClickListener(this);
        buttonResgister.setOnClickListener(this);

        Intent i = new Intent(LoginActivity.this, com.example.lihanxiao.mychat.com.example.wangbw.mychat.SocketService.class);
        startService(i);

        initMessageReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.Login)
        {
            if((editUser.getText().toString() != null) && (editPassword.getText().toString() != null))
            {
                try {
                    JSONObject root = new JSONObject();
                    root.put("user", editUser.getText());
                    root.put("password", editPassword.getText());
                    com.example.lihanxiao.mychat.com.example.wangbw.mychat.SocketService.send(root.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(v.getId() == R.id.Resgister)
        {
            Intent i = new Intent(LoginActivity.this,ChooseChatActivity.class);
            startActivity(i);
            UserInfo.setID(editUser.getText().toString());
        }
    }
}
