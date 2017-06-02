package dl.chatty.security.controller;

import static org.hamcrest.core.Is.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import dl.chatty.SecuredMvcTestBase;
import dl.chatty.SecurityTestUtil;
import dl.chatty.security.controller.UserDetailsController;

@WebMvcTest(UserDetailsController.class)
public class UserDetailsControllerTest extends SecuredMvcTestBase {

    @Autowired
    private MockMvc mvc;

    @WithAnonymousUser
    @Test
    public void shouldRespondForbiddenOnAnonymousUser() throws Exception {
        mvc.perform(post("/sec/userdetails")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = SecurityTestUtil.CUSTOMER_USERNAME, password = SecurityTestUtil.PASSWORD)
    @Test
    public void shouldReturnUserDetails() throws Exception {
        mvc.perform(post("/sec/userdetails").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("customer")));
    }

}
