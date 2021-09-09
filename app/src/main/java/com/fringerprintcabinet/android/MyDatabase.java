package com.fringerprintcabinet.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MyDatabase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydatabase_layout);

        Button createDatabase = findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.getDatabase();
            }
        });

        Button addDatabase = findViewById(R.id.add_data);
        addDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false; //标记判断保存是否成功
                identity identity = new identity();
                identity.setName("星河岸");
                identity.setAge(18);
                identity.setSex("男");
                identity.setPost("manager");
                List<identity> identityList = DataSupport
                        .select("name","sex","post")
                        .where("name = ?",identity.getName())
                        .where("sex = ?",identity.getSex())
                        .where("post = ?",identity.getPost())
                        .find(identity.class);
                if (identityList == null || identityList.size() == 0){
                    identity.save();
                    flag = true;
                }else {
                    for (int i = 0; i < identityList.size() ; i++) {
                        identityList.get(i).delete();
                    }
                }
                if (flag) {
                    Toast.makeText(MyDatabase.this,"保存成功",LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyDatabase.this, "保存失败", LENGTH_SHORT).show();
                }
            }
        });

        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button deleteButton = findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(identity.class);
            }
        });

        Button queryButton = findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<identity> identities = DataSupport.findAll(identity.class);
                for (identity identity: identities){
                    Log.d("Mainactivity", "name is " + identity.getName());
                    Log.d("Mainactivity", "age is " + identity.getAge());
                    Log.d("Mainactivity", "sex is " + identity.getSex());
                    Log.d("Mainactivity", "post is " + identity.getPost());
                }
            }
        });
    }



}