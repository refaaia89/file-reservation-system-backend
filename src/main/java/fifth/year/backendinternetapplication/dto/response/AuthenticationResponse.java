package fifth.year.backendinternetapplication.dto.response;

public record AuthenticationResponse(long userId,String username,String email,String role, String token, String refresh_token){
}
