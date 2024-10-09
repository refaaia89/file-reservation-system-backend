package fifth.year.backendinternetapplication.utils.handlers;

import fifth.year.backendinternetapplication.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class MyLogoutHandler implements LogoutHandler {

    private  final TokenRepository tokenRepository;

    public MyLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
    {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "You must be logged in to logout");
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null && (!storedToken.isExpired() || !storedToken.isRevoked())) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Bad Token sent with the request");
        }
    }
}
