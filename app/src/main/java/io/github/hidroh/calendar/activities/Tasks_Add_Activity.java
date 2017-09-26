package io.github.hidroh.calendar.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.adapter.Name;
import io.github.hidroh.calendar.adapter.NameAdapter;
import io.github.hidroh.calendar.apps.NetworkStateChecker;
import io.github.hidroh.calendar.utilities.DatabaseHelperTasks;
import io.github.hidroh.calendar.utilities.VolleySingleton;


public class Tasks_Add_Activity extends AppCompatActivity implements View.OnClickListener {
    /*
    * this is the url to our webservice
    * make sure you are using the ip instead of localhost
    * it will not work if you are using localhost
    * */
    public static final String URL_SAVE_NAME = "http://192.168.0.21:8090/project/db_android/saveName.php";
    //database helper object
    private DatabaseHelperTasks db;
    //View objects
    private EditText editTextName1,editTextName6,editTextName7;
    private Spinner spinner1,spinner2,spinner3;
    private LinearLayout parent1;
    private TextView txttanggal, txtwaktu;
    private int mYear, mMonth, mDay, mHour, mMinute;


    //List to store all the names
    private List<Name> names;
    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "Data Saved";
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;
    //adapterobject for list view
    private NameAdapter nameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks_ol_v1);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //initializing views and objects
        db = new DatabaseHelperTasks(this);
        names = new ArrayList<>();

        editTextName1 = (EditText) findViewById(R.id.editTextName1);
        editTextName6 = (EditText) findViewById(R.id.editTextName6);
        editTextName7 = (EditText) findViewById(R.id.editTextName7);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);

        txttanggal = (TextView) findViewById(R.id.txttanggal);
        txtwaktu = (TextView) findViewById(R.id.txtwaktu);
        parent1 = (LinearLayout) findViewById(R.id.parent1);

        txttanggal.setOnClickListener(this);
        txtwaktu.setOnClickListener(this);


        //calling the method to load all the stored names
        loadNames();
        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //loading the names again
                loadNames();
            }
        };
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }
    /*
    * this method will
    * load the names from the database
    * with updated sync status
    * */
    private void loadNames() {
        names.clear();
        Cursor cursor = db.getNames();
        nameAdapter = new NameAdapter(this, R.layout.names, names);
    }

    //saving the name to local storage
    private void saveNameToLocalStorage(String id_tasks,String subject_tasks, String status_tasks, String tanggal_tasks, String waktu_tasks, String outcome_tasks, String customers_tasks, String type_tasks,String description_tasks, int status) {

        editTextName1.setText("");
        editTextName6.setText("");
        editTextName7.setText("");
        txtwaktu.setText("");
        txttanggal.setText("");

        db.addName(id_tasks,subject_tasks,status_tasks, tanggal_tasks, waktu_tasks,outcome_tasks,customers_tasks,type_tasks,description_tasks, status);
        Name n = new Name(id_tasks,subject_tasks,status_tasks, tanggal_tasks, waktu_tasks,outcome_tasks,customers_tasks,type_tasks,description_tasks, status);
        names.add(n);
        refreshList();
    }

    /*
    * this method will simply refresh the list
    * */
    private void refreshList() {nameAdapter.notifyDataSetChanged();}

    /*
    * this method is saving the name to ther server
    * */
    private void saveNameToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Tasks...");
        progressDialog.show();

        final String id_tasks = null;
        final String subject_tasks = editTextName1.getText().toString().trim();
        final String status_tasks = spinner1.getSelectedItem().toString().trim();
        final String tanggal_tasks = txttanggal.getText().toString().trim();
        final String waktu_tasks = txtwaktu.getText().toString().trim();

        final String outcome_tasks = spinner2.getSelectedItem().toString().trim();
        final String customers_tasks = editTextName6.getText().toString().trim();
        final String type_tasks = spinner3.getSelectedItem().toString().trim();
        final String description_tasks = editTextName7.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveNameToLocalStorage(id_tasks,subject_tasks, status_tasks,tanggal_tasks, waktu_tasks,outcome_tasks,customers_tasks,type_tasks,description_tasks, NAME_SYNCED_WITH_SERVER);


                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveNameToLocalStorage(id_tasks,subject_tasks, status_tasks,tanggal_tasks, waktu_tasks,outcome_tasks,customers_tasks,type_tasks,description_tasks, NAME_NOT_SYNCED_WITH_SERVER);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveNameToLocalStorage(id_tasks,subject_tasks, status_tasks,tanggal_tasks, waktu_tasks,outcome_tasks,customers_tasks,type_tasks,description_tasks, NAME_NOT_SYNCED_WITH_SERVER);

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

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }







    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txttanggal:

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                txttanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.txtwaktu:

                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                txtwaktu.setText(hourOfDay + ":" + minute );
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_save:
                if( editTextName1.getText().toString().length() == 0 )
                    editTextName1.setError( "Subject is required!" );

                else
                {
                    saveNameToServer();
                }
                return true;

            case R.id.navigation_help:
                Intent myAplikasi2 = new Intent(this,LoginActivity.class);
                startActivity(myAplikasi2);
                return true;

            case R.id.navigation_exit:
            {
                keluarYN();//Pergi ke method exit
            }
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void keluarYN(){
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Konfirmasi");

        ad.setMessage("Anda Yakin ingin keluar?");
        ad.setPositiveButton("IYA",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }});
        ad.setNegativeButton("TIDAK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1) {
            }});
        ad.show();
    }

}