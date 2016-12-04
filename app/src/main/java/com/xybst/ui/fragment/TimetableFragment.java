package com.xybst.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xybst.dao.CourseDAO;
import com.xybst.net.HttpUtil;
import com.xybst.ui.activity.CourseDetailActivity;
import com.xybst.activity.R;
import com.xybst.bean.Course;
import com.xybst.utils.Info;
import com.xybst.ui.SelectWeekDialog;
import com.xybst.ui.layout.CourseLayout;
import com.xybst.ui.view.CourseView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TimetableFragment extends Fragment {

    public static final int DIALOG_FRAGMENT = 1;

    private int[] bg = {R.drawable.lesson_background_1, R.drawable.lesson_background_2, R.drawable.lesson_background_3, R.drawable.lesson_background_4,
            R.drawable.lesson_background_5, R.drawable.lesson_background_6, R.drawable.lesson_background_7, R.drawable.lesson_background_8};
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
            intent.putExtra("courseId", ((CourseView)v).getCourseId());
            startActivity(intent);
        }
    };
    Random random = new Random();
    private ProgressDialog progressDialog;
    private Info info;
    private CourseLayout layout;
    private TextView tvSelectedWeek;
    private int selectedWeek;
    private Toolbar toolbar;

    public TimetableFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = (Info) getActivity().getApplication();
        selectedWeek = info.getCurrentWeek();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        initWeekSelectView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        layout = (CourseLayout) view.findViewById(R.id.courses);
        if (info.getCourseContainer().isEmpty()) {
            progressDialog = ProgressDialog.show(getContext(), null, "获取课表中……");
            new BackgroundTask().execute();
        } else {
            initView();
        }
        return view;
    }

    private void initView() {
        layout.removeAllViews();
        int size = info.getCourseContainer().size();
        boolean flag[][][] = new boolean[11][11][11];
        for (int i = 0; i < size; i++) {
            Course course = info.getCourseContainer().get(i);
            if (selectedWeek >= course.getStartWeek() && course.getEndWeek() >= selectedWeek) {
                if ( ! flag[course.getDayOfWeek()][course.getStartSection()][course.getEndSection()])
                    flag[course.getDayOfWeek()][course.getStartSection()][course.getEndSection()] = true;
                else
                    continue;
                CourseView view = new CourseView(getContext().getApplicationContext());
                view.setCourseId(course.getCourseId());
                view.setStartSection(course.getStartSection());
                view.setEndSection(course.getEndSection());
                view.setDayOfWeek(course.getDayOfWeek());
                view.setText(course.getCourseName() + "@" + course.getClassroom());
                view.setBackgroundResource(bg[random.nextInt(bg.length)]);
                view.setTextColor(Color.BLACK);
                view.setTextSize(14);
                view.setGravity(Gravity.CENTER);
                view.setPadding(3, 3, 3, 3);
                view.setOnClickListener(listener);
                layout.addView(view);
            }
        }
    }

    private void initWeekSelectView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_selected_week, null);
        tvSelectedWeek = (TextView) view.findViewById(R.id.tvCurrentWeek);
        tvSelectedWeek.setText("第" + info.getCurrentWeek() + "周 ▾");
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        toolbar.addView(view, lp);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectWeekDialog dialog = SelectWeekDialog.newInstance(selectedWeek);
                dialog.setTargetFragment(TimetableFragment.this, DIALOG_FRAGMENT);
                dialog.show(getFragmentManager().beginTransaction(), "dialog");
            }
        });
    }

    private class BackgroundTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            getCourses(info.getStudentId(), info.getPassword());
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            initView();
        }
    }

    /**
     * 获取课程信息
     * */
    public void getCourses(String studentId, String password) {
        CourseDAO dbManage = new CourseDAO(getContext());
        if (dbManage.getCoursesCount() != 0) {
            loadCoursesFromDB();
        } else {
            loadCoursesFromWeb(studentId, password);
            saveCoursesInfo();
        }
    }

    public void loadCoursesFromDB() {
        CourseDAO dbManage = new CourseDAO(getContext());
        info.setCourseContainer(dbManage.getAllCourses());
    }

    public void loadCoursesFromWeb(String studentId, String password) {
        String stringUrl = HttpUtil.BASE_URL + "/Course";
        List<NameValuePair> pairList = new ArrayList<>();
        pairList.add(new BasicNameValuePair("studentId", studentId));
        pairList.add(new BasicNameValuePair("studentPassword", password));
        try {
            JSONObject jsonObject = new JSONObject(HttpUtil.get(stringUrl, pairList));
            info.setCourseContainer(getCourseList(jsonObject.getString("result")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveCoursesInfo() {
        CourseDAO dbManage = new CourseDAO(getContext());
        if(dbManage.addAllCourses(info.getCourseContainer())) {
            System.out.println("write courseDB successful");
        } else {
            System.out.println("write courseDB unsuccessful");
            Toast.makeText(getContext(), "写入不成功", Toast.LENGTH_SHORT).show();
        }
    }

    public static List<Course> getCourseList(String rawJsonResponse) throws JSONException {
        //{"courseName":"操作系统原理及应用 ","teacherName":"张金荣(张金荣)","classRoom":"6教0306","time":"周一第1,2节{第12-13周}"}
        List<Course> courses = new ArrayList<>();

        //周三第9,10节{第9-9周|单周}
        Pattern pWeekday = Pattern.compile("(?<=周).*?(?=第)");
        Pattern pStartSection = Pattern.compile("(?<=第).*(?=,)");
        Pattern pEndSection = Pattern.compile("(?<=,).*(?=节)");
        Pattern pStartWeek = Pattern.compile("(?<=\\{第).*(?=-)");
        Pattern pEndWeek = Pattern.compile("(?<=-).*?(?=周)");

        JSONObject jsonObject;
        JSONArray jsonArray = new JSONArray(rawJsonResponse);
        int courseId = 0;
        for(int i = 0; i < jsonArray.length(); i++) {
            jsonObject = (JSONObject) jsonArray.get(i);
            String courseName = jsonObject.getString("courseName");
            String teacherName = jsonObject.getString("teacherName");
            String classroom = jsonObject.getString("classRoom");
            String time = jsonObject.getString("time");
            int singleDoubleWeek = 0;
            if (time.contains("单周")) {
                singleDoubleWeek = 1;
            } else if (time.contains("双周")) {
                singleDoubleWeek = 2;
            }

            String dayOfWeek = null;
            String startSection = null;
            String endSection = null;
            String startWeek = null;
            String endWeek = null;
            Matcher m;

            m = pWeekday.matcher(time);
            if (m.find()) {
                dayOfWeek = m.group();
            }

            m = pStartSection.matcher(time);
            if (m.find()) {
                startSection = m.group();
            }

            m = pEndSection.matcher(time);
            if (m.find()) {
                endSection = m.group();
            }

            m = pStartWeek.matcher(time);
            if (m.find()) {
                startWeek = m.group();
            }

            m = pEndWeek.matcher(time);
            if (m.find()) {
                endWeek = m.group();
            }
            courses.add(new Course(++courseId, classroom, courseName, string2int(dayOfWeek), Integer.parseInt(endSection),
                    Integer.parseInt(endWeek), Integer.parseInt(startSection), Integer.parseInt(startWeek), teacherName, singleDoubleWeek));
        }
        for(Course c : courses) {
            System.out.println(c.toString());
        }
        return courses;
    }

    public static int string2int(String dayOfWeek) {
        if(dayOfWeek.equals("一")) {
            return 1;
        } else if(dayOfWeek.equals("二")) {
            return 2;
        } else if(dayOfWeek.equals("三")) {
            return 3;
        }else if(dayOfWeek.equals("四")) {
            return 4;
        }else if(dayOfWeek.equals("五")) {
            return 5;
        }else if(dayOfWeek.equals("六")) {
            return 6;
        }else if(dayOfWeek.equals("日")) {
            return 7;
        }
        return 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DIALOG_FRAGMENT) {
            selectedWeek = resultCode + 1;
            if (selectedWeek == info.getCurrentWeek()) {
                tvSelectedWeek.setText("第" + selectedWeek + "周 ▾");
            } else {
                tvSelectedWeek.setText("第" + selectedWeek + "周(非本周) ▾");
            }
            initView();
        }
    }

    @Override
    public void onDetach() {
        toolbar.removeView(getActivity().findViewById(R.id.tvCurrentWeek));
        super.onDetach();
    }
}