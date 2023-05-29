package pl.zajonz.currencyexchange.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    private String currency;
    private LocalDate timestamp;
    private Double bid;
    private Double ask;

    @Override
    public String toString() {
        return "Currency{" +
                "currency='" + currency + '\'' +
                ", timestamp=" + timestamp +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }

}
