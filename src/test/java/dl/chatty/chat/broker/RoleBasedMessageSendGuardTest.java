package dl.chatty.chat.broker;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.security.Roles;
import dl.chatty.security.UsernameSupplier;

@RunWith(MockitoJUnitRunner.class)
public class RoleBasedMessageSendGuardTest {

    @Mock
    private ChatRepository chatRepo;

    @Mock
    private UsernameSupplier usernameSupplier;

    @InjectMocks
    private RoleBasedMessageSendGuard guard;

    @Test
    public void shouldReturnCustomerChatForEmployee() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("employee", "password",
                Arrays.asList(new SimpleGrantedAuthority(Roles.EMPLOYEE)));

        when(chatRepo.getOne(any())).thenReturn(Optional.of(Chat.of("id", "title", "customer", new Date())));
        when(usernameSupplier.justGet()).thenReturn("employee");

        assertTrue(guard.messageChat("id", auth).isPresent());
    }

    @Test
    public void shouldReturnOwnedChatForCustomer() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("customer", "password",
                Arrays.asList(new SimpleGrantedAuthority(Roles.CUSTOMER)));

        when(chatRepo.getOne(any())).thenReturn(Optional.of(Chat.of("id", "title", "customer", new Date())));
        when(usernameSupplier.justGet()).thenReturn("customer");

        assertTrue(guard.messageChat("id", auth).isPresent());
    }

    @Test
    public void shouldReturnEmptyForChatNotOwnedByCustomer() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("customer", "password",
                Arrays.asList(new SimpleGrantedAuthority(Roles.CUSTOMER)));

        when(chatRepo.getOne(any())).thenReturn(Optional.of(Chat.of("id", "title", "customer1", new Date())));
        when(usernameSupplier.justGet()).thenReturn("customer");

        assertFalse(guard.messageChat("id", auth).isPresent());
    }

}
