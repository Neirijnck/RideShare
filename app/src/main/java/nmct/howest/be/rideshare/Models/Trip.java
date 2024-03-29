package nmct.howest.be.rideshare.Models;

import java.util.Comparator;
import java.util.List;

public class Trip {
    //Fields
    private String ID = "";
    private String userID = "";
    private String from  = "";
    private String fromLoc  = "";
    private String to = "";
    private String toLoc  = "";
    private String datetime = "";
    private String payment ="";
    private boolean[] repeat =  new boolean[7]; //mo, tu, we, th, fr, sa, su
    private List<Match> matches;
    private String type ="";
    private String facebookID="";
    private String userName="";

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

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFromLoc() { return fromLoc; }

    public void setFromLoc(String fromLoc) { this.fromLoc = fromLoc; }

    public String getToLoc() { return toLoc; }

    public void setToLoc(String toLoc) { this.toLoc = toLoc;}

    //Ctor
    public Trip(String ID, String userID, String from, String fromLoc, String to, String toLoc, String datetime, String payment, boolean[] repeat, List<Match> matches) {
        this.ID = ID;
        this.userID = userID;
        this.from = from;
        this.to = to;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.datetime = datetime;
        this.payment = payment;
        this.repeat = repeat;
        this.matches = matches;
    }

    public Trip() {}

    public static class compareToType implements Comparator
    {
        @Override
        public int compare(Object lhs, Object rhs) {
            Trip trip1 = (Trip) lhs;
            Trip trip2 = (Trip) rhs;
            return trip1.type.compareTo(trip2.type);
        }
    }

    public static class compareToDate implements Comparator
    {
        @Override
        public int compare(Object lhs, Object rhs) {
            Trip trip1 = (Trip) lhs;
            Trip trip2 = (Trip) rhs;
            return trip1.datetime.compareTo(trip2.datetime);
        }
    }

}
