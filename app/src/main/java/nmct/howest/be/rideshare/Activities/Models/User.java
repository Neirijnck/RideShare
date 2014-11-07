package nmct.howest.be.rideshare.Activities.Models;

import java.util.List;

public class User {
    //Fields
    private String ID = "";
    private String userName = "";
    private String facebookToken  ="";
    private String facebookID = "";
    private String facebookLink  ="";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String gender = "";
    private String birthday ="";
    private String location = "";
    private String carType = "";
    private String amountOfSeats ="";
    private List<Review> reviews;


    //Getters and setters
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getAmountOfSeats() {
        return amountOfSeats;
    }

    public void setAmountOfSeats(String amountOfSeats) {
        this.amountOfSeats = amountOfSeats;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    //Ctor
    public User(String ID, String userName, String facebookToken, String facebookID, String facebookLink, String firstName, String lastName, String email, String gender, String birthday, String location, String carType, String amountOfSeats, List<Review> reviews) {
        this.ID = ID;
        this.userName = userName;
        this.facebookToken = facebookToken;
        this.facebookID = facebookID;
        this.facebookLink = facebookLink;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.location = location;
        this.carType = carType;
        this.amountOfSeats = amountOfSeats;
        this.reviews = reviews;
    }
}
