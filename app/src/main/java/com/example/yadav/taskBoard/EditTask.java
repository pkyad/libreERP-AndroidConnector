package com.example.yadav.taskBoard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.libreerp.Helper;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;


public class EditTask extends AppCompatActivity {

    Task task;
    EditText titleEdit;
    EditText descriptionEdit;
    Button dueDateEdit;
    Button save;
    Context context;
    DBHandler dba;
    Button dueDateTime;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_task);

        task = getIntent().getParcelableExtra("Task_Object");
        titleEdit = (EditText) findViewById(R.id.title_task_edit);
        descriptionEdit = (EditText) findViewById(R.id.description_task_edit);
        dueDateEdit = (Button) findViewById(R.id.dueDate_task_edit);
        dueDateTime = (Button) findViewById(R.id.dueDate_time_task_edit);
        save = (Button) findViewById(R.id.save);
        titleEdit.setText(task.getTitle());
        descriptionEdit.setText(task.getDescription());

        Date dueDateDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dueDateDate = format.parse(task.getDueDate());
        } catch (ParseException e) {
            System.out.println("error while parsing");
        }
        dueDateEdit.setText(new SimpleDateFormat("dd-MM-yyyy").format(dueDateDate));
        dueDateTime.setText(new SimpleDateFormat("HH:mm:ss").format(dueDateDate));
        context = getApplicationContext();
        dba = new DBHandler(context, null, null, 2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditedTask();
            }
        });



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        String[] splitedTime = dueDateTime.getText().toString().split(":");
        final int hour = Integer.parseInt(splitedTime[0]);
        final int minute = Integer.parseInt(splitedTime[1]);

        dueDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedTime = selectedHour + ":" + selectedMinute + ":00";
                        if(selectedHour>=0 && selectedHour<=9){
                            String newselectedHour = "0" + selectedHour;
                            selectedTime = newselectedHour + ":" + selectedMinute + ":00";
                        }
                        if(selectedMinute>=0 && selectedMinute<=9){
                            String newselectedMinute = "0" + selectedMinute;
                            selectedTime = selectedHour + ":" + newselectedMinute + ":00";
                        }
                        dueDateTime.setText(selectedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        dueDateEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditTask.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dueDateEdit.setText(sdf.format(myCalendar.getTime()));
    }
    public void saveEditedTask() {
        new AlertDialog.Builder(this)
                .setTitle("Save Task")
                .setMessage("Are you sure you want to make the following changes?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Helper helper = new Helper(context);
                        AsyncHttpClient client = helper.getHTTPClient();
                        RequestParams params = new RequestParams();
                        params.put("title", titleEdit.getText().toString());
                        params.put("description", descriptionEdit.getText().toString());

                        params.put("dueDate", dueDateEdit.getText().toString() + "T" + dueDateTime.getText().toString() + "Z");
                        final String url = String.format("%s/%s/", helper.serverURL, "/api/taskBoard/task/" + task.getPk() + "/");
                        client.patch(url, params, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                dba.updateTask(task.getPk(), titleEdit.getText().toString(), descriptionEdit.getText().toString(), dueDateEdit.getText().toString()+ " " + dueDateTime.getText().toString());
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("GotoTODO", true);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject response) {
                                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                System.out.println("failure");
                                System.out.println(statusCode);
                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
