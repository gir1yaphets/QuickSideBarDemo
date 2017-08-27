package com.example.pengxiaolve.quicksidebardemo.adapter;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;


public class CityViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public CityViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public View getViewById(@IdRes int id) {
        View view = mViews.get(id);

        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }

        return view;
    }

    public void setText(@IdRes int id, String text) {
        TextView tv = (TextView) getViewById(id);
        tv.setText(text);
    }
}
