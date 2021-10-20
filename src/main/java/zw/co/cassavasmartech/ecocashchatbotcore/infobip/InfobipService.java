package zw.co.cassavasmartech.ecocashchatbotcore.infobip;

import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.infobip.data.InfoBipRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.infobip.data.OutboundRequest;

public interface InfobipService {
    ApiResponse<InfoBipRequest> sendMessage(OutboundRequest outboundRequest);
}
