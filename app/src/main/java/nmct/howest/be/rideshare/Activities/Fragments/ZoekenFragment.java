package nmct.howest.be.rideshare.Activities.Fragments;

/**
 * Created by Preben on 27/10/2014.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nmct.howest.be.rideshare.R;

public class ZoekenFragment extends Fragment
{
    public ZoekenFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zoeken, container, false);
    }
}
