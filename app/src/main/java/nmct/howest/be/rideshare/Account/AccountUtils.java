package nmct.howest.be.rideshare.Account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import nmct.howest.be.rideshare.Loaders.Database.Contract;

/**
 * Created by Preben on 19/01/2015.
 */
public class AccountUtils
{
    //An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "nmct.howest.be.rideshare.account";
    // The account name
    public static final String ACCOUNT = "RideShareAccount";
    // Incoming Intent key for extended data
    public static final String KEY_SYNC_REQUEST = "nmct.howest.be.rideshare.account.KEY_SYNC_REQUEST";

    public static void CreateSyncAccount(Context context) {
        //Remove any existing accounts
        removeAccount(context);

        //Get an instance of the Android account manager
        AccountManager manager = AccountManager.get(context);
        //Create the account type and default account
        Account account = new Account(ACCOUNT, ACCOUNT_TYPE);

        boolean isNewAccountAdded;
        try {
            isNewAccountAdded = manager != null && manager.addAccountExplicitly(account, null, null);
        } catch (SecurityException e) {
            Log.d("Account", "Setting up account...FAILED Account could not be added");
            return;
        }
        ContentResolver.setIsSyncable(account, Contract.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, Contract.AUTHORITY, true);
        Log.d("Account", "Setting up account...DONE");
    }

    public static void removeAccount(Context context)
    {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);
        for(Account account : accounts)
        {
            manager.removeAccount(account, null, null);
        }
        Log.d("Account", "Removing existing accounts...DONE");
    }

    public static boolean isAccountExists(Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);
        return accounts.length > 0;
    }

    public static Account getAccount(Context context)
    {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);

        //Return first available account
        if(accounts.length > 0)
        {
            return accounts[0];
        }
        return null;
    }

}
