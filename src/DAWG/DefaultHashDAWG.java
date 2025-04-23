package DAWG;

import java.io.Serializable;
import java.util.*;

public class DefaultHashDAWG implements Serializable {
    private final HashMap<String, String> root;

    public DefaultHashDAWG() {
        root = new HashMap<String, String>(); // Empty Root
    }

    public void insert(List<String> words) {
        String prev = words.get(0);
        for (int i = 1; i < words.size(); ++i) {
            var word = words.get(i);
            if (word.isEmpty()) {
                continue;
            }

            String dn = root.get(prev);

            if (dn == null){
                root.put(prev, word);
            }
            else{

            }
        }
        //current = current.getChild(word);
    }

}
