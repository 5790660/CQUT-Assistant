package com.xybst.bean;

import com.xybst.util.NewsType;

import java.io.Serializable;

/**
 * 新闻列表
 * Created by 创宇 on 2015/12/5.
 */
public class NewsItem implements Serializable{

    public static final String ID = "_id";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String PUBLISHER = "publisher";
    public static final String TIME = "timeTable";

    private int id;
    private NewsType type;
    private String title;
    private String link;
    private String publisher;
    private String time;

    public NewsItem() {
    }

    public NewsItem(int id, NewsType type, String title, String link, String publisher, String time) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.link = link;
        this.publisher = publisher;
        this.time = time;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", publisher='" + publisher + '\'' +
                ", timeTable='" + time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NewsType getType() {
        return type;
    }

    public void setType(NewsType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}



