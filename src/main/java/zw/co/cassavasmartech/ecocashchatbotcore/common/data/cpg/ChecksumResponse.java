package zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ChecksumResponse {
    private String checksum;

    public ChecksumResponse(String checksum){
        this.checksum=checksum;
    }
}
