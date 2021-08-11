package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import java.util.Date;

@Data
public class StatementRequest {
    private String msisdn;
    private String startDate;
    private String endDate;
    private String mime;
}
