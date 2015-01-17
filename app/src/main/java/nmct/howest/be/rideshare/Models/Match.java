package nmct.howest.be.rideshare.Models;

import java.util.List;

public class Match {
    //Fields
    private String id ="";
    private String userID = "";
    private String from = "";
    private String to = "";
    private String datetime = "";
    private List<Message> messages;
    private Integer status;
    private String facebookID="";

    //Getters en setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    //Ctor
    public Match(String userID, String from, String to, String datetime, List<Message> messages, Integer status) {
        this.userID = userID;
        this.from = from;
        this.to = to;
        this.datetime = datetime;
        this.messages = messages;
        this.status = status;
    }

    public Match() {
    }

}
