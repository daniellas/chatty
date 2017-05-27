package dl.chatty.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDetailsView {
    private final String username;
    private final Collection<String> roles;

    public static UserDetailsView of(Authentication authentication) {
        return new UserDetailsView(
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(a -> a.getAuthority()).collect(Collectors.toList()));
    }
}
