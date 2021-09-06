package zw.co.cassavasmartech.ecocashchatbotcore.security.role;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Role;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleApi {

    private final RoleService roleService;

    @PostMapping
    public ApiResponse<Role> save(@RequestBody Role role) {
        Optional<Role> optionalRole = roleService.save(role);
        role = optionalRole.orElseThrow(() -> new BusinessException("Error Creating Role"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, role);
    }

    @PostMapping("/update")
    public ApiResponse<Role> update(@RequestBody Role role) {
        Optional<Role> optionalRole = roleService.update(role);
        role = optionalRole.orElseThrow(() -> new BusinessException("Error Updating Role"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, role);
    }

    @PostMapping("/findby-name/{name}")
    public ApiResponse<Role> findByName(@PathVariable("name") String name) {
        Role role = roleService.findByName(name).orElseThrow(() -> new BusinessException("Record Not Found"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, role);
    }

    @GetMapping("/findby-id/{id}")
    public ApiResponse<Role> findById(@PathVariable("id") Long id) {
        Role role = roleService.findById(id).orElseThrow(() -> new BusinessException("Record Not Found"));
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, role);
    }

    @GetMapping("/list")
    public ApiResponse<List<Role>> findAll() {
        Optional<List<Role>> optionalRole = roleService.findAll();
        List<Role> roles = optionalRole.orElse(null);
        return new ApiResponse<>(HttpStatus.OK.value(), ApiConstants.SUCCESS_MESSAGE, roles);
    }

}
