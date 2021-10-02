package zw.co.cassavasmartech.ecocashchatbotcore.service;

import zw.co.cassavasmartech.ecocashchatbotcore.model.Prompt;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

import java.util.List;

public interface PromptService {
    Prompt findByIntentAndStage(String intent, int stage);
    List<Prompt> findAllByUsecase(UseCase usecase);
    Prompt findById(Long id);
    Prompt update(Long id, Prompt prompt);

    Prompt upgrade(Long id, Prompt prompt);

    Prompt create(Prompt prompt);
}
