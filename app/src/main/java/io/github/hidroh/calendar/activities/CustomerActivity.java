package io.github.hidroh.calendar.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.support.design.widget.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.RequestHandler;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class CustomerActivity extends AppCompatActivity implements ListView.OnItemClickListener, WaveSwipeRefreshLayout.OnRefreshListener{

    private ListView listViewCust;

    private String JSON_STRING;

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        listViewCust = (ListView) findViewById(R.id.listViewCust);
        listViewCust.setOnItemClickListener(this);
        getJSON();
        initView();
        getSupportActionBar().setElevation(0);

        fab = (FloatingActionButton) findViewById(R.id.btnAddCust);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this, CustomerAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipeCust);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(Color.rgb(239,83,80));

        listViewCust = (ListView) findViewById(R.id.listViewCust);
    }

    private void showCustomers(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(AppConfig.RESULT);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String cust_id = jo.getString(AppConfig.CUST_ID);
                String cust_name = jo.getString(AppConfig.CUST_NAME);
                String cust_st = jo.getString(AppConfig.CUST_ST);
                String cust_phone = jo.getString(AppConfig.CUST_PHONE);
                String cust_address = jo.getString(AppConfig.CUST_ADDRESS);
                String cust_desc = jo.getString(AppConfig.CUST_DESC);

                HashMap<String,String> customers = new HashMap<>();
                customers.put(AppConfig.CUST_ID,cust_id);
                customers.put(AppConfig.CUST_NAME,cust_name);
                customers.put(AppConfig.CUST_ST,cust_st);
                customers.put(AppConfig.CUST_PHONE,cust_phone);
                customers.put(AppConfig.CUST_ADDRESS,cust_address);
                customers.put(AppConfig.CUST_DESC,cust_desc);

                list.add(customers);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                CustomerActivity.this, list, R.layout.list_item_customer,
                new String[]{AppConfig.CUST_ID, AppConfig.CUST_NAME, AppConfig.CUST_ST, AppConfig.CUST_PHONE, AppConfig.CUST_ADDRESS, AppConfig.CUST_DESC},
                new int[]{R.id.cust_id, R.id.cust_name, R.id.cust_st, R.id.cust_phone, R.id.cust_address, R.id.cust_desc});

        listViewCust.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CustomerActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showCustomers();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(AppConfig.URL_GET_ALL_CUST);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CustomerDetailActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String cust_id = map.get(AppConfig.CUST_ID).toString();
        intent.putExtra(AppConfig.CUST_ID,cust_id);
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
