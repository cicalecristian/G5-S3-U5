package cristiancicale.G5S3U5.services;

import cristiancicale.G5S3U5.entities.Evento;
import cristiancicale.G5S3U5.entities.Ruolo;
import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.NotFoundException;
import cristiancicale.G5S3U5.payloads.EventoDTO;
import cristiancicale.G5S3U5.repositories.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento save(Evento evento, Utente organizzatore) {

        if (organizzatore.getRuolo() != Ruolo.ORGANIZZATORE) {
            throw new RuntimeException("Solo gli organizzatori possono creare eventi");
        }

        evento.setOrganizzatore(organizzatore);

        Evento saved = eventoRepository.save(evento);

        log.info("Evento creato con id " + saved.getId());

        return saved;
    }

    public Page<Evento> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 10) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventoRepository.findAll(pageable);
    }

    public Evento findById(UUID eventoId) {
        return this.eventoRepository.findById(eventoId).orElseThrow(() -> new NotFoundException(eventoId));
    }

    public Evento update(UUID id, EventoDTO body, Utente organizzatore) {

        Evento found = findById(id);

        if (!found.getOrganizzatore().getId().equals(organizzatore.getId())) {
            throw new RuntimeException("Non sei autorizzato a modificare questo evento");
        }

        found.setTitolo(body.titolo());
        found.setDescrizione(body.descrizione());
        found.setData(body.data());
        found.setLocation(body.location());
        found.setPosti_disponibili(body.postiDisponibili());

        Evento updated = eventoRepository.save(found);

        log.info("Evento aggiornato " + updated.getId());

        return updated;
    }

    public void delete(UUID id, Utente organizzatore) {

        Evento found = findById(id);

        if (!found.getOrganizzatore().getId().equals(organizzatore.getId())) {
            throw new RuntimeException("Non sei autorizzato a eliminare questo evento");
        }

        eventoRepository.delete(found);

        log.info("Evento eliminato " + id);
    }
}
