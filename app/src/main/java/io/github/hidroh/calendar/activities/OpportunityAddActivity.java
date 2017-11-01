package io.github.hidroh.calendar.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.adapter.CustomerAdapter;
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.RequestHandler;
import io.github.hidroh.calendar.utilities.VolleySingleton;

public class OpportunityAddActivity extends AppCompatActivity implements View.OnClickListener {

    private  Button btnCreateOpp;
    private TextView txtOppDate;
    private int mYear, mMonth, mDay, mHour, mMinute;

    protected EditText opp_name, opp_prob;
    private MaterialBetterSpinner opp_cust, opp_st, opp_stage;

    protected TextView opp_id, oppAddCust;
    protected String oppId;

    private MaterialBetterSpinner txtOppCust, txtOppStage, txtOppSt;
    private CustomerAdapter opportunityAdapter;
    private CoordinatorLayout coordinatorLayout;

    private String JSON_STRING;

    String[] SPINNER_ST = {"Open", "Negotiation", "Closed"};
    String[] SPINNER_STAGE = {"RFQ", "Negotiation", "Closed"};

    private String OppStKey;
    private int int_opp_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_add);

        btnCreateOpp = (Button) findViewById(R.id.btnCreateOpp);

        btnCreateOpp.setOnClickListener(this);

        txtOppDate = (TextView) findViewById(R.id.txtOppDate);

        txtOppDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDatePicker();
            }
        });

        opp_name = (EditText) findViewById(R.id.txtOppName);
        opp_prob = (EditText) findViewById(R.id.txtOppProb);

        opp_cust = (MaterialBetterSpinner) findViewById(R.id.txtOppCust);
        opp_st = (MaterialBetterSpinner) findViewById(R.id.txtOppSt);
        opp_stage = (MaterialBetterSpinner) findViewById(R.id.txtOppStage);

        getCustomer();

        oppAddCust = (TextView) findViewById(R.id.oppAddCust);
        ArrayAdapter<String> stAdapter = new ArrayAdapter<String>(OpportunityAddActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_ST);
        opp_st.setAdapter(stAdapter);

        ArrayAdapter<String> stageAdapter = new ArrayAdapter<String>(OpportunityAddActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_STAGE);
        opp_stage.setAdapter(stageAdapter);
    }

    private static class StringWithTag {
        public String string;
        public Object tag;

        public StringWithTag(String string, Object tag) {
            this.string = string;
            this.tag = tag;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    private void getCustomer(){
        class GetCustomer extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OpportunityAddActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
        ArrayList<String> cust_ids = new ArrayList<String>();
        HashMap<Integer, String> mOppCust = new HashMap<Integer, String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(AppConfig.RESULT);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String opp_id = jo.getString(AppConfig.CUST_ID);
                String opp_name = jo.getString(AppConfig.CUST_NAME);

                cust_ids.add(opp_id);
                cust_names.add(opp_name);

                try{
                    int_opp_id = Integer.parseInt(opp_id);
                } catch (NumberFormatException nfe) {

                }
                mOppCust.put(int_opp_id, opp_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<StringWithTag> itemList = new ArrayList<StringWithTag>();

        for (Map.Entry<Integer, String> entry : mOppCust.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            itemList.add(new StringWithTag(value, key));
        }

        ArrayAdapter<StringWithTag> custAdapter = new ArrayAdapter<StringWithTag>(OpportunityAddActivity.this, android.R.layout.simple_dropdown_item_1line, itemList);
        opp_cust.setAdapter(custAdapter);

        opp_cust.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StringWithTag swt = (StringWithTag) adapterView.getItemAtPosition(i);
                Integer key = (Integer) swt.tag;
                oppAddCust.setText(String.valueOf(key));
            }
        });
    }

    public void getDatePicker(){

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        txtOppDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    /*
    * this method is saving the name to ther server
    * */
    private void saveOpportunityToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Customer's data...");
        progressDialog.show();

        final String oppName = opp_name.getText().toString().trim();
        final String oppCust = "1";
        final String oppID = "";
        final String custId = oppAddCust.getText().toString().trim();
        final String oppSt = opp_st.getText().toString().trim();
        final String oppProb = opp_prob.getText().toString().trim();
        final String oppDate = txtOppDate.getText().toString().trim();
        final String oppStage = opp_stage.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SAVE_OPP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success

                            } else {
                                //if there is some error
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
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opp_id", oppID);
                params.put("opp_name", oppName);
                params.put("cust_id", custId);
                params.put("opp_st", oppSt);
                params.put("opp_stage", oppStage);
                params.put("opp_prob", oppProb);
                params.put("opp_date", oppDate);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        Intent callback = new Intent(OpportunityAddActivity.this, OpportunityActivity.class);
        OpportunityAddActivity.this.startActivity(callback);
    }
    @Override
    public void onClick(View v) {
        saveOpportunityToServer();
    }

}
