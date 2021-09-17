package zw.co.cassavasmartech.ecocashchatbotcore.model;

import zw.co.cassavasmartech.ecocashchatbotcore.functions.GetCustomerAlias;
import zw.co.cassavasmartech.ecocashchatbotcore.functions.GetCustomerFirstName;
import zw.co.cassavasmartech.ecocashchatbotcore.functions.GetCustomerLastName;
import zw.co.cassavasmartech.ecocashchatbotcore.functions.GetTimeOfDay;

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
