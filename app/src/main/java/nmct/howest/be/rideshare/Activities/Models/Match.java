package nmct.howest.be.rideshare.Activities.Models;

import java.util.List;

/**
 * Created by Sander on 5/11/2014.
 */
public class Match {
    //Fields
    private String userID = "";
    private String from = "";
    private String to = "";
    private String datetime = "";
    private List<Message> messages;
    private Integer status;

    //Getters en setters
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

    //Ctor
    public Match(String userID, String from, String to, String datetime, List<Message> messages, Integer status) {
        this.userID = userID;
        this.from = from;
        this.to = to;
        this.datetime = datetime;
        this.messages = messages;
        this.status = status;
    }
}