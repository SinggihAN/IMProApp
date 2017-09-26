package io.github.hidroh.calendar.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.hidroh.calendar.adapter.Name;


public class DatabaseHelperTasks extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "NamesDB";
    public static final String TABLE_NAME = "names";
    public static final String COLUMN_ID = "id_tasks";
    public static final String COLUMN_SUBJECT_TASKS = "subject_tasks";
    public static final String COLUMN_STATUS_TASKS = "status_tasks";
    public static final String COLUMN_TANGGAL = "tanggal_tasks";
    public static final String COLUMN_WAKTU = "waktu_tasks";
    public static final String COLUMN_OUTCOME = "outcome_tasks";
    public static final String COLUMN_CUSTOMERS = "customers_tasks";
    public static final String COLUMN_TYPE = "type_tasks";
    public static final String COLUMN_DESCRIPTION = "description_tasks";
    public static final String COLUMN_STATUS = "status";

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelperTasks(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SUBJECT_TASKS + " VARCHAR, "
                + COLUMN_STATUS_TASKS + " VARCHAR, "
                + COLUMN_TANGGAL + " VARCHAR, "
                + COLUMN_WAKTU + " VARCHAR, "
                + COLUMN_OUTCOME + " VARCHAR, "
                + COLUMN_CUSTOMERS + " VARCHAR, "
                + COLUMN_TYPE + " VARCHAR, "
                + COLUMN_DESCRIPTION + " VARCHAR, "
                + COLUMN_STATUS + " TINYINT);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Persons";
        db.execSQL(sql);
        onCreate(db);
    }

    /*
    * This method is taking two arguments
    * first one is the name that is to be saved
    * second one is the status
    * 0 means the name is synced with the server
    * 1 means the name is not synced with the server
    * */
    public boolean addName(String id_tasks,String subject_tasks, String status_tasks, String tanggal_tasks, String waktu_tasks, String outcome_tasks, String customers_tasks, String type_tasks,String description_tasks, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, id_tasks);
        contentValues.put(COLUMN_SUBJECT_TASKS, subject_tasks);
        contentValues.put(COLUMN_STATUS_TASKS, status_tasks);
        contentValues.put(COLUMN_TANGGAL, tanggal_tasks);
        contentValues.put(COLUMN_WAKTU, waktu_tasks);
        contentValues.put(COLUMN_OUTCOME, outcome_tasks);
        contentValues.put(COLUMN_CUSTOMERS, customers_tasks);
        contentValues.put(COLUMN_TYPE, type_tasks);
        contentValues.put(COLUMN_DESCRIPTION, description_tasks);
        contentValues.put(COLUMN_STATUS, status);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }



    public Name detailname (String id_tasks) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = \"" + id_tasks + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Name name1 = new Name();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            name1.setId_tasks(cursor.getString(0));
            name1.setSubject_tasks(cursor.getString(1));
            name1.setStatus_tasks(cursor.getString(2));
            name1.setTanggal_tasks(cursor.getString(3));
            name1.setWaktu_tasks(cursor.getString(4));

            name1.setOutcome_tasks(cursor.getString(5));
            name1.setCustomers_tasks(cursor.getString(6));
            name1.setType_tasks(cursor.getString(7));
            name1.setDescription_tasks(cursor.getString(8));
            name1.setStatus(cursor.getInt(9));

            cursor.close();
        } else {
            name1 = null;
        }
        db.close();
        return name1;
    }


    public void hapusData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String hapus = "DELETE FROM " + TABLE_NAME;
        db.execSQL(hapus);
        db.close();
    }

    /*
    * This method taking two arguments
    * first one is the id of the name for which
    * we have to update the sync status
    * and the second one is the status that will be changed
    * */
    public boolean updateNameStatus(String id_tasks, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id_tasks, null);
        db.close();
        return true;
    }

    /*
    * this method will give us all the name stored in sqlite
    * */
    public Cursor getNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
    * this method is for getting all the unsynced name
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }



}
