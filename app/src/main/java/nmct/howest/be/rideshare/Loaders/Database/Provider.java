package nmct.howest.be.rideshare.Loaders.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

import nmct.howest.be.rideshare.Helpers.SQLHelper;

public class Provider extends ContentProvider
{
    private static HashMap<String, String> sUsersProjectionMap;
    private static HashMap<String, String> sReviewsProjectionMap;
    private static HashMap<String, String> sTripsProjectionMap;
    private static HashMap<String, String> sMatchesProjectionMap;
    private static HashMap<String, String> sMessagesProjectionMap;

    private static final UriMatcher sUriMatcher;
    private static final int USERS = 1;
    private static final int USER_ID = 2;
    private static final int REVIEWS = 3;
    private static final int REVIEW_ID = 4;
    private static final int TRIPS = 5;
    private static final int TRIP_ID = 6;
    private static final int MATCHES = 7;
    private static final int MATCH_ID = 8;
    private static final int MESSAGES = 9;
    private static final int MESSAGE_ID = 10;

    private SQLHelper mOpenHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(Contract.AUTHORITY, "User", USERS);
        sUriMatcher.addURI(Contract.AUTHORITY, "User/*", USER_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, "Review", REVIEWS);
        sUriMatcher.addURI(Contract.AUTHORITY, "Review/*", REVIEW_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, "Trip", TRIPS);
        sUriMatcher.addURI(Contract.AUTHORITY, "Trip/*", TRIP_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, "Match", MATCHES);
        sUriMatcher.addURI(Contract.AUTHORITY, "Match/*", MATCH_ID);
        sUriMatcher.addURI(Contract.AUTHORITY, "Message", MESSAGES);
        sUriMatcher.addURI(Contract.AUTHORITY, "Message/*", MESSAGE_ID);

        sUsersProjectionMap = new HashMap<String, String>();
        sUsersProjectionMap.put(Contract.User._ID, Contract.User._ID);
        sUsersProjectionMap.put(Contract.User.KEY_API_ID, Contract.User.KEY_API_ID);
        sUsersProjectionMap.put(Contract.User.KEY_USERID, Contract.User.KEY_USERID);
        sUsersProjectionMap.put(Contract.User.KEY_FACEBOOK_TOKEN, Contract.User.KEY_FACEBOOK_TOKEN);
        sUsersProjectionMap.put(Contract.User.KEY_FACEBOOK_ID, Contract.User.KEY_FACEBOOK_ID);
        sUsersProjectionMap.put(Contract.User.KEY_FACEBOOK_URL, Contract.User.KEY_FACEBOOK_URL);
        sUsersProjectionMap.put(Contract.User.KEY_FIRSTNAME, Contract.User.KEY_FIRSTNAME);
        sUsersProjectionMap.put(Contract.User.KEY_LASTNAME, Contract.User.KEY_LASTNAME);
        sUsersProjectionMap.put(Contract.User.KEY_USERNAME, Contract.User.KEY_USERNAME);
        sUsersProjectionMap.put(Contract.User.KEY_EMAIL, Contract.User.KEY_EMAIL);
        sUsersProjectionMap.put(Contract.User.KEY_GENDER, Contract.User.KEY_GENDER);
        sUsersProjectionMap.put(Contract.User.KEY_BIRTHDAY, Contract.User.KEY_BIRTHDAY);
        sUsersProjectionMap.put(Contract.User.KEY_LOCATION, Contract.User.KEY_LOCATION);
        sUsersProjectionMap.put(Contract.User.KEY_CARTYPE, Contract.User.KEY_CARTYPE);
        sUsersProjectionMap.put(Contract.User.KEY_AMOUNT_OF_SEATS, Contract.User.KEY_AMOUNT_OF_SEATS);

