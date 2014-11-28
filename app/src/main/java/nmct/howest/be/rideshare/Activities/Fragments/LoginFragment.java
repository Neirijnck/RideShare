package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Collection;
import java.util.List;

import nmct.howest.be.rideshare.Activities.Helpers.ConnectivityHelper;
import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.R;

public class LoginFragment extends Fragment
{
    private FragmentActivity context;
    private TextView learnMore;
    private FragmentTransaction transaction;
    private LearnMoreFragment learnMoreFragment;
    private String TAG = "LoginActivity";
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions", "email");
    private boolean isPermissionsGiven = false;

    public LoginFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Has internet?
        Context c = getActivity();
        if(!ConnectivityHelper.isNetworkAvailable(c)) {
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
        }

        learnMore = (TextView) view.findViewById(R.id.txbLearnMore);
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

                    List<String> permissions = session.getPermissions();
                    Log.d("permissions", permissions.toString());
                    if (isSubsetOf(PERMISSIONS, permissions))
                        isPermissionsGiven = true;

                    Log.i(TAG, "Access Token" + session.getAccessToken());
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

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
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
