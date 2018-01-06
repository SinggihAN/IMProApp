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
 * Created by singgih on 11/6/2017.
 */

public class TaskAdapter extends ArrayAdapter<Task> {
    private List<Task> tasks;
    //context object
    private Context context;

    //constructor
    public TaskAdapter(Context context, int resource, List<Task> tasks) {
        super(context, resource, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //getting the layoutinflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //getting listview itmes
        View listViewItem = inflater.inflate(R.layout.tasks, null, true);
        TextView textViewTask = (TextView) listViewItem.findViewById(R.id.textViewTask);

        ImageView imageViewStatus = (ImageView) listViewItem.findViewById(R.id.imgTaskStatus);

        //getting the current task
        Task task_id = tasks.get(position);
        Task task = tasks.get(position);


        if (task.getStatus() == 0)
            //setting the task to textview
            textViewTask.setText(task.getTask_name());



        //if the synced status is 0 displaying
        //queued icon
        //else displaying synced icon
        if (task.getStatus() == 0)
            imageViewStatus.setBackgroundResource(R.drawable.stopwatch);
        else
            imageViewStatus.setBackgroundResource(R.drawable.success);

        return listViewItem;
    }
}
