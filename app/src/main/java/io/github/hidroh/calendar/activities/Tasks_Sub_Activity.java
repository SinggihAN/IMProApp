package io.github.hidroh.calendar.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.apps.Konfigurasi;
import io.github.hidroh.calendar.apps.RequestHandler;

public class Tasks_Sub_Activity extends AppCompatActivity implements View.OnClickListener{

    private TextView editId, Tanggal,Waktu;
    private EditText editSubject,editStatus,editOutcome,editCustomers,editType,editDescription;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_tasks_ol_v1);

        Intent intent = getIntent();

        id = intent.getStringExtra(Konfigurasi.EMP_ID);

        editId = (TextView) findViewById(R.id.id_tasks);
        editSubject = (EditText) findViewById(R.id.subject);
        editStatus = (EditText) findViewById(R.id.status);
        Tanggal = (TextView) findViewById(R.id.tanggal);

        Waktu = (TextView) findViewById(R.id.waktu);
        editOutcome = (EditText) findViewById(R.id.outcome);
        editCustomers = (EditText) findViewById(R.id.customers);
        editType = (EditText) findViewById(R.id.type);
        editDescription = (EditText) findViewById(R.id.description);

        Tanggal.setOnClickListener(this);
        Waktu.setOnClickListener(this);

        editId.setText(id);

        getEmployee();
    }

    private void getEmployee(){
        class GetEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tasks_Sub_Activity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showEmployee(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_GET_EMP,id);
                return s;
            }
        }
        GetEmployee ge = new GetEmployee();
        ge.execute();
    }

    private void showEmployee(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String subject_tasks = c.getString(Konfigurasi.TAG_SUBJECT_TASKS);
            String status_tasks = c.getString(Konfigurasi.TAG_STATUS_TASKS);
            String tanggal_tasks = c.getString(Konfigurasi.TAG_TANGGAL_TASKS);

            String waktu_tasks = c.getString(Konfigurasi.TAG_WAKTU_TASKS);
            String outcome_tasks = c.getString(Konfigurasi.TAG_OUTCOME_TASKS);
            String customers_tasks = c.getString(Konfigurasi.TAG_CUSTOMERS_TASKS);
            String type_tasks = c.getString(Konfigurasi.TAG_TYPE_TASKS);
            String description_tasks = c.getString(Konfigurasi.TAG_DESCRIPTION_TASKS);

            editSubject.setText(subject_tasks);
            editStatus.setText(status_tasks);
            Tanggal.setText(tanggal_tasks);

            Waktu.setText(waktu_tasks);
            editOutcome.setText(outcome_tasks);
            editCustomers.setText(customers_tasks);
            editType.setText(type_tasks);
            editDescription.setText(description_tasks);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tanggal:

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                Tanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;

            case R.id.waktu:

                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                Waktu.setText(hourOfDay + ":" + minute );
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
        }
    }






    private void updateEmployee(){
        final String subject_tasks = editSubject.getText().toString().trim();
        final String status_tasks = editStatus.getText().toString().trim();
        final String tanggal_tasks = Tanggal.getText().toString().trim();

        final String waktu_tasks = Waktu.getText().toString().trim();
        final String outcome_tasks = editOutcome.getText().toString().trim();
        final String customers_tasks = editCustomers.getText().toString().trim();
        final String type_tasks = editType.getText().toString().trim();
        final String description_tasks = editDescription.getText().toString().trim();

        class UpdateEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tasks_Sub_Activity.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Tasks_Sub_Activity.this,s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(Konfigurasi.KEY_EMP_ID_TASKS,id);
                hashMap.put(Konfigurasi.KEY_EMP_SUBJECT_TASKS,subject_tasks);
                hashMap.put(Konfigurasi.KEY_EMP_STATUS_TASKS,status_tasks);
                hashMap.put(Konfigurasi.KEY_EMP_TANGGAL_TASKS,tanggal_tasks);

                hashMap.put(Konfigurasi.KEY_EMP_WAKTU_TASKS,waktu_tasks);
                hashMap.put(Konfigurasi.KEY_EMP_OUTCOME_TASKS,outcome_tasks);
                hashMap.put(Konfigurasi.KEY_EMP_CUSTOMERS_TASKS,customers_tasks);
                hashMap.put(Konfigurasi.KEY_EMP_TYPE_TASKS,type_tasks);
                hashMap.put(Konfigurasi.KEY_EMP_DESCRIPTION_TASKS,description_tasks);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Konfigurasi.URL_UPDATE_EMP,hashMap);

                return s;
            }
        }

        UpdateEmployee ue = new UpdateEmployee();
        ue.execute();
    }

    private void deleteEmployee(){
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tasks_Sub_Activity.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Tasks_Sub_Activity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Konfigurasi.URL_DELETE_EMP, id);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Anda Yakin Ingin Menghapus Tasks ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEmployee();
                        startActivity(new Intent(Tasks_Sub_Activity.this,Tasks_Activity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                updateEmployee();
                return true;
            }
            case R.id.action_delete: {
                confirmDeleteEmployee();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }

}
