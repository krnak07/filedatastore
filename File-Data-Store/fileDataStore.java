import com.sun.source.tree.SynchronizedTree;
import org.json.simple.JSONObject;

public interface fileDataStore {
    public String create(String key, JSONObject value, int TTL);
    public JSONObject read(String key);
    public String delete(String key);
}

