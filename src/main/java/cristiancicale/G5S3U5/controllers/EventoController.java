package cristiancicale.G5S3U5.controllers;

import cristiancicale.G5S3U5.entities.Evento;
import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.ValidationException;
import cristiancicale.G5S3U5.payloads.EventoDTO;
import cristiancicale.G5S3U5.payloads.EventoRespDTO;
import cristiancicale.G5S3U5.services.EventoService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventoRespDTO saveEvento(@RequestBody @Validated EventoDTO body, BindingResult validationResult, @RequestAttribute Utente user) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errors);
        }

        Evento newEvento = this.eventoService.save(body, user);

        return new EventoRespDTO(newEvento.getId());
    }

    @GetMapping
    public Page<Evento> getEventi(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "data") String sortBy) {
        return this.eventoService.findAll(page, size, sortBy);
    }

    @GetMapping("/{eventoId}")
    public Evento getById(@PathVariable UUID eventoId) {
        return this.eventoService.findById(eventoId);
    }


    @PutMapping("/{eventoId}")
    public Evento getByIdAndUpdate(@PathVariable UUID eventoId, @RequestBody EventoDTO body, @RequestAttribute Utente user) {
        return this.eventoService.findByIdAndUpdate(eventoId, body, user);
    }

    @DeleteMapping("/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void getByIdAndDelete(@PathVariable UUID eventoId, @RequestAttribute Utente user) {
        this.eventoService.findByIdAndDelete(eventoId, user);
    }
}
