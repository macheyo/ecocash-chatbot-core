package zw.co.cassavasmartech.ecocashchatbotcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import zw.co.cassavasmartech.ecocashchatbotcore.config.MyThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class EcocashChatbotCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcocashChatbotCoreApplication.class, args);
	}

	@Bean("threadPoolTaskExecutor")
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new MyThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(100);
		taskExecutor.setThreadNamePrefix("my-executor-");
		return taskExecutor;
	}

}
