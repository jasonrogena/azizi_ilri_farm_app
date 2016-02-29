package org.cgiar.ilri.farm.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.cgiar.ilri.farm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrogena on 29/02/2016.
 */
public class LocationsToolbarAdapter extends ArrayAdapter<LocationsToolbarAdapter.Location> {
    private final List<Location> locations;
    private final int resource;
    private final int textViewResourceId;
    private final Context context;
    private final LayoutInflater inflater;

    public LocationsToolbarAdapter(Context context, int resource, int textViewResourceId, List<Location> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context;
        this.locations = objects;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Location location = locations.get(position);
        View view = inflater.inflate(resource, parent, false);
        TextView level1TV = (TextView) view.findViewById(R.id.level_1_tv);
        level1TV.setText(location.getLevel1());
        TextView level2TV = (TextView) view.findViewById(R.id.level_2_tv);
        if(location.getLevel2() == null || location.getLevel2().length() == 0) {
            level2TV.setVisibility(View.GONE);
        } else {
            level2TV.setVisibility(View.VISIBLE);
            level2TV.setText(location.getLevel2());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Location location = locations.get(position);
        View view = inflater.inflate(textViewResourceId, parent, false);
        TextView level1TV = (TextView) view.findViewById(R.id.level_1_tv);
        level1TV.setText(location.getLevel1());
        TextView level2TV = (TextView) view.findViewById(R.id.level_2_tv);
        if(location.getLevel2() == null || location.getLevel2().length() == 0) {
            level2TV.setVisibility(View.GONE);
        } else {
            level2TV.setVisibility(View.VISIBLE);
            level2TV.setText(location.getLevel2());
        }
        return view;
    }

    public static class Location {
        public static final int DEFAULT_ID = -1;
        private final int id;
        private final String level1;
        private final String level2;

        /**
         * Constructor that sets the id as DEFAULT_ID
         *
         * @param level1
         * @param level2
         */
        public Location(String level1, String level2) {
            this.id = DEFAULT_ID;
            this.level1 = level1;
            this.level2 = level2;
        }

        /**
         * Default constructor
         *
         * @param id
         * @param level1
         * @param level2
         */
        public Location(int id, String level1, String level2) {
            this.id = id;
            this.level1 = level1;
            this.level2 = level2;
        }

        public int getId() {
            return id;
        }

        public String getLevel1() {
            return level1;
        }

        public String getLevel2() {
            return level2;
        }
    }
}
