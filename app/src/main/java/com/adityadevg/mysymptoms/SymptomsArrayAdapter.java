package com.adityadevg.mysymptoms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adityadevg.mysymptoms.DatabaseModule.ListItemSymptomDetailsDTO;

import java.util.List;

/**
 * Created by adityadev on 3/31/2016.
 */
public class SymptomsArrayAdapter extends ArrayAdapter<ListItemSymptomDetailsDTO> {

    public SymptomsArrayAdapter(Context context, List<ListItemSymptomDetailsDTO> listOfSymptomDetails) {
        super(context, 0, listOfSymptomDetails);
    }

    private class ViewHolder {
        TextView tv_dateTime;
        TextView tv_bodyPart;
        TextView tv_severityLevel;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_symptoms,
                    parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_dateTime = (TextView) convertView.findViewById(R.id.list_item_symptom_date_time);
            viewHolder.tv_bodyPart = (TextView) convertView.findViewById(R.id.list_item_body_part);
            viewHolder.tv_severityLevel = (TextView) convertView.findViewById(R.id.list_item_severity_level);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        // Gets the artist object from the ArrayAdapter at the appropriate position
        ListItemSymptomDetailsDTO symptomDetails = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        TextView tv_dateTime = (TextView) convertView.findViewById(R.id.list_item_symptom_date_time);
        viewHolder.tv_dateTime.setText(symptomDetails.getDateTimeStamp());

        TextView tv_bodyPart = (TextView) convertView.findViewById(R.id.list_item_body_part);
        viewHolder.tv_bodyPart.setText(symptomDetails.getBodyPart());

        TextView tv_severityLevel = (TextView) convertView.findViewById(R.id.list_item_severity_level);
        viewHolder.tv_severityLevel.setText(symptomDetails.getLevelOfSecurity());

        return convertView;
    }



}
