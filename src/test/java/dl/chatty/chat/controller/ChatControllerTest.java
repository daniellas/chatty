package dl.chatty.chat.controller;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
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
            Chat.of(1l, "title1", "user1", new Date()),
            Chat.of(2l, "title2", "user2", new Date()));

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

        MvcResult result = mvc.perform(get("/chats"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[1].id", is(2)));
    }

    @WithMockUser(username = SecurityTestUtil.CUSTOMER_USERNAME, password = SecurityTestUtil.PASSWORD, roles = { "CUSTOMER" })
    @Test
    public void shouldReturnValidationErrorOnNullTitle() throws Exception {
        mvc.perform(post("/chats")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new ChatView(null, null, null, null))))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = SecurityTestUtil.CUSTOMER_USERNAME, password = SecurityTestUtil.PASSWORD, roles = { "CUSTOMER" })
    @Test
    public void shouldGetById() throws JsonProcessingException, Exception {
        when(chatStreams.getOne(1l)).thenReturn(Observable.just(new ChatView(1l, "title", "user", new Date())));

        MvcResult result = mvc.perform(get("/chats/{id}", 1))
                .andExpect(request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.createdBy", is("user")))
                .andExpect(jsonPath("$.createTs", is(not(empty()))));
    }

    @WithMockUser(username = SecurityTestUtil.CUSTOMER_USERNAME, password = SecurityTestUtil.PASSWORD, roles = { "CUSTOMER" })
    @Test
    public void shouldCreateChat() throws Exception {
        when(chatStreams.create(any())).thenReturn(Observable.just(new ChatView(1l, "title", "user", new Date())));

        MvcResult result = mvc.perform(post("/chats")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsString(new ChatView(null, "title", null, null))))
                .andExpect(request().asyncStarted())
                .andReturn();

        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("title")))
                .andExpect(jsonPath("$.createdBy", is("user")))
                .andExpect(jsonPath("$.createTs", is(not(empty()))));

    }

}
