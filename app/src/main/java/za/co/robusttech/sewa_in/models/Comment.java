package za.co.robusttech.sewa_in.models;

public class Comment {
    private String comment;
    private String publisher;
    private String commentid;
    private String commentTitle;

    public Comment(String comment, String publisher, String commentid , String commentTitle) {
        this.comment = comment;
        this.publisher = publisher;
        this.commentid = commentid;
        this.commentTitle = commentTitle;
    }

    public Comment() {
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
