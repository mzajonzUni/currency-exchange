package pl.zajonz.currencyexchange.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zajonz.currencyexchange.model.AvailableCurrencies;
import pl.zajonz.currencyexchange.model.Exchange;
import pl.zajonz.currencyexchange.model.Query;
import pl.zajonz.currencyexchange.service.ExchangeService;

@RequestMapping("api/v1/exchange")
@RestController
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/currencies")
    public AvailableCurrencies getAllCurrencies(){
        return exchangeService.getAllCurrencies();
    }

    @GetMapping("/convert")
    public Exchange convertCurrency(@RequestBody Query query){
        return exchangeService.convertCurrency(query);
    }

}
