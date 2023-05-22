package pl.zajonz.currencyexchange.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zajonz.currencyexchange.model.Currency;
import pl.zajonz.currencyexchange.repository.ExchangeRepository;
import pl.zajonz.currencyexchange.model.AvailableCurrencies;

@Configuration
public class AvailableCurrenciesConfiguration {

    @Bean
    public AvailableCurrencies availableCurrencies(ExchangeRepository repository) {
        return new AvailableCurrencies(repository.findAll()
                .stream()
                .map(Currency::getCurrency)
                .toList());
    }

}
