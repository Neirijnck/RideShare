package nmct.howest.be.rideshare.Activities.Models;

public class Message {
    //Fields
    private String userID = "";
    private String datetime = "";
    private String text = "";

    //Getters en setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //Ctor
    public Message(String userID, String datetime, String text) {
        this.userID = userID;
        this.datetime = datetime;
        this.text = text;
    }

    public Message() {
    }

}
