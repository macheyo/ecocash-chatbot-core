package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler.*;

public enum Function {
    GETTIMEOFDAY (new GetTimeOfDay()),
    GETCUSTOMERALIAS (new GetCustomerAlias()),
    GETCUSTOMERFIRSTNAME(new GetCustomerFirstName()),
    GETCUSTOMERLASTNAME(new GetCustomerLastName()),
    GETAMOUNT(new GetAmount()),
    GETBILLER(new GetBillProvider()),
    GETBILLACCOUNT(new GetBillAccount()),
    GETMERCHANT(new GetMerchant()),
    GETBENEFICIARY(new GetBeneficiary()),
    GETTRANSACTIONREVERSALDETAILS(new GetTransactionReversalDetails()),
    GETTRANSACTIONPENDINGREVERSALDETAILS(new GetTransactionPendingReversalDetails()),
    GETSTATEMENT(new GetStatement()),
    GETSTATEMENTDETAILS(new GetStatementDetails()),
    GETPENDINGREVERSALS(new GetPendingReversals()),
    CARRIAGERETURN(new CarriageReturn()),
    SENDEMAILTONUNURAI(new SendEmailToNunurai()),

    ;

    private final FunctionAdapter fa;
    Function(FunctionAdapter fa) {
        this.fa = fa;
    }

    /** Return a Function's adapter by name; null if unknown. */
    public static FunctionAdapter lookup(String name) {
        Function f;
        try {
            f = Enum.valueOf(Function.class, name.replaceAll("[^a-zA-Z ]", "").toUpperCase());
        } catch (RuntimeException e) {
            return null;
        }
        return f.fa;
    }
}
