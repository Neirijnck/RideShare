package nmct.howest.be.rideshare.Activities.Helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Preben on 2/12/2014.
 */
public class Utils
{

    //Method for dynamically changing the height of a listview based on amoount of children
    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        Adapter adapter = listView.getAdapter();
        if (adapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (15 * (adapter.getCount()));
        params.width = params.MATCH_PARENT;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
