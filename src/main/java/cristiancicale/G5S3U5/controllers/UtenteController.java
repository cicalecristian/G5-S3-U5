package cristiancicale.G5S3U5.controllers;

import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.ValidationException;
import cristiancicale.G5S3U5.payloads.UtenteDTO;
import cristiancicale.G5S3U5.payloads.UtenteRespDTO;
import cristiancicale.G5S3U5.services.UtenteService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public UtenteRespDTO saveDipendente(@RequestBody @Validated UtenteDTO body, BindingResult validationResult) {


        if (validationResult.hasErrors()) {

            List<String> errors = validationResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            throw new ValidationException(errors);
        }

        Utente newUtente = this.utenteService.save(body);
        return new UtenteRespDTO(newUtente.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ORGANIZZATORE')")
    public Page<Utente> getUtenti(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "username") String sortBy) {
        return this.utenteService.findAll(page, size, sortBy);
    }

    @GetMapping("/me")
    public Utente getOwnProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public Utente updateOwnProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser, @RequestBody UtenteDTO body) {
        return this.utenteService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnProfile(@AuthenticationPrincipal Utente currentAuthenticatedUser) {
        this.utenteService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

    @GetMapping("/{utenteId}")
    public Utente getById(@PathVariable UUID utenteId) {
        return this.utenteService.findById(utenteId);
    }

    @PutMapping("/{utenteId}")
    public Utente getByIdAndUpdate(@PathVariable UUID utenteId, @RequestBody UtenteDTO body) {
        return this.utenteService.findByIdAndUpdate(utenteId, body);
    }

    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void getByIdAndDelete(@PathVariable UUID utenteId) {
        this.utenteService.findByIdAndDelete(utenteId);
    }
}
