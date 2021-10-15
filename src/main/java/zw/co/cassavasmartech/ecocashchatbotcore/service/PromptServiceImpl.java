package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.PromptNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Prompt;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.PromptRepository;

import java.util.List;

@Service
public class PromptServiceImpl implements PromptService{
    @Autowired
    PromptRepository promptRepository;
    @Override
    public Prompt findByIntentAndStage(String intent, int stage) {
        return promptRepository.findByIntentAndPosition(intent,stage).orElseThrow(()->new PromptNotFoundException());
    }

    @Override
    public List<Prompt> findAllByUsecase(UseCase usecase) {
        return promptRepository.findAllByUsecase(usecase);
    }

    @Override
    public Prompt findById(Long id) {
        return promptRepository.findById(id).orElseThrow(()->new PromptNotFoundException());
    }

    @Override
    public Prompt update(Long id, Prompt prompt) {
        return promptRepository.findById(id).map(p->{
            p.setText(prompt.getText());
            p.setLastModifiedDate(prompt.getLastModifiedDate());
            p.setLastModifiedBy(prompt.getLastModifiedBy());
            return promptRepository.save(p);
        }).orElseThrow(()->new PromptNotFoundException());
    }

    @Override
    public Prompt upgrade(Long id, Prompt prompt) {
        return promptRepository.findById(id).map(p->{
            p.setText(prompt.getText());
            p.setLastModifiedDate(prompt.getLastModifiedDate());
            p.setLastModifiedBy(prompt.getLastModifiedBy());
            p.setFunctions(prompt.getFunctions());
            p.setDescription(prompt.getDescription());
            p.setIntent(prompt.getIntent());
            p.setPosition(prompt.getPosition());
            p.setUsecase(prompt.getUsecase());
            return promptRepository.save(p);
        }).orElseThrow(()->new PromptNotFoundException());
    }

    @Override
    public Prompt create(Prompt prompt) {
        return promptRepository.save(prompt);
    }
}
