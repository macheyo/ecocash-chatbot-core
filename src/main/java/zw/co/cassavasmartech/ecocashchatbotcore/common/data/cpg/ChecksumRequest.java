package zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ChecksumRequest {
    private String vendorCode;
    private String vendorKey;
    private String keyStoreLocation;
    private String keyStoreAlias;
    private String keystoreType;
    private String keystorePass;
    private String transactionType;
    private String transactionReference;
    private String applicationCode;

}
