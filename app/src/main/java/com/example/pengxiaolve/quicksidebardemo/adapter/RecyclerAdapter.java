package com.example.pengxiaolve.quicksidebardemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pengxiaolve.quicksidebardemo.R;
import com.example.pengxiaolve.quicksidebardemo.module.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerAdapter extends RecyclerView.Adapter<CityViewHolder> {

    private List<City> mData;
    private Map<Integer, String> mSection;
    private List<String> mTotalData;
    private Context mContext;

    private static final int SECTION_TYPE = 0;
    private static final int DATA_TYPE = 1;

    private static final String TAG = "RecyclerAdapter";

    public RecyclerAdapter(List<City> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        getSectionViewPos();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = DATA_TYPE;

        if (mSection.containsKey(position)) {
            viewType = SECTION_TYPE;
        }

        return viewType;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CityViewHolder holder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == DATA_TYPE) {
            view = inflater.inflate(R.layout.item_view, parent, false);
            holder = new CityViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.section_view, parent, false);
            holder = new CityViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == DATA_TYPE) {
            holder.setText(R.id.textview, mTotalData.get(position));
        } else {
            holder.setText(R.id.section_text, mSection.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mTotalData.size();
    }

    private void getSectionViewPos() {
        mTotalData = new ArrayList<>();
        mSection = new HashMap<>();
        mSection.put(0, mData.get(0).getFirstLetter());
        mTotalData.add(mSection.get(0));

        for (int i = 0; i < mData.size() - 1; i++) {
            mTotalData.add(mData.get(i).getCityName());
            String currentLetter = mData.get(i).getFirstLetter();
            String nextLetter = mData.get(i + 1).getFirstLetter();

            if (!currentLetter.equals(nextLetter)) {
                int sectionCount = mSection.size();
                mSection.put(i + 1 + sectionCount, nextLetter);
                mTotalData.add(nextLetter);
            }
        }
    }

    public int calculateSectionCount(String letter) {
        int count = 0;

        if (mSection.containsValue(letter)) {
            for (Map.Entry<Integer, String> entry : mSection.entrySet()) {
                if (entry.getValue().equals(letter)) {
                    count = entry.getKey();
                }
            }
        }

        Log.d(TAG, "calculateSectionCount: count = " + count);
        return count;
    }
}
