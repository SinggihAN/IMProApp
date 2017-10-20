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

import io.github.hidroh.calendar.activities.Tasks_List_Draft_Activity;
import io.github.hidroh.calendar.utilities.DatabaseHelperTasks;


public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelperTasks db;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelperTasks(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_SUBJECT_TASKS)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_STATUS_TASKS)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_TANGGAL)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_WAKTU)),

                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_OUTCOME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_CUSTOMERS)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_TYPE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTasks.COLUMN_DESCRIPTION))

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
    private void saveName(final String id_tasks, final String subject_tasks, final String status_tasks, final String tanggal_tasks, final String waktu_tasks, final String outcome_tasks,
             final String customers_tasks, final String type_tasks, final String description_tasks) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Tasks_List_Draft_Activity.URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateNameStatus(id_tasks, Tasks_List_Draft_Activity.NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(Tasks_List_Draft_Activity.DATA_SAVED_BROADCAST));
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
                params.put("id", id_tasks);
                params.put("subject_tasks", subject_tasks);
                params.put("status_tasks", status_tasks);
                params.put("tanggal_tasks", tanggal_tasks);
                params.put("waktu_tasks", waktu_tasks);

                params.put("outcome_tasks", outcome_tasks);
                params.put("customers_tasks", customers_tasks);
                params.put("type_tasks", type_tasks);
                params.put("description_tasks", description_tasks);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
