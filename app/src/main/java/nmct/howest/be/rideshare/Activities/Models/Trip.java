package nmct.howest.be.rideshare.Activities.Models;

import java.util.List;

public class Trip {
    //Fields
    private String ID = "";
    private String userID = "";
    private String from  ="";
    private String to = "";
    private String datetime = "";
    private String payment ="";
    private boolean[] repeat =  new boolean[7]; //mo, tu, we, th, fr, sa, su
    private List<Match> matches;
    private String facebookID="";

    //Getters en setters
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public boolean[] getRepeat() {
        return repeat;
    }

    public void setRepeat(boolean[] repeat) {
        this.repeat = repeat;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    //Ctor
    public Trip(String ID, String userID, String from, String to, String datetime, String payment, boolean[] repeat, List<Match> matches) {
        this.ID = ID;
        this.userID = userID;
        this.from = from;
        this.to = to;
        this.datetime = datetime;
        this.payment = payment;
        this.repeat = repeat;
        this.matches = matches;
    }

    public Trip() {
    }
}
