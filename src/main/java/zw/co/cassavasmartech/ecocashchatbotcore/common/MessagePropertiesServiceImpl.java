package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Service
@Description("Wrapper for Message source properties...in case we want to play around with Locales")
@Slf4j
@RequiredArgsConstructor
public class MessagePropertiesServiceImpl implements MessagePropertiesService {

    private final MessageSource messageSource;


    @Override
    public String getByKey(String key, Object[] args) {


        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Message Property Key is required");
        }

        log.trace("Processing get message by key {}", key);

        return messageSource.getMessage(key, args, Locale.getDefault());

    }

    @Override
    public String getByKey(String key) {

        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Message Property Key is required");
        }

        log.trace("Processing get message by key {}", key);

        return messageSource.getMessage(key, null, Locale.getDefault());
    }
}
