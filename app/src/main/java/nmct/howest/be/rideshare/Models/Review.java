package nmct.howest.be.rideshare.Models;

public class Review {
    //Fields
    private String userID = "";
    private String userName="";
    private String date = "";
    private Integer score;
    private String text = "";


    //Getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    //Ctor
    public Review(String userID, String userName, String date, Integer score, String text) {
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.score = score;
        this.text = text;
    }

    public Review() {
    }

}