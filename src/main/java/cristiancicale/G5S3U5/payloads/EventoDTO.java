package cristiancicale.G5S3U5.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EventoDTO(

        @NotBlank(message = "Titolo obbligatorio")
        String titolo,

        String descrizione,

        @NotNull(message = "Data obbligatoria")
        @FutureOrPresent(message = "La data non può essere nel passato")
        LocalDate data,

        @NotBlank(message = "Location obbligatoria")
        String location,

        @Min(value = 1, message = "Deve esserci almeno 1 posto")
        int postiDisponibili
) {
}
