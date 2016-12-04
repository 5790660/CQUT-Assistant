package com.xybst.ui.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xybst.activity.R;
import com.xybst.adapter.GradeListAdapter;
import com.xybst.adapter.ListDropDownAdapter;
import com.xybst.bean.Grade;
import com.xybst.dao.GradeDAO;
import com.xybst.net.HttpUtil;
import com.xybst.utils.Info;
import com.xybst.ui.DropDownMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class GradeFragment extends Fragment {

    private String year = "2016-2017";
    private String term = "1";
    private String headers[] = { "2016-2017", "第1学期"};
    private String years[] = {"2013-2014", "2014-2015", "2015-2016", "2016-2017"};
    private String terms[] = {"第1学期", "第2学期", "第3学期"};

    private List<View> popupViews = new ArrayList<>();
    private Info info;
    private GradeListAdapter gradeListAdapter;
    private ProgressDialog progressDialog;

    public GradeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            onDestroy();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.inquiry);

        info = (Info) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_inquiry, container, false);
        final DropDownMenu mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);

        final ListView yearView = new ListView(getActivity());
        yearView.setDividerHeight(0);
        final ListDropDownAdapter yearAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(years));
        yearAdapter.setCheckItem(years.length - 1);
        yearView.setAdapter(yearAdapter);
        yearView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(years[position]);
                mDropDownMenu.closeMenu();
                year = years[position];
                gradeListAdapter.updateData(getItemsFromContainer(year, term));
            }
        });

        final ListView termView = new ListView(getActivity());
        termView.setDividerHeight(0);
        final ListDropDownAdapter termAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(terms));
        termAdapter.setCheckItem(Integer.valueOf(term) - 1);
        termView.setAdapter(termAdapter);
        termView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                termAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(terms[position]);
                mDropDownMenu.closeMenu();
                term = String.valueOf(position + 1);
                gradeListAdapter.updateData(getItemsFromContainer(year, term));
            }
        });

        popupViews.add(yearView);
        popupViews.add(termView);

        ListView listView = new ListView(getActivity());
        listView.setDivider(null);
        listView.setVerticalScrollBarEnabled(false);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (info.getGradeContainer().isEmpty()) {
            getItemsFormDB();
        }
        gradeListAdapter = new GradeListAdapter(container.getContext(), getItemsFromContainer(year, term));
        listView.setAdapter(gradeListAdapter);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, listView);
        if (info.getGradeContainer().isEmpty()) {
            initiateRefresh();
        }
        return view;
    }

    public void initiateRefresh() {
        progressDialog = ProgressDialog.show(getContext(), null, "获取成绩中……");
        new RefreshGradeTask().execute();
    }

    private class RefreshGradeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return getItemsFromWeb();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            parseResult(result);
        }
    }

    public void parseResult(String result) {
        progressDialog.dismiss();
        if (result == null) {
            Toast.makeText(getContext(), "请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            switch (jsonObject.getInt("code")) {
                case 200:
                    List<Grade> grades = new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(jsonObject.getString("result"));
                    Log.d("jsonArray",jsonArray.length()+"");
                    for(int i = 0 ; i < jsonArray.length() ; i++) {
                        jsonObject = (JSONObject) jsonArray.get(i);
                        grades.add(new Grade(jsonObject.getString("className"), jsonObject.getString("credit"), jsonObject.getString("point"),
                                jsonObject.getString("grade"), jsonObject.getString("stuPeriod"), jsonObject.getString("stuYear")));
                    }
                    for (Grade grade : grades) {
                        System.out.println(grade.toString());
                    }
                    info.setGradeContainer(grades);
                    new GradeDAO(getContext()).addGrades(grades);
                    gradeListAdapter.updateData(getItemsFromContainer(year, term));
                    break;
                case 400:
                    Toast.makeText(getContext(), "教务系统暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
                case 403:
                    Toast.makeText(getContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Grade> getItemsFromContainer(String year, String term) {
        List<Grade> grades = new ArrayList<>();
        for (Grade grade : info.getGradeContainer()) {
            if (year.equals(grade.getYear()) && term.equals(grade.getTerm())) {
                grades.add(grade);
            }
        }
        return grades;
    }

    public  void getItemsFormDB() {
        GradeDAO dao = new GradeDAO(getContext());
        info.setGradeContainer(dao.getGrades());
    }

    public String getItemsFromWeb() {
        String stringUrl = HttpUtil.BASE_URL + "/Score";
        List<NameValuePair> pairList = new ArrayList<>();
        pairList.add(new BasicNameValuePair("studentId", info.getStudentId()));
        pairList.add(new BasicNameValuePair("studentPassword", info.getPassword()));
        return HttpUtil.get(stringUrl, pairList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_inquiry, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            initiateRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
