package dl.chatty.security;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

import dl.chatty.SecurityTestUtil;

public class SecurityContextCurrentUsernameSupplierTest {

    private UsernameSupplier usernameSupplier = new SecurityContextUsernameSupplier(new SecurityContextAuthenticationSupplier());

    @Test
    public void shouldReturnCurrentUser() {
        SecurityTestUtil.setAuthentication("username", "password", Roles.CUSTOMER);

        Assert.assertEquals("username", usernameSupplier.get().get());
    }

    @Test
    public void shouldReturnCurrentUserByJustGet() {
        SecurityTestUtil.setAuthentication("username", "password", Roles.CUSTOMER);

        Assert.assertEquals("username", usernameSupplier.justGet());
    }

    @Test
    public void shouldReturnCurrentUserByGetOrThrow() {
        SecurityTestUtil.setAuthentication("username", "password", Roles.CUSTOMER);

        Assert.assertEquals("username", usernameSupplier.getOrThrowNoCredentials());
    }

    @Test
    public void shouldBeEmptyOnNullAuthentication() {
        SecurityTestUtil.clearAuthentication();
        Assert.assertFalse(usernameSupplier.get().isPresent());
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void shouldThrowOnNullAuthentication() {
        SecurityTestUtil.clearAuthentication();
        usernameSupplier.getOrThrowNoCredentials();
    }

}
