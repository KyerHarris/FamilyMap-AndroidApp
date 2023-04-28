package kyer.harris.familymap;
import Model.*;
import Requests.*;
import Results.EventResult;
import Results.LoginResult;
import Results.PersonResult;
import Results.RegisterResult;
import kyer.harris.familymap.backend.DataCache;
import kyer.harris.familymap.backend.ServerProxy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class  ServerProxyTest {
    private static final User SHEILA = new User("sheila", "parker", "sheila@parker.com", "Sheila", "Parker", "f", "Sheila_Parker");
    private static final User PATRICK = new User("patrick", "spencer", "sheila@spencer.com", "Patrick", "Spencer", "m", "Patrick_Spencer");
    private static final LoginRequest loginRequest = new LoginRequest(SHEILA.getUsername(), SHEILA.getPassword());
    private static final LoginRequest loginRequest2 = new LoginRequest(PATRICK.getUsername(), PATRICK.getPassword());
    private static final RegisterRequest registerRequest = new RegisterRequest(SHEILA.getUsername(), SHEILA.getPassword(), SHEILA.getEmail(), SHEILA.getFirstName(), SHEILA.getLastName(), SHEILA.getGender());
    private static final ServerProxy proxy = new ServerProxy();
    private String authToken;


    @BeforeAll
    private void setup(){
        proxy.setServerHost("localhost");
    }
    @Test
    @DisplayName("Register Valid User Test")
    public void testValidRegister(){
        RegisterResult result = proxy.register(registerRequest);
        assertNotNull(result);
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getUsername());
        assertNotNull(result.getPersonID());
        authToken = result.getAuthtoken();
    }
    @Test
    @DisplayName("Register Invalid User Test")
    public void testInvalidRegister(){
        RegisterResult result = proxy.register(registerRequest);
        assertNotNull(result);
        assertNotNull(result.getMessage());
    }
    @Test
    @DisplayName("Login Valid User Test")
    public void testValidLogin() {
        LoginResult result = proxy.login(loginRequest);
        assertNotNull(result);
        assertNotNull(result.getAuthtoken());
        assertNotNull(result.getPersonID());
        assertNotNull(result.getUsername());
    }
    @Test
    @DisplayName("Login Invalid User Test")
    public void testInvalidLogin(){
        LoginResult result = proxy.login(loginRequest2);
        assertNotNull(result);
        assertNotNull(result.getMessage());
    }
    @Test
    @DisplayName("Retrieve Valid Relatives Test")
    public void retrieveRelativesValid(){
        PersonResult result = proxy.getPersons();
        assertNotNull(result);
        assertNotNull(result.getData());
    }
    @Test
    @DisplayName("Retrieve Invalid Relatives Test")
    public void retrieveRelativesInvalid(){
        PersonResult result = proxy.getPersons();
        assertNotNull(result);
        assertNotNull(result.getMessage());
    }
    @Test
    @DisplayName("Retrieve Valid Events Test")
    public void retrieveEventsValid(){
        EventResult result = proxy.getEvents();
        assertNotNull(result);
        assertNotNull(result.getData());
    }
    @Test
    @DisplayName("Retrieve Invalid Events Test")
    public void retrieveEventsInvalid(){
        DataCache.getInstance().setAuthtoken("test");
        EventResult result = proxy.getEvents();
        assertNotNull(result);
        assertNotNull(result.getMessage());
    }
}
