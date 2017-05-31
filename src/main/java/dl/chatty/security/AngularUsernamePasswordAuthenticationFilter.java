package dl.chatty.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AngularUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper mapper;

    public AngularUsernamePasswordAuthenticationFilter(String url, AuthenticationManager authenticationManager, ObjectMapper mapper) {
        super(url);
        setAuthenticationManager(authenticationManager);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp)
            throws AuthenticationException, IOException, ServletException {
        if (!req.getMethod().equals(RequestMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + req.getMethod());

        }
        Credentials credentials = mapper.readValue(req.getReader(), Credentials.class);

        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        mapper.writeValue(response.getOutputStream(), UserDetailsView.of(authResult));
    }

    @AllArgsConstructor
    @Getter
    static class Credentials {
        private String username;
        private String password;

    }

}
