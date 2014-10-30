package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

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

}
