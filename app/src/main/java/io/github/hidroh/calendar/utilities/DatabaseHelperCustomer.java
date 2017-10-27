package io.github.hidroh.calendar.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import io.github.hidroh.calendar.adapter.Customer;
import io.github.hidroh.calendar.adapter.Name;

/**
 * Created by singgih on 10/20/2017.
 */

public class DatabaseHelperCustomer extends SQLiteOpenHelper {

    //define DB
    public static final String DB_NAME = "IMProDB";
    //define table
    public static final String TABLE_NAME = "Customers";
    //define fields
    public static final String CUST_ID = "cust_id";
    public static final String CUST_NAME = "cust_name";
    public static final String CUST_ST = "cust_st";
    public static final String CUST_PHONE = "cust_phone";
    public static final String CUST_ADDRESS = "cust_address";
    public static final String CUST_DESC = "cust_desc";
    public static final String CUST_IS_DRAFT = "is_draft";

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelperCustomer(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + CUST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CUST_NAME + " VARCHAR, "
                + CUST_ST + " VARCHAR, "
                + CUST_PHONE + " VARCHAR, "
                + CUST_ADDRESS + " VARCHAR, "
                + CUST_DESC+ " VARCHAR, "
                + CUST_IS_DRAFT + " TINYINT);";
        Log.d("Data", "onCreate: " + sql);
        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Customers";
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
    public boolean addCustomer(String cust_id,String cust_name, String cust_st, String cust_phone, String cust_address, String cust_desc, int is_draft) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUST_ID, cust_id);
        contentValues.put(CUST_NAME, cust_name);
        contentValues.put(CUST_ST, cust_st);
        contentValues.put(CUST_PHONE, cust_phone);
        contentValues.put(CUST_ADDRESS, cust_address);
        contentValues.put(CUST_DESC, cust_desc);
        contentValues.put(CUST_IS_DRAFT, is_draft);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }



    public Customer detailCustomer (String id_tasks) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + CUST_ID + " = \"" + id_tasks + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Customer customer1 = new Customer();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            customer1.setCust_id(cursor.getString(0));
            customer1.setCust_name(cursor.getString(1));
            customer1.setCust_st(cursor.getString(2));
            customer1.setCust_phone(cursor.getString(3));
            customer1.setCust_address(cursor.getString(4));
            customer1.setCust_desc(cursor.getString(5));
            customer1.setIs_draft(cursor.getInt(6));

            cursor.close();
        } else {
            customer1 = null;
        }
        db.close();
        return customer1;
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
    public boolean updateCustomerStatus(String cust_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUST_ST, status);
        db.update(TABLE_NAME, contentValues, CUST_ST + "=" + cust_id, null);
        db.close();
        return true;
    }

    /*
    * this method will give us all the name stored in sqlite
    * */
    public Cursor getCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + CUST_ID + " ASC;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /*
    * this method is for getting all the unsynced name
    * so that we can sync it with database
    * */
    public Cursor getUnsyncedCustomers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + CUST_IS_DRAFT + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


}
