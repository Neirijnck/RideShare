package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.Selection;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

import nmct.howest.be.rideshare.Activities.Helpers.POSTHelper;
import nmct.howest.be.rideshare.Activities.SearchActivity;
import nmct.howest.be.rideshare.R;

public class PlanningFragment extends Fragment{

    //Variables
    private SwitchCompat repeatSwitch;
    private LinearLayout tglBtns;
    static EditText txbDatePlan;
    static EditText txbTimePlan;
    static EditText txtPrice;
    static EditText txtNaarPlan;
    static EditText txtVanPlan;
    static Button btnOpslaan;


    //Ctor
    public PlanningFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planning, container, false);


        //Get widgets
        repeatSwitch = (SwitchCompat) view.findViewById(R.id.repeatSwitch);
        tglBtns = (LinearLayout) view.findViewById(R.id.tglBtns);
        txtPrice = (EditText) view.findViewById(R.id.txtPrice);
        txtNaarPlan = (EditText) view.findViewById(R.id.txtNaarPlan);
        txtVanPlan = (EditText) view.findViewById(R.id.txtVanPlan);
        btnOpslaan = (Button) view.findViewById(R.id.btnOpslaan);

        //Enable togglebuttons when switch is on
        repeatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                checkToggleBtns(isChecked);
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

        //Price field
        txtPrice.setText("€");
        Selection.setSelection(txtPrice.getText(), txtPrice.getText().length());

        txtPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("€")){
                    txtPrice.setText("€");
                    Selection.setSelection(txtPrice.getText(), txtPrice.getText().length());
                }
            }
        });

        btnOpslaan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String date = txbDatePlan.getText().toString();
                String time = txbTimePlan.getText().toString();
                String price = txtPrice.getText().toString();
                String from = txtVanPlan.getText().toString();
                String to = txtNaarPlan.getText().toString();

                //sendPostRequest(from, to);
                POSTHelper.PlanTrip(from, to, date, time, price);
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



    //POST
    /*public void sendPostRequest(String from, String to) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://188.226.154.228:8080/api/v1/trips");

                    //set header
                    httppost.addHeader("auth", "000");

                    // Add your data
                    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                    parameters.add(new BasicNameValuePair("from", from));
                    parameters.add(new BasicNameValuePair("to", to));
                    //nameValuePairs.add(new BasicNameValuePair("datetime", datetime));
                    httppost.setEntity(new UrlEncodedFormEntity(parameters));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                    HttpEntity entity = response.getEntity();

                    InputStream is = entity.getContent();

                    Toast.makeText(getActivity(), "send", Toast.LENGTH_LONG).show();

                }
                catch (ClientProtocolException e) {
                    Log.e("log_tag", "Error in http connection "+e.toString());
                } catch (IOException e) {
                    Log.e("log_tag", "Error in http connection "+e.toString());
                }
            }

        }.execute(null, null, null);


    }*/



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
