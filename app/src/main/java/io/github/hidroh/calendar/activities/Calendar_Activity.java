package io.github.hidroh.calendar.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.apps.Konfigurasi;
import io.github.hidroh.calendar.apps.RequestHandler;

public class Calendar_Activity extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;
    private String JSON_STRING;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        listView = (ListView) findViewById(R.id.main_list);
        listView.setOnItemClickListener(this);
        getJSON();

        getSupportActionBar().setElevation(0);
    }


    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id_task = jo.getString(Konfigurasi.TAG_ID_TASKS);
                String subject_task = jo.getString(Konfigurasi.TAG_SUBJECT_TASKS);
                String status_tasks = jo.getString(Konfigurasi.TAG_STATUS_TASKS);
                String tanggal_tasks = jo.getString(Konfigurasi.TAG_TANGGAL_TASKS);
                String waktu_tasks = jo.getString(Konfigurasi.TAG_WAKTU_TASKS);


                HashMap<String,String> employees = new HashMap<>();
                employees.put(Konfigurasi.TAG_ID_TASKS,id_task);
                employees.put(Konfigurasi.TAG_SUBJECT_TASKS,subject_task);
                employees.put(Konfigurasi.TAG_STATUS_TASKS,status_tasks);
                employees.put(Konfigurasi.TAG_TANGGAL_TASKS,tanggal_tasks);
                employees.put(Konfigurasi.TAG_WAKTU_TASKS,waktu_tasks);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                Calendar_Activity.this, list, R.layout.item_calendar_activity,
                new String[]{Konfigurasi.TAG_ID_TASKS, Konfigurasi.TAG_SUBJECT_TASKS, Konfigurasi.TAG_STATUS_TASKS, Konfigurasi.TAG_TANGGAL_TASKS,Konfigurasi.TAG_WAKTU_TASKS},
                new int[]{R.id.id_tasks_ol,R.id.subject_tasks, R.id.status_tasks, R.id.tanggal_tasks, R.id.waktu_tasks});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Calendar_Activity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Tasks_Sub_Activity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String empId = map.get(Konfigurasi.TAG_ID_TASKS).toString();
        intent.putExtra(Konfigurasi.EMP_ID,empId);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
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
