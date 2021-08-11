package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import java.util.Date;

@Data
public class StatementRequest {
    private String msisdn;
    private Date startDate;
    private Date endDate;
    private String mime;
}
