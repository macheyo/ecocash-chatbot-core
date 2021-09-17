package zw.co.cassavasmartech.ecocashchatbotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Prompt;
import zw.co.cassavasmartech.ecocashchatbotcore.model.State;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

import java.util.List;
import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    Optional<Prompt> findByIntentAndStage(String intent, int stage);

    List<Prompt> findAllByUsecase(Usecase usecase);
}
