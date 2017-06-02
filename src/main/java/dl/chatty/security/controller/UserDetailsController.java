package dl.chatty.security.controller;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dl.chatty.security.UserDetailsView;

@RestController
@RequestMapping("/sec")
public class UserDetailsController {

    @RequestMapping(value = "/userdetails", method = RequestMethod.POST)
    public UserDetailsView userDetails() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(UserDetailsView::of)
                .orElse(null);
    }
}
