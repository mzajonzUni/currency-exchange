package pl.zajonz.currencyexchange.event;

import lombok.Data;
import pl.zajonz.currencyexchange.model.Query;

@Data
public class QueryEvent {
    private final Query query;
}
