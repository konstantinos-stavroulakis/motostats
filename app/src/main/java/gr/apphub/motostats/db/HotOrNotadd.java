package gr.apphub.motostats.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Konstantinos on 4/7/15.
 */
public class HotOrNotadd {
    private Context ourContextadd;
    private DBHelper_add ourHelperadd;
    private SQLiteDatabase ourDatabaseadd;

    public HotOrNotadd(Context c) {
        ourContextadd = c;

    }

    public HotOrNotadd open() throws SQLException {
        ourHelperadd = new DBHelper_add(ourContextadd);
        ourDatabaseadd = ourHelperadd.getWritableDatabase();
        return this;

    }

    public void close() {
        ourHelperadd.close();
    }

    public void createEntryadd(double num_of_litres, double price_per_litre, String date, double pay_amount, double km, String VEH_NAME, String VEH_TYPE) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper_add.NUM_OF_LITRES, num_of_litres);
            cv.put(DBHelper_add.PRICE_PER_LITRE, price_per_litre);
            cv.put(DBHelper_add.DATE, date);
            cv.put(DBHelper_add.PAY_AMOUNT, pay_amount);
            cv.put(DBHelper_add.KM, km);
            cv.put(DBHelper_add.VEH_NAME, VEH_NAME);
            cv.put(DBHelper_add.VEH_TYPE, VEH_TYPE);

            ourDatabaseadd.insert("add_tbl", null, cv);

        } catch (Exception e) {
            Log.e("Exception in insert :", e.toString());
            e.printStackTrace();
        }
    }

    public Cursor getDataadd() {

        String[] columns = new String[]{DBHelper_add.ROWID, DBHelper_add.NUM_OF_LITRES, DBHelper_add.PRICE_PER_LITRE, DBHelper_add.DATE, DBHelper_add.PAY_AMOUNT, DBHelper_add.KM, DBHelper_add.VEH_NAME, DBHelper_add.VEH_TYPE};
        Cursor c = ourDatabaseadd.query(DBHelper_add.DATABASE_TABLE, columns, null,
                null, null, null, DBHelper_add.ROWID + " DESC");
        return c;
    }

    // metraei tis eggrafes
    public long fetchPlacesCount() {
        String sql = "SELECT COUNT(*) FROM " + DBHelper_add.DATABASE_TABLE;
        SQLiteStatement statement = ourDatabaseadd.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public static class DBHelper_add extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "add.db";
        public static final String DATABASE_TABLE = "add_tbl";

        public static final String ROWID = "_id";
        public static final String NUM_OF_LITRES = "num_of_litres";
        public static final String PRICE_PER_LITRE = "price_per_litre";
        public static final String DATE = "date";
        public static final String PAY_AMOUNT = "pay_amount";
        public static final String KM = "km";
        public static final String VEH_NAME = "veh_name";
        public static final String VEH_TYPE = "veh_type";

        public DBHelper_add(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + ROWID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NUM_OF_LITRES
                    + " DOUBLE , " + PRICE_PER_LITRE + " DOUBLE ,  " + DATE + " TEXT , " + PAY_AMOUNT + " DOUBLE, " + KM + " DOUBLE," + VEH_NAME + " TEXT, " + VEH_TYPE + " TEXT);");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Constants",
                    "Upgrading database, which will destroy all data");
            db.execSQL("DROP TABLE IF EXISTS add_tbl");
            onCreate(db);

        }
    }

}// end mainclass
