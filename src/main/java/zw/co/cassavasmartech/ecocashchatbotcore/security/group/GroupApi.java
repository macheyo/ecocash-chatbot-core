package zw.co.cassavasmartech.ecocashchatbotcore.security.group;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.RecordNotFound;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UserGroup;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/group")
@Slf4j
public class GroupApi {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ApiResponse<UserGroup> save(@RequestBody UserGroup userGroup) {
        userGroup = groupService.save(userGroup).orElseThrow(() -> new BusinessException("Error Occurred. Saving UserGroup"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userGroup);
    }

    @PostMapping("/update")
    public ApiResponse<UserGroup> update(@RequestBody UserGroup userGroup) {
        userGroup = groupService.update(userGroup).orElseThrow(() -> new BusinessException("Error Occurred. Updating UserGroup"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userGroup);
    }

    @GetMapping("/list")
    public ApiResponse<List<UserGroup>> findAll() {
        List<UserGroup> userGroupList = groupService.findAll().orElse(new ArrayList());
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userGroupList);
    }

    @GetMapping("/findby-name/{name}")
    public ApiResponse<UserGroup> findByName(@PathVariable("name") String name) {
        UserGroup userGroup = groupService.findByName(name).orElseThrow(() -> new RecordNotFound("Record Not Found"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userGroup);
    }

    @GetMapping("/findby-id/{id}")
    public ApiResponse<UserGroup> findById(@PathVariable("id") Long id) {
        UserGroup userGroup = groupService.findById(id).orElseThrow(() -> new RecordNotFound("Record Not Found"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, userGroup);
    }

}
