package dl.chatty.security;

import static org.hamcrest.core.Is.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import dl.chatty.MvcTestBase;

@WebMvcTest(UserDetailsController.class)
public class UserDetailsControllerTest extends MvcTestBase {

    @Autowired
    private MockMvc mvc;

    @WithAnonymousUser
    @Test
    public void shouldRespondUnauthorizedOnAnonymousUser() throws Exception {
        mvc.perform(post("/sec/userdetails")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = "customer", password = "password")
    @Test
    public void shouldReturnUserDetails() throws Exception {
        mvc.perform(post("/sec/userdetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("customer")));
    }

}
