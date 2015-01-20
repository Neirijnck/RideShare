package nmct.howest.be.rideshare.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.Loaders.Json.PlaceJSONParser;
import nmct.howest.be.rideshare.Loaders.Tasks.PlacesTask;
import nmct.howest.be.rideshare.R;

public class SearchFragment extends Fragment
{
    //Variables
    private Button btnSearch;
    static EditText txtDateSearch;
    static EditText txtTimeSearch;
    private AutoCompleteTextView txtFromSearch;
    private AutoCompleteTextView txtToSearch;
    private EditText txtDate;
    private EditText txtTime;
    private CheckBox chkShare;

    HashMap<String, String> fromLoc = new HashMap<String, String>();
    HashMap<String, String> toLoc = new HashMap<String, String>();

    public SearchFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        txtFromSearch = (AutoCompleteTextView) view.findViewById(R.id.txtFromSearch);
        txtToSearch = (AutoCompleteTextView) view.findViewById(R.id.txtToSearch);
        txtDateSearch = (EditText) view.findViewById(R.id.txtDateSearch);
        txtTimeSearch = (EditText) view.findViewById(R.id.txtTimeSearch);
        chkShare = (CheckBox) view.findViewById(R.id.chkShareOnFacebook);


        //From Google Maps
        txtFromSearch.setThreshold(2);
        txtFromSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s != "" && start >= 2) {
                    Log.d("request send", count + " -- " + s.toString());
                    PlacesTask placesTask = new PlacesTask(txtFromSearch, getActivity());
                    placesTask.execute(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Auto-generated method stub
            }
        });
        txtFromSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                fromLoc = PlaceJSONParser.PLACES.get(pos);
            }
        });


        //To Google Maps
        txtToSearch.setThreshold(2);
        txtToSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s != "" && start >= 2) {
                    PlacesTask placesTask = new PlacesTask(txtToSearch, getActivity());
                    placesTask.execute(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Auto-generated method stub
            }
        });
        txtToSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                toLoc = PlaceJSONParser.PLACES.get(pos);
            }
        });

        //SearchActivity
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                //Required fields should not be empty
                if(TextUtils.isEmpty(txtFromSearch.getText().toString())||TextUtils.isEmpty(txtToSearch.getText().toString())||TextUtils.isEmpty(txtDateSearch.getText().toString())||TextUtils.isEmpty(txtTimeSearch.getText().toString())) {
                    Toast.makeText(getActivity(), "Vul alle verplichte velden in", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String fromPlaceid = "";
                    String toPlaceid = "";

                    if(!fromLoc.isEmpty()) {
                        fromPlaceid = "placeid=" + fromLoc.get("place_id");
                    }

                    if(!toLoc.isEmpty()) {
                        toPlaceid = "placeid=" + toLoc.get("place_id");
                    }

                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    Bundle b = new Bundle();
                    //Enter all parameters
                    b.putString("from", txtFromSearch.getText().toString());
                    b.putString("to", txtToSearch.getText().toString());
                    b.putString("date", txtDateSearch.getText().toString());
                    b.putString("time", txtTimeSearch.getText().toString());
                    b.putString("fromPlaceid", fromPlaceid);
                    b.putString("toPlaceid", toPlaceid);
                    if(chkShare.isChecked()){b.putBoolean("share", true);}
                    else{b.putBoolean("share", false);}

                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        //Date picker
        txtDateSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        //Time picker
        txtTimeSearch.setOnClickListener(new View.OnClickListener() {

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

            txtDateSearch.setText(date);
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

            txtTimeSearch.setText(time);
        }
    }
}
