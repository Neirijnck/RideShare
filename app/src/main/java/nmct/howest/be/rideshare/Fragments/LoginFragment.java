package nmct.howest.be.rideshare.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Arrays;
import java.util.List;

import nmct.howest.be.rideshare.Activities.MainActivity;
import nmct.howest.be.rideshare.Adapters.ImagePagerAdapter;
import nmct.howest.be.rideshare.Helpers.APIHelper;
import nmct.howest.be.rideshare.Helpers.ConnectivityHelper;
import nmct.howest.be.rideshare.Helpers.Utils;
import nmct.howest.be.rideshare.R;

public class LoginFragment extends Fragment{

    //Variables
    private FragmentActivity context;
    private String TAG = "LoginActivity";

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Has internet?
        Context c = getActivity();
        if (!ConnectivityHelper.isNetworkAvailable(c)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("No Internet connection.");
            builder.setMessage("You have no internet connection");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                    /*Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            builder.show();
        }

        //Set adapter to viewpager
        ViewPager gallery = (ViewPager) view.findViewById(R.id.gallery_view);
        gallery.setAdapter(new ImagePagerAdapter(getActivity().getSupportFragmentManager()));

        //Bind indicators to pager
        CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(gallery);

        //Facebook Login
        LoginButton authButton = (LoginButton) view.findViewById(R.id.btnLogin);
        authButton.setOnErrorListener(new LoginButton.OnErrorListener() {
            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "Error " + error.getMessage());
            }
        });

        authButton.setReadPermissions(Arrays.asList("email", "user_birthday", "public_profile"));

        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(final Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    List<String> permissions = session.getPermissions();
                    Log.d("permissies", permissions.toString());
                    Log.i(TAG, "Access Token: " + session.getAccessToken());
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                String location = "";
                                String gender = "";
                                String birthday = "";

                                if (user.getBirthday() != null) {
                                    String date = user.getBirthday();
                                    date = date.replace("/", "-");
                                    birthday = Utils.parseDateToISOString(date, "00:00");
                                }
                                else{
                                    birthday=Utils.parseDateToISOString("01-01-0001", "00:00");
                                }

                                if (user.getLocation() != null) {
                                    location = user.getLocation().getName().substring(0, user.getLocation().getName().indexOf(","));
                                }

                                if (user.asMap().get("gender").toString() != null) {
                                    if (user.asMap().get("gender").toString().equals("male"))
                                        gender = "M";
                                    else if (user.asMap().get("gender").toString().equals("female"))
                                        gender = "V";
                                }

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                String reg_id = prefs.getString("REG_ID", "");
                                Log.d("reg_id", reg_id );

                                APIHelper.AddUser(user.getName(), user.getFirstName(),
                                        user.getLastName(), user.asMap().get("email").toString(),
                                        session.getAccessToken(), user.getLink(), user.getId(), location,
                                        gender, "https://graph.facebook.com/" + user.getId() + "/picture?type=large", birthday, reg_id);

                                SharedPreferences.Editor edt = prefs.edit();
                                edt.putString("accessToken", session.getAccessToken());
                                edt.putString("myUserID", user.getId());
                                edt.commit();
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
        context = (FragmentActivity) activity;
        super.onAttach(activity);
    }

}
