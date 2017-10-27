package io.github.hidroh.calendar.apps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.github.hidroh.calendar.activities.CustomerDraftActivity;
import io.github.hidroh.calendar.utilities.DatabaseHelperCustomer;

/**
 * Created by singgih on 10/20/2017.
 */

public class CustomerDraftChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelperCustomer db;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelperCustomer(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedCustomers();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveCustomer(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_NAME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_ST)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_PHONE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_ADDRESS)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_DESC))

                        );
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    /*
    * method taking two arguments
    * name that is to be saved and id of the name from SQLite
    * if the name is successfully sent
    * we will update the status as synced in SQLite
    * */
    private void saveCustomer(final String cust_id, final String cust_name, final String cust_st, final String cust_phone, final String cust_address, final String cust_desc) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SAVE_CUST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateCustomerStatus(cust_id, CustomerDraftActivity.CUST_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(CustomerDraftActivity.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cust_id", cust_id);
                params.put("cust_name", cust_name);
                params.put("cust_st", cust_st);
                params.put("cust_phone", cust_phone);
                params.put("cust_address", cust_address);
                params.put("cust_desc", cust_desc);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
