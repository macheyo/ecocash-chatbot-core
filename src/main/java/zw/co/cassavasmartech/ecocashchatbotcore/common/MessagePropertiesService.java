package zw.co.cassavasmartech.ecocashchatbotcore.common;

public interface MessagePropertiesService {

    String getByKey(String key, Object[] args);

    String getByKey(String key);

}
