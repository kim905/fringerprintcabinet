package com.fringerprintcabinet.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fringerprintcabinet.android.MqttServer.IGetMessageCallBack;
import com.fringerprintcabinet.android.MqttServer.MQTTService;
import com.fringerprintcabinet.android.MqttServer.MyServiceConnection;

public class MainActivity extends AppCompatActivity implements IGetMessageCallBack {
//    private static final int UPDATE_TEXT = 1;

    private TextView textView;
    private Button button;
    private MyServiceConnection serviceConnection;
    private MQTTService mqttService;

//    private Handler handler = new Handler(){
//        public void handleMessage(Message msg){
//            switch (msg.what){
//                case UPDATE_TEXT:
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        button = (Button) findViewById(R.id.button);
        button.getBackground().setAlpha(100);//透明度0-255

        serviceConnection = new MyServiceConnection();//回调函数
        serviceConnection.setIGetMessageCallBack(MainActivity.this);

        Intent intent = new Intent(MainActivity.this, MQTTService.class);
        //绑定活动和服务
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //点击监听器进行发布主题
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MQTTService.publish("/测试一下子");
            }
        });


        Button Button_database = findViewById(R.id.all_database);
        Button_database.getBackground().setAlpha(100);//透明度0-255
        Button_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyDatabase.class);
                startActivity(intent);
            }
        });

        Button View_identity = findViewById(R.id.view_identity);
        View_identity.getBackground().setAlpha(100);
        View_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.fringerprintcabinet.ACTION_START");
                intent.addCategory("com.example.fringerprintcabinet.MY_CATEGORY");
                startActivityForResult(intent,1);
            }
        });
    }

    //订阅主题进行显示
    @Override
    public void setMessage(String message) {
        textView.setText(message);
        mqttService = serviceConnection.getMqttService();
        mqttService.toCreateNotification(message);//消息进行通知
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);//取绑
        super.onDestroy();
    }
}