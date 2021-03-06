package com.xybst.ui.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xybst.activity.R;
import com.xybst.bean.Course;
import com.xybst.bean.Grade;
import com.xybst.persistence.CourseDAO;
import com.xybst.persistence.GradeDAO;
import com.xybst.ui.fragment.TimetableFragment;
import com.xybst.util.CacheLoader;
import com.xybst.util.Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginDialog extends DialogFragment {

    @BindView(R.id.editTextId)
    EditText etStudentId;
    @BindView(R.id.editTextPassword)
    EditText etPassword;
    ProgressDialog progressDialog;
    Info info;

    public static LoginDialog newInstance(){
        return new LoginDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.dialog_login, null);
        ButterKnife.bind(this, view);
        info = (Info) getActivity().getApplication();
        return new AlertDialog.Builder(getContext()).setTitle("  登录").setView(view).create();
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        if (isPasswordValid(etPassword.getText().toString())) {
            progressDialog = ProgressDialog.show(getContext(), null, "登录中，请稍后……");
            new LoginTask().execute();
        }
    }

    @OnClick(R.id.btnCancel)
    public void cancel(){
        getDialog().dismiss();
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO: 2017/1/4
      /*      String stringUrl = HttpUtils.BASE_URL + "/ALL";
            List<NameValuePair> pairList = new ArrayList<>();
            pairList.add(new BasicNameValuePair("studentId", etStudentId.getText().toString()));
            pairList.add(new BasicNameValuePair("studentPassword", etPassword.getText().toString()));
            return HttpUtils.doGet(stringUrl, pairList);*/
      return null;
        }

       @Override
        protected void onPostExecute(String result) {
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
                    //获取姓名
                    jsonObject = jsonObject.getJSONObject("result");
                    saveUserInfo(jsonObject.getString("name"));
                    //获取成绩
                    List<Grade> grades = new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(jsonObject.getString("score"));
                    Log.d("jsonArray",jsonArray.length()+"");
                    for(int i = 0 ; i < jsonArray.length() ; i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        grades.add(new Grade(object.getString("className"), object.getString("credit"), object.getString("point"),
                                object.getString("grade"), object.getString("stuPeriod"), object.getString("stuYear")));
                    }
                    CacheLoader.setGradeCtr(grades);
                    new GradeDAO(getContext()).addGrades(grades);
                    //获取课表
                    List<Course> courses = TimetableFragment.getCourseList(jsonObject.getString("course"));
                    CacheLoader.setCourseCtr(courses);
                    new CourseDAO(getContext()).addAllCourses(courses);

                    getDialog().dismiss();
                    getActivity().recreate();
                    break;
                case 400:
                    Toast.makeText(getContext(), "校园网暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
                case 403:
                    Toast.makeText(getContext(), "学号或密码错误", Toast.LENGTH_SHORT).show();
                    break;
                case 406:
                    Toast.makeText(getContext(), "参数错误", Toast.LENGTH_SHORT).show();
                    break;
                case 500:
                    Toast.makeText(getContext(), "服务器暂时不可用", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveUserInfo(String studentName) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Info.PREFERENCES_USER_INFO, Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.putString("studentId", etStudentId.getText().toString());
        editor.putString("password", etPassword.getText().toString());
        editor.putString("studentName", studentName);
        editor.apply();
        info.setStudentId(etStudentId.getText().toString());
        info.setPassword(etPassword.getText().toString());
        info.setStudentName(studentName);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }
}
