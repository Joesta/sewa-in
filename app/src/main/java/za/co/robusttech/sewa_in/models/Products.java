package za.co.robusttech.sewa_in.models;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Products implements Serializable {

    private String productName;
    private String productImage;
    private String productRatings;
    private String productPrice;
    private String productDeliveryTime;
    private String productDesciption;
    private String productDiscount;
    private String productCategory;
    private String productId;
    private String productSeller;
    private String productAvailability;
    private String productOriginalPrice;
    private String mKey;
    private String gridName;
    private String gridDescription;
    private String gridPrice;
    private String cartNum;



    public Products(){

    }

    public Products(String productName, String productImage, String productRatings, String productPrice, String productDeliveryTime, String productDesciption, String productDiscount, String productCategory, String productId, String productSeller, String productAvailability, String productOriginalPrice, String mKey, String gridName, String gridDescription, String gridPrice, String cartNum) {
        this.productName = productName;
        this.productImage = productImage;
        this.productRatings = productRatings;
        this.productPrice = productPrice;
        this.productDeliveryTime = productDeliveryTime;
        this.productDesciption = productDesciption;
        this.productDiscount = productDiscount;
        this.productCategory = productCategory;
        this.productId = productId;
        this.productSeller = productSeller;
        this.productAvailability = productAvailability;
        this.productOriginalPrice = productOriginalPrice;
        this.mKey = mKey;
        this.gridName = gridName;
        this.gridDescription = gridDescription;
        this.gridPrice = gridPrice;
        this.cartNum = cartNum;
    }

    public Products(String productImage, String productName, String productDesciption, String productPrice, String productId, String productCategory,
                    String productDiscount, String productDeliveryTime, String productRatings, String productSeller, String productAvailability, String productOriginalPrice ) {
        this.productImage = productImage;
        this.productName = productName;
        this.productDesciption = productDesciption;
        this.productPrice = productPrice;
        this.productId = productId;
        this.productCategory = productCategory;
        this.productDiscount = productDiscount;
        this.productDeliveryTime = productDeliveryTime;
        this.productRatings = productRatings;
        this.productSeller = productSeller;
        this.productAvailability = productAvailability;
        this.productOriginalPrice = productOriginalPrice;

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

    public String getProductSeller() {
        return productSeller;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public String getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(String productAvailability) {
        this.productAvailability = productAvailability;
    }

    public String getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public void setProductOriginalPrice(String productOriginalPrice) {
        this.productOriginalPrice = productOriginalPrice;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

    public String getGridDescription() {
        return gridDescription;
    }

    public void setGridDescription(String gridDescription) {
        this.gridDescription = gridDescription;
    }

    public String getGridPrice() {
        return gridPrice;
    }

    public void setGridPrice(String gridPrice) {
        this.gridPrice = gridPrice;
    }

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}