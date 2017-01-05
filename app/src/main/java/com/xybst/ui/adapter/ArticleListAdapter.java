package com.xybst.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.bean.NewsItem;
import com.xybst.util.CacheLoader;
import com.xybst.util.NewsType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_LIST = 0;

    private final static int VIEW_TYPE_LOAD_MORE = 1;

    private List<NewsItem> mValues = new ArrayList<>();

    private OnArticleListInteractionListener mListener;

    public ArticleListAdapter(OnArticleListInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_articlelist, parent, false);
            return new ListViewHolder(view);
        }else {
            View view = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.item_articlelist_footer, parent, false);
            return new ViewHolder(view) {};
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(getItemViewType(position) != VIEW_TYPE_LIST)
            return;

        final ListViewHolder listViewHolder = (ListViewHolder) holder;
        listViewHolder.mItem = mValues.get(position);
        listViewHolder.tvTitle.setText(mValues.get(position).getTitle());
        listViewHolder.tvPublisher.setText(mValues.get(position).getPublisher());
        listViewHolder.tvDate.setText(mValues.get(position).getTime());

        listViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onArticleListInteraction(listViewHolder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size() == 0 ? 0 : mValues.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.size() == 0 || position < mValues.size()) {
            return VIEW_TYPE_LIST;
        }else {
            return VIEW_TYPE_LOAD_MORE;
        }
    }

    public void updateData(List<NewsItem> items) {
        if (items != null) {
            mValues = items;
            notifyDataSetChanged();
        }
    }

    public class ListViewHolder extends ViewHolder {

        View mView;
        @BindView(R.id.title)
        TextView tvTitle;
        @BindView(R.id.publisher)
        TextView tvPublisher;
        @BindView(R.id.time)
        TextView tvDate;
        NewsItem mItem;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvTitle.getText() + "'";
        }
    }

    public interface OnArticleListInteractionListener {
        void onArticleListInteraction(NewsItem item);
    }
}
