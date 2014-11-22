package nmct.howest.be.rideshare.Activities.Loaders;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.JsonReader;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import nmct.howest.be.rideshare.Activities.Helpers.JSONLoaderHelper;
import nmct.howest.be.rideshare.R;

/**
 * Created by Preben on 12/11/2014.
 */
public class TripLoader extends JSONLoaderHelper
{

    public TripLoader(Context context) {
        super(context, new String[]{"_id","userID","from", "to", "datetime", "payment","repeat","matches"}, R.raw.trip);
    }

    @Override
    protected void parse(JsonReader reader, MatrixCursor cursor) throws IOException {
        int id=1;
        String ID="";
        String userID="";
        String from="";
        String to="";
        String dateTime="";
        String payment="";
        Boolean mo=false;
        Boolean tu=false;
        Boolean we=false;
        Boolean th=false;
        Boolean fr=false;
        Boolean sa=false;
        Boolean su=false;
        String matchUserID="";
        String matchFrom="";
        String matchTo="";
        String matchDateTime="";
        int matchStatus=0;
        ArrayList<String> messages= new ArrayList<String>();

        while(reader.hasNext())
        {
            String key = reader.nextName();
            if(key.equals("_id")){ID = reader.nextString();}
            else if(key.equals("userID")){userID = reader.nextString();}
            else if(key.equals("from")){from = reader.nextString();}
            else if(key.equals("to")){to = reader.nextString();}
            else if(key.equals("datetime")){dateTime = reader.nextString();}
            else if(key.equals("payment")){payment = reader.nextString();}
            else if(key.equals("repeat"))
            {
                reader.beginObject();
                while(reader.hasNext())
                {
                    String key_second = reader.nextName();
                    if(key_second.equals("mo")){mo = reader.nextBoolean();}
                    else if(key_second.equals("tu")){tu = reader.nextBoolean();}
                    else if(key_second.equals("we")){we = reader.nextBoolean();}
                    else if(key_second.equals("th")){th = reader.nextBoolean();}
                    else if(key_second.equals("fr")){fr = reader.nextBoolean();}
                    else if(key_second.equals("sa")){sa = reader.nextBoolean();}
                    else if(key_second.equals("su")){su = reader.nextBoolean();}
                    else{reader.skipValue();}
                }
                reader.endObject();
            }
            else if(key.equals("matches"))
            {
                reader.beginArray();
                while(reader.hasNext())
                {
                    reader.beginObject();
                    while(reader.hasNext())
                    {
                        String key_third = reader.nextName();
                        if(key_third.equals("userID")){matchUserID = reader.nextString();}
                        else if(key_third.equals("from")){matchFrom = reader.nextString();}
                        else if(key_third.equals("to")){matchTo = reader.nextString();}
                        else if(key_third.equals("datetime")){matchDateTime = reader.nextString();}
                        else if(key_third.equals("messages"))
                        {
                            reader.beginArray();
                            while(reader.hasNext())
                            {
                                reader.beginObject();
                                while(reader.hasNext())
                                {
                                    String key_four = reader.nextName();
                                    if(key_four.equals("userID")){messages.add(reader.nextString());}
                                    else if(key_four.equals("datetime")){messages.add(reader.nextString());}
                                    else if(key_four.equals("text")){messages.add(reader.nextString());}
                                    else{reader.skipValue();}
                                }
                                reader.endObject();
                            }
                            reader.endArray();
                        }
                        else if(key.equals("status"))
                        {
                            matchStatus = reader.nextInt();

                            //Last property - add the object to a row

                            MatrixCursor.RowBuilder builder = cursor.newRow();
                            //builder.add(new Object[]{ID, userID, from, to, dateTime, payment, mo, tu, we, th, fr, sa, su, matchUserID, matchFrom, matchTo, matchDateTime, matchStatus, messages});
                            if(!TextUtils.isEmpty(ID)&& !TextUtils.isEmpty(userID)&&!TextUtils.isEmpty(from)&&!TextUtils.isEmpty(to)&& !TextUtils.isEmpty(dateTime)&& !TextUtils.isEmpty(payment)) {
                                builder.add(from);
                                builder.add(to);
                                builder.add(dateTime);
                            }

                        }
                        else{reader.skipValue();}
                    }
                    reader.endObject();
                }
                reader.endArray();
            }
            else{reader.skipValue();}

            id++;
        }

        //Dit zal nog niet werken bij meerdere, zal string telkens overschrijven
/*      builder.add(ID);
        builder.add(userID);
        builder.add(from);
        builder.add(to);
        builder.add(dateTime);
        builder.add(payment);
        builder.add(mo);
        builder.add(tu);
        builder.add(we);
        builder.add(th);
        builder.add(fr);
        builder.add(sa);
        builder.add(su);
        builder.add(matchUserID);
        builder.add(matchFrom);
        builder.add(matchTo);
        builder.add(matchDateTime);
        builder.add(matchStatus);
        builder.add(messages);*/
    }
}
