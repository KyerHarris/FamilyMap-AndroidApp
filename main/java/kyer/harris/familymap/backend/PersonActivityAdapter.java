package kyer.harris.familymap.backend;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import Model.*;

import java.util.List;

import kyer.harris.familymap.R;

public class PersonActivityAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final Person individual;
    private final List<String> expandableListTitle;
    private final List<Event> expandableListEvents;
    private final List<Person> expandableListPersons;

    public PersonActivityAdapter(Context context, List<String> expandableListTitle, List<Event> expandableListEvents, List<Person> expandableListPersons, Person individual) {
        this.context = context;
        this.individual = individual;
        this.expandableListTitle = expandableListTitle;
        this.expandableListEvents = expandableListEvents;
        this.expandableListPersons = expandableListPersons;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        if(listPosition == 0){
            return expandableListEvents.get(expandedListPosition);
        }
        else if(listPosition == 1){
            return expandableListPersons.get(expandedListPosition);
        }
        return null;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextInfo = convertView.findViewById(R.id.expandedListInfo);
        TextView expandedListTextDescription = convertView.findViewById(R.id.expandedListDescription);
        switch(listPosition){
            case 0:
                final Event event = (Event)getChild(listPosition, expandedListPosition);
                String info = event.getEventType() + " " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
                Person person = DataCache.getInstance().getPerson(event.getPersonID());
                String description = person.getFirstName() + " " + person.getLastName();
                expandedListTextInfo.setText(info);
                expandedListTextDescription.setText(description);
                break;
            case 1:
                person = (Person)getChild(listPosition, expandedListPosition);
                info = person.getFirstName() + " " + person.getLastName();
                description = DataCache.getInstance().getRelationship(person, individual);
                expandedListTextInfo.setText(info);
                expandedListTextDescription.setText(description);
                break;
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        switch(listPosition){
            case 0:
                return expandableListEvents.size();
            case 1:
                return expandableListPersons.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
