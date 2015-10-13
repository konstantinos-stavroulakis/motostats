package gr.apphub.motostats.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Konstantinos on 4/7/15.
 */
public class HotOrNotMyVehicles {
    private Context ourContextveh;
    private DBHelper_myveh ourHelperveh;
    private SQLiteDatabase ourDatabaseveh;

    public HotOrNotMyVehicles(Context c) {
        ourContextveh = c;

    }

    public HotOrNotMyVehicles open() throws SQLException {
        ourHelperveh = new DBHelper_myveh(ourContextveh);
        ourDatabaseveh = ourHelperveh.getWritableDatabase();
        return this;

    }

    public void close() {
        ourHelperveh.close();
    }

    public void createEntryveh(String VEH_NAME, String VEH_MODEL1, String VEH_MODEL2, String VEH_YEAR, String VEH_CC, String VEH_TYPE) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper_myveh.VEH_NAME, VEH_NAME);
            cv.put(DBHelper_myveh.VEH_MODEL1, VEH_MODEL1);
            cv.put(DBHelper_myveh.VEH_MODEL2, VEH_MODEL2);
            cv.put(DBHelper_myveh.VEH_YEAR, VEH_YEAR);
            cv.put(DBHelper_myveh.VEH_CC, VEH_CC);
            cv.put(DBHelper_myveh.VEH_TYPE, VEH_TYPE);

            ourDatabaseveh.insert("veh_tbl", null, cv);

        } catch (Exception e) {
            Log.e("Exception in insert :", e.toString());
            e.printStackTrace();
        }
    }

    public void updateEntry(String OLD_VEH_NAME, String OLD_VEH_MODEL2, String OLD_VEH_TYPE, String VEH_NAME, String VEH_MODEL1, String VEH_MODEL2, String VEH_YEAR, String VEH_CC, String VEH_TYPE) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper_myveh.VEH_NAME, VEH_NAME);
            cv.put(DBHelper_myveh.VEH_MODEL1, VEH_MODEL1);
            cv.put(DBHelper_myveh.VEH_MODEL2, VEH_MODEL2);
            cv.put(DBHelper_myveh.VEH_YEAR, VEH_YEAR);
            cv.put(DBHelper_myveh.VEH_CC, VEH_CC);
            cv.put(DBHelper_myveh.VEH_TYPE, VEH_TYPE);


            ourDatabaseveh.update(DBHelper_myveh.DATABASE_TABLE, cv, DBHelper_myveh.VEH_NAME + "=? and " + DBHelper_myveh.VEH_MODEL2 + " =? and " + DBHelper_myveh.VEH_TYPE + "=?",
                    new String[]{OLD_VEH_NAME, OLD_VEH_MODEL2, OLD_VEH_TYPE});

        } catch (Exception e) {
            Log.e("Exception in update :", e.toString());
            e.printStackTrace();
        } // updating row

    }

    public Cursor getDataveh() {

        String[] columns = new String[]{DBHelper_myveh.ROWID, DBHelper_myveh.VEH_NAME, DBHelper_myveh.VEH_MODEL1, DBHelper_myveh.VEH_MODEL2, DBHelper_myveh.VEH_YEAR, DBHelper_myveh.VEH_CC, DBHelper_myveh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_myveh.DATABASE_TABLE, columns, null,
                null, null, null, null);
        return c;
    }

    public Cursor getDatavehByData(String name, String model2, String type) {

        String[] columns = new String[]{DBHelper_myveh.ROWID, DBHelper_myveh.VEH_NAME, DBHelper_myveh.VEH_MODEL1, DBHelper_myveh.VEH_MODEL2, DBHelper_myveh.VEH_YEAR, DBHelper_myveh.VEH_CC, DBHelper_myveh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_myveh.DATABASE_TABLE, columns, DBHelper_myveh.VEH_NAME + " ='" + name + "' and " + DBHelper_myveh.VEH_MODEL2 + " ='" + model2 + "' and " + DBHelper_myveh.VEH_TYPE + "= '" + type,
                null, null, null, null);
        return c;
    }

    public Cursor sameNameVehicle(String name, String type) {

        String[] columns = new String[]{DBHelper_myveh.ROWID, DBHelper_myveh.VEH_NAME, DBHelper_myveh.VEH_MODEL1, DBHelper_myveh.VEH_MODEL2, DBHelper_myveh.VEH_YEAR, DBHelper_myveh.VEH_CC, DBHelper_myveh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_myveh.DATABASE_TABLE, columns, DBHelper_myveh.VEH_NAME + "= '" + name + "' and " + DBHelper_myveh.VEH_TYPE + "= '" + type + "'",
                null, null, null, null);
        return c;
    }

    public void deleteItem(String name, String model2) {
        ourDatabaseveh.execSQL("DELETE FROM favorite WHERE VEH_NAME = '" + name + "' and VEH_MODEL2 = '" + model2 + "'");
    }

    public void deleteItem2(String name, String model2) {

        ourDatabaseveh.delete(DBHelper_myveh.DATABASE_TABLE, DBHelper_myveh.VEH_NAME + "= '" + name + "' and " + DBHelper_myveh.VEH_MODEL2 + "= '" + model2 + "'", null);
        Log.i("HotOrNotData::refresh", "items deleted from db");

    }

    public Cursor getDatavehByName(String name) {

        String[] columns = new String[]{DBHelper_myveh.ROWID, DBHelper_myveh.VEH_NAME, DBHelper_myveh.VEH_MODEL1, DBHelper_myveh.VEH_MODEL2, DBHelper_myveh.VEH_YEAR, DBHelper_myveh.VEH_CC, DBHelper_myveh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_myveh.DATABASE_TABLE, columns, DBHelper_myveh.VEH_NAME + "= '" + name + "'",
                null, null, null, null);
        return c;
    }

    public static class DBHelper_myveh extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "veh.db";
        public static final String DATABASE_TABLE = "veh_tbl";

        public static final String ROWID = "_id";
        public static final String VEH_NAME = "VEH_NAME";
        public static final String VEH_MODEL1 = "VEH_MODEL1";
        public static final String VEH_MODEL2 = "VEH_MODEL2";
        public static final String VEH_YEAR = "VEH_YEAR";
        public static final String VEH_CC = "VEH_CC";
        public static final String VEH_TYPE = "VEH_TYPE";

        public DBHelper_myveh(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + ROWID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VEH_NAME
                    + " TEXT , " + VEH_MODEL1 + " TEXT ,  " + VEH_MODEL2 + " TEXT , " + VEH_YEAR + " TEXT, " + VEH_CC + " TEXT, " + VEH_TYPE + " TEXT);");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Constants",
                    "Upgrading database, which will destroy all data");
            db.execSQL("DROP TABLE IF EXISTS veh_tbl");
            onCreate(db);

        }
    }
}// end mainclass
