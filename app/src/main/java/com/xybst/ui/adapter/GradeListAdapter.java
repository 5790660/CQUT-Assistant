package com.xybst.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.bean.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/8.
 */
public class GradeListAdapter extends BaseAdapter{

    private List<Grade> items = new ArrayList<>();
    private LayoutInflater inflater;

    public GradeListAdapter(Context context, List<Grade> grades) {
        this.inflater = inflater.from(context);
        items.addAll(grades);
    }

    public void updateData(List<Grade> grades) {
        items.clear();
        items.addAll(grades);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.item_gradelist, null);
        ((TextView) view.findViewById(R.id.tvCourseName)).append(items.get(position).getCourseName());
        ((TextView) view.findViewById(R.id.tvCredit)).append(items.get(position).getCredit());
        ((TextView) view.findViewById(R.id.tvGradePoint)).append(items.get(position).getPoint());
        ((TextView) view.findViewById(R.id.tvScore)).append(items.get(position).getScore());
        return view;
    }
}
