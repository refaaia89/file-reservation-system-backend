package fifth.year.backendinternetapplication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fifth.year.backendinternetapplication.dto.request.LoginRequest;
import fifth.year.backendinternetapplication.dto.request.RegisterRequest;
import fifth.year.backendinternetapplication.dto.response.AuthenticationResponse;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.dto.response.main.SuccessResponse;
import fifth.year.backendinternetapplication.model.Token;
import fifth.year.backendinternetapplication.model.User;
import fifth.year.backendinternetapplication.model.enums.TokenType;
import fifth.year.backendinternetapplication.repository.RoleRepository;
import fifth.year.backendinternetapplication.repository.TokenRepository;
import fifth.year.backendinternetapplication.repository.UserRepository;
import fifth.year.backendinternetapplication.utils.validator.ObjectsValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final ObjectsValidator<RegisterRequest> registerValidator;
    private final ObjectsValidator<LoginRequest> loginValidator;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository repository, ObjectsValidator<RegisterRequest> registerValidator, ObjectsValidator<LoginRequest> loginValidator, RoleRepository roleRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.registerValidator = registerValidator;
        this.loginValidator = loginValidator;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<Response> register(RegisterRequest request) {
        registerValidator.validate(request);
        User user = new User();
        UserService.userCreate(request, user, passwordEncoder, roleRepository);
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return ResponseEntity.ok(new SuccessResponse("User Registeration Successfully",
                new AuthenticationResponse(savedUser.getId(),savedUser.getName(),savedUser.getEmail(),
                        savedUser.getRole().getName(),jwtToken, jwtRefreshToken)));
    }

    public ResponseEntity<Response> login(LoginRequest request) {
        loginValidator.validate(request);
        authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        var user = repository.findByEmail(request.email()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return ResponseEntity.ok(new SuccessResponse("User Logging Successfully",
                new AuthenticationResponse(user.getId(), user.getName(), user.getEmail(), user.getRole().getName(),
                        jwtToken, jwtRefreshToken)));
    }
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.repository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                saveUserToken(user, accessToken);
                var authResponse = new SuccessResponse("User Re-Logging Successfully",
                        new AuthenticationResponse(
                        user.getId(),user.getName(),user.getEmail(),user.getRole().getName(),accessToken,refreshToken));
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);
            }
        }
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setToken_type(TokenType.BEARER);
        token.setRevoked(false);
        token.setExpired(false);
        tokenRepository.save(token);
    }

   /* private void revokeAllUserTokens(String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "You must be logged in to logout");
        }
        final String jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null && (!storedToken.isExpired() || !storedToken.isRevoked())) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        } else {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Bad Token sent with the request");
        }

      *//*  var validTokens=tokenRepository.findAllValidTokensByUser(user.getId());
        if(validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }*/
}
