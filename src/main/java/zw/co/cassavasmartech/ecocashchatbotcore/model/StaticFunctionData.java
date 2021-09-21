package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.Function;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StaticFunctionData {
    private List<Function> functions;
}
