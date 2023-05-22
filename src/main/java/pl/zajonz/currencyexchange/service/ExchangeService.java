package pl.zajonz.currencyexchange.service;

import pl.zajonz.currencyexchange.model.AvailableCurrencies;
import pl.zajonz.currencyexchange.model.Exchange;
import pl.zajonz.currencyexchange.model.Query;

public interface ExchangeService {
    AvailableCurrencies getAllCurrencies();

    Exchange convertCurrency(Query query);
}
