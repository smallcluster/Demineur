package com.pierre.demineur;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class MainAdapter extends BaseExpandableListAdapter {

    private Context context;
    // Liste des catégories
    List<String> listGroup;
    // listes des items par catégorie
    HashMap<String, List<String>> listItem;

    public MainAdapter(Context context, List<String> listGroup, HashMap<String, List<String>> listItem){
        this.listGroup = listGroup;
        this.listItem = listItem;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listItem.get(listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listItem.get(listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // On gonfle le layout si nécessaire
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView textView = convertView.findViewById(R.id.list_parent);
        String group = listGroup.get(groupPosition);
        textView.setText(group);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // On gonfle le layout si nécessaire
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.cell_layout, null);
        }
        TextView textDate = convertView.findViewById(R.id.textDate);
        TextView textScore = convertView.findViewById(R.id.textScore);
        TextView textTemps = convertView.findViewById(R.id.textTemps);

        // Voir la variable "stats" de MainActivity pour le format des données
        String[] s = ((String) getChild(groupPosition, childPosition)).split(";");

        textDate.setText(s[0]);
        textScore.setText(s[1]);
        textTemps.setText(s[2]);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // les statistiques ne peuvent être modifiées
        return false;
    }
}
