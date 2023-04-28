package kyer.harris.familymap.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import Model.*;
import kyer.harris.familymap.backend.DataCache;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import kyer.harris.familymap.R;

import java.util.List;

public class SearchActivity extends AppCompatActivity{
    private static final int PERSON_VIEW_TYPE = 0;
    private static final int EVENT_VIEW_TYPE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Family Map: Search");

        RecyclerView recyclerView = findViewById(R.id.search_results_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        SearchView searchView = findViewById(R.id.search_view);
        SearchAdapter adapter = new SearchAdapter(DataCache.getInstance().getPersonList("1234"), DataCache.getInstance().getEventsList("1234"));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchAdapter adapter = new SearchAdapter(DataCache.getInstance().getPersonList(query), DataCache.getInstance().getEventsList(query));
                recyclerView.setAdapter(adapter);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text changes
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }


    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Person> pList;
        private final List<Event> eList;
        public SearchAdapter(List<Person> pList, List<Event> eList) {
            this.pList = pList;
            this.eList = eList;
        }
        @Override
        public int getItemViewType(int position) {
            return position < pList.size() ? PERSON_VIEW_TYPE : EVENT_VIEW_TYPE;
        }
        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if(viewType == PERSON_VIEW_TYPE) {
                view = inflater.inflate(R.layout.recycler_item, parent, false);
            } else {
                view = inflater.inflate(R.layout.recycler_item, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }
        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < pList.size()) {
                holder.bind(pList.get(position));
            } else {
                holder.bind(eList.get(position - pList.size()));
            }
        }
        @Override
        public int getItemCount() {
            return pList.size() + eList.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView top;
        private final TextView bottom;
        private final int viewType;
        private Person person;
        private Event event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            top = itemView.findViewById(R.id.recycler_top);
            bottom = itemView.findViewById(R.id.recycler_bottom);
        }

        public void bind(Person person) {
            this.person = person;
            top.setText(person.getFirstName() + " " + person.getLastName());
            bottom.setText(" ");
        }

        public void bind(Event event) {
            this.event = event;
            top.setText(event.getEventType() + " " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            bottom.setText(DataCache.getInstance().getPerson(event.getPersonID()).getFirstName() + " " + DataCache.getInstance().getPerson(event.getPersonID()).getLastName());
        }

        @Override
        public void onClick(View view) {
            if(viewType == PERSON_VIEW_TYPE) {
                startActivity(new Intent(SearchActivity.this, PersonActivity.class).putExtra("personid", person.getPersonID()));

            } else {
                startActivity(new Intent(SearchActivity.this, EventActivity.class).putExtra("eventid", event.getEventID()));

            }
        }
    }
}