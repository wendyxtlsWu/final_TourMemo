package edu.neu.madcourse.tourmemo.model;

public class Notification {
    private String userId;
    private String text;
    private String postId;
    private boolean isPost;

    public Notification() {
    }

    public Notification(String userid, String text, String postid, boolean isPost) {
        this.userId = userid;
        this.text = text;
        this.postId = postid;
        this.isPost = isPost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userid) {
        this.userId = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isIsPost() {
        return isPost;
    }

    public void setIsPost(boolean post) {
        isPost = post;
    }
}
