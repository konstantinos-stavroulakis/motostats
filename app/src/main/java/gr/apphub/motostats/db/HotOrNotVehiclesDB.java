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
public class HotOrNotVehiclesDB {
    private Context ourContextveh;
    private DBHelper_veh ourHelperveh;
    private SQLiteDatabase ourDatabaseveh;

    public HotOrNotVehiclesDB(Context c) {
        ourContextveh = c;

    }

    public HotOrNotVehiclesDB open() throws SQLException {
        ourHelperveh = new DBHelper_veh(ourContextveh);
        ourDatabaseveh = ourHelperveh.getWritableDatabase();
        return this;

    }

    public void close() {
        ourHelperveh.close();
    }

    public void createEntryveh(String VEH_MODEL1, String VEH_MODEL2, String VEH_YEAR, String VEH_CC, String VEH_TYPE) {

        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper_veh.VEH_MODEL1, VEH_MODEL1);
            cv.put(DBHelper_veh.VEH_MODEL2, VEH_MODEL2);
            cv.put(DBHelper_veh.VEH_YEAR, VEH_YEAR);
            cv.put(DBHelper_veh.VEH_CC, VEH_CC);
            cv.put(DBHelper_veh.VEH_TYPE, VEH_TYPE);

            ourDatabaseveh.insert("vehdb_tbl", null, cv);

        } catch (Exception e) {
            Log.e("Exception in insert :", e.toString());
            e.printStackTrace();
        }
    }

    public Cursor getDatavehModel1() {

        String[] columns = new String[]{DBHelper_veh.ROWID, DBHelper_veh.VEH_MODEL1, DBHelper_veh.VEH_MODEL2, DBHelper_veh.VEH_YEAR, DBHelper_veh.VEH_CC, DBHelper_veh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_veh.DATABASE_TABLE, columns, null,
                null, null, null, DBHelper_veh.VEH_MODEL1 + " ASC");
        return c;
    }

    public Cursor getDatavehModel2(String model1) {

        String[] columns = new String[]{DBHelper_veh.ROWID, DBHelper_veh.VEH_MODEL1, DBHelper_veh.VEH_MODEL2, DBHelper_veh.VEH_YEAR, DBHelper_veh.VEH_CC, DBHelper_veh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_veh.DATABASE_TABLE, columns, DBHelper_veh.VEH_MODEL1 + " = '" + model1 + "'",
                null, null, null, DBHelper_veh.VEH_MODEL2 + " ASC");
        return c;
    }

    public Cursor getDatavehYear(String model2) {

        String[] columns = new String[]{DBHelper_veh.ROWID, DBHelper_veh.VEH_MODEL1, DBHelper_veh.VEH_MODEL2, DBHelper_veh.VEH_YEAR, DBHelper_veh.VEH_CC, DBHelper_veh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_veh.DATABASE_TABLE, columns, DBHelper_veh.VEH_MODEL2 + " = '" + model2 + "'",
                null, null, null, DBHelper_veh.VEH_MODEL2 + " ASC");
        return c;
    }

    public Cursor getDatavehCc(String model2, String year) {

        String[] columns = new String[]{DBHelper_veh.ROWID, DBHelper_veh.VEH_MODEL1, DBHelper_veh.VEH_MODEL2, DBHelper_veh.VEH_YEAR, DBHelper_veh.VEH_CC, DBHelper_veh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_veh.DATABASE_TABLE, columns, DBHelper_veh.VEH_MODEL2 + " = '" + model2 + "'" + " and " + DBHelper_veh.VEH_YEAR + " = '" + year + "'",
                null, null, null, DBHelper_veh.VEH_MODEL2 + " ASC");
        return c;
    }

    public Cursor getDatavehType(String model1, String model2, String year, String cc) {

        String[] columns = new String[]{DBHelper_veh.ROWID, DBHelper_veh.VEH_MODEL1, DBHelper_veh.VEH_MODEL2, DBHelper_veh.VEH_YEAR, DBHelper_veh.VEH_CC, DBHelper_veh.VEH_TYPE};
        Cursor c = ourDatabaseveh.query(DBHelper_veh.DATABASE_TABLE, columns, DBHelper_veh.VEH_MODEL1 + " = '" + model1 + "' and " + DBHelper_veh.VEH_MODEL2 + " = '" + model2 + "' and " + DBHelper_veh.VEH_YEAR + " = '" + year + "' and " + DBHelper_veh.VEH_CC + " = '" + cc + "'",
                null, null, null, null);
        return c;
    }

    public int deleteAll() {
        return ourDatabaseveh.delete(DBHelper_veh.DATABASE_TABLE, null, null);
    }

    public static class DBHelper_veh extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "vehdb.db";
        public static final String DATABASE_TABLE = "vehdb_tbl";

        public static final String ROWID = "_id";
        public static final String VEH_MODEL1 = "VEH_MODEL1";
        public static final String VEH_MODEL2 = "VEH_MODEL2";
        public static final String VEH_YEAR = "VEH_YEAR";
        public static final String VEH_CC = "VEH_CC";
        public static final String VEH_TYPE = "VEH_TYPE";

        public DBHelper_veh(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + ROWID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VEH_MODEL1 + " TEXT ,  " + VEH_MODEL2 + " TEXT , " + VEH_YEAR + " TEXT, " + VEH_CC + " TEXT, " + VEH_TYPE + " TEXT);");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Constants",
                    "Upgrading database, which will destroy all data");
            db.execSQL("DROP TABLE IF EXISTS vehdb_tbl");
            onCreate(db);

        }
    }


}// end mainclass
