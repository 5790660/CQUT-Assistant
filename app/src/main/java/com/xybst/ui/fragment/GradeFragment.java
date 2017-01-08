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
import com.xybst.ui.adapter.GradeListAdapter;
import com.xybst.ui.adapter.ListDropDownAdapter;
import com.xybst.bean.Grade;
import com.xybst.persistence.GradeDAO;
import com.xybst.util.CacheLoader;
import com.xybst.util.Home;
import com.xybst.ui.view.DropDownMenu;
import com.xybst.util.Info;
import com.xybst.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class GradeFragment extends Fragment {

    private String headers[] = TimeUtils.getCurTerm();
    private String years[] = TimeUtils.getSchoolYear();
    private String terms[] = TimeUtils.getTerms();
    private String year = TimeUtils.getCurTerm()[0];
    private String term = TimeUtils.getCurTerm()[1];

    private List<View> popupViews = new ArrayList<>();

    private GradeListAdapter gradeListAdapter= new GradeListAdapter();

    private ProgressDialog progressDialog;

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

        View view = inflater.inflate(R.layout.fragment_inquiry, container, false);
        final DropDownMenu mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        ListView gradeList = (ListView) view.findViewById(R.id.gradeList);

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
                gradeListAdapter.updateData(getItemsFromCtr(year, term));
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
                gradeListAdapter.updateData(getItemsFromCtr(year, term));
            }
        });

        popupViews.add(yearView);
        popupViews.add(termView);

        gradeList.setVerticalScrollBarEnabled(false);
        gradeList.setAdapter(gradeListAdapter);

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, gradeList);

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
                    CacheLoader.setGradeCtr(grades);
                    new GradeDAO(getContext()).addGrades(grades);
                    gradeListAdapter.updateData(getItemsFromCtr(year, term));
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

    public List<Grade> getItemsFromCtr(String year, String term) {
        List<Grade> grades = new ArrayList<>();
        for (Grade grade : CacheLoader.getGradeCtr()) {
            if (year.equals(grade.getYear()) && term.equals(grade.getTerm())) {
                grades.add(grade);
            }
        }
        return grades;
    }

    public  void getItemsFormDB() {
        GradeDAO dao = new GradeDAO(getContext());
        CacheLoader.setGradeCtr(dao.getGrades());
    }

    public String getItemsFromWeb() {
        String stringUrl = Home.BASE_URL + "/Score";
        List<NameValuePair> pairList = new ArrayList<>();
        pairList.add(new BasicNameValuePair("studentId", Info.getInstance().getStudentId()));
        pairList.add(new BasicNameValuePair("studentPassword", Info.getInstance().getPassword()));
//        return HttpUtils.get(stringUrl, pairList);
        return null;
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
