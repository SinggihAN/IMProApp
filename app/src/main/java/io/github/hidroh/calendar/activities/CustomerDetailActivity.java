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
import android.widget.TextView;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
                String s = rh.sendGetRequestParam(AppConfig.URL_GET_CUST, custId + AppConfig.API_KEY);
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
        final String custName = cust_name.getText().toString().trim();
        final String custSt = cust_st.getText().toString().trim();
        final String custPhone = cust_phone.getText().toString().trim();

        final String custAddress = cust_address.getText().toString().trim();
        final String custDesc = cust_desc.getText().toString().trim();

        class UpdateCustomer extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CustomerDetailActivity.this,"Updating...","Wait...",false,false);
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
                        Toast.makeText(CustomerDetailActivity.this, aJsonString, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CustomerDetailActivity.this, "Customer Updated!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(AppConfig.CUST_ID, custId);
                hashMap.put(AppConfig.CUST_NAME, custName);
                hashMap.put(AppConfig.CUST_ST, custSt);
                hashMap.put(AppConfig.CUST_PHONE, custPhone);
                hashMap.put(AppConfig.CUST_ADDRESS, custAddress);
                hashMap.put(AppConfig.CUST_DESC, custDesc);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_UPDATE_CUST + custId + AppConfig.API_KEY, hashMap);

                return s;
            }
        }

        UpdateCustomer ue = new UpdateCustomer();
        ue.execute();

    }
    public void confirmDeleteCustomer(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to delete this customer?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteCustomer();
                        startActivity(new Intent(CustomerDetailActivity.this, CustomerActivity.class));
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

    private void deleteCustomer(){
        class DeleteCustomer extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CustomerDetailActivity.this, "Deleting...", "Wait...", false, false);
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
                        Toast.makeText(CustomerDetailActivity.this, aJsonString, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(CustomerDetailActivity.this, "Customer Deleted!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(AppConfig.URL_DELETE_CUST + custId + AppConfig.API_KEY);
                return s;
            }
        }

        DeleteCustomer de = new DeleteCustomer();
        de.execute();
    }
}
