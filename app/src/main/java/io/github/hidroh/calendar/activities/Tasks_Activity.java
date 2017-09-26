package io.github.hidroh.calendar.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Tasks_Activity extends AppCompatActivity implements ListView.OnItemClickListener, WaveSwipeRefreshLayout.OnRefreshListener{

    private ListView listView;

    private String JSON_STRING;

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tasks_ol_v2);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
        initView();
        getSupportActionBar().setElevation(0);
    }

    private void initView() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(Color.rgb(239,83,80));

        //mWaveSwipeRefreshLayout.setMaxDropHeight(1500);

        /*TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
          actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        mWaveSwipeRefreshLayout.setTopOffsetOfWave(actionBarHeight);*/

        listView = (ListView) findViewById(R.id.listView);
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


                HashMap<String,String> employees = new HashMap<>();
                employees.put(Konfigurasi.TAG_ID_TASKS,id_task);
                employees.put(Konfigurasi.TAG_SUBJECT_TASKS,subject_task);
                employees.put(Konfigurasi.TAG_STATUS_TASKS,status_tasks);
                employees.put(Konfigurasi.TAG_TANGGAL_TASKS,tanggal_tasks);

                list.add(employees);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                Tasks_Activity.this, list, R.layout.list_item,
                new String[]{Konfigurasi.TAG_ID_TASKS, Konfigurasi.TAG_SUBJECT_TASKS, Konfigurasi.TAG_STATUS_TASKS, Konfigurasi.TAG_TANGGAL_TASKS},
                new int[]{R.id.id_tasks_ol,R.id.subject_tasks, R.id.status_tasks, R.id.tanggal_tasks});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tasks_Activity.this,"Mengambil Data","Mohon Tunggu...",false,false);
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

    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        //mWaveSwipeRefreshLayout.setRefreshing(true);
        refresh();
        super.onResume();
    }
    @Override
    public void onRefresh() {
        refresh();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.navigation_add:
//                Intent myAplikasi1 = new Intent(this,Tasks_Add_Activity.class);
//                startActivity(myAplikasi1);
//                return true;
//
//            case R.id.navigation_draft:
//                Intent myAplikasi3 = new Intent(this,Tasks_List_Draft_Activity.class);
//                startActivity(myAplikasi3);
//                return true;
//
//
//
//            case R.id.navigation_exit:
//            {
//                keluarYN();//Pergi ke method exit
//            }
//            return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWaveSwipeRefreshLayout.setRefreshing(true);
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);

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
