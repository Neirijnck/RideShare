package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class SQLHelper extends SQLiteOpenHelper {

    private static SQLHelper INSTANCE;
    private static Object lock = new Object();

    private static final String DB_NAME = "sharemyride.db";
    private static final int DB_VERSION = 1;

    private SQLHelper(Context context) {
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
        /*db.execSQL("DROP TABLE " + Contract.Ribbons.CONTENT_DIRECTORY);
        db.execSQL("DROP TABLE " + Contract.Orders.CONTENT_DIRECTORY);

        createOrdersTableV1(db);
        createRibbonsTableV1(db);*/
    }
    /*
    private void createOrdersTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Contract.Orders.CONTENT_DIRECTORY + " ("
                + Contract.OrderColumns._ID + " INTEGER PRIMARY KEY,"
                + Contract.OrderColumns.NAME + " TEXT"
                + ");";

        db.execSQL(sql);
    }

    private void createRibbonsTableV1(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + Contract.Ribbons.CONTENT_DIRECTORY + " ("
                + Contract.RibbonColumns._ID + " INTEGER PRIMARY KEY,"
                + Contract.RibbonColumns.NAME + " TEXT,"
                + Contract.RibbonColumns.IMAGE + " TEXT,"
                + Contract.RibbonColumns.ORDER_ID + " TEXT"
                + ");";

        db.execSQL(sql);
    }*/


}
