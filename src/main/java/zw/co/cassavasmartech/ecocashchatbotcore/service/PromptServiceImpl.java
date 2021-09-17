package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.PromptNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Prompt;
import zw.co.cassavasmartech.ecocashchatbotcore.model.State;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.PromptRepository;

import java.util.List;

@Service
public class PromptServiceImpl implements PromptService{
    @Autowired
    PromptRepository promptRepository;
    @Override
    public Prompt findByIntentAndStage(String intent, int stage) {
        return promptRepository.findByIntentAndStage(intent,stage).orElseThrow(()->new PromptNotFoundException(intent));
    }

    @Override
    public List<Prompt> findAllByUsecase(Usecase usecase) {
        return promptRepository.findAllByUsecase(usecase);
    }

    @Override
    public Prompt findById(Long id) {
        return promptRepository.findById(id).orElseThrow(()->new PromptNotFoundException(id.toString()));
    }

    @Override
    public Prompt update(Long id, Prompt prompt) {
        return promptRepository.findById(id).map(p->{
            p.setText(prompt.getText());
            p.setLastModifiedDate(prompt.getLastModifiedDate());
            p.setLastModifiedBy(prompt.getLastModifiedBy());
            return promptRepository.save(p);
        }).orElseThrow(()->new PromptNotFoundException(id.toString()));
    }

    @Override
    public Prompt create(Prompt prompt) {
        return promptRepository.save(prompt);
    }
}
