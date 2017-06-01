package dl.chatty.chat.broker;

import java.util.Optional;

import org.springframework.security.core.Authentication;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.security.AuthenticationSupplier;
import dl.chatty.security.Roles;
import dl.chatty.security.SecurityRoleChecker;
import dl.chatty.security.UsernameSupplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleBasedMessageSendGuard implements MessageSendGuard, SecurityRoleChecker {

    private final ChatRepository chatRepo;

    private final AuthenticationSupplier authenticationSupplier;

    private final UsernameSupplier usernameSupplier;

    @Override
    public Optional<Chat> messageChat(String chatId) {
        Authentication auth = authenticationSupplier.getOrThrowIfAnonymous();
        boolean isEmployee = hasAuthority(auth, Roles.EMPLOYEE).isPresent();

        return chatRepo.getOne(chatId)
                .filter(c -> isEmployee || usernameSupplier.justGet().equals(c.getCreatedBy()));
    }

}
