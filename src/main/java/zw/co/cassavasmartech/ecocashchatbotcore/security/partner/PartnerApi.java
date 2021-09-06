package zw.co.cassavasmartech.ecocashchatbotcore.security.partner;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.RecordNotFound;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerApi {

    private final PartnerService partnerService;

    @PostMapping
    public ApiResponse<Partner> save(@RequestBody Partner partner){
        partner = partnerService.create(partner).orElseThrow(()-> new BusinessException("Error Occurred. Creating Partner"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, partner);
    }

    @PostMapping("/update")
    public ApiResponse<Partner> update(@RequestBody Partner partner){
        partner = partnerService.update(partner).orElseThrow(()-> new BusinessException("Error Occurred. Updating Partner"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, partner);
    }

    @GetMapping("/list/page/{page}/size/{size}")
    public ApiResponse<List<Partner>> findAll(@PathVariable("page")int page, @PathVariable("size") int size){
        size = size == 0 ? 50: size;

        log.info("List Partners page:{} and size:{}",page,size);
        List<Partner> partnerList = partnerService.findAll(page,size);
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, partnerList);
    }

    @GetMapping("/findby-partner-id/{partnerId}")
    public ApiResponse<Partner> findByName(@PathVariable("partnerId") String partnerId){
        Partner partner = partnerService.findByPartnerId(partnerId).orElseThrow(()-> new RecordNotFound("Record Not Found"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, partner);
    }


}
