package com.helloworld.bihu_rewrite.DataClass;

/**
 * Created by Administrator on 2016/2/27.
 */
public class Question {
    private String id;
    private String title;
    private String content;
    private String date;
    private String recent;
    private String authorName;
    private String authorFace;

    public Question(String id, String title, String content, String date, String recent, String authorName, String authorFace) {
        this.authorFace = authorFace;
        this.authorName = authorName;
        this.content = content;
        this.date = date;
        this.id = id;
        this.recent = recent;
        this.title = title;
    }


    public String getAuthorFace() {
        return authorFace;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getRecent() {
        return recent;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return authorName+"在"+date+"发表了"+title+":"+content;
    }
}

/*
            "id": "70",
            "title": "为什么欧洲各国都在争夺人民币离岸中心？",
            "content": "最近在欧洲除了英国，德国法国也都加入人民币离岸中心的争夺，而早前在亚洲像新加坡，香港，日本等地区也有过类似的情况，各国为什么会如此看重？这对各国将产生什么影响？而对中国来说，又该如何选择？",
            "bestAnswerId": null,
            "date": "2015-06-08 19:38:33",
            "recent": "2015-06-10 15:17:04",
            "answerCount": "1",
            "authorId": "58",
            "authorName": "day",
            "authorFace": null
 */
