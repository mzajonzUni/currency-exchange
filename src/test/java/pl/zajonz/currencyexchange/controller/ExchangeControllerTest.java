package pl.zajonz.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajonz.currencyexchange.model.Currency;
import pl.zajonz.currencyexchange.model.Query;
import pl.zajonz.currencyexchange.repository.ExchangeRepository;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExchangeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExchangeRepository exchangeRepository;
    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    public void init(){
//        Currency currencyEUR = new Currency("EUR", LocalDate.now(), 4.5, 4.6);
//        Currency currencyUSD = new Currency("USD", LocalDate.now(), 4.5, 4.6);
//        exchangeRepository.save(currencyEUR);
//        exchangeRepository.save(currencyUSD);
//    }
//
//
//    @AfterEach
//    public void clear(){
//        exchangeRepository.deleteAll();
//    }

    @Test
    void testGetAllCurrencies() throws Exception{
        //given
        List<Currency> currencyList = exchangeRepository.findAll();

        //when //then
        mockMvc.perform(get("/api/v1/exchange/currencies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies").isArray())
                .andExpect(jsonPath("$.currencies", hasSize(currencyList.size())));
    }

    @Test
    void testConvertCurrency_ReversedRateFromDataBase_ShouldReturnExchange() throws Exception{
        //given
        Query query = new Query("PLN", "EUR", 1.0);
        Currency curr = exchangeRepository.findById("EUR").get();

        //when //then
        mockMvc.perform(get("/api/v1/exchange/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.rate", equalTo(1/curr.getAsk())))
                .andExpect(jsonPath("$.query.from", equalTo(query.getFrom())))
                .andExpect(jsonPath("$.query.to", equalTo(query.getTo())))
                .andExpect(jsonPath("$.query.amount", equalTo(query.getAmount())))
                .andExpect(jsonPath("$.result", equalTo(1/curr.getAsk())));
    }

    @Test
    void testConvertCurrency_RateFromDataBase_ShouldReturnExchange() throws Exception{
        //given
        Query query = new Query("EUR", "PLN", 1.0);
        Currency curr = exchangeRepository.findById("EUR").get();
        //when //then
        mockMvc.perform(get("/api/v1/exchange/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(query)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.rate", equalTo(curr.getBid())))
                .andExpect(jsonPath("$.query.from", equalTo(query.getFrom())))
                .andExpect(jsonPath("$.query.to", equalTo(query.getTo())))
                .andExpect(jsonPath("$.query.amount", equalTo(query.getAmount())))
                .andExpect(jsonPath("$.result", equalTo(curr.getBid())));
    }

    @Test
    void testConvertCurrency_FromAndToAreEquals_ShouldReturnExchange() throws Exception{
        //given
        Query query = new Query("EUR", "EUR", 1.0);

        //when //then
        mockMvc.perform(get("/api/v1/exchange/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(query)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.rate", equalTo(1.0)))
                .andExpect(jsonPath("$.query.from", equalTo(query.getFrom())))
                .andExpect(jsonPath("$.query.to", equalTo(query.getTo())))
                .andExpect(jsonPath("$.query.amount", equalTo(query.getAmount())))
                .andExpect(jsonPath("$.result", equalTo(1.0)));
    }

    @Test
    void testConvertCurrency_RateCalculated_ShouldReturnExchange() throws Exception{
        //given
        Query query = new Query("EUR", "USD", 1.0);
        Currency currUSD = exchangeRepository.findById("USD").get();
        Currency currEUR = exchangeRepository.findById("EUR").get();
        //when //then
        mockMvc.perform(get("/api/v1/exchange/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(query)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.rate", equalTo(currUSD.getAsk() / currEUR.getBid())))
                .andExpect(jsonPath("$.query.from", equalTo(query.getFrom())))
                .andExpect(jsonPath("$.query.to", equalTo(query.getTo())))
                .andExpect(jsonPath("$.query.amount", equalTo(query.getAmount())))
                .andExpect(jsonPath("$.result", equalTo(currUSD.getAsk() / currEUR.getBid())));
    }

    @Test
    void testConvertCurrency_Validation() throws Exception {
        //given
        Query query = new Query("", "", 0.0);

        //when //then
        mockMvc.perform(get("/api/v1/exchange/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(query)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", notNullValue()))
                .andExpect(jsonPath("$.message", equalTo("Validation errors")))
                .andExpect(jsonPath("$.violations[*].message",
                        containsInAnyOrder("From cannot be blank",
                                "To cannot be blank",
                                "Amount must be greater or equal 1",
                                "Currency is not available",
                                "Currency is not available")));
    }
}