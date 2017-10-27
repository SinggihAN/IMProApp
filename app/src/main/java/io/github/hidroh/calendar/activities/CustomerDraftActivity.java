package io.github.hidroh.calendar.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.github.hidroh.calendar.R;
import io.github.hidroh.calendar.adapter.Customer;
import io.github.hidroh.calendar.adapter.CustomerAdapter;
import io.github.hidroh.calendar.apps.CustomerDraftChecker;
import io.github.hidroh.calendar.utilities.DatabaseHelperCustomer;

public class CustomerDraftActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    AutoCompleteTextView auto;
    /*
    * this is the url to our webservice
    * make sure you are using the ip instead of localhost
    * it will not work if you are using localhost
    * */

    //database helper object
    private DatabaseHelperCustomer db;

    //View objects

    private ListView listViewCust;

    //List to store all the names
    private List<Customer> customers;
    //1 means data is synced and 0 means data is not synced
    public static final int CUST_SYNCED_WITH_SERVER = 1;
    public static final int CUST_NOT_SYNCED_WITH_SERVER = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "Data Saved";
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;
    //adapterobject for list view
    private CustomerAdapter customerAdapter;

    private EditText editid_cust;
    private String cust_id;

    DatabaseHelperCustomer dbcenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_draft);

        registerReceiver(new CustomerDraftChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //initializing views and objects
        db = new DatabaseHelperCustomer(this);
        customers = new ArrayList<>();
        listViewCust = (ListView) findViewById(R.id.listViewCust);
        listViewCust.setOnItemClickListener(this);


        //calling the method to load all the stored names
        loadCust();
        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                loadCust();
            }
        };
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


    }

    /*
    * this method will
    * load the names from the database
    * with updated sync status
    * */
    private void loadCust() {
        customers.clear();
        Cursor cursor = db.getCustomers();
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer(
                        cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_NAME)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_ST)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_PHONE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_DESC)),

                        cursor.getInt(cursor.getColumnIndex(DatabaseHelperCustomer.CUST_IS_DRAFT))
                );
                customers.add(customer);
            } while (cursor.moveToNext());
        }

        customerAdapter = new CustomerAdapter(this, R.layout.customers, customers);
        listViewCust.setAdapter(customerAdapter);
    }



    @Override
    public void onClick(View view) {

    }


    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

        final Customer n = (Customer) parent.getItemAtPosition(position);

        final CharSequence[] dialogitem = {"Lihat Draft","Edit Draft"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDraftActivity.this);
        builder.setTitle("Pilihan");
        builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item) {
                    case 0:
                        Intent i = new Intent(CustomerDraftActivity.this, Tasks_Detail_List_Draft_Activity.class);
                        i.putExtra("pesan", n.getCust_id());
                        startActivity(i);
                        break;
                    case 1:

                }
            }
        });
        builder.create().show();
    }
}
