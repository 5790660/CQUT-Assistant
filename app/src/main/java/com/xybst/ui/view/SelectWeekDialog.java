package com.xybst.ui.view;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xybst.activity.R;
import com.xybst.ui.adapter.SelectWeekDialogAdapter;
import com.xybst.util.Info;

/**
 * Created by 创宇 on 2016/1/4.
 */
public class SelectWeekDialog extends DialogFragment{

    private static final String ARG_PARAM = "selectedWeek";
    private int selectedWeek;
    private Info info;

    private Toolbar toolbar;
    private ListView listView;

    public static SelectWeekDialog newInstance(int selectWeek) {
        SelectWeekDialog fragment = new SelectWeekDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, selectWeek);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            selectedWeek = getArguments().getInt(ARG_PARAM);
        }
        info = (Info) getActivity().getApplication();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_select_week, null);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new SelectWeekDialogAdapter(getContext(), info.getCurrentWeek(), selectedWeek));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //此处应为(int requestCode,int resultCode, @Nullable android.content.Intent data) 为简便直接将pos当作resultCode传递
                getTargetFragment().onActivityResult(getTargetRequestCode(), position, null);
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.TOP;
        lp.width = dip2px(210);
        lp.height = dip2px(260);
        // TODO: 2016/1/11 weekDialog偏移量
        lp.y = toolbar.getHeight() - toolbar.getContentInsetStart() ;
        lp.dimAmount = 0.0f;
        lp.alpha = 0.9f;
        getDialog().onWindowAttributesChanged(lp);
        listView.setSelection(selectedWeek - 3);
    }

    public int getToolbarHeight() {
        return ((AppCompatActivity)getActivity()).getSupportActionBar().getHeight();
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int getStatusBarHeight() {
        Rect frame = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    public int getScreenHeight() {
        int screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();
        System.out.println("height3  " +screenHeight);
        return screenHeight;
    }
}
