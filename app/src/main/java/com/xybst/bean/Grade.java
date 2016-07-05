package com.xybst.bean;

/**
 * Created by ben_29 on 12/26/15.
 */
public class Grade {

    public static final String YEAR = "year";
    public static final String TERM = "term";
    public static final String COURSENAME = "courseName";
    public static final String CREDIT = "credit";
    public static final String POINT = "point";
    public static final String SCORE = "score";

    //"学年"
    private String year;
    //学期"
    private String term;
    //"课程名称"
    private String courseName;
    //"学分" 
    private String credit;
    //"绩点" 
    private String point;
    //"成绩" 
    private String score;

    public Grade() {
    }

    public Grade(String courseName, String credit, String point, String score, String term, String year) {

        this.courseName = courseName;
        this.credit = credit;
        this.point = point;
        this.score = score;
        this.term = term;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "courseName='" + courseName + '\'' +
                ", year='" + year + '\'' +
                ", term='" + term + '\'' +
                ", credit='" + credit + '\'' +
                ", point='" + point + '\'' +
                ", score='" + score + '\'' +
                '}';
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
