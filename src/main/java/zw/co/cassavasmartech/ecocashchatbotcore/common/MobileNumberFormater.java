package zw.co.cassavasmartech.ecocashchatbotcore.common;

public interface MobileNumberFormater {
    String formatMsisdnMinimum(String msisdn);

    boolean isValidMsisdn(String msisdn);

    String formatMobileNumberInternational(String msisdn);
}
