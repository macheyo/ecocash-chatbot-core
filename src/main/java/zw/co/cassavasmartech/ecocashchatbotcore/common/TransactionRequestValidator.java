//package zw.co.cassavasmartech.ecocashchatbotcore.common;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
//import zw.co.cassavasmartech.esb.commons.data.TransactionRequest;
//import zw.co.cassavasmartech.esb.commons.enums.ErrorResponse;
//import zw.co.cassavasmartech.esb.exception.EsbValidationException;
//import zw.co.cassavasmartech.esb.model.Transaction;
//import zw.co.cassavasmartech.esb.transaction.TransactionService;
//
//import java.util.Optional;
//import java.util.regex.Pattern;
//
//@Slf4j
//@Service
//public class TransactionRequestValidator {
//
//    @Autowired
//    private TransactionService transactionService;
//
//    public void validateTransactionRequest(TransactionRequest request) throws EsbValidationException {
//        isDefined(request, ErrorResponse.EMPTY_REQUEST);
//        isDefined(request.getField2(), ErrorResponse.NO_PAYMENT_METHOD);
//        isDefined(request.getField10(), ErrorResponse.NO_SOURCE_REF);
//        checkSourceReference(request.getField10());
//        checkValidEmailAddress(request.getField7(), ErrorResponse.INVALID_CUSTOMER_EMAIL);
//    }
//
//
//
//
//    private void checkValidEmailAddress(String email, ErrorResponse errorResponse) throws EsbValidationException {
//        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(email.trim()) && !isEmailValid(email)) {
//            throw new EsbValidationException(errorResponse);
//        }
//    }
//
//    public void validatelookupRequest(TransactionRequest request) throws EsbValidationException {
//        isDefined(request, ErrorResponse.EMPTY_REQUEST);
//        isDefined(request.getField10(), ErrorResponse.NO_SOURCE_REF);
//    }
//
//    public void validateProductsLookUpRequest(TransactionRequest request) throws EsbValidationException {
//        isDefined(request, ErrorResponse.EMPTY_REQUEST);
//    }
//
//
//    private static void isDefined(Object object, ErrorResponse errorResponse) throws EsbValidationException {
//        if (ObjectUtils.isEmpty(object)) {
//            throw new EsbValidationException(errorResponse);
//        }
//    }
//
//    private static boolean isEmailValid(String email) {
//        String pattern = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//        return Pattern.matches(pattern, email);
//    }
//
//    public void checkSourceReference(String sourceReference) throws EsbValidationException {
//        final Optional<Transaction> transactionOptional = transactionService.findBySourceReference(sourceReference);
//        if (transactionOptional.isPresent())
//            throw new EsbValidationException(ErrorResponse.DUPLICATE_SOURCE_REF);
//    }
//}
