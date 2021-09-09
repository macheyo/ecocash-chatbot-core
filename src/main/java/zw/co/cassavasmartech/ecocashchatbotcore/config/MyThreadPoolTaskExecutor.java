package zw.co.cassavasmartech.ecocashchatbotcore.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MyThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new MyCallable<>(task, RequestContextHolder.currentRequestAttributes()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(new MyCallable<>(task, RequestContextHolder.currentRequestAttributes()));
    }

    private static class MyCallable<T> implements Callable<T> {
        private final Callable<T> callable;
        private final RequestAttributes requestAttributes;

        private MyCallable(Callable<T> callable, RequestAttributes requestAttributes) {
            this.callable = callable;
            this.requestAttributes = copy(requestAttributes);
        }

        private RequestAttributes copy(RequestAttributes requestAttributes) {
            // RequestAttributes needs to be copied as it will be garbage collected when origin request will complete.
            return requestAttributes;
        }

        @Override
        public T call() throws Exception {
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                return callable.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
