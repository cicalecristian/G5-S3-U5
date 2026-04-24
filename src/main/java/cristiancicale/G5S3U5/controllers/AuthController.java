package cristiancicale.G5S3U5.controllers;

import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.ValidationException;
import cristiancicale.G5S3U5.payloads.LoginDTO;
import cristiancicale.G5S3U5.payloads.LoginRespDTO;
import cristiancicale.G5S3U5.payloads.UtenteDTO;
import cristiancicale.G5S3U5.payloads.UtenteRespDTO;
import cristiancicale.G5S3U5.services.AuthService;
import cristiancicale.G5S3U5.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtenteService utenteService;

    public AuthController(AuthService authService, UtenteService utenteService) {

        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // 201
    public UtenteRespDTO saveUser(@RequestBody @Validated UtenteDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        Utente newUtente = this.utenteService.save(body);
        return new UtenteRespDTO(newUtente.getId());
    }
}
