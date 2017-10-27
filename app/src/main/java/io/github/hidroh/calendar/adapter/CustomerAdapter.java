package io.github.hidroh.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.hidroh.calendar.R;

/**
 * Created by singgih on 10/20/2017.
 */

public class CustomerAdapter extends ArrayAdapter<Customer>{
    //storing all the names in the list
    private List<Customer> customers;

    //context object
    private Context context;

    //constructor
    public CustomerAdapter(Context context, int resource, List<Customer> customers) {
        super(context, resource, customers);
        this.context = context;
        this.customers = customers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.customers, null, true);
        TextView textViewCust = (TextView) listViewItem.findViewById(R.id.textViewCust);

        ImageView imageViewStatusCust = (ImageView) listViewItem.findViewById(R.id.imageViewStatusCust);

        //getting the current customer
        Customer cust_id = customers.get(position);
        Customer customer = customers.get(position);


        if (customer.getIs_draft() == 0)
            //setting the customer to textview
            textViewCust.setText(customer.getCust_name());



        //if the synced status is 0 displaying
        //queued icon
        //else displaying synced icon
        if (customer.getIs_draft() == 0)
            imageViewStatusCust.setBackgroundResource(R.drawable.stopwatch);
        else
            imageViewStatusCust.setBackgroundResource(R.drawable.success);

        return listViewItem;
    }
}
