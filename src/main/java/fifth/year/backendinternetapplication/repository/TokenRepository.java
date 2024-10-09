package fifth.year.backendinternetapplication.repository;

import fifth.year.backendinternetapplication.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(
            """
                select t from Token t inner join User u on t.user.id = u.id
                where u.id = :user_id and (t.expired = false or t.revoked = false)
            """
    )
    List<Token> findAllValidTokensByUser(long user_id);

    Optional<Token> findByToken(String token);
}
