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
import io.github.hidroh.calendar.utilities.DatabaseHelperTask;

/**
 * Created by singgih on 11/6/2017.
 */

public class TaskDraftChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private DatabaseHelperTask db;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DatabaseHelperTask(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = db.getUnsyncedTask();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveTask(
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_NAME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_PRIORITY)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_TYPE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_STATUS)),

                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_DUE)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_OUTCOME)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_PERCENT)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.TASK_DESC)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.CUST_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.OPP_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.LEAD_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.USER_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseHelperTask.CONTACT_ID))

                        );
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    private void saveTask(final String task_id,
                          final String task_name,
                          final String task_priority,
                          final String task_type,
                          final String task_status,
                          final String task_due,
                          final String task_outcome,
                          final String task_percent,
                          final String task_desc,
                          final String cust_id,
                          final String opp_id,
                          final String lead_id,
                          final String user_id,
                          final String contact_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SAVE_TASK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.updateTaskStatus(task_id, AppConfig.DATA_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(AppConfig.DATA_SAVED_BROADCAST));
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
                params.put("task_id", task_id);
                params.put("task_name", task_name);
                params.put("task_priority", task_priority);
                params.put("task_type", task_type);
                params.put("task_status", task_status);

                params.put("task_due", task_due);
                params.put("task_outcome", task_outcome);
                params.put("task_percent", task_percent);
                params.put("task_desc", task_desc);

                params.put("cust_id", cust_id);
                params.put("opp_id", opp_id);
                params.put("lead_id", lead_id);
                params.put("user_id", user_id);
                params.put("contact_id", contact_id);

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
