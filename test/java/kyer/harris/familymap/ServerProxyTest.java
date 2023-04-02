package kyer.harris.familymap;
import Model.*;
import Requests.*;
import kyer.harris.familymap.backend.ServerProxy;


import org.junit.jupiter.api.*;

public class  ServerProxyTest {
    private static final User SHEILA = new User("sheila", "parker", "sheila@parker.com", "Sheila", "Parker", "f", "Sheila_Parker");
    private static final User PATRICK = new User("patrick", "spencer", "sheila@spencer.com", "Patrick", "Spencer", "m", "Patrick_Spencer");
    private static final LoginRequest loginRequest = new LoginRequest(SHEILA.getUsername(), SHEILA.getPassword());
    private static final LoginRequest loginRequest2 = new LoginRequest(PATRICK.getUsername(), PATRICK.getPassword());
    private static final RegisterRequest registerRequest = new RegisterRequest(SHEILA.getUsername(), SHEILA.getPassword(), SHEILA.getEmail(), SHEILA.getFirstName(), SHEILA.getLastName(), SHEILA.getGender());
    private ServerProxy proxy = new ServerProxy();


    @Test
    @DisplayName("Register Valid New User Test")
    public void testValidNewRegister(TestInfo testInfo) {

    }
}
