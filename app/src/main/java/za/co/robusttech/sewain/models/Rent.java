package za.co.robusttech.sewain.models;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Rent {

    private String firstRentTime;
    private String secondRentTime;
    private String thirdRentTime;
    private String fourthRentTime;
    private String fifthRentTime;
    private String sixthRentTime;
    private String seventhRentTime;
    private String eighthRentTime;
    private String ninthRentTime;
    private String tenthRentTime;
    private String id;
    private String productId;
    private String productPrice;
    private String productName;
    private String amountPayed;
    private String perRent;
    private String amountBalance;


    public Rent() {

    }

    public Rent(String firstRentTime, String secondRentTime, String thirdRentTime, String fourthRentTime, String fifthRentTime, String sixthRentTime, String seventhRentTime, String eighthRentTime, String ninthRentTime, String tenthRentTime, String id, String productId, String productPrice, String productName, String amountPayed, String perRent, String amountBalance) {
        this.firstRentTime = firstRentTime;
        this.secondRentTime = secondRentTime;
        this.thirdRentTime = thirdRentTime;
        this.fourthRentTime = fourthRentTime;
        this.fifthRentTime = fifthRentTime;
        this.sixthRentTime = sixthRentTime;
        this.seventhRentTime = seventhRentTime;
        this.eighthRentTime = eighthRentTime;
        this.ninthRentTime = ninthRentTime;
        this.tenthRentTime = tenthRentTime;
        this.id = id;
        this.productId = productId;
        this.productPrice = productPrice;
        this.productName = productName;
        this.amountPayed = amountPayed;
        this.perRent = perRent;
        this.amountBalance = amountBalance;
    }

    public String getFirstRentTime() {
        return firstRentTime;
    }

    public void setFirstRentTime(String firstRentTime) {
        this.firstRentTime = firstRentTime;
    }

    public String getSecondRentTime() {
        return secondRentTime;
    }

    public void setSecondRentTime(String secondRentTime) {
        this.secondRentTime = secondRentTime;
    }

    public String getThirdRentTime() {
        return thirdRentTime;
    }

    public void setThirdRentTime(String thirdRentTime) {
        this.thirdRentTime = thirdRentTime;
    }

    public String getFourthRentTime() {
        return fourthRentTime;
    }

    public void setFourthRentTime(String fourthRentTime) {
        this.fourthRentTime = fourthRentTime;
    }

    public String getFifthRentTime() {
        return fifthRentTime;
    }

    public void setFifthRentTime(String fifthRentTime) {
        this.fifthRentTime = fifthRentTime;
    }

    public String getSixthRentTime() {
        return sixthRentTime;
    }

    public void setSixthRentTime(String sixthRentTime) {
        this.sixthRentTime = sixthRentTime;
    }

    public String getSeventhRentTime() {
        return seventhRentTime;
    }

    public void setSeventhRentTime(String seventhRentTime) {
        this.seventhRentTime = seventhRentTime;
    }

    public String getEighthRentTime() {
        return eighthRentTime;
    }

    public void setEighthRentTime(String eighthRentTime) {
        this.eighthRentTime = eighthRentTime;
    }

    public String getNinthRentTime() {
        return ninthRentTime;
    }

    public void setNinthRentTime(String ninthRentTime) {
        this.ninthRentTime = ninthRentTime;
    }

    public String getTenthRentTime() {
        return tenthRentTime;
    }

    public void setTenthRentTime(String tenthRentTime) {
        this.tenthRentTime = tenthRentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmountPayed() {
        return amountPayed;
    }

    public void setAmountPayed(String amountPayed) {
        this.amountPayed = amountPayed;
    }

    public String getPerRent() {
        return perRent;
    }

    public void setPerRent(String perRent) {
        this.perRent = perRent;
    }

    public String getAmountBalance() {
        return amountBalance;
    }

    public void setAmountBalance(String amountBalance) {
        this.amountBalance = amountBalance;
    }
}
