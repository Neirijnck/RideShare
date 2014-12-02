package nmct.howest.be.rideshare.Activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import nmct.howest.be.rideshare.Activities.Helpers.ConnectivityHelper;
import nmct.howest.be.rideshare.Activities.Helpers.ParseHelper;
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
        //Has internet?
        Context c = getActivity();
        if(!ConnectivityHelper.isNetworkAvailable(c)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("No Internet connection.");
            builder.setMessage("You have no internet connection");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            builder.show();
        }
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


        authButton.setReadPermissions(Arrays.asList("email", "user_birthday", "public_profile"));

        authButton.setSessionStatusCallback(new Session.StatusCallback() {
            @Override
            public void call(final Session session, SessionState state, Exception exception) {

                if (session.isOpened()) {

                    List<String> permissions = session.getPermissions();
                    Log.d("permissies", permissions.toString());


                    Log.i(TAG, "Access Token" + session.getAccessToken());
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {
                                        Log.i(TAG, "token: " + session.getAccessToken());
                                        Log.i(TAG, "Email " + user.asMap().get("email"));
                                        parseLoginToDatabase();
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


    private class JSONParse extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();


        private ProgressDialog pDialog;

        protected void onPreExecute() {

            super.onPreExecute();
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Logging...");
            builder.setMessage("We are saving your data");
            builder.show();

        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

            ParseHelper jParser = new ParseHelper();
            JSONObject json = jParser.getJSONData("http://188.226.154.228:8080/api/v1/profile");
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid", 1);
                jsonObject.put("gender", "m");
                jsonObject.put("lastName", "De Coster");
                jsonObject.put("firstName", "Ashim");
                jsonObject.put("facebookLink","link");
                jsonObject.put("facebookID", "id");
                jsonObject.put("facebookToken", "token");
                jsonObject.put("userName", "jimmydc");
                    HashMap<String, String> map = new HashMap<String, String>();
                  //  map.put(HEAD_LINE, headLine);
                   // map.put(DATE_LINE, dateLine);
                    //newsList.add(map);
                } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return newsList;
        }
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {


            super.onPostExecute(result);
            pDialog.dismiss();
            String []from = {HEAD_LINE,DATE_LINE};
            int []to = {R.id.tv_headLn,R.id.tv_dateLn};
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, result, R.layout.listview_item, from, to);
            setListAdapter(adapter);

        }

    }


}
