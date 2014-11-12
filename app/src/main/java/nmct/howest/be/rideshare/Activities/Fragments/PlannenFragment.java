package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import nmct.howest.be.rideshare.Activities.Helpers.TimePickerHelper;
import nmct.howest.be.rideshare.R;

public class PlannenFragment extends Fragment
{
    //Variables
    private Switch repeatSwitch;
    private ToggleButton tglMonday;
    private ToggleButton tglTuesday;
    private ToggleButton tglWednesday;
    private ToggleButton tglThursday;
    private ToggleButton tglFriday;
    private ToggleButton tglSaturday;
    private ToggleButton tglSunday;
    static EditText txbDatePlan;
    static EditText txbTimePlan;


    //Ctor
    public PlannenFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plannen, container, false);

        //Get widgets
        repeatSwitch = (Switch) view.findViewById(R.id.repeatSwitch);
        tglMonday = (ToggleButton) view.findViewById(R.id.tglMaandag);
        tglTuesday = (ToggleButton) view.findViewById(R.id.tglDinsdag);
        tglWednesday = (ToggleButton) view.findViewById(R.id.tglWoensdag);
        tglThursday = (ToggleButton) view.findViewById(R.id.tglDonderdag);
        tglFriday = (ToggleButton) view.findViewById(R.id.tglVrijdag);
        tglSaturday = (ToggleButton) view.findViewById(R.id.tglZaterdag);
        tglSunday = (ToggleButton) view.findViewById(R.id.tglZondag);


        //Enable togglebuttons when switch is on
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    //Enable all weektogglebuttons
                    tglMonday.setEnabled(true);
                    tglTuesday.setEnabled(true);
                    tglWednesday.setEnabled(true);
                    tglThursday.setEnabled(true);
                    tglFriday.setEnabled(true);
                    tglSaturday.setEnabled(true);
                    tglSunday.setEnabled(true);
                } else {
                    //Disable all weektogglebuttons
                    tglMonday.setEnabled(false);
                    tglTuesday.setEnabled(false);
                    tglWednesday.setEnabled(false);
                    tglThursday.setEnabled(false);
                    tglFriday.setEnabled(false);
                    tglSaturday.setEnabled(false);
                    tglSunday.setEnabled(false);
                }
            }
        });


        //Date picker
        txbDatePlan = (EditText) view.findViewById(R.id.txtDatePlan);
        txbDatePlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        //Time picker
        txbTimePlan = (EditText) view.findViewById(R.id.txtTimePlan);
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
                date = "0" + day + "/";
            } else {
                date = day + "/";
            }
            if (month < 10) {
                date += "0" + month + "/";
            } else {
                date += month + "/";
            }

            date += year;

            Log.e("", date);

            txbDatePlan.setText(date.toString());
        }
    }

    //timepicker class
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerHelper.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerHelper(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + ":" + minute;

            txbTimePlan.setText(time.toString());
        }
    }
}
