package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Charginginformation{
    private BigDecimal amount;
    private String currency;
    private String description;
}