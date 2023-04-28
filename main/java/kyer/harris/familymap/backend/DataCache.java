package kyer.harris.familymap.backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.*;

public class DataCache {
    private final static DataCache instance = new DataCache();
    public static DataCache getInstance(){
        return instance;
    }
    private DataCache(){}

    //information
    private final HashMap<String, Person> people = new HashMap<>();
    private final HashMap<String, Event> events = new HashMap<>();
    private String authtoken;
    private String personID;
    private final HashMap<String, Person> maternalAncestors = new HashMap<>();
    private final HashMap<String, Event> maternalEvents = new HashMap<>();
    private final HashMap<String, Person> paternalAncestors = new HashMap<>();
    private final HashMap<String, Event> paternalEvents = new HashMap<>();
    private final HashMap<String, Person> femaleAncestors = new HashMap<>();
    private final HashMap<String, Event> femaleEvents = new HashMap<>();
    private final HashMap<String, Person> maleAncestors = new HashMap<>();
    private final HashMap<String, Event> maleEvents = new HashMap<>();

    Settings settings = new Settings();

    public void reset(){
        people.clear();
        events.clear();
        maternalEvents.clear();
        maternalAncestors.clear();
        paternalEvents.clear();
        paternalAncestors.clear();
        femaleEvents.clear();
        femaleAncestors.clear();
        maleEvents.clear();
        maleAncestors.clear();
        authtoken = null;
        personID = null;
    }

