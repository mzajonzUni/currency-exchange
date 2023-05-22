package pl.zajonz.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajonz.currencyexchange.model.Currency;

public interface ExchangeRepository extends JpaRepository<Currency,String> {
}
