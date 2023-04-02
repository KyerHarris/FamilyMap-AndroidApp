package kyer.harris.familymap.backend;

import java.util.HashMap;
import java.util.Map;

import Model.*;

public class DataCache {
    private static DataCache instance = new DataCache();
    public static DataCache getInstance(){
        return instance;
    }
    private DataCache(){}

    //information
    private HashMap<String, Person> people = new HashMap<>();
    private HashMap<String, Event> events = new HashMap<>();
    private String authtoken;
    private String personID;
    //private HashMap<String, ArrayList<Event>> personEvents; //Chronological
    private HashMap<String, Person> maternalAncestors = new HashMap<>();
    private HashMap<String, Event> maternalEvents = new HashMap<>();
    private HashMap<String, Person> paternalAncestors = new HashMap<>();
    private HashMap<String, Event> paternalEvents = new HashMap<>();
    private HashMap<String, Person> femaleAncestors = new HashMap<>();
    private HashMap<String, Event> femaleEvents = new HashMap<>();
    private HashMap<String, Person> maleAncestors = new HashMap<>();
    private HashMap<String, Event> maleEvents = new HashMap<>();

    Settings settings = new Settings();

    public Settings getSettings() {
        return settings;
    }

    public HashMap<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Event[] eventsArray) {
        for (int i = 0; i < eventsArray.length; i++) {
            events.put(eventsArray[i].getEventID(), eventsArray[i]);
            for (Map.Entry<String, Person> p: maternalAncestors.entrySet()) {
                if(eventsArray[i].getPersonID().equals(p.getKey())){
                    maternalEvents.put(eventsArray[i].getPersonID(), eventsArray[i]);
                }
            }
            for (Map.Entry<String, Person> p: paternalAncestors.entrySet()) {
                if(eventsArray[i].getPersonID().equals(p.getKey())){
                    paternalEvents.put(eventsArray[i].getEventID(), eventsArray[i]);
                }
            }
            for (Map.Entry<String, Person> p: femaleAncestors.entrySet()) {
                if(eventsArray[i].getPersonID().equals(p.getKey())){
                    femaleEvents.put(eventsArray[i].getEventID(), eventsArray[i]);
                }
            }
            for (Map.Entry<String, Person> p: maleAncestors.entrySet()) {
                if(eventsArray[i].getPersonID().equals(p.getKey())){
                    maleEvents.put(eventsArray[i].getEventID(), eventsArray[i]);
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

    public HashMap<String, Person> getFemaleAncestors() {
        return femaleAncestors;
    }

    public HashMap<String, Event> getFemaleEvents() {
        return femaleEvents;
    }

    public HashMap<String, Person> getMaleAncestors() {
        return maleAncestors;
    }

    public HashMap<String, Event> getMaleEvents() {
        return maleEvents;
    }

    public Person getPerson(String personID) {
        return people.get(personID);
    }
    public void setPeople(Person[] persons){
        people.clear();
        for (int i = 0; i < persons.length; i++) {
            people.put(persons[i].getPersonID(), persons[i]);
            if(i < 14){
                maternalAncestors.put(persons[i].getPersonID(), persons[i]);
            }
            else if(i < 28){
                paternalAncestors.put(persons[i].getPersonID(), persons[i]);
            }
            else if(i == 28){
                maternalAncestors.put(persons[i].getPersonID(), persons[i]);
            }
            else if(i == 29){
                paternalAncestors.put(persons[i].getPersonID(), persons[i]);
            }
            else if(i == 30){
                paternalAncestors.put(persons[i].getPersonID(), persons[i]);
                maternalAncestors.put(persons[i].getPersonID(), persons[i]);
            }
            if(i == persons.length -1){
                if (persons[i].getGender().equals("m")){
                    maleAncestors.put(persons[i].getPersonID(), persons[i]);
                }
                else{
                    femaleAncestors.put(persons[i].getPersonID(), persons[i]);
                }
            }
            else if(i % 2 == 0){
                femaleAncestors.put(persons[i].getPersonID(), persons[i]);
            }
            else if(i % 2 == 1){
                maleAncestors.put(persons[i].getPersonID(), persons[i]);
            }
        }
    }

    public void setPeople(Person person){
        people.put(person.getPersonID(), person);
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
}

