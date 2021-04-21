package edu.neu.madcourse.tourmemo.model;

public class Post {

    private String zipcode;
    private String spotMame;
    private String description;
    private String postId;
    private String publisher;
    private String imageUrl;
    
    public Post() {
    }
    
    public Post(String zipcode, String description, String postId, String publisher,
                String imageUrl, String spotMame) {
        this.spotMame = spotMame;
        this.zipcode = zipcode;
        this.description = description;
        this.postId = postId;
        this.publisher = publisher;
        this.imageUrl = imageUrl;
    }

    public String getSpotMame() {
        return spotMame;
    }

    public void setSpotMame(String spotMame) {
        this.spotMame = spotMame;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
