package com.xybst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.bean.ArticlesListItem;
import com.xybst.bean.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/1/8.
 */
public class SearchListAdapter extends BaseAdapter{
    private List<ArticlesListItem> items = new ArrayList<>();
    private LayoutInflater inflater;

    public SearchListAdapter(Context context, List<ArticlesListItem> list) {
        this.inflater = inflater.from(context);
        items.addAll(list);
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
        view = inflater.inflate(R.layout.item_articlelist, null);
        ((TextView) view.findViewById(R.id.title)).setText(items.get(position).getTitle());
        ((TextView) view.findViewById(R.id.publisher)).setText(items.get(position).getPublisher());
        ((TextView) view.findViewById(R.id.time)).setText(items.get(position).getTime());
        return view;
    }
}
