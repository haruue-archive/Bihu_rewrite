package com.helloworld.bihu_rewrite.DataClass;

/**
 * Created by Administrator on 2016/2/27.
 */
public class Answer {
    private String id;
    private String content;
    private String date;
    private String authorId;
    private String authorName;
    private String authorFace;

    public String getAuthorFace() {
        return authorFace;
    }

    public String getAuthorId() {
        return authorId;
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

    public Answer() {
    }

    public Answer(String id, String content, String date, String authorId, String authorName,String authorFace) {
        this.authorFace = authorFace;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.date = date;
        this.id = id;
    }

    @Override
    public String toString() {
        return getAuthorName()+":"+getContent();
    }
/*
    {
                "id": "42",
                "content": "hehe",
                "date": "2015-05-02 16:49:14",
                "authorId": "21",
                "authorName": "zhs",
                "authorFace": null
            }
     */
}
