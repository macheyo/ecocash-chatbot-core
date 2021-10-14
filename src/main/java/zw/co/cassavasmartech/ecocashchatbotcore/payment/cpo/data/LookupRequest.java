package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LookupRequest {
    @JsonProperty("vendor_id")
    private String vendorId;
    @JsonProperty("search_ids")
    private List<Integer> searchIds;
    @JsonProperty("search_orders")
    private List<String> searchOrders;
}
