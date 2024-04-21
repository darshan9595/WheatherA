package com.example.wheathera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherapp.R;

public class DataActivity extends AppCompatActivity {

    private TextView receiverMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);

        initializeUI();

        String str = getIntent().getStringExtra("message_key");
        displayReceivedMessage(str);
    }

    private void initializeUI() {
        receiverMsg = findViewById(R.id.received_value_id);
    }

    private void displayReceivedMessage(String message) {
        receiverMsg.setText(message);
    }

    public boolean isCitySaved() {
        SharedPreferences sharedPreferences = getSharedPreferences("message_key", Context.MODE_PRIVATE);
        return sharedPreferences.contains("CITY");
    }
}