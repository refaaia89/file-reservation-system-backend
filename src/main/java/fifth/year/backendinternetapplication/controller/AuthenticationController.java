package fifth.year.backendinternetapplication.controller;


import fifth.year.backendinternetapplication.dto.request.LoginRequest;
import fifth.year.backendinternetapplication.dto.request.RegisterRequest;
import fifth.year.backendinternetapplication.dto.response.main.Response;
import fifth.year.backendinternetapplication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService service;
    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request){
        return service.register(request);
    }
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest request){
        return service.login(request);
    }
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request,response);
    }
}
