package cristiancicale.G5S3U5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "eventi")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Evento {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(nullable = false)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int posti_disponibili;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizzatore_id", nullable = false)
    private Utente organizzatore;

    public Evento(String titolo, String descrizione, LocalDate data, String location, int posti_disponibili, Utente organizzatore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
        this.location = location;
        this.posti_disponibili = posti_disponibili;
        this.organizzatore = organizzatore;
    }
}
