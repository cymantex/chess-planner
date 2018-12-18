package se.umu.smnrk.schackplaneraren.controller.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import se.umu.smnrk.schackplaneraren.R;

/**
 * Describes how to display a expandable list of filters.
 * @author Simon Eriksson
 * @version 1.1
 */
public class FilterAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupData;
    private HashMap<String, List<String>> groupToChildData;

    /**
     * @param context to inflate layouts with.
     * @param groupData the heading for a group a filters.
     * @param groupToChildData maps each group header to a list of it's
     *                         children.
     */
    public FilterAdapter(Context context, List<String> groupData,
                         HashMap<String, List<String>> groupToChildData){
        this.context = context;
        this.groupData = groupData;
        this.groupToChildData = groupToChildData;
    }

    @Override
    public int getGroupCount(){
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition){
        return groupToChildData.get(groupData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition){
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition){
        return groupToChildData.get(groupData.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition){
        return childPosition;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater == null){ return null; }
            convertView = inflater.inflate(R.layout.filter_group, parent, false);
        }

        TextView filterGroup = convertView.findViewById(R.id.filter_group);
        filterGroup.setTypeface(null, Typeface.BOLD);
        filterGroup.setText((String)getGroup(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView,
                             ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(inflater == null){ return null; }
            convertView = inflater.inflate(R.layout.filter_item, parent, false);
        }

        TextView filterItem = convertView.findViewById(R.id.filter_item);
        filterItem.setText((String)getChild(groupPosition, childPosition));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition){
        return true;
    }
}
