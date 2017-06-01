package dl.chatty.chat.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dl.chatty.SecuredMvcTestBase;
import dl.chatty.SecurityTestUtil;
import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.mapping.ChatMapper;
import dl.chatty.chat.stream.ChatStreams;
import dl.chatty.chat.view.ChatView;
import io.reactivex.Observable;

@WebMvcTest(ChatController.class)
public class ChatControllerTest extends SecuredMvcTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatStreams chatStreams;

    @Autowired
    private ObjectMapper mapper;

    private static Collection<Chat> CHATS = Arrays.asList(
            Chat.of("1", "title1", "user1", new Date()),
            Chat.of("2", "title2", "user2", new Date()));

    @WithAnonymousUser
    @Test
    public void shouldRespondForbiddenOnAnonymousList() throws Exception {
        mvc.perform(get("/chats")).andExpect(status().isForbidden());
    }

    @WithMockUser(username = SecurityTestUtil.CUSTOMER_USERNAME, password = SecurityTestUtil.PASSWORD)
    @Test
    public void shouldReturnChats() throws Exception {
        Observable<Collection<ChatView>> observable = Observable.just(CHATS)
                .map(ChatMapper::toViewList);

        when(chatStreams.findAll()).thenReturn(observable);
        mvc.perform(get("/chats")).andExpect(status().isOk());
    }

    @WithMockUser(username = SecurityTestUtil.CUSTOMER_USERNAME, password = SecurityTestUtil.PASSWORD, roles = { "CUSTOMER" })
    @Test
    public void shouldReturnValidationErrorOnNullTitle() throws Exception {
        mvc.perform(post("/chats")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new ChatView(null, null, null, null))))
                .andExpect(status().isBadRequest());
    }

}
