package mira.space.model.repo;

import mira.space.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //Hibernate сам поймет, что нужно найти пользователя по sessionId
    Optional<User> findBySessionId(String sessionId);
}
