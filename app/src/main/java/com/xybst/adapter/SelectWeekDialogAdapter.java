package com.xybst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xybst.activity.R;
import com.xybst.utils.Info;

/**
 * Created by 创宇 on 2016/1/5.
 */
public class SelectWeekDialogAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    Info info;
    // TODO: 2016/1/10  weekSum 
    private int weekSum = 25;
    int selectedWeek;
    int currentWeek;

    public SelectWeekDialogAdapter(Context context , int currentWeek, int selectedWeek){
        this.inflater = LayoutInflater.from(context);
        this.currentWeek = currentWeek;
        this.selectedWeek = selectedWeek;
    }

    @Override
    public int getCount() {
        return weekSum;
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
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.item_dialog_select_week, null);
        TextView tvWeek = (TextView) view.findViewById(R.id.tvWeekChoose);
        if (position + 1 == currentWeek) {
            tvWeek.setText("第" + (position + 1) + "周(本周)");
        } else {
            tvWeek.setText("第" + (position + 1) + "周");
        }
        if (position + 1 == selectedWeek) {
            view.setBackgroundResource(R.drawable.week_choose);
            tvWeek.setTextColor(view.getResources().getColor(android.R.color.white));
        }
        return view;
    }
}
