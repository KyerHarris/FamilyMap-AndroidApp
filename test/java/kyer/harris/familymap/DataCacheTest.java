package kyer.harris.familymap;
import Model.*;
import Requests.*;
import Results.EventResult;
import Results.LoginResult;
import Results.PersonResult;
import kyer.harris.familymap.backend.DataCache;
import kyer.harris.familymap.backend.ServerProxy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Map;

public class DataCacheTest {
    private static final User SHEILA = new User("sheila", "parker", "sheila@parker.com", "Sheila", "Parker", "f", "Sheila_Parker");

    @BeforeAll
    static void setup(){
        ServerProxy proxy = new ServerProxy();
        proxy.setServerHost("localhost");
        LoginResult result = proxy.login(new LoginRequest("sheila", "parker"));
        DataCache.getInstance().setPersonID(result.getPersonID());
        DataCache.getInstance().setAuthtoken(result.getAuthtoken());
        ServerProxy server = new ServerProxy();
        PersonResult personResult = server.getPersons();
        EventResult eventResult = server.getEvents();
        DataCache.getInstance().setPeople(personResult.getData());
        DataCache.getInstance().setEvents(eventResult.getData());
    }

    @Test
    @DisplayName("Calculate Relationship")
    public void calculateRelationship(){
        Person sheila = DataCache.getInstance().getPerson(SHEILA.getPersonID());
        Person mother = DataCache.getInstance().getPerson(sheila.getMotherID());
        Person father = DataCache.getInstance().getPerson(sheila.getFatherID());
        Person spouse = DataCache.getInstance().getPerson(sheila.getSpouseID());
        String relationship = DataCache.getInstance().getRelationship(father, sheila);
        assertEquals(relationship, "FATHER");
        relationship = DataCache.getInstance().getRelationship(mother, sheila);
        assertEquals(relationship, "MOTHER");
        relationship = DataCache.getInstance().getRelationship(spouse, sheila);
        assertEquals(relationship, "SPOUSE");
    }

    @Test
    @DisplayName("Calculate Not Relationship")
    public void calculateNotRelationship(){
        Person sheila = DataCache.getInstance().getPerson(SHEILA.getPersonID());
        Person random = DataCache.getInstance().getPerson(DataCache.getInstance().getPerson(sheila.getFatherID()).getFatherID());
        String relationship = DataCache.getInstance().getRelationship(random, sheila);
        assertEquals(relationship, "UNRELATED");
    }

    @Test
    @DisplayName("Filter Events")
    public void filterEvents(){
        for (Map.Entry<String, Event> m: DataCache.getInstance().getFemaleEvents().entrySet()) {
            assertEquals(DataCache.getInstance().getPerson(m.getValue().getPersonID()).getGender(), "f");
        }
        for (Map.Entry<String, Event> m: DataCache.getInstance().getMaleEvents().entrySet()) {
            assertEquals(DataCache.getInstance().getPerson(m.getValue().getPersonID()).getGender(), "m");
        }

    }

    @Test
    @DisplayName("Chronological Sort")
    public void chronologicalSort(){
        ArrayList<Event> events = DataCache.getInstance().getIndividualsEvents(SHEILA.getPersonID());
        assertTrue(events.get(0).getYear() < events.get(1).getYear());
        assertTrue(events.get(1).getYear() < events.get(2).getYear());
    }

    @Test
    @DisplayName("Search Test")
    public void searchTest(){
        String test = "s";
        ArrayList<Event> list = DataCache.getInstance().getEventsList(test);
        for (int i = 0; i < list.size(); i++) {
            String response = list.get(i).getEventType() + " " + list.get(i).getCity() + ", " + list.get(i).getCountry() + " (" + list.get(i).getYear() + ")";
            assertTrue(response.contains(test));
        }
        test = "al";
        list = DataCache.getInstance().getEventsList(test);
        for (int i = 0; i < list.size(); i++) {
            String response = list.get(i).getEventType() + " " + list.get(i).getCity() + ", " + list.get(i).getCountry() + " (" + list.get(i).getYear() + ")";
            assertTrue(response.contains(test));
        }
        test = "qwerty";
        list = DataCache.getInstance().getEventsList(test);
        assertEquals(0, list.size());
    }

    //Calculates family relationships (i.e., spouses, parents, children)
    //Filters events according to the current filter settings
    //Chronologically sorts a person’s individual events (birth first, death last, etc.)
    //Correctly searches for people and events (for your Search Activity)
}
