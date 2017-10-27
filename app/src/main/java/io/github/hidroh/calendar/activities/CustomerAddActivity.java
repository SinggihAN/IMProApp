package io.github.hidroh.calendar.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.adapter.Customer;
import io.github.hidroh.calendar.adapter.CustomerAdapter;
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.CustomerDraftChecker;
import io.github.hidroh.calendar.utilities.DatabaseHelperCustomer;
import io.github.hidroh.calendar.utilities.VolleySingleton;

public class CustomerAddActivity extends AppCompatActivity {
    /*
    * this is the url to our webservice
    * make sure you are using the ip instead of localhost
    * it will not work if you are using localhost
    * */
    //database helper object
    private DatabaseHelperCustomer db;
    //View objects
    private EditText txtCustName, txtCustPhone, txtCustAddress, txtCustDesc;
    private  Button btnCreateCust;
    private MaterialBetterSpinner txtCustSt;
    private LinearLayout parent1;


    private List<Customer> customers;

    public static final int CUST_SYNCED_WITH_SERVER = 1;
    public static final int CUST_NOT_SYNCED_WITH_SERVER = 0;

    public static final String DATA_SAVED_BROADCAST = "Data Saved";

    private BroadcastReceiver broadcastReceiver;

    private CustomerAdapter customerAdapter;
    private CoordinatorLayout coordinatorLayout;

    String[] SPINNER_DATA = {"Active", "Inactive"};

    private boolean err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);
        getSupportActionBar();

        txtCustSt = (MaterialBetterSpinner)findViewById(R.id.txtCustSt);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomerAddActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        txtCustSt.setAdapter(adapter);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        err = false;

        db = new DatabaseHelperCustomer(this);
        customers = new ArrayList<>();

        txtCustName = (EditText) findViewById(R.id.txtCustName);
        txtCustPhone = (EditText) findViewById(R.id.txtCustPhone);
        txtCustAddress = (EditText) findViewById(R.id.txtCustAddress);
        txtCustDesc = (EditText) findViewById(R.id.txtCustDesc);

        txtCustName.addTextChangedListener(new MyTextWatcher(txtCustName));
        txtCustPhone.addTextChangedListener(new MyTextWatcher(txtCustPhone));
        txtCustAddress.addTextChangedListener(new MyTextWatcher(txtCustAddress));
        txtCustDesc.addTextChangedListener(new MyTextWatcher(txtCustDesc));

        btnCreateCust = (Button) findViewById(R.id.btnCreateCust);
        btnCreateCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCustomerToServer();
            }
        });
    }

    /*
    * this method will
    * load the names from the database
    * with updated sync status
    * */
    private void loadCustomers() {
        customers.clear();
        Cursor cursor = db.getCustomers();
        customerAdapter = new CustomerAdapter(this, R.layout.customers, customers);
    }

    //saving the name to local storage
    private void saveCustomerToLocalStorage(String custId,String custName, String custSt, String custPhone, String custAddress, String custDesc, int status) {

        txtCustName.setText("");
        txtCustPhone.setText("");
        txtCustAddress.setText("");
        txtCustDesc.setText("");
        txtCustSt.setText("");

        db.addCustomer(custId, custName, custSt, custPhone, custAddress, custDesc, status);
        Customer n = new Customer(custId, custName, custSt,  custPhone, custAddress, custDesc, status);
        customers.add(n);
        refreshList();
    }

    /*
    * this method will simply refresh the list
    * */
    private void refreshList() {customerAdapter.notifyDataSetChanged();}

    /*
    * this method is saving the name to ther server
    * */
    private void saveCustomerToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Customer's data...");
        progressDialog.show();

        final String custId = "";
        final String custName = txtCustName.getText().toString().trim();
        final String custSt = txtCustSt.getText().toString();
        final String custPhone = txtCustPhone.getText().toString().trim();
        final String custAddress = txtCustAddress.getText().toString().trim();
        final String custDesc = txtCustDesc.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SAVE_CUST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveCustomerToLocalStorage(custId, custName, custSt, custPhone, custAddress, custDesc, CUST_SYNCED_WITH_SERVER);


                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
//                                saveCustomerToLocalStorage(custId, custName, custSt, custPhone, custAddress, custDesc, CUST_NOT_SYNCED_WITH_SERVER);
                                err = true;
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
//                        saveCustomerToLocalStorage(custId, custName, custSt, custPhone, custAddress, custDesc, CUST_NOT_SYNCED_WITH_SERVER);
                        err = true;
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cust_id", custId);
                params.put("cust_name", custName);
                params.put("cust_st", custSt);
                params.put("cust_phone", custPhone);
                params.put("cust_address", custAddress);
                params.put("cust_desc", custDesc);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        if (err) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Error while saving data, Check your internet connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            Intent callback = new Intent(CustomerAddActivity.this,CustomerActivity.class);
            CustomerAddActivity.this.startActivity(callback);
        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}
