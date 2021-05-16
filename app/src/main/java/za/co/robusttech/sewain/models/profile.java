package za.co.robusttech.sewain.models;

public class profile {

    private String email;
    private String id;
    private String imageURL;
    private String name;
    private String address;
    private String addressName;
    private String phone;
    private String addressPin;

    public profile(String email, String id, String imageURL, String name, String address, String addressName, String phone, String addressPin) {
        this.email = email;
        this.id = id;
        this.imageURL = imageURL;
        this.name = name;
        this.address = address;
        this.addressName = addressName;
        this.phone = phone;
        this.addressPin = addressPin;
    }

    public profile(){

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressPin() {
        return addressPin;
    }

    public void setAddressPin(String addressPin) {
        this.addressPin = addressPin;
    }
}