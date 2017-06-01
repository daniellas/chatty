package dl.chatty.chat.broker;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.security.Roles;
import dl.chatty.security.SecurityRoleChecker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleBasedMessageSendGuard implements MessageSendGuard, SecurityRoleChecker {

    private final ChatRepository chatRepo;

    @Override
    public Optional<Chat> messageChat(Long chatId, Principal user) {
        Authentication auth = (UsernamePasswordAuthenticationToken) user;
        boolean isEmployee = hasAuthority(auth, Roles.EMPLOYEE).isPresent();

        return Optional.ofNullable(chatRepo.findOne(chatId))
                .filter(c -> isEmployee || user.getName().equals(c.getCreatedBy()));
    }

}