    public Settings getSettings() {
        return settings;
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Event[] eventsArray) {
        for (Event event : eventsArray) {
            events.put(event.getEventID(), event);
            for (Map.Entry<String, Person> p : maternalAncestors.entrySet()) {
                if (event.getPersonID().equals(p.getKey())) {
                    maternalEvents.put(event.getPersonID(), event);
                }
            }
            for (Map.Entry<String, Person> p : paternalAncestors.entrySet()) {
                if (event.getPersonID().equals(p.getKey())) {
                    paternalEvents.put(event.getEventID(), event);
                }
            }
            for (Map.Entry<String, Person> p : femaleAncestors.entrySet()) {
                if (event.getPersonID().equals(p.getKey())) {
                    femaleEvents.put(event.getEventID(), event);
                }
            }
            for (Map.Entry<String, Person> p : maleAncestors.entrySet()) {
                if (event.getPersonID().equals(p.getKey())) {
                    maleEvents.put(event.getEventID(), event);
                }
            }
        }
    }
    public HashMap<String, Event> getMaternalEvents() {
        return maternalEvents;
    }
    public HashMap<String, Event> getPaternalEvents() {
        return paternalEvents;
    }
    public HashMap<String, Event> getFemaleEvents() {
        return femaleEvents;
    }
    public HashMap<String, Event> getMaleEvents() {
        return maleEvents;
    }
    public Person getPerson(String personID) {
        return people.get(personID);
    }
    public void setPeople(Person[] persons){
        people.clear();
        for (Person person : persons) {
            people.put(person.getPersonID(), person);
            if (person.getGender().equals("m")) {
                maleAncestors.put(person.getPersonID(), person);
            } else {
                femaleAncestors.put(person.getPersonID(), person);
            }
        }
        Person root = people.get(personID);

        if(root != null && root.getFatherID() != null && root.getMotherID() != null) {
            setPaternalAncestors(people.get(root.getFatherID()));
            setMaternalAncestors(people.get(root.getMotherID()));
        }
    }
    public HashMap<String, Person> getPeople() {
        return people;
    }
    public HashMap<String, Person> getMaternalAncestors() {
        return maternalAncestors;
    }
    public HashMap<String, Person> getPaternalAncestors() {
        return paternalAncestors;
    }
    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }
    public String getAuthtoken() {
        return authtoken;
    }
    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
    private void setPaternalAncestors(Person person){
        paternalAncestors.put(person.getPersonID(), person);
        if(person.getMotherID() != null){
            setPaternalAncestors(people.get(person.getMotherID()));
        }
        if(person.getFatherID() != null){
            setPaternalAncestors(people.get(person.getFatherID()));
        }
    }
    private void setMaternalAncestors(Person person){
        maternalAncestors.put(person.getPersonID(), person);
        if(person.getMotherID() != null){
            setMaternalAncestors(people.get(person.getMotherID()));
        }
        if(person.getFatherID() != null){
            setMaternalAncestors(people.get(person.getFatherID()));
        }
    }
    public ArrayList<Event> currEvents(){
        ArrayList<Event> events = new ArrayList<>();
        //Both sides of tree
        if (settings.isFatherSide() && settings.isMotherSide()) {
            if(settings.isMaleEvents() && settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getEvents());
            }
            //all sides just male
            else if(settings.isMaleEvents() && !settings.isFemaleEvents()){
                events = join(DataCache.getInstance().getMaleEvents());
            }
            //all sides just female
            else if(!settings.isMaleEvents() && settings.isFemaleEvents()){
                events = join(DataCache.getInstance().getFemaleEvents());
            }
        }
        //just father's side of tree
        else if (!settings.isMotherSide() && settings.isFatherSide()) {
            //father's side male & female
            if (settings.isMaleEvents() && settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getPaternalEvents());
            }
            //father's side just male
            else if (settings.isMaleEvents() && !settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getPaternalEvents(), DataCache.getInstance().getMaleEvents());
            }
            //father's side just female
            else if (!settings.isMaleEvents() && settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getPaternalEvents(), DataCache.getInstance().getFemaleEvents());
            }
        }
        //mother's side of tree
        else if (settings.isMotherSide() && !settings.isFatherSide()) {
            //mother's side male & female
            if (settings.isMaleEvents() && settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getMaternalEvents());

            }
            //mother's side just male
            else if (settings.isMaleEvents() && !settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getMaternalEvents(), DataCache.getInstance().getMaleEvents());
            }
            //mother's side just female
            else if (!settings.isMaleEvents() && settings.isFemaleEvents()) {
                events = join(DataCache.getInstance().getMaternalEvents(), DataCache.getInstance().getFemaleEvents());
            }
        }
        return events;
    }
    private ArrayList<Event> join(HashMap<String, Event> mapA){
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Event> e : mapA.entrySet()) {
            events.add(e.getValue());
        }
        return events;
    }
    private ArrayList<Event> join(HashMap<String, Event> mapA, HashMap<String, Event> mapB){
        ArrayList<Event> events = new ArrayList<>();
        for (Map.Entry<String, Event> entryA : mapA.entrySet()) {
            for (Map.Entry<String, Event> entryB: mapB.entrySet()) {
                if(entryA.getKey().equals(entryB.getKey())){
                    events.add(entryA.getValue());
                    break;
                }
            }
        }
        return events;
    }
    public ArrayList<Event> getIndividualsEvents(String personID){
        ArrayList<Event> temp = new ArrayList<>();
        for (Map.Entry<String, Event> entryB: events.entrySet()) {
            if(personID.equals(entryB.getValue().getPersonID())){continue;}
            temp.add(entryB.getValue());
        }
        temp.sort(Comparator.comparing(Event::getYear));
        return temp;
    }
    public String getRelationship(Person person, Person individual){
        String description;
        if(person.getFatherID() != null && person.getMotherID() != null) {
            if (person.getFatherID().equals(individual.getPersonID()) || person.getMotherID().equals(individual.getPersonID())) {
                description = "CHILD";
            }
        }
        if(person.getSpouseID() != null && person.getSpouseID().equals(individual.getPersonID())){
            description = "SPOUSE";
        }
        else if(individual.getFatherID() != null && individual.getFatherID().equals(person.getPersonID())){
            description= "FATHER";
        }
        else if(individual.getMotherID() != null && individual.getMotherID().equals(person.getPersonID())){
            description = "MOTHER";
        }
        else{
            description = "UNRELATED";
        }
        return description;
    }
    public ArrayList<Event> getEventsList(String query){
        ArrayList<Event> events = new ArrayList<>();
        //father and mother side
        if (DataCache.getInstance().getSettings().isMotherSide() && DataCache.getInstance().getSettings().isFatherSide()) {
            //male and female
            if (DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> m : DataCache.getInstance().getEvents().entrySet()) {
                    if (m.getValue().getCountry().contains(query) || m.getValue().getCity().contains(query) || m.getValue().getEventType().contains(query)) {
                        events.add(m.getValue());
                    }
                }
            }
            //female
            else if (DataCache.getInstance().getSettings().isFemaleEvents() && !DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> m : DataCache.getInstance().getFemaleEvents().entrySet()) {
                    if (m.getValue().getCountry().contains(query) || m.getValue().getCity().contains(query) || m.getValue().getEventType().contains(query)) {
                        events.add(m.getValue());
                    }
                }
            }
            //male
            else if (!DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> m : DataCache.getInstance().getMaleEvents().entrySet()) {
                    if (m.getValue().getCountry().contains(query) || m.getValue().getCity().contains(query) || m.getValue().getEventType().contains(query)) {
                        events.add(m.getValue());
                    }
                }
            }
        }
        //mother side
        else if (DataCache.getInstance().getSettings().isMotherSide() && !DataCache.getInstance().getSettings().isFatherSide()) {
            //male and female
            if (DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> m : DataCache.getInstance().getMaternalEvents().entrySet()) {
                    if (m.getValue().getCountry().contains(query) || m.getValue().getCity().contains(query) || m.getValue().getEventType().contains(query)) {
                        events.add(m.getValue());
                    }
                }
            }
            //female
            else if (DataCache.getInstance().getSettings().isFemaleEvents() && !DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> entryA : DataCache.getInstance().getMaternalEvents().entrySet()) {
                    if (entryA.getValue().getCountry().contains(query) || entryA.getValue().getCity().contains(query) || entryA.getValue().getEventType().contains(query)) {
                        for (Map.Entry<String, Event> entryB : DataCache.getInstance().getFemaleEvents().entrySet()) {
                            if (entryA.getKey().equals(entryB.getKey())) {
                                events.add(entryA.getValue());
                                break;
                            }
                        }
                    }
                }
            }
            //male
            else if (!DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> entryA : DataCache.getInstance().getMaternalEvents().entrySet()) {
                    if (entryA.getValue().getCountry().contains(query) || entryA.getValue().getCity().contains(query) || entryA.getValue().getEventType().contains(query)) {
                        for (Map.Entry<String, Event> entryB : DataCache.getInstance().getMaleEvents().entrySet()) {
                            if (entryA.getKey().equals(entryB.getKey())) {
                                events.add(entryA.getValue());
                                break;
                            }
                        }
                    }
                }
            }
        }
        //father side
        else if (!DataCache.getInstance().getSettings().isMotherSide() && DataCache.getInstance().getSettings().isFatherSide()) {
            //male and female
            if (DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> m : DataCache.getInstance().getPaternalEvents().entrySet()) {
                    if (m.getValue().getCountry().contains(query) || m.getValue().getCity().contains(query) || m.getValue().getEventType().contains(query)) {
                        events.add(m.getValue());
                    }
                }
            }
            //female
            else if (DataCache.getInstance().getSettings().isFemaleEvents() && !DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> entryA : DataCache.getInstance().getPaternalEvents().entrySet()) {
                    if (entryA.getValue().getCountry().contains(query) || entryA.getValue().getCity().contains(query) || entryA.getValue().getEventType().contains(query)) {
                        for (Map.Entry<String, Event> entryB : DataCache.getInstance().getFemaleEvents().entrySet()) {
                            if (entryA.getKey().equals(entryB.getKey())) {
                                events.add(entryA.getValue());
                                break;
                            }
                        }
                    }
                }
            }
            //male
            else if (!DataCache.getInstance().getSettings().isFemaleEvents() && DataCache.getInstance().getSettings().isMaleEvents()) {
                for (Map.Entry<String, Event> entryA : DataCache.getInstance().getPaternalEvents().entrySet()) {
                    if (entryA.getValue().getCountry().contains(query) || entryA.getValue().getCity().contains(query) || entryA.getValue().getEventType().contains(query)) {
                        for (Map.Entry<String, Event> entryB : DataCache.getInstance().getMaleEvents().entrySet()) {
                            if (entryA.getKey().equals(entryB.getKey())) {
                                events.add(entryA.getValue());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return events;
    }
    public List<Person> getPersonList(String query) {
        List<Person> persons = new ArrayList<>();
        for (Map.Entry<String, Person> m : DataCache.getInstance().getPeople().entrySet()) {
            if ((m.getValue().getFirstName().toLowerCase()).contains(query) || (m.getValue().getLastName().toLowerCase()).contains(query)) {
                persons.add(m.getValue());
            }
        }
        return persons;
    }
}

