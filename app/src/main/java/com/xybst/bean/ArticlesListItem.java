package com.xybst.bean;

import java.io.Serializable;

/**
 * 新闻列表
 * Created by 创宇 on 2015/12/5.
 */
public class ArticlesListItem implements Serializable{

    public static final String ID = "_id";
    public static final String MODULE = "module";
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String PUBLISHER = "publisher";
    public static final String TIME = "time";

    private int id;
    private String module;
    private String title;
    private String link;
    private String publisher;
    private String time;

    public ArticlesListItem() {

    }

    public ArticlesListItem(Article article) {
        this.link = article.getLink();
        this.publisher = article.getPublisher();
        this.time = article.getCreateTime();
        this.title = article.getTitle();
    }

    public ArticlesListItem(int id, String link, String module, String publisher, String time, String title) {
        this.id = id;
        this.link = link;
        this.module = module;
        this.publisher = publisher;
        this.time = time;
        this.title = title;
    }

      @Override
    public String toString() {
        return "ArticlesListItem{" +
                "id=" + id +
                ", module='" + module + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", publisher='" + publisher + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}



