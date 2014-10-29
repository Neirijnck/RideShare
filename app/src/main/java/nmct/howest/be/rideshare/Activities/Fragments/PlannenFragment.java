package nmct.howest.be.rideshare.Activities.Fragments;

/**
 * Created by Preben on 27/10/2014.
 */
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
    private Switch herhaalSwitch;
    private ToggleButton tglMaandag;
    private ToggleButton tglDinsdag;
    private ToggleButton tglWoensdag;
    private ToggleButton tglDonderdag;
    private ToggleButton tglVrijdag;
    private ToggleButton tglZaterdag;
    private ToggleButton tglZondag;

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
        herhaalSwitch = (Switch) view.findViewById(R.id.switchHerhaal);
        tglMaandag = (ToggleButton) view.findViewById(R.id.tglMaandag);
        tglDinsdag = (ToggleButton) view.findViewById(R.id.tglDinsdag);
        tglWoensdag = (ToggleButton) view.findViewById(R.id.tglWoensdag);
        tglDonderdag = (ToggleButton) view.findViewById(R.id.tglDonderdag);
        tglVrijdag = (ToggleButton) view.findViewById(R.id.tglVrijdag);
        tglZaterdag = (ToggleButton) view.findViewById(R.id.tglZaterdag);
        tglZondag = (ToggleButton) view.findViewById(R.id.tglZondag);


        //Enable togglebuttons when switch is on
        herhaalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    //Enable all weektogglebuttons
                    tglMaandag.setEnabled(true);
                    tglDinsdag.setEnabled(true);
                    tglWoensdag.setEnabled(true);
                    tglDonderdag.setEnabled(true);
                    tglVrijdag.setEnabled(true);
                    tglZaterdag.setEnabled(true);
                    tglZondag.setEnabled(true);
                }
                else
                {
                    //Disable all weektogglebuttons
                    tglMaandag.setEnabled(false);
                    tglDinsdag.setEnabled(false);
                    tglWoensdag.setEnabled(false);
                    tglDonderdag.setEnabled(false);
                    tglVrijdag.setEnabled(false);
                    tglZaterdag.setEnabled(false);
                    tglZondag.setEnabled(false);
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
