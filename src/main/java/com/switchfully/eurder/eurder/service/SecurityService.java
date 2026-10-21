package com.switchfully.eurder.eurder.service;

import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.domain.user.User;
import com.switchfully.eurder.eurder.exception.ForbiddenException;
import com.switchfully.eurder.eurder.exception.UnauthorizedException;
import com.switchfully.eurder.eurder.repository.ItemRepository;
import com.switchfully.eurder.eurder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SecurityService {
    UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateAuthorization(String authorization, Feature feature) {
        if(authorization == null || !authorization.startsWith("Basic")) {
            throw new UnauthorizedException();
        }

        String encodedCredentials = authorization.replaceFirst("^Basic\\s+", "");
        String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials), StandardCharsets.UTF_8);

        if (!decodedCredentials.contains(":")) {
            throw new UnauthorizedException();

        }

        String[] emailAndPassword = decodedCredentials.split( ":", 2);

        String email = emailAndPassword[0];
        String password = emailAndPassword[1];

        User user = userRepository.get(email);

        if(!passwordMatch(user, password)){
            throw new UnauthorizedException();
        }

        if(!user.allowedToPerform(feature)){
            throw new ForbiddenException();
        }
    }

    private boolean passwordMatch(User user, String password) {
        return userRepository.get(user.getEmail()).getPassword().equals(password);
    }
}
