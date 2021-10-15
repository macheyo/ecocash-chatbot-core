package zw.co.cassavasmartech.ecocashchatbotcore.common.enums;

import java.util.Arrays;

public enum ErrorResponse {

    EMPTY_REQUEST(400,"No request data found in request"),
    NO_PAYMENT_METHOD(401,"Payment method is a required field"),
    NO_TRANSACTION_TYPE(402,"Transaction type is a required field"),
    NO_SOURCE_REF(403,"Source reference is a required field"),
    DUPLICATE_SOURCE_REF(405,"Source reference passed has already been processed"),
    INTERNAL_ERROR(500,"Internal server error"),
    INVALID_METHOD_AND_TYPE(406,"Payment method and transaction type posted invalid"),
    INVALID_PAYMENT_METHOD(407,"Payment method invalid"),
    INVALID_TRANSACTION_TYPE(408,"Transaction type invalid"),
    INVALID_CURRENCY_CODE(409,"Currency code invalid"),
    INVALID_PARTNER(410,"Partner id invalid"),
    INVALID_PROCESS_CODE(411,"Process code invalid"),
    INVALID_CHANNEL(412,"Channel code invalid"),
    NO_PAYMENT_API_RESPONSE(413,"No response received from payment provider"),
    NO_BPN_DEFINED(414,"BPN number is required for zimra validation request"),
    INVALID_CUSTOMER_EMAIL(415,"Customer email address is invalid"),
    NO_ZIMRA_VALIDATION_TYPE(416,"Zimra validation type is a required field"),
    INVALID_ZIMRA_VALIDATION_TYPE(417,"Zimra validation type passed invalid"),
    NO_YEAR_DEFINED(418,"Year for Zimra assessment validation is required"),
    NO_OFFICECODE_DEFINED(419,"Office code for Zimra assessment validation is required"),
    NO_ASSESSMENT_NUM_DEFINED(420,"Assessment number for Zimra assessment validation is required"),
    NO_TAXCODE_DEFINED(421,"Tax code for Zimra assessment validation is required");

    final int responseCode;
    final String message;

    ErrorResponse(int responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getMessage(){
        return message;
    }

    public static ErrorResponse fromResponseCode(int responseCode) {
        return Arrays.stream(ErrorResponse.values()).filter(t -> t.responseCode == responseCode).findFirst().orElse(null);
    }
}
