package com.xybst.util;

import java.util.Calendar;

/**
 * Created by 创宇 on 2017/1/6.
 */

public class TimeUtils {

    /**
     *获取用户入学时间
     *@return int year
     */
    public static int getStartYear() {
        return Integer.valueOf("20" + Info.getInstance().getStudentId().substring(1,2));
    }

    public static String[] getTerms() {
        return new String[]{"第1学期", "第2学期", "第3学期"};
    }

    /**
     *获取用户在校年份
     *@return String[] years
     */
    public static String[] getSchoolYear() {
        String[] years = new String[4];
        int srtYear = getStartYear();
        for (int i = 0; i < 4; i ++)
            years[i] = String.valueOf(srtYear + i) + "-" + String.valueOf(srtYear + i + 1);
        return years;
    }

    /**
     *获取用户当前学期
     *@return String curTerm;
     */
    public static String[] getCurTerm() {
        Calendar cal=Calendar.getInstance();
        String[] curTerm = new String[2];
        curTerm[0] = getSchoolYear()[cal.get(Calendar.YEAR) - getStartYear()];
        int month = cal.get(Calendar.MONTH ) + 1;
        curTerm[1] = month > 2 && month < 9 ? getTerms()[1] : getTerms()[0];
        return curTerm;
    }
}
