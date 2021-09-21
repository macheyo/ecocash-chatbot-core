package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.Function;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Prompt;
import zw.co.cassavasmartech.ecocashchatbotcore.model.StaticFunctionData;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;
import zw.co.cassavasmartech.ecocashchatbotcore.service.PromptService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/prompt")
@Slf4j
public class PromptController {
    @Autowired
    PromptService promptService;
    @GetMapping("/list/{usecase}")
    public ApiResponse<List<Prompt>> findAllByUsecase(@PathVariable Usecase usecase){
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                promptService.findAllByUsecase(usecase));
    }
    @GetMapping("/{id}")
    public ApiResponse<Prompt> findById(@PathVariable Long id){
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                promptService.findById(id)
        );
    }
    @PutMapping("/update/{id}")
    public ApiResponse<Prompt> update(@PathVariable Long id, @Valid @RequestBody Prompt prompt, HttpServletRequest httpServletRequest){
        Principal principal = httpServletRequest.getUserPrincipal();
        prompt.setLastModifiedBy(principal.getName());
        prompt.setLastModifiedDate(LocalDateTime.now());
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                promptService.update(id, prompt)
        );
    }

    @PutMapping("/upgrade/{id}")
    public ApiResponse<Prompt> upgrade(@PathVariable Long id, @Valid @RequestBody Prompt prompt, HttpServletRequest httpServletRequest){
        Principal principal = httpServletRequest.getUserPrincipal();
        prompt.setLastModifiedBy(principal.getName());
        prompt.setLastModifiedDate(LocalDateTime.now());
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                promptService.upgrade(id, prompt)
        );
    }

    @PostMapping("/create")
    public ApiResponse<Prompt> create(@Valid @RequestBody Prompt prompt, HttpServletRequest httpServletRequest){
        Principal principal = httpServletRequest.getUserPrincipal();
        prompt.setCreatedBy(principal.getName());
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                promptService.create(prompt)
        );
    }

    @GetMapping("/list/functions")
    public ApiResponse<StaticFunctionData> functions(){
        StaticFunctionData staticFunctionData = new StaticFunctionData();
        staticFunctionData.setFunctions(Arrays.asList(Function.values()));
        return new ApiResponse<>(
                HttpStatus.OK.value(),
                ApiConstants.SUCCESS_MESSAGE,
                staticFunctionData
        );
    }
}
