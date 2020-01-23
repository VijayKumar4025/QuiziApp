package com.infowithvijay.quizi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    public static DBHelper DBHelper;


    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        DBHelper = new DBHelper(getApplicationContext());
        try {
            DBHelper.createDB();   // calling the method inside onCreate()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
