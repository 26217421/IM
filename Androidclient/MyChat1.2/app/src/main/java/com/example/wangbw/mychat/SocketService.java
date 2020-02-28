package com.example.wangbw.mychat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;



public class SocketService extends Service {
    private static Socket socket;
    private static BufferedWriter bWriter=null;//输出流
    private static BufferedReader bReader=null;//输入流

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(SocketService.this, "connect success", Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new Socket("49.140.58.97",1996);
                    bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
                    bReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));

                    String s;
                    while ((s = bReader.readLine()) != null) {
                        Intent intent = new Intent();
                        intent.putExtra("jsonString", s);
                        intent.setAction("SocketClient");
                        sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void send(String jsonString)
    {
        jsonString += "\n";
        try {
            bWriter.write(jsonString);
//            bWriter.flush();
            new Thread(){
                @Override
                public void run() {
                    try {
                        bWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
