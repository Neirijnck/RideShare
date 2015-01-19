package nmct.howest.be.rideshare.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Selection;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.HashMap;

import nmct.howest.be.rideshare.Account.AccountUtils;
import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.ConnectivityHelper;
import nmct.howest.be.rideshare.Loaders.Database.Contract;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.Loaders.Json.PlaceJSONParser;
import nmct.howest.be.rideshare.Loaders.Tasks.PlacesTask;
import nmct.howest.be.rideshare.R;
import nmct.howest.be.rideshare.RideshareApp;

public class PlanFragment extends Fragment{

    //Variables
    private SwitchCompat repeatSwitch;
    private LinearLayout tglBtns;
    static AutoCompleteTextView txtPlanTo;
    static AutoCompleteTextView txtPlanFrom;
    static EditText txbDatePlan;
    static EditText txbTimePlan;
    static EditText txtPlanPrice;
    static Button btnPlanSave;
    private ToggleButton tglMo;
    private ToggleButton tglTu;
    private ToggleButton tglWe;
    private ToggleButton tglTh;
    private ToggleButton tglFr;
    private ToggleButton tglSa;
    private ToggleButton tglSu;

    HashMap<String, String> fromLoc = new HashMap<String, String>();
    HashMap<String, String> toLoc = new HashMap<String, String>();

    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(RideshareApp.getAppContext());
    String token = pref.getString("accessToken", "");

    public PlanFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        txtPlanTo = (AutoCompleteTextView) view.findViewById(R.id.txtPlanTo);
        txtPlanFrom = (AutoCompleteTextView) view.findViewById(R.id.txtPlanFrom);
        repeatSwitch = (SwitchCompat) view.findViewById(R.id.repeatSwitch);
        tglBtns = (LinearLayout) view.findViewById(R.id.tglBtns);
        txtPlanPrice = (EditText) view.findViewById(R.id.txtPlanPrice);
        btnPlanSave = (Button) view.findViewById(R.id.btnPlanSave);

        tglMo = (ToggleButton) view.findViewById(R.id.tglMonday);
        tglTu = (ToggleButton) view.findViewById(R.id.tglTuesday);
        tglWe = (ToggleButton) view.findViewById(R.id.tglWednesday);
        tglTh = (ToggleButton) view.findViewById(R.id.tglThursday);
        tglFr = (ToggleButton) view.findViewById(R.id.tglFriday);
        tglSa = (ToggleButton) view.findViewById(R.id.tglSaturday);
        tglSu = (ToggleButton) view.findViewById(R.id.tglSunday);

