package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeMetaData{
    private String channel;
    private String purchaseCategoryCode;
    private String onBeHalfOf;
}