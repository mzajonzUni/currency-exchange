package pl.zajonz.currencyexchange.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import pl.zajonz.currencyexchange.model.AvailableCurrencies;
import pl.zajonz.currencyexchange.model.Currency;
import pl.zajonz.currencyexchange.model.Exchange;
import pl.zajonz.currencyexchange.model.Query;
import pl.zajonz.currencyexchange.repository.ExchangeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceImplTest {

    @InjectMocks
    private ExchangeServiceImpl exchangeService;
    @Mock
    private ExchangeRepository exchangeRepository;
    @Spy
    private AvailableCurrencies availableCurrencies = new AvailableCurrencies(List.of("EUR"));

    @Test
    void testGetAllCurrencies_ResultsInCurrencyListBeingReturned() {
        //given
        AvailableCurrencies availableCurr = new AvailableCurrencies(List.of("EUR"));

        //when
        AvailableCurrencies returned = exchangeService.getAllCurrencies();

        //then
        assertEquals(availableCurr.getCurrencies(),returned.getCurrencies());
    }

    @Test
    void testConvertCurrency_ReversedRateFromDataBase_ResultsInExchangeBeingReturned() {
        //given
        Query query = new Query("PLN", "EUR", 1.0);
        Currency currencyEUR = new Currency("EUR", LocalDate.now(), 4.5, 4.6);
        Double rate = 1 / currencyEUR.getAsk();
        when(exchangeRepository.findById(anyString())).thenReturn(Optional.of(currencyEUR));
        //when
        Exchange returned = exchangeService.convertCurrency(query);

        //then
        assertEquals(query, returned.getQuery());
        assertEquals(rate, returned.getRate());
        assertEquals(rate * query.getAmount(), returned.getResult());
    }

    @Test
    void testConvertCurrency_RateFromDatabase_ResultsInExchangeBeingReturned() {
        //given
        Query query = new Query("EUR", "PLN", 1.0);
        Currency currencyEUR = new Currency("EUR", LocalDate.now(), 4.5, 4.6);
        Double rate = currencyEUR.getBid();
        when(exchangeRepository.findById(anyString())).thenReturn(Optional.of(currencyEUR));
        //when
        Exchange returned = exchangeService.convertCurrency(query);

        //then
        assertEquals(query, returned.getQuery());
        assertEquals(rate, returned.getRate());
        assertEquals(rate * query.getAmount(), returned.getResult());
    }

    @Test
    void testConvertCurrency_FromAndToAreEquals_ResultsInExchangeBeingReturned() {
        //given
        Query query = new Query("EUR", "EUR", 1.0);

        //when
        Exchange returned = exchangeService.convertCurrency(query);

        //then
        assertEquals(query, returned.getQuery());
        assertEquals(1, returned.getRate());
        assertEquals(1, returned.getResult());
    }

    @Test
    void testConvertCurrency_RateCalculated_ResultsInExchangeBeingReturned() {
        //given
        Query query = new Query("EUR", "USD", 1.0);
        Currency currencyEUR = new Currency("EUR", LocalDate.now(), 4.5, 4.6);
        Currency currencyUSD = new Currency("USD", LocalDate.now(), 4.5, 4.6);
        when(exchangeRepository.findById("EUR")).thenReturn(Optional.of(currencyEUR));
        when(exchangeRepository.findById("USD")).thenReturn(Optional.of(currencyEUR));
        //when
        Exchange returned = exchangeService.convertCurrency(query);

        //then
        assertEquals(query, returned.getQuery());
        assertEquals(currencyEUR.getAsk() / currencyUSD.getBid(), returned.getRate());
        assertEquals(currencyEUR.getAsk() / currencyUSD.getBid(), returned.getResult());
    }

    @Test
    void testConvertCurrency_WrongToValue_ResultsInEntityNotFoundException() {
        //given
        Query query = new Query("EUR", "TEST1", 1.0);
        String exceptionMsg = "Not found currency \"to\" with code: TEST1";

        when(exchangeRepository.findById(anyString())).thenReturn(Optional.empty());
        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> exchangeService.convertCurrency(query));

        assertEquals(exceptionMsg, exception.getMessage());
    }

    @Test
    void testConvertCurrency_WrongFromValue_ResultsInEntityNotFoundException() {
        //given
        Query query = new Query("TEST1", "EUR", 1.0);
        String exceptionMsg = "Not found currency \"from\" with code: " + query.getFrom();
        Currency currencyEUR = new Currency("EUR", LocalDate.now(), 4.5, 4.6);
        when(exchangeRepository.findById("EUR")).thenReturn(Optional.of(currencyEUR));
        when(exchangeRepository.findById("TEST1")).thenReturn(Optional.empty());
        //when //then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> exchangeService.convertCurrency(query));

        assertEquals(exceptionMsg, exception.getMessage());
    }
}