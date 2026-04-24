package cristiancicale.G5S3U5.repositories;

import cristiancicale.G5S3U5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtenteRepository extends JpaRepository<Utente, UUID> {
    boolean existsByUsername(String email);

    Optional<Utente> findByUsername(String username);
}
