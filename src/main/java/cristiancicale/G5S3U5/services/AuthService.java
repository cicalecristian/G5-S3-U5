package cristiancicale.G5S3U5.services;

import cristiancicale.G5S3U5.entities.Utente;
import cristiancicale.G5S3U5.exceptions.NotFoundException;
import cristiancicale.G5S3U5.exceptions.UnauthorizedException;
import cristiancicale.G5S3U5.payloads.LoginDTO;
import cristiancicale.G5S3U5.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteService utenteService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UtenteService utenteService, TokenTools tokenTools, PasswordEncoder bcrypt) {

        this.utenteService = utenteService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        try {
            Utente found = this.utenteService.findByUsername(body.username());
            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Credenziali errate");
            }
        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}
