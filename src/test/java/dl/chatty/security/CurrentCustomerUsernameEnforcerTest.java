package dl.chatty.security;

import static dl.chatty.SecurityTestUtil.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.security.authentication.InsufficientAuthenticationException;

public class CurrentCustomerUsernameEnforcerTest {

    private UsernameEnforcer enforcer = new CustomerUsernameEnforcer(new SecurityContextAuthenticationSupplier());

    @Test(expected = InsufficientAuthenticationException.class)
    public void shouldFailOnNullAuthentication() {
        clearAuthentication();
        enforcer.apply(null);
    }

    @Test(expected = InsufficientAuthenticationException.class)
    public void shouldFailOnAnonymousAuthentication() {
        setAnonymousAuthentication("anon", "anon", "ANON");

        enforcer.apply(null);
    }

    @Test
    public void shouldReturnNullOnEmployeeRole() {
        setAuthentication(EMPLOYEE_USERNAME, PASSWORD, Roles.EMPLOYEE);

        assertNull(enforcer.apply(null));
    }

    @Test
    public void shouldReturnGivenOnEmployeeRole() {
        setAuthentication(EMPLOYEE_USERNAME, PASSWORD, Roles.EMPLOYEE);

        assertEquals("customer", enforcer.apply("customer"));
    }

    @Test
    public void shouldReturnCurrentUsernameOnCustomerRole() {
        setAuthentication(CUSTOMER_USERNAME, PASSWORD, Roles.CUSTOMER);

        assertEquals("customer", enforcer.apply(null));
        assertEquals("customer", enforcer.apply("customer"));
        assertEquals("customer", enforcer.apply("customer1"));
    }

}
