package fifth.year.backendinternetapplication.model;

import fifth.year.backendinternetapplication.model.enums.TokenType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "tokens"
)
public class Token extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType token_type;

    private boolean expired;

    private boolean revoked;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Token() {
        created_at = LocalDateTime.now();
    }

    public Token(long id, String token, TokenType token_type, boolean expired, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.token_type = token_type;
        this.expired = expired;
        this.revoked = revoked;
        this.user = user;
        created_at = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getToken_type() {
        return token_type;
    }

    public void setToken_type(TokenType token_type) {
        this.token_type = token_type;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        if (user == null) {
            user=new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return id == token1.id && expired == token1.expired && revoked == token1.revoked && Objects.equals(token, token1.token) && token_type == token1.token_type && Objects.equals(user, token1.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", token_type=" + token_type +
                ", expired=" + expired +
                ", revoked=" + revoked +
                ", user=" + user +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
