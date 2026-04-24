package cristiancicale.G5S3U5.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PrenotazioneDTO(

        @NotNull(message = "Utente obbligatorio")
        UUID utenteId,

        @NotNull(message = "Evento obbligatorio")
        UUID eventoId,

        @Min(value = 1, message = "Devi prenotare almeno 1 posto")
        int postiPrenotati
) {
}
