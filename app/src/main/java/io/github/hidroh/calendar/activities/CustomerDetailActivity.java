package io.github.hidroh.calendar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.adapter.CustomerAdapter;
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.Konfigurasi;
import io.github.hidroh.calendar.apps.RequestHandler;

public class CustomerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    protected EditText cust_name, cust_phone, cust_address, cust_desc;
    private MaterialBetterSpinner cust_st;

    private Button btnupdateCustDetail;
    private Button btndeleteCustDetail;

    protected   TextView cust_id;
    protected String custId;

    private MaterialBetterSpinner txtCustStDetail;
    private CustomerAdapter customerAdapterDetail;
    private CoordinatorLayout coordinatorLayoutDetail;

    String[] SPINNER_DATA = {"Active", "Inactive"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Intent intent = getIntent();

        custId = intent.getStringExtra(AppConfig.CUST_ID);

        cust_id = (TextView) findViewById(R.id.txtCustIdDetail);
        cust_name = (EditText) findViewById(R.id.txtCustNameDetail);
        cust_phone = (EditText) findViewById(R.id.txtCustPhoneDetail);
        cust_address = (EditText) findViewById(R.id.txtCustAddressDetail);
        cust_desc = (EditText) findViewById(R.id.txtCustDescDetail);

        cust_st = (MaterialBetterSpinner) findViewById(R.id.txtCustStDetail);

        btnupdateCustDetail = (Button) findViewById(R.id.btnupdateCustDetail);
        btndeleteCustDetail = (Button) findViewById(R.id.btndeleteCustDetail);

        btnupdateCustDetail.setOnClickListener(this);
        btndeleteCustDetail.setOnClickListener(this);

        cust_id.setText(custId);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerDetailActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        cust_st.setAdapter(adapter);

        getCustomer();
    }

    private void getCustomer(){
        class GetCustomer extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CustomerDetailActivity.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showCustomer(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(AppConfig.URL_GET_CUST,custId + AppConfig.API_KEY);
                return s;
            }
        }
        GetCustomer gc = new GetCustomer();
        gc.execute();
    }

    private void showCustomer(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject(AppConfig.RESULT);
            String custName = result.getString(AppConfig.CUST_NAME);
            String custSt = result.getString(AppConfig.CUST_ST);
            String custPhone = result.getString(AppConfig.CUST_PHONE);

            String custAddress = result.getString(AppConfig.CUST_ADDRESS);
            String custDesc = result.getString(AppConfig.CUST_DESC);

            cust_name.setText(custName);
            cust_st.setText(custSt);
            cust_phone.setText(custPhone);

            cust_address.setText(custAddress);
            cust_desc.setText(custDesc);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if(v == btnupdateCustDetail){
            updateCustomer();
        }

        if(v == btndeleteCustDetail){
            confirmDeleteCustomer();
        }
    }

    public void updateCustomer(){

    }
    public void confirmDeleteCustomer(){

    }
}
