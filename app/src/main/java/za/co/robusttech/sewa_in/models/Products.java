package za.co.robusttech.sewa_in.models;

public class Products {

    private String productName;
    private String productImage;
    private String productRatings;
    private String productPrice;
    private String productDeliveryTime;
    private String productDesciption;
    private String productDiscount;
    private String productCategory;
    private String productId;

    public Products(String productName, String productImage, String productRatings, String productPrice, String productDeliveryTime, String productDesciption, String productDiscount, String productCategory, String productId) {
        this.productName = productName;
        this.productImage = productImage;
        this.productRatings = productRatings;
        this.productPrice = productPrice;
        this.productDeliveryTime = productDeliveryTime;
        this.productDesciption = productDesciption;
        this.productDiscount = productDiscount;
        this.productCategory = productCategory;
        this.productId = productId;
    }

    public Products(){

    }

    public Products(String productImage, String productName, String productDesciption, String productPrice) {

        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productDesciption = productDesciption;

    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductRatings() {
        return productRatings;
    }

    public void setProductRatings(String productRatings) {
        this.productRatings = productRatings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDeliveryTime() {
        return productDeliveryTime;
    }

    public void setProductDeliveryTime(String productDeliveryTime) {
        this.productDeliveryTime = productDeliveryTime;
    }

    public String getProductDesciption() {
        return productDesciption;
    }

    public void setProductDesciption(String productDesciption) {
        this.productDesciption = productDesciption;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}