        sReviewsProjectionMap = new HashMap<String, String>();
        sReviewsProjectionMap.put(Contract.Review._ID, Contract.Review._ID);
        sUsersProjectionMap.put(Contract.Review.KEY_API_ID, Contract.Review.KEY_API_ID);
        sReviewsProjectionMap.put(Contract.Review.KEY_USERID, Contract.Review.KEY_USERID);
        sReviewsProjectionMap.put(Contract.Review.KEY_CREATED_ON, Contract.Review.KEY_CREATED_ON);
        sReviewsProjectionMap.put(Contract.Review.KEY_SCORE , Contract.Review.KEY_SCORE);
        sReviewsProjectionMap.put(Contract.Review.KEY_TEXT,  Contract.Review.KEY_TEXT);
        sReviewsProjectionMap.put(Contract.Review.KEY_USER_ID,  Contract.Review.KEY_USER_ID);

        sTripsProjectionMap = new HashMap<String, String>();
        sTripsProjectionMap.put(Contract.Trip._ID, Contract.Trip._ID);
        sUsersProjectionMap.put(Contract.Trip.KEY_API_ID, Contract.Trip.KEY_API_ID);
        sTripsProjectionMap.put(Contract.Trip.KEY_USERID, Contract.Trip.KEY_USERID);
        sTripsProjectionMap.put(Contract.Trip.KEY_DATE_TIME, Contract.Trip.KEY_DATE_TIME);
        sTripsProjectionMap.put(Contract.Trip.KEY_FROM, Contract.Trip.KEY_FROM);
        sTripsProjectionMap.put(Contract.Trip.KEY_TO, Contract.Trip.KEY_TO);
        sTripsProjectionMap.put(Contract.Trip.KEY_PAYMENT, Contract.Trip.KEY_PAYMENT);
        sTripsProjectionMap.put(Contract.Trip.KEY_REPEAT, Contract.Trip.KEY_REPEAT);
        sTripsProjectionMap.put(Contract.Trip.KEY_USER_ID, Contract.Trip.KEY_USER_ID);

        sMatchesProjectionMap = new HashMap<String, String>();
        sMatchesProjectionMap.put(Contract.Match._ID, Contract.Match._ID);
        sUsersProjectionMap.put(Contract.Match.KEY_API_ID, Contract.Match.KEY_API_ID);
        sMatchesProjectionMap.put(Contract.Match.KEY_USERID, Contract.Match.KEY_USERID);
        sMatchesProjectionMap.put(Contract.Match.KEY_DATE_TIME, Contract.Match.KEY_DATE_TIME);
        sMatchesProjectionMap.put(Contract.Match.KEY_FROM, Contract.Match.KEY_FROM);
        sMatchesProjectionMap.put(Contract.Match.KEY_TO, Contract.Match.KEY_TO);
        sMatchesProjectionMap.put(Contract.Match.KEY_STATUS, Contract.Match.KEY_STATUS);
        sMatchesProjectionMap.put(Contract.Match.KEY_TRIP_ID, Contract.Match.KEY_TRIP_ID);

