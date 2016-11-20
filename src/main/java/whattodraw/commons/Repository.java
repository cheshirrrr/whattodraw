package whattodraw.commons;

import java.util.Map;
import java.util.Set;

public interface Repository<T> {
     T getRandom();

    Map<String, Set<String>> getAllVariants();

     boolean removeVariant(String key, String value);
}
