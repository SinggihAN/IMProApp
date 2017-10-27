package io.github.hidroh.calendar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.apps.AppConfig;
import io.github.hidroh.calendar.apps.Konfigurasi;

public class CustomerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    protected EditText cust_name, cust_phone, cust_address, cust_desc;

    private Button btnupdateCustDetail;
    private Button btndeleteCustDetail;

    protected   TextView cust_id;
    protected String custId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        Intent intent = getIntent();

        custId = intent.getStringExtra(AppConfig.CUST_ID);

        cust_id = (TextView) findViewById(R.id.txtCustIdDetail);
        cust_name = (EditText) findViewById(R.id.input_custNameDetail);
        cust_phone = (EditText) findViewById(R.id.input_custPhoneDetail);
        cust_address = (EditText) findViewById(R.id.input_custAddress);
        cust_desc = (EditText) findViewById(R.id.input_custDescDetail);

        btnupdateCustDetail = (Button) findViewById(R.id.btnupdateCustDetail);
        btndeleteCustDetail = (Button) findViewById(R.id.btndeleteCustDetail);

        btnupdateCustDetail.setOnClickListener(this);
        btndeleteCustDetail.setOnClickListener(this);

        cust_id.setText(custId);
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
