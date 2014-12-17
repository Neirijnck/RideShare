package nmct.howest.be.rideshare.Activities.Loaders.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Preben on 26/11/2014.
 */
public class Contract
{
    public static final String AUTHORITY = "nmct.howest.be.rideshare.contentprovider";

    //USER TABLE
    public interface UserColumns extends BaseColumns
    {
        public static final String KEY_API_ID="_id";
        public static final String KEY_USERID = "UserID";
        public static final String KEY_FACEBOOK_TOKEN = "FacebookToken";
        public static final String KEY_FACEBOOK_ID = "FacebookID";
        public static final String KEY_FACEBOOK_URL = "FacebookURL";
        public static final String KEY_FIRSTNAME = "FirstName";
        public static final String KEY_LASTNAME = "LastName";
        public static final String KEY_USERNAME = "UserName";
        public static final String KEY_EMAIL = "Email";
        public static final String KEY_GENDER = "Gender";
        public static final String KEY_BIRTHDAY = "Birthday";
        public static final String KEY_LOCATION = "Location";
        public static final String KEY_CARTYPE = "Cartype";
        public static final String KEY_AMOUNT_OF_SEATS = "AmountOfSeats";
    }

    public static final class User implements UserColumns
    {
        public static final String CONTENT_DIRECTORY = "User";
        public static final int USER_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nmct.howest.be.rideshare.contentprovider.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nmct.howest.be.rideshare.contentprovider.user";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }


    //REVIEW TABLE
    public interface ReviewColumns extends BaseColumns
    {
        public static final String KEY_API_ID="_id";
        public static final String KEY_USERID = "UserID";
        public static final String KEY_CREATED_ON = "DateTime";
        public static final String KEY_LAST_EDITED_ON = "DateTime";
        public static final String KEY_SCORE = "Score";
        public static final String KEY_TEXT = "Text";
        public static final String KEY_USER_ID = "User_ID";
    }

    public static final class Review implements ReviewColumns
    {
        public static final String CONTENT_DIRECTORY = "Review";
        public static final int REVIEW_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nmct.howest.be.rideshare.contentprovider.review";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nmct.howest.be.rideshare.contentprovider.review";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }


    //TRIP TABLE
    public interface TripColumns extends BaseColumns
    {
        public static final String KEY_API_ID="_id";
        public static final String KEY_USERID = "UserID";
        public static final String KEY_DATE_TIME = "DateTime";
        public static final String KEY_FROM = "From";
        public static final String KEY_TO = "To";
        public static final String KEY_PAYMENT = "Payment";
        public static final String KEY_REPEAT= "Repeat";
        public static final String KEY_USER_ID = "User_ID";
    }

    public static final class Trip implements TripColumns
    {
        public static final String CONTENT_DIRECTORY = "Trip";
        public static final int TRIP_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nmct.howest.be.rideshare.contentprovider.trip";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nmct.howest.be.rideshare.contentprovider.trip";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }


    //MATCH TABLE
    public interface MatchColumns extends BaseColumns
    {
        public static final String KEY_API_ID="_id";
        public static final String KEY_USERID = "UserID";
        public static final String KEY_DATE_TIME = "DateTime";
        public static final String KEY_FROM = "From";
        public static final String KEY_TO = "To";
        public static final String KEY_STATUS = "Status";
        public static final String KEY_TRIP_ID = "Trip_ID";
    }

    public static final class Match implements MatchColumns
    {
        public static final String CONTENT_DIRECTORY = "Match";
        public static final int MATCH_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nmct.howest.be.rideshare.contentprovider.match";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nmct.howest.be.rideshare.contentprovider.match";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }


    //MESSAGE TABLE
    public interface MessageColumns extends BaseColumns
    {
        public static final String KEY_API_ID="_id";
        public static final String KEY_USERID = "UserID";
        public static final String KEY_TEXT = "Text";
        public static final String KEY_DATE_TIME = "DateTime";
        public static final String KEY_MATCH_ID = "Match_ID";
    }

    public static final class Message implements MessageColumns
    {
        public static final String CONTENT_DIRECTORY = "Message";
        public static final int MESSAGE_ID_PATH_POSITION = 1;
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.nmct.howest.be.rideshare.contentprovider.message";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.nmct.howest.be.rideshare.contentprovider.message";
        public static final String ITEM_CONTENT_PATH = "/" + CONTENT_DIRECTORY +"/";
        public static final Uri ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + ITEM_CONTENT_PATH);
        public static final String CONTENT_PATH = "/" + CONTENT_DIRECTORY;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + CONTENT_PATH);
    }

}
