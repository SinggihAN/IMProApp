package io.github.hidroh.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import io.github.hidroh.calendar.adapter.Name;
import io.github.hidroh.calendar.utilities.DatabaseHelperTasks;
import io.github.hidroh.calendar.R;

public class Tasks_Detail_List_Draft_Activity extends AppCompatActivity implements OnClickListener {


	private TextView id_tasks,tanggal, waktu ;
    private TextView subject,customer,desc;
    private TextView outcome,type,status;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_draft_tasks_v1);

		DatabaseHelperTasks db = new DatabaseHelperTasks(this);


		Intent i = getIntent();
		id_tasks = (TextView) findViewById(R.id.id_tasks);
		subject = (TextView) findViewById(R.id.subject);
		status = (TextView) findViewById(R.id.status);
		tanggal = (TextView) findViewById(R.id.tanggal);
		waktu = (TextView) findViewById(R.id.waktu);
		outcome = (TextView) findViewById(R.id.outcome);
		customer = (TextView) findViewById(R.id.customers);
		type = (TextView) findViewById(R.id.type);
		desc = (TextView) findViewById(R.id.description);


		Name name1 = db.detailname(i.getStringExtra("pesan"));
		if (name1 != null) {
			id_tasks.setText(name1.getId_tasks());
			subject.setText(name1.getSubject_tasks());
			status.setText(name1.getStatus_tasks());
			tanggal.setText(name1.getTanggal_tasks());
			waktu.setText(name1.getWaktu_tasks());
			outcome.setText(name1.getOutcome_tasks());
			customer.setText(name1.getCustomers_tasks());
			type.setText(name1.getType_tasks());
			desc.setText(name1.getDescription_tasks());

		} else {
			id_tasks.setText("Data tidak ditemukan");
			subject.setText("Data tidak ditemukan");
            status.setText("Data tidak ditemukan");
			tanggal.setText("Data tidak ditemukan");
			waktu.setText("Data tidak ditemukan");
			outcome.setText("Data tidak ditemukan");
			customer.setText("Data tidak ditemukan");
			type.setText("Data tidak ditemukan");
			desc.setText("Data tidak ditemukan");

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
