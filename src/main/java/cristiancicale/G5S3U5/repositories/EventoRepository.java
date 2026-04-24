package cristiancicale.G5S3U5.repositories;

import cristiancicale.G5S3U5.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
}
