package pl.zajonz.currencyexchange.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajonz.currencyexchange.event.QueryEvent;
import pl.zajonz.currencyexchange.model.AvailableCurrencies;
import pl.zajonz.currencyexchange.model.Currency;
import pl.zajonz.currencyexchange.model.Exchange;
import pl.zajonz.currencyexchange.model.Query;
import pl.zajonz.currencyexchange.repository.ExchangeRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeRepository exchangeRepository;
    private final AvailableCurrencies availableCurrencies;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AvailableCurrencies getAllCurrencies() {
        return availableCurrencies;
    }

//    @Override
//    public Exchange convertCurrency(Query query) {
//        if (query.getTo().equals(query.getFrom())) {
//            return new Exchange(LocalDate.now(), 1.0, query, query.getAmount() * 1.0);
//        }
//
//        Currency currencyTo = null;
//        Currency currencyFrom = null;
//
//        if (!query.getTo().equals("PLN")) {
//            currencyTo = exchangeRepository.findById(query.getTo()).orElseThrow(
//                    () -> new EntityNotFoundException("Not found currency \"to\" with code: " + query.getTo()));
//        }
//        if (!query.getFrom().equals("PLN")) {
//            currencyFrom = exchangeRepository.findById(query.getFrom()).orElseThrow(
//                    () -> new EntityNotFoundException("Not found currency \"from\" with code: " + query.getFrom()));
//        }
//
//        double rate;
//        if (query.getFrom().equals("PLN")) {
//            rate = 1 / currencyTo.getAsk();
//        } else if (query.getTo().equals("PLN")) {
//            rate = currencyFrom.getBid();
//        } else {
//            rate = currencyFrom.getAsk() / currencyTo.getBid();
//        }
//
//        return new Exchange(LocalDate.now(), rate, query, query.getAmount() * rate);
//    }

    @Override
    @Transactional
    public Exchange convertCurrency(Query query) {

        //...

        eventPublisher.publishEvent(new QueryEvent(query));

        //...

        return new Exchange(LocalDate.now(), 0.0, query, 0.0);
    }
}
