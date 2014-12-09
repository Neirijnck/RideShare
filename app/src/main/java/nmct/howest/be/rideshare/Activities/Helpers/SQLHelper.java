package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLHelper extends SQLiteOpenHelper {

    private static SQLHelper INSTANCE;
    private static Object lock = new Object();

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DB_VERSION = 1;

    //Database Name
    private static final String DB_NAME = "sharemyride.db";

    //Table Names
    private static final String TABLE_USER = "User";
    private static final String TABLE_REVIEW = "Review";
    private static final String TABLE_TRIP = "Trip";
    private static final String TABLE_MATCH = "Match";
    private static final String TABLE_MESSAGE = "Message";

    // Common column names
    private static final String KEY_ID = "ID";
    private static final String KEY_USERID = "UserID";
    private static final String KEY_USER_ID = "User_ID";
    private static final String KEY_DATE_TIME = "DateTime";
    private static final String KEY_FROM = "From";
    private static final String KEY_TO = "To";
    private static final String KEY_TEXT = "Text";

    // USER Table - column names
    private static final String KEY_FACEBOOK_TOKEN = "FacebookToken";
    private static final String KEY_FACEBOOK_ID = "FacebookID";
    private static final String KEY_FACEBOOK_URL = "FacebookURL";
    private static final String KEY_FIRSTNAME = "FirstName";
    private static final String KEY_LASTNAME = "LastName";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_GENDER = "Gender";
    private static final String KEY_BIRTHDAY = "Birthday";
    private static final String KEY_LOCATION = "Location";
    private static final String KEY_CARTYPE = "Cartype";
    private static final String KEY_AMOUNT_OF_SEATS = "AmountOfSeats";

    // REVIEW Table - column names
    private static final String KEY_SCORE = "Score";

    // TRIP Table - column names
    private static final String KEY_TRIPID = "TripID";
    private static final String KEY_PAYMENT = "Payment";
    private static final String KEY_REPEAT= "Repeat";

    // MATCH Table - column names
    private static final String KEY_STATUS = "Status";
    private static final String KEY_TRIP_ID = "Trip_ID";

    // MESSAGE Table - column names
    private static final String KEY_MATCH_ID = "Match_ID";


    // Table Create Statements
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERID
            + " TEXT," + KEY_FACEBOOK_TOKEN + " TEXT," + KEY_FACEBOOK_ID + " TEXT" + KEY_FACEBOOK_URL + " TEXT,"
            + KEY_FIRSTNAME + " TEXT,"+ KEY_LASTNAME + " TEXT,"+ KEY_EMAIL + " TEXT,"+ KEY_GENDER + " TEXT,"
            + KEY_BIRTHDAY + " TEXT,"+ KEY_LOCATION + " TEXT,"+ KEY_CARTYPE + " TEXT,"+ KEY_AMOUNT_OF_SEATS + " TEXT,"
            + ")";

    private static final String CREATE_TABLE_REVIEW ="CREATE TABLE "
            + TABLE_REVIEW + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERID
            + " TEXT," + KEY_DATE_TIME + " DATETIME," + KEY_SCORE + " INTEGER" + KEY_TEXT + " TEXT" + KEY_USER_ID + " INTEGER"+ ")";

    private static final String CREATE_TABLE_TRIP ="CREATE TABLE "
            + TABLE_TRIP + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TRIP_ID + " TEXT," + KEY_USERID + " TEXT,"
            + KEY_FROM + " TEXT" + KEY_TO + " TEXT"+ KEY_DATE_TIME + " DATETIME"+ KEY_PAYMENT + " TEXT"
            + KEY_REPEAT + " TEXT"+ KEY_USER_ID + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_MATCH ="CREATE TABLE "
            + TABLE_MATCH + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERID + " TEXT," + KEY_FROM + " TEXT,"
            + KEY_TO + " TEXT" + KEY_DATE_TIME + " DATETIME"+ KEY_STATUS + " INTEGER"+ KEY_TRIP_ID + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_MESSAGE ="CREATE TABLE "
            + TABLE_MESSAGE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERID + " TEXT,"
            + KEY_TEXT + " TEXT," + KEY_DATE_TIME + " DATETIME" + KEY_MATCH_ID + " INTEGER,"
            + ")";


    public SQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SQLHelper getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (lock) {
                if(INSTANCE == null) {
                    INSTANCE = new SQLHelper(context.getApplicationContext());
                }
            }
        }

        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + TABLE_REVIEW);
        db.execSQL("DROP TABLE " + TABLE_TRIP);
        db.execSQL("DROP TABLE " + TABLE_MATCH);
        db.execSQL("DROP TABLE " + TABLE_MESSAGE);

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_REVIEW);
        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_MATCH);
        db.execSQL(CREATE_TABLE_MESSAGE);
    }


}
