package com.xybst.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.bean.ArticlesListItem;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class ArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ArticlesListItem> mValues = new ArrayList<>();
    private OnArticleListInteractionListener mListener;
    private final static int VIEW_TYPE_LIST = 0;
    private final static int VIEW_TYPE_LOAD_MORE = 1;

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
            return new LoadMoreViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(getItemViewType(position) == VIEW_TYPE_LIST) {

            final ListViewHolder listViewHolder = (ListViewHolder) holder;
            listViewHolder.mItem = mValues.get(position);
            listViewHolder.tvTitle.setText(mValues.get(position).getTitle());
            listViewHolder.tvPublisher.setText(mValues.get(position).getPublisher());
            listViewHolder.tvDate.setText(mValues.get(position).getTime());

            listViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onArticleListInteraction(listViewHolder.mItem);
                    }
                }
            });
        } else if (getItemViewType(position) == VIEW_TYPE_LOAD_MORE) {
            //Load More Items
        }
    }

    @Override
    public int getItemCount() {
        if (mValues.size() != 0)
            return mValues.size() + 1;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mValues.size() == 0 || position < mValues.size()) {
            return VIEW_TYPE_LIST;
        }else {
            return VIEW_TYPE_LOAD_MORE;
        }
    }

    public void updateData(List<ArticlesListItem> items) {
        if (items != null) {
            mValues = items;
            notifyDataSetChanged();
        }
    }

    public void addMoreData(List<ArticlesListItem> items) {
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    public class ListViewHolder extends ViewHolder {

        public final View mView;
        public final TextView tvTitle;
        public final TextView tvPublisher;
        public final TextView tvDate;
        public ArticlesListItem mItem;

        public ListViewHolder(View view) {
            super(view);
            mView = view;
            tvTitle = (TextView) view.findViewById(R.id.title);
            tvPublisher = (TextView) view.findViewById(R.id.publisher);
            tvDate = (TextView) view.findViewById(R.id.time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvTitle.getText() + "'";
        }
    }

    public class LoadMoreViewHolder extends ViewHolder {

        public final View mView;

        public LoadMoreViewHolder(View view) {
            super(view);
            mView = view;
        }
    }

    public interface OnArticleListInteractionListener {
        void onArticleListInteraction(ArticlesListItem item);
    }
}
