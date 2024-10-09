package fifth.year.backendinternetapplication.dto.response;

import fifth.year.backendinternetapplication.model.enums.TokenType;

import java.time.LocalDateTime;

public record TokenResponse(
        long tokenId,
        String token,
        TokenType tokenType,
        Boolean expired,
        Boolean revoked,
        LocalDateTime created_at,
        LocalDateTime updated_at
) {

}