        sMessagesProjectionMap = new HashMap<String, String>();
        sMessagesProjectionMap.put(Contract.Message._ID, Contract.Message._ID);
        sUsersProjectionMap.put(Contract.Message.KEY_API_ID, Contract.Message.KEY_API_ID);
        sMessagesProjectionMap.put(Contract.Message.KEY_USERID, Contract.Message.KEY_USERID);
        sMessagesProjectionMap.put(Contract.Message.KEY_TEXT, Contract.Message.KEY_TEXT);
        sMessagesProjectionMap.put(Contract.Message.KEY_DATE_TIME , Contract.Message.KEY_DATE_TIME);
        sMessagesProjectionMap.put(Contract.Message.KEY_MATCH_ID, Contract.Message.KEY_MATCH_ID);
    }

    public Provider() {}

    @Override
    public boolean onCreate() {
        mOpenHelper = SQLHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        String orderBy = sortOrder;

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case USERS:
                qb.setTables(Contract.User.CONTENT_DIRECTORY);
                qb.setProjectionMap(sUsersProjectionMap);
                break;
            case USER_ID:
                qb.setTables(Contract.User.CONTENT_DIRECTORY);
                qb.setProjectionMap(sUsersProjectionMap);
                qb.appendWhere(
                        "(" + Contract.User.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.User.USER_ID_PATH_POSITION) + "')"
                );
                break;
            case REVIEWS:
                qb.setTables(Contract.Review.CONTENT_DIRECTORY);
                qb.setProjectionMap(sReviewsProjectionMap);
                break;
            case REVIEW_ID:
                qb.setTables(Contract.Review.CONTENT_DIRECTORY);
                qb.setProjectionMap(sReviewsProjectionMap);
                qb.appendWhere(
                        "(" + Contract.Review.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Review.REVIEW_ID_PATH_POSITION) + "')"
                );
                break;
            case TRIPS:
                qb.setTables(Contract.Trip.CONTENT_DIRECTORY);
                qb.setProjectionMap(sTripsProjectionMap);
                break;
            case TRIP_ID:
                qb.setTables(Contract.Trip.CONTENT_DIRECTORY);
                qb.setProjectionMap(sTripsProjectionMap);
                qb.appendWhere(
                        "(" + Contract.Trip.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Trip.TRIP_ID_PATH_POSITION) + "')"
                );
                break;
            case MATCHES:
                qb.setTables(Contract.Match.CONTENT_DIRECTORY);
                qb.setProjectionMap(sMatchesProjectionMap);
                break;
            case MATCH_ID:
                qb.setTables(Contract.Match.CONTENT_DIRECTORY);
                qb.setProjectionMap(sMatchesProjectionMap);
                qb.appendWhere(
                        "(" + Contract.Match.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Match.MATCH_ID_PATH_POSITION) + "')"
                );
                break;
            case MESSAGES:
                qb.setTables(Contract.Message.CONTENT_DIRECTORY);
                qb.setProjectionMap(sMessagesProjectionMap);
                break;
            case MESSAGE_ID:
                qb.setTables(Contract.Message.CONTENT_DIRECTORY);
                qb.setProjectionMap(sMessagesProjectionMap);
                qb.appendWhere(
                        "(" + Contract.Message.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Message.MESSAGE_ID_PATH_POSITION) + "')"
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor c = qb.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                orderBy
        );

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case USERS:
                return Contract.User.CONTENT_TYPE;
            case USER_ID:
                return Contract.User.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return Contract.Review.CONTENT_TYPE;
            case REVIEW_ID:
                return Contract.Review.CONTENT_ITEM_TYPE;
            case TRIPS:
                return Contract.Trip.CONTENT_TYPE;
            case TRIP_ID:
                return Contract.Trip.CONTENT_ITEM_TYPE;
            case MATCHES:
                return Contract.Match.CONTENT_TYPE;
            case MATCH_ID:
                return Contract.Match.CONTENT_ITEM_TYPE;
            case MESSAGES:
                return Contract.Message.CONTENT_TYPE;
            case MESSAGE_ID:
                return Contract.Message.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId;

        switch (sUriMatcher.match(uri)) {
            case USERS:
                if(!values.containsKey(Contract.User.KEY_USERNAME))
                    throw new IllegalArgumentException(Contract.User.KEY_USERNAME
                            + " is required for " + Contract.User.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.User.CONTENT_DIRECTORY,
                        Contract.User.KEY_USERNAME,
                        values
                );

                if (rowId > 0) {
                    Uri orderUri = ContentUris.withAppendedId(Contract.User.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(orderUri, null);
                    return orderUri;
                }
                break;
            case REVIEWS:
                if(!values.containsKey(Contract.Review.KEY_TEXT))
                    throw new IllegalArgumentException(Contract.Review.KEY_TEXT
                            + " is required for " + Contract.Review.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.Review.CONTENT_DIRECTORY,
                        Contract.Review.KEY_TEXT,
                        values
                );

                if (rowId > 0) {
                    Uri ribbonUri = ContentUris.withAppendedId(Contract.Review.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(ribbonUri, null);
                    return ribbonUri;
                }
                break;
            case TRIPS:
                if(!values.containsKey(Contract.Trip.KEY_PAYMENT))
                    throw new IllegalArgumentException(Contract.Trip.KEY_PAYMENT
                            + " is required for " + Contract.Trip.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.Trip.CONTENT_DIRECTORY,
                        Contract.Trip.KEY_PAYMENT,
                        values
                );

                if (rowId > 0) {
                    Uri ribbonUri = ContentUris.withAppendedId(Contract.Trip.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(ribbonUri, null);
                    return ribbonUri;
                }
                break;
            case MATCHES:
                if(!values.containsKey(Contract.Match.KEY_STATUS))
                    throw new IllegalArgumentException(Contract.Match.KEY_STATUS
                            + " is required for " + Contract.Match.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.Match.CONTENT_DIRECTORY,
                        Contract.Match.KEY_STATUS,
                        values
                );

                if (rowId > 0) {
                    Uri ribbonUri = ContentUris.withAppendedId(Contract.Match.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(ribbonUri, null);
                    return ribbonUri;
                }
                break;
            case MESSAGES:
                if(!values.containsKey(Contract.Message.KEY_TEXT))
                    throw new IllegalArgumentException(Contract.Message.KEY_TEXT
                            + " is required for " + Contract.Message.CONTENT_DIRECTORY);

                rowId = db.insert(
                        Contract.Message.CONTENT_DIRECTORY,
                        Contract.Message.KEY_TEXT,
                        values
                );

                if (rowId > 0) {
                    Uri ribbonUri = ContentUris.withAppendedId(Contract.Message.ITEM_CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(ribbonUri, null);
                    return ribbonUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        String finalWhere;
        int count;

        switch (sUriMatcher.match(uri)) {
            case USERS:
                count = db.delete(
                        Contract.User.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            case REVIEWS:
                count = db.delete(
                        Contract.Review.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            case TRIPS:
                count = db.delete(
                        Contract.Trip.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            case MATCHES:
                count = db.delete(
                        Contract.Match.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            case MESSAGES:
                count = db.delete(
                        Contract.Message.CONTENT_DIRECTORY,
                        where,
                        whereArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs)
    {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (sUriMatcher.match(uri)) {
            case USERS:
                count = db.update(
                        Contract.User.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;
            case USER_ID:
                finalWhere = Contract.User.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.User.USER_ID_PATH_POSITION) +"'";
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.User.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            case REVIEWS:
                count = db.update(
                        Contract.Review.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;
            case REVIEW_ID:
                finalWhere = Contract.Review.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Review.REVIEW_ID_PATH_POSITION)+"'";
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.Review.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            case TRIPS:
                count = db.update(
                        Contract.Trip.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;
            case TRIP_ID:
                finalWhere = Contract.Trip.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Trip.TRIP_ID_PATH_POSITION) + "'";
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.Trip.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            case MATCHES:
                count = db.update(
                        Contract.Match.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;
            case MATCH_ID:
                finalWhere = Contract.Match.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Match.MATCH_ID_PATH_POSITION) + "'";
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.Match.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;
            case MESSAGES:
                count = db.update(
                        Contract.Message.CONTENT_DIRECTORY,
                        values,
                        where,
                        whereArgs
                );
                break;
            case MESSAGE_ID:
                finalWhere = Contract.Message.KEY_API_ID + "='" + uri.getPathSegments().get(Contract.Message.MESSAGE_ID_PATH_POSITION) + "'";
                if (where != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, where);
                }

                count = db.update(
                        Contract.Message.CONTENT_DIRECTORY,
                        values,
                        finalWhere,
                        whereArgs
                );
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

}
