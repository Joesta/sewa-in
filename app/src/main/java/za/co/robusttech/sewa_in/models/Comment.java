package za.co.robusttech.sewa_in.models;

public class Comment {
    private String comment;
    private String publisher;
    private String commentid;
    private String commentTitle;
    private String productImageUrl;
    private float commentRatings;



    public Comment(String comment, String publisher, String commentid , String commentTitle, String productImageUrl, float commentRatings) {
        this.comment = comment;
        this.publisher = publisher;
        this.commentid = commentid;
        this.commentTitle = commentTitle;
        this.productImageUrl = productImageUrl;
        this.commentRatings = commentRatings;

    }

    public Comment() {
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public float getCommentRatings() {
        return commentRatings;
    }

    public void setCommentRatings(float commentRatings) {
        this.commentRatings = commentRatings;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }
}
