package com.xybst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.bean.ArticlesListItem;
import com.xybst.dao.FavoriteDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 创宇 on 2016/3/16.
 */
public class FavoriteAdapter extends BaseAdapter{

    private List<ArticlesListItem> items = new ArrayList<>();
    private LayoutInflater inflater;
    private FavoriteDAO dao;

    public FavoriteAdapter(Context context) {
        this.inflater = inflater.from(context);
        dao = new FavoriteDAO(context);
        items.addAll(dao.getFavoriteArticleList());
    }

    public void update() {
        items.clear();
        items.addAll(dao.getFavoriteArticleList());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
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
