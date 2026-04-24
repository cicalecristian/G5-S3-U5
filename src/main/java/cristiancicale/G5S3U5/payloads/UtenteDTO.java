package cristiancicale.G5S3U5.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UtenteDTO(

        @NotBlank(message = "Username obbligatorio")
        @Size(min = 3, max = 30, message = "Username tra 3 e 30 caratteri")
        String username,

        @NotBlank(message = "Password obbligatoria")
        @Size(min = 4, message = "Minimo 4 caratteri")
        String password
) {
}
