package cristiancicale.G5S3U5.services;

import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.BadRequestException;
import cristiancicale.G5S3U5.exceptions.NotFoundException;
import cristiancicale.G5S3U5.payloads.UtenteDTO;
import cristiancicale.G5S3U5.repositories.UtenteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder bcrypt;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder bcrypt) {
        this.utenteRepository = utenteRepository;
        this.bcrypt = bcrypt;
    }

    public Utente save(Utente utente) {

        if (this.utenteRepository.existsByUsername(utente.getUsername()))
            throw new BadRequestException("Username già in uso");

        utente.setPassword(this.bcrypt.encode(utente.getPassword()));

        Utente saved = this.utenteRepository.save(utente);

        log.info("Utente con id " + saved.getId() + " registrato");

        return saved;
    }

    public Page<Utente> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 10) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utenteRepository.findAll(pageable);
    }

    public Utente findById(UUID utenteId) {
        return this.utenteRepository.findById(utenteId).orElseThrow(() -> new NotFoundException(utenteId));
    }

    public Utente findByUsername(String username) {
        return this.utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Username non trovato"));
    }

    public Utente findByIdAndUpdate(UUID utenteId, UtenteDTO body) {
        Utente found = this.findById(utenteId);

        if (!found.getUsername().equals(body.username())) {

            if (this.utenteRepository.existsByUsername(body.username()))
                throw new BadRequestException("L'username" + body.username() + "è gia in uso");
        }

        found.setUsername(body.username());

        Utente updateUtente = this.utenteRepository.save(found);

        log.info("L'utente " + updateUtente.getId() + "è stato aggiornato correttamente");

        return updateUtente;
    }

    public void findByIdAndDelete(UUID utenteId) {
        Utente found = this.findById(utenteId);
        this.utenteRepository.delete(found);
    }
}
