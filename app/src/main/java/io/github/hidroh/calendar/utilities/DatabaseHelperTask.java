package io.github.hidroh.calendar.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.hidroh.calendar.adapter.Name;
import io.github.hidroh.calendar.adapter.Task;

/**
 * Created by singgih on 11/6/2017.
 */

public class DatabaseHelperTask extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names
    public static final String DB_NAME = "IMProDB";
    public static final String TABLE_NAME = "tasks";
    public static final String TASK_ID = "id_tasks";
    public static final String TASK_NAME = "subject_tasks";
    public static final String TASK_PRIORITY = "status_tasks";
    public static final String TASK_TYPE = "tanggal_tasks";
    public static final String TASK_STATUS = "waktu_tasks";
    public static final String TASK_DUE = "outcome_tasks";
    public static final String TASK_OUTCOME = "customers_tasks";
    public static final String TASK_PERCENT = "type_tasks";
    public static final String TASK_DESC = "description_tasks";
    public static final String CUST_ID = "cust_id";
    public static final String OPP_ID = "opp_id";
    public static final String LEAD_ID = "lead_id";
    public static final String USER_ID = "user_id";
    public static final String CONTACT_ID = "contact_id";
    public static final String STATUS = "status";

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelperTask(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TASK_NAME + " VARCHAR, "
                + TASK_PRIORITY + " VARCHAR, "
                + TASK_TYPE + " VARCHAR, "
                + TASK_STATUS + " VARCHAR, "
                + TASK_DUE + " VARCHAR, "
                + TASK_OUTCOME + " VARCHAR, "
                + TASK_PERCENT + " VARCHAR, "
                + TASK_DESC + " VARCHAR, "
                + CUST_ID + " VARCHAR, "
                + OPP_ID + " VARCHAR, "
                + LEAD_ID + " VARCHAR, "
                + USER_ID + " VARCHAR, "
                + CONTACT_ID + " TINYINT);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS tasks";
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
    public boolean addTask(
            String task_id,
            String task_name,
            String task_priority,
            String task_type,
            String task_status,
            String task_due,
            String task_outcome,
            String task_percent,
            String task_desc,
            String cust_id,
            String opp_id,
            String lead_id,
            String user_id,
            String contact_id,
            int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TASK_ID, task_id);
        contentValues.put(TASK_NAME, task_name);
        contentValues.put(TASK_PRIORITY, task_priority);
        contentValues.put(TASK_TYPE, task_type);
        contentValues.put(TASK_STATUS, task_status);
        contentValues.put(TASK_DUE, task_due);
        contentValues.put(TASK_OUTCOME, task_outcome);
        contentValues.put(TASK_PERCENT, task_percent);
        contentValues.put(TASK_DESC, task_desc);
        contentValues.put(CUST_ID, cust_id);
        contentValues.put(OPP_ID, opp_id);
        contentValues.put(LEAD_ID, lead_id);
        contentValues.put(USER_ID, user_id);
        contentValues.put(CONTACT_ID, contact_id);
        contentValues.put(STATUS, status);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public Task detailTask (String task_id) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + TASK_ID + " = \"" + task_id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Task task = new Task();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            task.setTask_id(cursor.getString(0));
            task.setTask_name(cursor.getString(1));
            task.setTask_priority(cursor.getString(2));
            task.setTask_type(cursor.getString(3));
            task.setTask_status(cursor.getString(4));
            task.setTask_due(cursor.getString(5));
            task.setTask_outcome(cursor.getString(6));
            task.setTask_percent(cursor.getString(7));
            task.setTask_desc(cursor.getString(8));
            task.setCust_id(cursor.getString(9));
            task.setOpp_id(cursor.getString(10));
            task.setLead_id(cursor.getString(11));
            task.setUser_id(cursor.getString(12));
            task.setContact_id(cursor.getString(13));
            task.setStatus(cursor.getInt(14));

            cursor.close();
        } else {
            task = null;
        }
        db.close();
        return task;
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
    public boolean updateTaskStatus(String task_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME, contentValues, TASK_ID + "=" + task_id, null);
        db.close();
        return true;
    }

    /*
    * this method will give us all the name stored in sqlite
    * */
    public Cursor getTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + TASK_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
    * this method is for getting all the unsynced name
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
}
