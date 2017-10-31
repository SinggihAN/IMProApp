package io.github.hidroh.calendar.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.adapter.CustomerAdapter;
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.RequestHandler;

public class OpportunityDetailActivity extends AppCompatActivity implements View.OnClickListener {

    protected EditText opp_name, opp_prob, opp_date;
    private MaterialBetterSpinner opp_cust, opp_st, opp_stage;

    private Button btnupdateOppDetail;
    private Button btndeleteOppDetail;

    protected TextView opp_id;
    protected String oppId;

    private MaterialBetterSpinner txtOppCustDetail, txtOppStageDetail, txtOppStDetail;
    private CustomerAdapter opportunityAdapterDetail;
    private CoordinatorLayout coordinatorLayoutDetail;

    private String JSON_STRING;

    String[] SPINNER_ST = {"Open", "Negotiation", "Closed"};
    String[] SPINNER_STAGE = {"RFQ", "Negotiation", "Closed"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_detail);
        Intent intent = getIntent();

        oppId = intent.getStringExtra(AppConfig.OPP_ID);

        opp_id = (TextView) findViewById(R.id.txtOppIdDetail);
        opp_name = (EditText) findViewById(R.id.txtOppNameDetail);
        opp_prob = (EditText) findViewById(R.id.txtOppProbDetail);
        opp_date = (EditText) findViewById(R.id.txtOppDateDetail);

        opp_cust = (MaterialBetterSpinner) findViewById(R.id.txtOppCustDetail);
        opp_st = (MaterialBetterSpinner) findViewById(R.id.txtOppStDetail);
        opp_stage = (MaterialBetterSpinner) findViewById(R.id.txtOppStageDetail);

        btnupdateOppDetail = (Button) findViewById(R.id.btnupdateOppDetail);
        btndeleteOppDetail = (Button) findViewById(R.id.btndeleteOppDetail);

        btnupdateOppDetail.setOnClickListener(this);
        btndeleteOppDetail.setOnClickListener(this);

        opp_id.setText(oppId);

        getCustomer();

        ArrayAdapter<String> stAdapter = new ArrayAdapter<String>(OpportunityDetailActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_ST);
        opp_st.setAdapter(stAdapter);

        ArrayAdapter<String> stageAdapter = new ArrayAdapter<String>(OpportunityDetailActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_STAGE);
        opp_stage.setAdapter(stageAdapter);

        getOpportunity();
    }

    private void getCustomer(){
        class GetCustomer extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OpportunityDetailActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
                String s = rh.sendGetRequest(AppConfig.URL_GET_CUST_NAME);
                return s;
            }
        }
        GetCustomer gj = new GetCustomer();
        gj.execute();
    }

    private void showCustomers(){
        JSONObject jsonObject = null;
        ArrayList<String> cust_names = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(AppConfig.RESULT);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String opp_name = jo.getString(AppConfig.CUST_NAME);
                cust_names.add(opp_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> custAdapter = new ArrayAdapter<String>(OpportunityDetailActivity.this, android.R.layout.simple_dropdown_item_1line, cust_names);
        opp_cust.setAdapter(custAdapter);
    }

    private void getOpportunity(){
        class GetOpportunity extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OpportunityDetailActivity.this,"Fetching data...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showOpportunity(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(AppConfig.URL_GET_OPP, oppId + AppConfig.API_KEY);
                return s;
            }
        }
        GetOpportunity gc = new GetOpportunity();
        gc.execute();
    }

    private void showOpportunity(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject(AppConfig.RESULT);
            String oppName = result.getString(AppConfig.OPP_NAME);
            String oppCust = result.getString(AppConfig.OPP_CUST);
            String oppSt = result.getString(AppConfig.OPP_ST);

            String oppProb = result.getString(AppConfig.OPP_PROB);
            String oppStage = result.getString(AppConfig.OPP_STAGE);
            String oppDate = result.getString(AppConfig.OPP_DATE);

            opp_name.setText(oppName);
            opp_cust.setText(oppCust);
            opp_st.setText(oppSt);

            opp_prob.setText(oppProb);
            opp_stage.setText(oppStage);
            opp_date.setText(oppDate);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnupdateOppDetail){
            updateOpportunity();
        }

        if(v == btndeleteOppDetail){
            confirmDeleteOpportunity();
        }
    }

    public void  updateOpportunity(){
        final String oppName = opp_name.getText().toString().trim();
//        final String oppCust = opp_cust.getText().toString().trim();
        final String oppSt = opp_st.getText().toString().trim();
        final String oppProb = opp_prob.getText().toString().trim();
        final String oppDate = opp_date.getText().toString().trim();
        final String oppStage = opp_stage.getText().toString().trim();

        class UpdateOpportunity extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OpportunityDetailActivity.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSONObject jObject;
                try {
                    jObject = new JSONObject(s);
                    if (jObject.has("error")) {
                        String aJsonString = jObject.getString("error");
                        Toast.makeText(OpportunityDetailActivity.this, aJsonString, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OpportunityDetailActivity.this, "Opportunity Updated!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(AppConfig.OPP_ID, oppId);
                hashMap.put(AppConfig.OPP_NAME, oppName);
                hashMap.put(AppConfig.OPP_ST, oppSt);
                hashMap.put(AppConfig.OPP_PROB, oppProb);
                hashMap.put(AppConfig.OPP_STAGE, oppStage);
                hashMap.put(AppConfig.OPP_DATE, oppDate);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_UPDATE_OPP+ oppId + AppConfig.API_KEY, hashMap);

                return s;
            }
        }

        UpdateOpportunity ue = new UpdateOpportunity();
        ue.execute();

    }

    public void confirmDeleteOpportunity() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to delete this opportunity?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteOpportunity();
                        startActivity(new Intent(OpportunityDetailActivity.this, OpportunityActivity.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteOpportunity(){
        class DeleteOpportunity extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OpportunityDetailActivity.this, "Deleting...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSONObject jObject;
                try {
                    jObject = new JSONObject(s);
                    if (jObject.has("error")) {
                        String aJsonString = jObject.getString("error");
                        Toast.makeText(OpportunityDetailActivity.this, aJsonString, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OpportunityDetailActivity.this, "Opportunity Deleted!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(AppConfig.URL_DELETE_OPP + oppId + AppConfig.API_KEY);
                return s;
            }
        }

        DeleteOpportunity de = new DeleteOpportunity();
        de.execute();
    }
}
