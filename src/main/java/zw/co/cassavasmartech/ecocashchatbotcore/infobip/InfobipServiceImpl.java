package zw.co.cassavasmartech.ecocashchatbotcore.infobip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.infobip.data.InfoBipRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.infobip.data.OutboundRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Statement;

@Component
@RequiredArgsConstructor
@Slf4j
public class InfobipServiceImpl implements InfobipService{
    @Autowired
    InfoBipAdapterConfigurationProperties infoBipAdapterConfigurationProperties;
    private final CoreInvoker coreInvoker;

    @Override
    public ApiResponse<InfoBipRequest> sendMessage(OutboundRequest outboundRequest) {
        return coreInvoker.invoke(outboundRequest,
                infoBipAdapterConfigurationProperties.getUrl()+"/infobip/outbound",
                HttpMethod.POST,
                new ParameterizedTypeReference<ApiResponse<InfoBipRequest>>() {});
    }
}
