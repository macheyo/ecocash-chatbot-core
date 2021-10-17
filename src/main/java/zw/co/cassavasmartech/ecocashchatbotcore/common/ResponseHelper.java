//package zw.co.cassavasmartech.ecocashchatbotcore.common;
//
//import zw.co.cassavasmartech.esb.commons.data.Response;
//import zw.co.cassavasmartech.esb.commons.enums.ErrorResponse;
//import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
//
//public interface ResponseHelper {
//
//    default Response getErrorResponse(ErrorResponse errorResponse, String reference) {
//        Response response = new Response();
//        response.setField1(String.valueOf(errorResponse.getResponseCode()));
//        response.setField2(errorResponse.getMessage());
//        response.setField3(reference);
//        response.setField5(TransactionStatus.FAILED.name());
//        return response;
//    }
//
//    default Response getResponse(TransactionStatus txnStatus, String reference) {
//        Response response = new Response();
//        response.setField1(String.valueOf(txnStatus.getStatusCode()));
//        response.setField2(txnStatus.name());
//        response.setField3(reference);
//        response.setField5(txnStatus.name());
//        return response;
//    }
//
//}
