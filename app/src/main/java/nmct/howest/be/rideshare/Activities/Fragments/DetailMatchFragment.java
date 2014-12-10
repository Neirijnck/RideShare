package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nmct.howest.be.rideshare.R;


public class DetailMatchFragment extends Fragment {

    public  DetailMatchFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_detail_match, container, false);
    }


}
