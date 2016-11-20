package whattodraw.suggestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import javax.annotation.PostConstruct;
import java.util.Set;

@org.springframework.stereotype.Repository
public class SuggestionRepository {

    @Autowired
    @Qualifier("StringTemplate")
    private RedisTemplate<String, String> redisTemplate;

    SetOperations<String, String> setOps;

    @PostConstruct
    private void init() {
        setOps = redisTemplate.opsForSet();
    }

    public boolean addSuggestion(String key, String value) {
        if (!isValid(value)) {
            return false;
        }

        return setOps.add(key, value) > 0;
    }

    public boolean removeSuggestion(String key, String value) {
        if (!isValid(value)) {
            return false;
        }

        return setOps.remove(key ,value) > 0;
    }

    private boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    public Set<String> getSuggestions(String key) {
        return setOps.members(key);
    }
}
