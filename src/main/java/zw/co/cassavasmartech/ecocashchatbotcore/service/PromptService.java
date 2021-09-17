package zw.co.cassavasmartech.ecocashchatbotcore.service;

import zw.co.cassavasmartech.ecocashchatbotcore.model.Prompt;
import zw.co.cassavasmartech.ecocashchatbotcore.model.State;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

import java.util.List;

public interface PromptService {
    Prompt findByIntentAndStage(String intent, int stage);
    List<Prompt> findAllByUsecase(Usecase usecase);
    Prompt findById(Long id);
    Prompt update(Long id, Prompt prompt);
    Prompt create(Prompt prompt);
}
