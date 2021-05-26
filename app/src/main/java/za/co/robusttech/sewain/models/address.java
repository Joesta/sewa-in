package za.co.robusttech.sewain.models;

public class address {

    private String id;
    private String fullName;
    private String mobileNumber;
    private String pinCode;
    private String flatHouse;
    private String areaColony;
    private String landMark;
    private String townCity;

    public address(String id, String fullName, String mobileNumber, String pinCode, String flatHouse, String areaColony, String landMark, String townCity) {
        this.id = id;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.pinCode = pinCode;
        this.flatHouse = flatHouse;
        this.areaColony = areaColony;
        this.landMark = landMark;
        this.townCity = townCity;
    }

    public address(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getFlatHouse() {
        return flatHouse;
    }

    public void setFlatHouse(String flatHouse) {
        this.flatHouse = flatHouse;
    }

    public String getAreaColony() {
        return areaColony;
    }

    public void setAreaColony(String areaColony) {
        this.areaColony = areaColony;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getTownCity() {
        return townCity;
    }

    public void setTownCity(String townCity) {
        this.townCity = townCity;
    }
}
