package com.example.pengxiaolve.quicksidebardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.pengxiaolve.quicksidebardemo.adapter.RecyclerAdapter;
import com.example.pengxiaolve.quicksidebardemo.constant.CityData;
import com.example.pengxiaolve.quicksidebardemo.module.City;
import com.example.quicksidebar.QuickSideBarView;
import com.example.quicksidebar.QuickSideTipsView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements com.example.quicksidebar.listener.onLetterTouchedListener{

    private ArrayList<City> mCities;
    private QuickSideBarView mQuickSideBar;
    private QuickSideTipsView mQuickTipsView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.citylist);
        mQuickSideBar = (QuickSideBarView) findViewById(R.id.quicksidebarview);
        mQuickTipsView = (QuickSideTipsView) findViewById(R.id.quicksidetipsview);

        mCities = parseJson();
        mAdapter = new RecyclerAdapter(mCities, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mQuickSideBar.addOnLetterTouchListener(this);
        mQuickSideBar.addOnLetterTouchListener(mQuickTipsView);
    }

    private ArrayList<City> parseJson() {
        Type listType = new TypeToken<ArrayList<City>>(){}.getType();
        Gson gson = new Gson();
        ArrayList<City> cities = gson.fromJson(CityData.cityDataList, listType);
        return cities;
    }

    @Override
    public void onLetterChanged(String letter) {
        Log.d(TAG, "onLetterChanged() called with: letter = [" + letter + "]");
        int position = mAdapter.calculateSectionCount(letter);
        Log.d(TAG, "onLetterChanged() called with: position = [" + position + "]");
        mRecyclerView.scrollToPosition(position);
    }
}
