package com.xybst.net;

public class Home {

	//每页的条目数
	public static final int PAGE_SIZE = 30;

	//数字化校园登陆入口
	public static final String HOST_LOGINCOLLAGENETWORKS = "http://www.cqut.edu.cn/userLogin.aspx";

	//网络教学平台
	//网络教学平台登陆入口
	public static final String HOST_LOGINEOLNETWORKS = "http://eol.cqut.edu.cn/eol/homepage/common/opencourse/login.jsp";
	//课程列表
	public static final String HOST_COURSELIST = "http://eol.cqut.edu.cn/eol/lesson/student.lesson.list.jsp";
	//课程通知列表
	public static final String HOST_COURSENOTICELIST = "http://eol.cqut.edu.cn/eol/common/inform/index_stu.jsp?lid=0&s_order=0";
	//课程通知
	public static final String HOST_COURSENOTICEITEM = "http://eol.cqut.edu.cn/eol/common/inform/message_content.jsp?nid=";
	//课程作业列表
	public static final String HOST_COURSEHOMEWORKLIST = "http://eol.cqut.edu.cn/eol/common/hw/student/hwtask.jsp";
	//课程作业
	public static final String HOST_COURSEHOMEWORK = "http://eol.cqut.edu.cn/eol/common/hw/student/hwtask.view.jsp?hwtid=";
	//课程作业->提交
	public static final String HOST_COURSEHOMEWORKCOMMIT = "http://eol.cqut.edu.cn/eol/common/hw/student/write.jsp?hwtid=";
	//课程作业->查看结果
	public static final String HOST_COURSEHOMEWORKRESULT = "http://eol.cqut.edu.cn/eol/common/hw/student/taskanswer.jsp?hwtid=";
	//课程教师信息
	public static final String HOST_TRACHERINFOMATION = "http://eol.cqut.edu.cn/eol/lesson/teacher_info.jsp?lid=";
	//课程教学材料
	public static final String HOST_COURSEDATA = "http://eol.cqut.edu.cn/eol/common/script/courseResource.jsp?folderid=0&lid=19230&groupid=4";

	//新闻网
	//主页
	public static final String HOST_NEWS_HOMEPAGE = "http://news.cqut.edu.cn";
	//新闻列表访问入口
	public static final String HOST_ARTICLELIST = "http://news.cqut.edu.cn/Article/PageList";
	//搜索新闻访问入口
	public static final String HOST_SEARCHARTICLE = "http://news.cqut.edu.cn/Search";
	//头条新闻
	public static final String NEWSMODULE_HEADLINES = "/Home/Module/4a1cd010-b6ff-4f8e-9901-78dd5ec0ad38";
	//综合新闻
	public static final String NEWSMODULE_COLLIGATE = "/Home/Module/907f5d8b-06ca-4223-a77d-9d6d3e2b45dc";
	//学校通知
	public static final String NEWSMODULE_NOTIFICATION_COLLAGE ="/Home/Module/6f09b7e2-e71f-44b5-9d33-d2d09bfdbb25";
	//部门通知
	public static final String NEWSMODULE_NOTIFICATION_DEPARTMENT = "/Home/Module/e613ac6a-dc54-45f9-aab1-e9dfcfc528b0";
	//学生通知
	public static final String NEWSMODULE_NOTIFICATION_STUDENT = "/Home/Module/74dd1d56-78cf-4b6e-938b-b3db996758e6";
	//招生就业
	public static final String NEWSMODULE_RECRUITMENT_EMPLOYMENT = "/Home/Module/217fc75b-b935-449b-a7bc-8e887f7d4985";

}