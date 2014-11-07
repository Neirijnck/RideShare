package nmct.howest.be.rideshare.Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 7/11/2014.
 */
public class ImageFragment extends Fragment
{
    public ImageFragment(){}

    //New instance of this fragment with image as param
    public static ImageFragment newInstance(int image) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);

        //Get image from parambundle and assign it to the imageview from the layoutfile
        ImageView image = (ImageView) view.findViewById(R.id.imgWithIndicator);
        image.setImageResource(getArguments().getInt("image"));

        return view;
    }
}
