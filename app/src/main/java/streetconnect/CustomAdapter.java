package streetconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import streetconnect.streetconnect.R;

// Custom List Adapter to display notifications in a Listview (in the Notifications fragment class).
// Not used as of now, but I'm leaving it in in case it's needed later.
// It likely won't be, however, as we've moved to storing notifications in a SQLite database.

public class CustomAdapter extends ArrayAdapter<Information> {

    private final List<Information> info;

    public CustomAdapter(Context context, int resource, List<Information> info)
    {
        super(context, resource, info);
        this.info = info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return getInformationView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        return getInformationView(position, convertView, parent);
    }

    public View getInformationView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row, null);

        TextView textView = (TextView) row.findViewById(R.id.rowText);
        textView.setText(info.get(position).getProviderName()); // may need to change this to actually reflect the title

        /*TextView textView2 = (TextView) row.findViewById(R.id.rowTime);
        textView2.setText(info.get(position).getTime());*/

        return row;
    }
}
