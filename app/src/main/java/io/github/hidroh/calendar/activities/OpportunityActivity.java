package io.github.hidroh.calendar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.RequestHandler;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class OpportunityActivity extends AppCompatActivity implements ListView.OnItemClickListener, WaveSwipeRefreshLayout.OnRefreshListener{

    private ListView listViewOpp;

    private String JSON_STRING;

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity);

        listViewOpp = (ListView) findViewById(R.id.listViewOpp);
        listViewOpp.setOnItemClickListener(this);
        getJSON();
        initView();
        getSupportActionBar().setElevation(0);

        fab = (FloatingActionButton) findViewById(R.id.btnAddOpp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OpportunityActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OpportunityActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
                String s = rh.sendGetRequest(AppConfig.URL_GET_ALL_OPP);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showCustomers(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(AppConfig.RESULT);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String opp_id = jo.getString(AppConfig.OPP_ID);
                String opp_name = jo.getString(AppConfig.OPP_NAME);
                String opp_cust = jo.getString(AppConfig.OPP_CUST);
                String opp_st = jo.getString(AppConfig.OPP_ST);
                String opp_date = jo.getString(AppConfig.OPP_DATE);

                HashMap<String,String> opportunity = new HashMap<>();
                opportunity.put(AppConfig.OPP_ID, opp_id);
                opportunity.put(AppConfig.OPP_NAME, opp_name);
                opportunity.put(AppConfig.OPP_CUST, opp_cust);
                opportunity.put(AppConfig.OPP_ST, opp_st);
                opportunity.put(AppConfig.OPP_DATE, opp_date);

                list.add(opportunity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                OpportunityActivity.this, list, R.layout.list_item_opportunity,
                new String[]{AppConfig.OPP_ID, AppConfig.OPP_NAME, AppConfig.OPP_CUST, AppConfig.OPP_ST, AppConfig.OPP_DATE},
                new int[]{R.id.opp_id, R.id.opp_name, R.id.opp_cust, R.id.opp_st, R.id.opp_date});

        listViewOpp.setAdapter(adapter);
    }

    private void initView() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipeOpp);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setOnRefreshListener(this);
        mWaveSwipeRefreshLayout.setWaveColor(Color.rgb(239,83,80));

        listViewOpp = (ListView) findViewById(R.id.listViewOpp);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OpportunityDetailActivity.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String opp_id = map.get(AppConfig.OPP_ID).toString();
        intent.putExtra(AppConfig.OPP_ID, opp_id);
        startActivity(intent);
    }

    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
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
}
