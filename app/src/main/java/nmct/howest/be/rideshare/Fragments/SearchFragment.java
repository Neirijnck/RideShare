package nmct.howest.be.rideshare.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.R;

public class SearchFragment extends Fragment
{
    //Variables
    private Button btnSearch;
    static EditText txbDatePlan;
    static EditText txbTimePlan;
    private EditText txtFrom;
    private EditText txtTo;
    private EditText txtDate;
    private EditText txtTime;
    private CheckBox chkShare;

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        txtFrom = (EditText) view.findViewById(R.id.txtFromSearch);
        txtTo = (EditText) view.findViewById(R.id.txtToSearch);
        txbDatePlan = (EditText) view.findViewById(R.id.txtDateSearch);
        //txtDate = (EditText) view.findViewById(R.id.txtDateSearch);
        txbTimePlan = (EditText) view.findViewById(R.id.txtTimeSearch);
        //txtTime = (EditText) view.findViewById(R.id.txtTimeSearch);
        chkShare = (CheckBox) view.findViewById(R.id.chkShareOnFacebook);

        //SearchActivity
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //Required fields should not be empty
                if(TextUtils.isEmpty(txtFrom.getText().toString())||TextUtils.isEmpty(txtTo.getText().toString())||TextUtils.isEmpty(txbDatePlan.getText().toString())||TextUtils.isEmpty(txbTimePlan.getText().toString()))
                {
                    Toast.makeText(getActivity(), "Vul alle verplichte velden in", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    Bundle b = new Bundle();
                    //Enter all parameters
                    b.putString("from", txtFrom.getText().toString());
                    b.putString("to", txtTo.getText().toString());
                    b.putString("date", txbDatePlan.getText().toString());
                    b.putString("time", txbTimePlan.getText().toString());
                    if(chkShare.isChecked()){b.putBoolean("share", true);}
                    else{b.putBoolean("share", false);}

                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        //Date picker
        txbDatePlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        //Time picker
        txbTimePlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Datepicker class
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String date;
            month++;
            if (day < 10) {
                date = "0" + day + "-";
            } else {
                date = day + "-";
            }
            if (month < 10) {
                date += "0" + month + "-";
            } else {
                date += month + "-";
            }

            date += year;

            Log.e("", date);

            txbDatePlan.setText(date);
        }
    }

    //timepicker class
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time;
            if (hourOfDay < 10) {
                time = "0" + hourOfDay + ":";
            } else {
                time = hourOfDay + ":";
            }
            if (minute < 10) {
                time += "0" + minute;
            } else {
                time += minute;
            }

            txbTimePlan.setText(time);
        }
    }

}
