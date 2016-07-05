package com.xybst.bean;

/**
 * Created by 创宇 on 2015/12/20.
 */

public class Course {

    public static final String COURSE_ID = "courseId";
    public static final String COURSE_NAME = "courseName";
    public static final String TEACHER_NAME= "teacherName";
    public static final String CLASSROOM = "classroom";
    public static final String DAY_OF_WEEK = "dayOfWeek";
    public static final String START_WEEK = "startWeek";
    public static final String END_WEEK = "endWeek";
    public static final String START_SECTION = "startSection";
    public static final String END_SECTION = "endSection";
    public static final String SINGLE_DOUBLE_WEEK = "singleDoubleWeek";

    private int courseId;
    private String courseName;
    private String teacherName;
    private String classroom;
    private int dayOfWeek;
    private int startWeek;
    private int endWeek;
    private int startSection;
    private int endSection;
    private int singleDoubleWeek = 0;

    public Course() {
    }


    public Course(int courseId, String classroom, String courseName, int dayOfWeek,
                  int endSection, int endWeek, int startSection, int startWeek, String teacherName, int singleDoubleWeek) {
        this.courseId = courseId;
        this.classroom = classroom;
        this.courseName = courseName;
        this.dayOfWeek = dayOfWeek;
        this.endSection = endSection;
        this.endWeek = endWeek;
        this.startSection = startSection;
        this.startWeek = startWeek;
        this.teacherName = teacherName;
        this.singleDoubleWeek = singleDoubleWeek;

    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getEndSection() {
        return endSection;
    }

    public void setEndSection(int endSection) {
        this.endSection = endSection;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public int getStartSection() {
        return startSection;
    }

    public void setStartSection(int startSection) {
        this.startSection = startSection;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getSingleDoubleWeek() {
        return singleDoubleWeek;
    }

    public void setSingleDoubleWeek(int singleDoubleWeek) {
        this.singleDoubleWeek = singleDoubleWeek;
    }

    @Override
    public String toString() {
        return "Course{" +
                "classroom='" + classroom + '\'' +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", startSection=" + startSection +
                ", endSection=" + endSection +
                ", singleDoubleWeek=" + singleDoubleWeek +
                '}';
    }
}
