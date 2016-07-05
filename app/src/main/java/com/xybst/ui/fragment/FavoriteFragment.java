package com.xybst.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xybst.ui.activity.ArticleContentActivity;
import com.xybst.activity.R;
import com.xybst.adapter.FavoriteAdapter;
import com.xybst.bean.ArticlesListItem;


/**
 * A simple {@link Fragment} subclass.
 */
// TODO: 2016/1/12
public class FavoriteFragment extends Fragment {

    private FavoriteAdapter adapter;
    private ListView listView;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.favorite);
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new FavoriteAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("article",(ArticlesListItem)adapter.getItem(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        adapter.update();
        super.onResume();
    }
}