        //Enable togglebuttons when switch is on
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                checkToggleBtns(isChecked);
            }
        });

        //From Google Maps
        txtPlanFrom.setThreshold(2);
        txtPlanFrom.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s != "" && start >= 2) {
                    Log.d("request send", count + " -- " + s.toString());
                    PlacesTask placesTask = new PlacesTask(txtPlanFrom, getActivity());
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
        txtPlanFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                fromLoc = PlaceJSONParser.PLACES.get(pos);
            }
        });


        //To Google Maps
        txtPlanTo.setThreshold(2);
        txtPlanTo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s != "" && start >= 2) {
                    PlacesTask placesTask = new PlacesTask(txtPlanTo, getActivity());
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
        txtPlanTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                toLoc = PlaceJSONParser.PLACES.get(pos);
            }
        });

        //Date picker
        txbDatePlan = (EditText) view.findViewById(R.id.txtPlanDate);
        txbDatePlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getFragmentManager(), "datePicker");
            }
        });

        //Time picker
        txbTimePlan = (EditText) view.findViewById(R.id.txtPlanTime);
        txbTimePlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        //Price field
        txtPlanPrice.setText("€");
        Selection.setSelection(txtPlanPrice.getText(), txtPlanPrice.getText().length());

        txtPlanPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("€")) {
                    txtPlanPrice.setText("€");
                    Selection.setSelection(txtPlanPrice.getText(), txtPlanPrice.getText().length());
                }
            }
        });

        btnPlanSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String date = txbDatePlan.getText().toString();
                String time = txbTimePlan.getText().toString();
                String price = txtPlanPrice.getText().toString();
                String from = txtPlanFrom.getText().toString();
                String to = txtPlanTo.getText().toString();
                boolean mo = tglMo.isChecked();
                boolean tu = tglTu.isChecked();
                boolean we = tglWe.isChecked();
                boolean th = tglTh.isChecked();
                boolean fr = tglFr.isChecked();
                boolean sa = tglSa.isChecked();
                boolean su = tglSu.isChecked();
                boolean[] repeatDays = new boolean[7];

                //Check if required fields are not empty
                if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(from) || TextUtils.isEmpty(to)) {
                    Toast.makeText(getActivity(), "Vul alle verplichte velden in", Toast.LENGTH_SHORT).show();
                } else {
                    //Post info to database

                    //Has internet?
                    Context c = getActivity();
                    if (!Utils.isNetworkAvailable(c)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                        builder.setTitle("No Internet connection.");
                        builder.setMessage("You have no internet connection");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else {

                        //Coordinates
                        String fromPlaceid = "";
                        String toPlaceid = "";
                        if(!fromLoc.isEmpty()) {
                            fromPlaceid = "placeid=" + fromLoc.get("place_id");
                        }
                        if(!toLoc.isEmpty()) {
                            toPlaceid = "placeid=" + toLoc.get("place_id");
                        }

                        boolean[] emptyBoolArr = null;

                        //If there is only a price
                        if (!price.equals("€") && !repeatSwitch.isChecked()) {
                            APIHelper.PlanTrip(token, from.trim(),fromPlaceid, to.trim(),toPlaceid, date, time, price.trim(), emptyBoolArr);
                        }
                        //If there are only repeat days
                        else if (price.equals("€") && repeatSwitch.isChecked()) {
                            repeatDays[0] = mo;
                            repeatDays[1] = tu;
                            repeatDays[2] = we;
                            repeatDays[3] = th;
                            repeatDays[4] = fr;
                            repeatDays[5] = sa;
                            repeatDays[6] = su;
                            APIHelper.PlanTrip(token, from.trim(),fromPlaceid, to.trim(),toPlaceid, date, time, "€", repeatDays);
                        }
                        //Both price and repeat days
                        else if (!price.equals("€") && repeatSwitch.isChecked()) {
                            repeatDays[0] = mo;
                            repeatDays[1] = tu;
                            repeatDays[2] = we;
                            repeatDays[3] = th;
                            repeatDays[4] = fr;
                            repeatDays[5] = sa;
                            repeatDays[6] = su;
                            APIHelper.PlanTrip(token, from.trim(),fromPlaceid, to.trim(),toPlaceid, date, time, price.trim(), repeatDays);
                        }
                        //No price and repeat days
                        else {
                            APIHelper.PlanTrip(token, from.trim(),fromPlaceid, to.trim(),toPlaceid, date, time, "€", emptyBoolArr);
                        }
                    }
                }
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
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkToggleBtns (Boolean isChecked) {
        if (isChecked) {
            AlphaAnimation fade_in = new AlphaAnimation(0, 1);
            fade_in.setDuration(500);
            fade_in.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation arg0)
                {
                    tglBtns.setVisibility(View.VISIBLE);
                }
                public void onAnimationRepeat(Animation arg0)
                {
                }
                public void onAnimationEnd(Animation arg0)
                {
                }
            });
            tglBtns.startAnimation(fade_in);

        } else {
            AlphaAnimation fade_out = new AlphaAnimation(1, 0);
            fade_out.setDuration(500);
            fade_out.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation arg0)
                {
                }
                public void onAnimationRepeat(Animation arg0)
                {
                }
                public void onAnimationEnd(Animation arg0)
                {
                    tglBtns.setVisibility(View.GONE);
                }
            });
            tglBtns.startAnimation(fade_out);
        }
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
