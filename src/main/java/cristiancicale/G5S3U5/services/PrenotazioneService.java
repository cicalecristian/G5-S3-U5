package cristiancicale.G5S3U5.services;

import cristiancicale.G5S3U5.entities.Evento;
import cristiancicale.G5S3U5.entities.Prenotazione;
import cristiancicale.G5S3U5.entities.Ruolo;
import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.BadRequestException;
import cristiancicale.G5S3U5.exceptions.NotFoundException;
import cristiancicale.G5S3U5.exceptions.UnauthorizedException;
import cristiancicale.G5S3U5.repositories.PrenotazioneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PrenotazioneService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final EventoService eventoService;

    public PrenotazioneService(PrenotazioneRepository prenotazioneRepository, EventoService eventoService) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.eventoService = eventoService;
    }

    public Prenotazione save(UUID eventoId, Utente utente, int postiPrenotati) {

        if (utente.getRuolo() != Ruolo.UTENTE) {
            throw new UnauthorizedException("Solo gli utenti possono effettuare prenotazioni");
        }

        if (postiPrenotati <= 0) {
            throw new BadRequestException("Devi prenotare almeno 1 posto");
        }

        Evento evento = eventoService.findById(eventoId);

        if (evento == null) {
            throw new NotFoundException("Evento non trovato");
        }

        if (evento.getPosti_disponibili() < postiPrenotati) {
            throw new BadRequestException("Posti non disponibili per questo evento");
        }

        evento.setPosti_disponibili(
                evento.getPosti_disponibili() - postiPrenotati
        );

        Prenotazione prenotazione = new Prenotazione(utente, evento, postiPrenotati);

        Prenotazione saved = prenotazioneRepository.save(prenotazione);

        log.info("Prenotazione creata con id " + saved.getId());

        return saved;
    }

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 10) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    public void delete(UUID id, Utente utente) {

        Prenotazione found = findById(id);

        if (!found.getUtente().getId().equals(utente.getId())) {
            throw new UnauthorizedException("Non sei autorizzato a eliminare questa prenotazione");
        }

        prenotazioneRepository.delete(found);

        log.info("Prenotazione eliminata " + id);
    }
}
