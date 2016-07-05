package com.xybst.bean;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Article {

    private String title;
    private String author;
    private String publisher;
    private String createTime;
    private String deadlineTime;
    private String content;
    private String link;

    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String PUBLISHER = "publisher";
    public static final String CREATETIME = "createTime";
    public static final String DEADLINETIME = "deadlineTime";
    public static final String CONTENT = "content";
    public static final String LINK = "link";

    public Article(InputStream input) throws IOException {

        StringBuilder buff = new StringBuilder();
        String html;

        Scanner scanner = new Scanner(input);
        while(scanner.hasNextLine()) {
            buff.append(scanner.nextLine());
        }

        html = buff.toString();

        input.close();

        Pattern p;
        Matcher m;
        String regEx;

        regEx = "(?<=<span class=\"title\".).*(?=</span></h3>)";
        p = Pattern.compile(regEx);
        m = p.matcher(html);
        if (m.find())
            this.title = m.group();

        System.out.println(title);

        regEx = "(?<=发文单位：).*(?=</span><span class=\"author\">)";
        p = Pattern.compile(regEx);
        m = p.matcher(html);
        if (m.find())
            this.publisher = m.group();

        System.out.println(publisher);

        regEx = "(?<=作者：).*(?=</span><span>)";
        p = Pattern.compile(regEx);
        m = p.matcher(html);
        if (m.find())
            this.author = m.group();

        System.out.println(author);

        regEx = "(?<=</span><span>创建时间：).*(?=</span>\\s*<span>过期时间)";
        p = Pattern.compile(regEx);
        m = p.matcher(html);
        if (m.find())
            this.createTime = m.group();

//		System.out.println(createTime);
//		
//		regEx = "(?<=<span.过期时间：).*(?=</span>\\s*</div>\\s*<div class=\"tags\">)";
//		p = Pattern.compile(regEx);
//		m = p.matcher(html);
//		if (m.find())
//			this.deadlineTime = m.group();
//		
//		System.out.println(deadlineTime);

        regEx = "(?<=<div class=\"content\".).*(?=</div>\\s*<div class=\"memo\">)";
        p = Pattern.compile(regEx);
        m = p.matcher(html);
        if (m.find())
            this.content = m.group();

        System.out.println(content);

    }

    public Article() {

    }

    public Article(String title, String publisher, String content) {

        this(title, null, publisher, null, null, content);

    }

    public Article(String title, String author, String publisher,
                   String createTime, String deadlineTime, String content) {

        super();
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.createTime = createTime;
        this.deadlineTime = deadlineTime;
        this.content = content;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString() {
        return this.title;
    }

}