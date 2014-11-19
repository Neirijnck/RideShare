package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.R;

public class LoginFragment extends Fragment
{
    private FragmentActivity context;
    private TextView learnMore;
    private FragmentTransaction transaction;
    private LearnMoreFragment learnMoreFragment;
    private String TAG = "LoginActivity";
    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
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

        //Facebook Login
        LoginButton authButton = (LoginButton) view.findViewById(R.id.btnLogin);
        authButton.setOnErrorListener(new LoginButton.OnErrorListener() {
            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "Error " + error.getMessage());
            }
        });
        authButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(final Session session, SessionState state, Exception exception) {

                if (session.isOpened()) {
                    Log.i(TAG,"Access Token"+ session.getAccessToken());
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {
                                        Log.i(TAG, "token: " + session.getAccessToken());
                                        Log.i(TAG, "Email " + user.asMap().get("email"));


                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                         getActivity().startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });
        //End Facebook Login

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        context=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    //facebook loggedInCheck
    public boolean isLoggedIn() {
        Session session = Session.getActiveSession();
        return (session != null && session.isOpened());
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
