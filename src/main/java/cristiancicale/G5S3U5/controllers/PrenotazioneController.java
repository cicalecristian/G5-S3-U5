package cristiancicale.G5S3U5.controllers;

import cristiancicale.G5S3U5.entities.Prenotazione;
import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.ValidationException;
import cristiancicale.G5S3U5.payloads.PrenotazioneDTO;
import cristiancicale.G5S3U5.payloads.PrenotazioneRespDTO;
import cristiancicale.G5S3U5.services.PrenotazioneService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrenotazioneRespDTO savePrenotazione(@RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult, @RequestAttribute Utente user) {

        if (validationResult.hasErrors()) {

            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();

            throw new ValidationException(errors);
        }

        Prenotazione newPrenotazione = this.prenotazioneService.save(body.eventoId(), user, body.postiPrenotati());

        return new PrenotazioneRespDTO(newPrenotazione.getId());
    }

    @GetMapping
    public Page<Prenotazione> getPrenotazioni(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "data") String sortBy) {
        return prenotazioneService.findAll(page, size, sortBy);
    }

    @GetMapping("/{prenotazioneId}")
    public Prenotazione getById(@PathVariable UUID prenotazioneId) {
        return prenotazioneService.findById(prenotazioneId);
    }
    
    @DeleteMapping("/{prenotazioneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID prenotazioneId, @RequestAttribute Utente user) {
        prenotazioneService.findByIdAndDelete(prenotazioneId, user);
    }
}
