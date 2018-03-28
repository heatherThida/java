package streetconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import streetconnect.streetconnect.R;

// Also relates to deprecated CustomAdapter/Information system. Ignore for the time being.
// Meant to display service providers in a ListView in the Organizations Fragment.

public class ProviderAdapter extends ArrayAdapter<Providers>
{
    private final List<Providers> info;

    public ProviderAdapter(Context context, int resource, List<Providers> info)
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
        View row = inflater.inflate(R.layout.provider_row, null);

        TextView textView = (TextView) row.findViewById(R.id.providerTextView);
        textView.setText("Testing!");

        /*TextView textView = (TextView) row.findViewById(R.id.providerRowText);
        textView.setText(info.get(position).getName());

        TextView textView2 = (TextView) row.findViewById(R.id.providerRowText2);
        textView2.setText(info.get(position).getType());*/

        return row;
    }
}
