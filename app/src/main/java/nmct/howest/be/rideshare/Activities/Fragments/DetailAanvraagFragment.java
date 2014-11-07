package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 7/11/2014.
 */
public class DetailAanvraagFragment extends Fragment
{
    public DetailAanvraagFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_detail_aanvraag, container, false);
    }
}
