package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputContext {
    private String name;
    private int lifespanCount;
    private Object parameters;
}
