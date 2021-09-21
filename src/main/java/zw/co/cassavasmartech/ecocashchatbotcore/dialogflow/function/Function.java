package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler.GetCustomerAlias;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler.GetCustomerFirstName;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler.GetCustomerLastName;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler.GetTimeOfDay;

public enum Function {
    GETTIMEOFDAY (new GetTimeOfDay()),
    GETCUSTOMERALIAS (new GetCustomerAlias()),
    GETCUSTOMERFIRSTNAME(new GetCustomerFirstName()),
    GETCUSTOMERLASTNAME(new GetCustomerLastName()),
    ;

    private final FunctionAdapter fa;
    Function(FunctionAdapter fa) {
        this.fa = fa;
    }

    /** Return a Function's adapter by name; null if unknown. */
    public static FunctionAdapter lookup(String name) {
        Function f;
        try {
            f = Enum.valueOf(Function.class, name.toUpperCase());
        } catch (RuntimeException e) {
            return null;
        }
        return f.fa;
    }
}