package pl.zajonz.currencyexchange.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.zajonz.currencyexchange.constraint.CurrencyConstraint;

@Getter
@AllArgsConstructor
public class Query {
    @NotBlank(message = "From cannot be blank")
    @CurrencyConstraint
    private String from;

    @NotBlank(message = "To cannot be blank")
    @CurrencyConstraint
    private String to;
    @Min(value = 1, message = "Amount must be greater or equal 1")
    private Double amount;

    @Override
    public String toString() {
        return "Query{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                '}';
    }
}
