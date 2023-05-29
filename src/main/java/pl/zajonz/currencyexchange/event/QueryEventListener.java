package pl.zajonz.currencyexchange.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.zajonz.currencyexchange.model.Query;

@Service
@Slf4j
public class QueryEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleQueryEvent(QueryEvent event) {
        Query query = event.getQuery();
        log.info("{}, {}, {}", query.getFrom(), query.getTo(), query.getAmount());
    }
}
