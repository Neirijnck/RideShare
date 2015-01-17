package nmct.howest.be.rideshare.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import nmct.howest.be.rideshare.Loaders.Database.Contract;

public class SQLHelper extends SQLiteOpenHelper {

    //Variables
    private static SQLHelper INSTANCE;
    private static Object lock = new Object();

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DB_VERSION = 1;

    //Database Name
    private static final String DB_NAME = "sharemyride.db";


    // Table Create Statements
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + Contract.User.CONTENT_DIRECTORY + "("
            + Contract.UserColumns._ID + " INTEGER PRIMARY KEY,"
            + Contract.UserColumns.KEY_API_ID + "TEXT,"
            + Contract.UserColumns.KEY_USERID + " TEXT,"
            + Contract.UserColumns.KEY_FACEBOOK_TOKEN + " TEXT,"
            + Contract.UserColumns.KEY_FACEBOOK_ID + " TEXT,"
            + Contract.UserColumns.KEY_FACEBOOK_URL + " TEXT,"
            + Contract.UserColumns.KEY_FIRSTNAME + " TEXT,"
            + Contract.UserColumns.KEY_LASTNAME + " TEXT,"
            + Contract.UserColumns.KEY_USERNAME + " TEXT,"
            + Contract.UserColumns.KEY_EMAIL + " TEXT,"
            + Contract.UserColumns.KEY_GENDER + " TEXT,"
            + Contract.UserColumns.KEY_BIRTHDAY + " TEXT,"
            + Contract.UserColumns.KEY_LOCATION + " TEXT,"
            + Contract.UserColumns.KEY_CARTYPE + " TEXT,"
            + Contract.UserColumns.KEY_AMOUNT_OF_SEATS + " TEXT"
            + ")";

    private static final String CREATE_TABLE_REVIEW ="CREATE TABLE "
            + Contract.Review.CONTENT_DIRECTORY + "("
            + Contract.ReviewColumns._ID + " INTEGER PRIMARY KEY,"
            + Contract.ReviewColumns.KEY_API_ID + "TEXT,"
            + Contract.ReviewColumns.KEY_USERID+ " TEXT,"
            + Contract.ReviewColumns.KEY_CREATED_ON + " DATETIME,"
            + Contract.ReviewColumns.KEY_LAST_EDITED_ON + " DATETIME,"
            + Contract.ReviewColumns.KEY_SCORE + " INTEGER,"
            + Contract.ReviewColumns.KEY_TEXT + " TEXT,"
            + Contract.ReviewColumns.KEY_USER_ID + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_TRIP ="CREATE TABLE "
            + Contract.Trip.CONTENT_DIRECTORY + "("
            + Contract.TripColumns._ID + " INTEGER PRIMARY KEY,"
            + Contract.TripColumns.KEY_API_ID + "TEXT,"
            + Contract.TripColumns.KEY_USERID + " TEXT,"
            + Contract.TripColumns.KEY_FROM + " TEXT,"
            + Contract.TripColumns.KEY_TO + " TEXT,"
            + Contract.TripColumns.KEY_DATE_TIME + " DATETIME,"
            + Contract.TripColumns.KEY_PAYMENT + " TEXT,"
            + Contract.TripColumns.KEY_REPEAT + " TEXT,"
            + Contract.TripColumns.KEY_USER_ID + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_MATCH ="CREATE TABLE "
            + Contract.Match.CONTENT_DIRECTORY + "("
            + Contract.MatchColumns._ID + " INTEGER PRIMARY KEY,"
            + Contract.MatchColumns.KEY_API_ID + "TEXT,"
            + Contract.MatchColumns.KEY_USERID + " TEXT,"
            + Contract.MatchColumns.KEY_FROM + " TEXT,"
            + Contract.MatchColumns.KEY_TO + " TEXT,"
            + Contract.MatchColumns.KEY_DATE_TIME + " DATETIME,"
            + Contract.MatchColumns.KEY_STATUS + " INTEGER,"
            + Contract.MatchColumns.KEY_TRIP_ID + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_MESSAGE ="CREATE TABLE "
            + Contract.Message.CONTENT_DIRECTORY + "("
            + Contract.MessageColumns._ID + " INTEGER PRIMARY KEY,"
            + Contract.MessageColumns.KEY_API_ID + "TEXT,"
            + Contract.MessageColumns.KEY_USERID + " TEXT,"
            + Contract.MessageColumns.KEY_TEXT + " TEXT,"
            + Contract.MessageColumns.KEY_DATE_TIME + " DATETIME,"
            + Contract.MessageColumns.KEY_MATCH_ID + " INTEGER"
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
        Log.e(LOG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE " + Contract.User.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE " + Contract.Review.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE " + Contract.Trip.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE " + Contract.Match.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE " + Contract.Message.CONTENT_DIRECTORY);

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_REVIEW);
        db.execSQL(CREATE_TABLE_TRIP);
        db.execSQL(CREATE_TABLE_MATCH);
        db.execSQL(CREATE_TABLE_MESSAGE);
    }

}
