package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.Data;

@Data
public class OutputContext {
    private String name;
    private int lifespanCount;
    private Object parameters;
}
