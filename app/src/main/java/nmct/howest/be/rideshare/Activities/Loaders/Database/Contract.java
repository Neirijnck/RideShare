package nmct.howest.be.rideshare.Activities.Loaders.Database;

import android.provider.BaseColumns;

/**
 * Created by Preben on 26/11/2014.
 */
public class Contract
{
    public static final String AUTHORITY = "nmct.howest.be.rideshare.Activities.Loaders.Database";
    public static final String ACCOUNT_TYPE = "nmct.howest.be.rideshare.Activities.Loaders.Database.account";

    public interface ProfileColumns extends BaseColumns
    {
        public static final String USERID = "userID";
        public static final String USERNAME = "userName";
        public static final String FACEBOOKTOKEN = "facebookToken";
        public static final String FACEBOOKID = "facebookID";
        public static final String FACEBOOKLINK = "facebookLink";
        public static final String FIRSTNAME = "firstName";
        public static final String LASTNAME = "lastName";
        public static final String EMAIL = "email";
        public static final String GENDER = "gender";
        public static final String BIRTHDAY = "birthday";
        public static final String LOCATION = "birthday";
        public static final String CARTYPE = "birthday";
        public static final String AMOUNTOFSEATS = "birthday";

    }

}
