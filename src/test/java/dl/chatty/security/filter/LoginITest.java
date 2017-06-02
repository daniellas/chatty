package dl.chatty.security.filter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

import dl.chatty.IntegrationTestBase;
import dl.chatty.config.properties.SecurityProperties;
import dl.chatty.security.Roles;
import dl.chatty.security.UserDetailsView;
import dl.chatty.security.filter.AngularUsernamePasswordAuthenticationFilter.Credentials;

public class LoginITest extends IntegrationTestBase {

    @Autowired
    private SecurityProperties securityProperties;

    @Test(expected = HttpClientErrorException.class)
    public void shouldFailOnNonPostLoginAttempt() {
        restTemplate.getForObject(baseUrl() + "/sec/login", UserDetailsView.class);
    }

    @Test
    public void shouldReturnCorrectUserDetailsOnCustomerLogin() {
        UserDetailsView userDetails = restTemplate.postForObject(
                baseUrl() + "/sec/login",
                new Credentials(securityProperties.getCustomerUsername(), securityProperties.getCustomerPassword()),
                UserDetailsView.class);

        assertEquals(securityProperties.getCustomerUsername(), userDetails.getUsername());
        assertThat(userDetails.getRoles(), containsInAnyOrder(Roles.CUSTOMER));
    }

    @Test
    public void shouldReturnCorrectUserDetailsOnEmployeeLogin() {
        UserDetailsView userDetails = restTemplate.postForObject(
                baseUrl() + "/sec/login",
                new Credentials(securityProperties.getEmployeeUsername(), securityProperties.getEmployeePassword()),
                UserDetailsView.class);

        assertEquals(securityProperties.getEmployeeUsername(), userDetails.getUsername());
        assertThat(userDetails.getRoles(), containsInAnyOrder(Roles.EMPLOYEE));
    }

}
