package pl.zajonz.currencyexchange.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class Exchange {

    private LocalDate timestamp;
    private Double rate;
    private Query query;
    private Double result;

}
