package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.Activities.ZoekActivity;
import nmct.howest.be.rideshare.R;

public class LoginFragment extends Fragment
{
    private FragmentActivity context;
    private TextView learnMore;
    private FragmentTransaction transaction;
    private LearnMoreFragment learnMoreFragment;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        learnMore = (TextView) view.findViewById(R.id.txbLearnMore);
        //Set click listener for "Learn more" textview
        learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                learnMoreFragment = new LearnMoreFragment();
                slideUpFragment();
            }
        });


        //Button to start mainactivity
        //!!TEMPORARY
        Button btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        context=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    private void slideUpFragment()
    {
        transaction = context.getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_out_top);
        transaction.replace(R.id.fragment_container, learnMoreFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }



}
