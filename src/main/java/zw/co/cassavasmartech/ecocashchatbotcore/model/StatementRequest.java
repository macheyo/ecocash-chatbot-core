package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StatementRequest {
    private String msisdn;
    private String startDate;
    private String endDate;
    private String mime;
}
