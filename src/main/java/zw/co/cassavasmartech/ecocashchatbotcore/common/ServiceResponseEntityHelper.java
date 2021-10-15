package zw.co.cassavasmartech.ecocashchatbotcore.common;

//import com.econetwireless.device.credit.integrations.endpoints.epay.banks.CreditResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.process.ProcessConstants;

import java.util.Map;

@Service
public class ServiceResponseEntityHelper {

    private String getString (Map<String, String> map){
        StringBuilder builder=new StringBuilder("");
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key).append(":").append(value).append(",");
        }
        String receiptDataString=builder.toString();
        return receiptDataString.substring(0,receiptDataString.length()-1);
    }


    public ServiceAllocationRecord updateServiceResponseEntity(TransactionResponse zesaCreditResponse, ServiceAllocationRecord serviceResponse){
        serviceResponse.setResponseCode(zesaCreditResponse.getField1());
        serviceResponse.setResponseData(zesaCreditResponse.getField2());//full response
        serviceResponse.setResponseMessage(zesaCreditResponse.getField9());//token only
        serviceResponse.setResponseReference(zesaCreditResponse.getField3());
        serviceResponse.setTransactionStatus(resolveZesaStatus(zesaCreditResponse.getField1()));
        return serviceResponse;
    }


    private TransactionStatus resolveZesaStatus(String responseCode) {
        TransactionStatus status = TransactionStatus.fromStatusCode(Integer.valueOf(responseCode));
        if (ObjectUtils.isEmpty(status)) {
            status = TransactionStatus.FAILED;
        } else if (ProcessConstants.TIMEDOUT_CPG_RESPONSE.equalsIgnoreCase(responseCode)){
            status = TransactionStatus.PENDING_TOKEN_CHECK;
        }
        return status;
    }

    private TransactionStatus resolveTopupStatus(String responseCode) {
        if (ProcessConstants.SUCCESS_EPAY_RESPONSE.equalsIgnoreCase(responseCode)) {
            return TransactionStatus.SUCCESS;
        } else {
            return TransactionStatus.FAILED;
        }
    }

}
