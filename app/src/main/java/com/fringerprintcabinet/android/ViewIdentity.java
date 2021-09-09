package com.fringerprintcabinet.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ViewIdentity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewidentity_layout);
        TextView Textview = findViewById(R.id.View_data);
        List<identity> identities = DataSupport.findAll(identity.class);
        for (identity identity : identities) {
            Textview.append("name is " + identity.getName() + "\n");
            Textview.append("sex is " + identity.getSex() + "\n");
            Textview.append("age is " + identity.getAge() + "\n");
            Textview.append("post is " + identity.getPost() + "\n");
        }
    }
}