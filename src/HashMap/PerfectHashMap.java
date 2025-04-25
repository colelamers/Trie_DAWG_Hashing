package HashMap;

import java.io.Serializable;
import java.util.*;
import Utilities.Nodes.Node;

public class PerfectHashMap<K, V> implements Serializable {
    private CustomHashMap<K, V> lookupTable;  // Use CustomHashMap for fast lookups
    private List<Node<K, V>> nodeTable;
    private List<Node<K, V>> hashTable;
    private int m;
    private int r;
    private int[] T;

    public PerfectHashMap() {
        this.nodeTable = new ArrayList<>();
        this.hashTable = new ArrayList<>();
        this.lookupTable = new CustomHashMap<>();
    }

    public void put(K key, V value) throws Exception {
        if (lookupTable.get(key) == null) {
            nodeTable.add(new Node<>(key, value));
            lookupTable.put(key, value);
        }
    }
    public void rebuild() throws Exception {
        if (nodeTable.isEmpty()) {
            return; // Empty trie means basically, you made an array of size 0.
        }

        r = nodeTable.size();
        m = r * 2;
        T = new int[m];
        Arrays.fill(T, 0);
        this.hashTable = new ArrayList<>(Collections.nCopies(m, null));

        for (Node<K, V> tNode : nodeTable) {
            int primaryHash = computeHash(tNode.key, m);
            if (this.hashTable.get(primaryHash) == null) {
                this.hashTable.set(primaryHash, tNode);
                T[primaryHash] = 1;
            } else {
                // Collision detected, need to find an injective sub-hash
                findInjectiveSubHash(tNode);
            }
        }

        // Fill the hashTable
        this.hashTable = new ArrayList<>(Collections.nCopies(m, null));
        for (Node<K, V> tNode : nodeTable) {
            int pos = computeHash(tNode.key, m);
            this.hashTable.set(pos, tNode);
        }
    }

    private void findInjectiveSubHash(Node<K, V> newNode) {
        int MAX_ATTEMPTS = 1000000;
        for (int l = 1; l < MAX_ATTEMPTS; ++l) {
            int subHash = computeSubHash(newNode.key, l);
            if (T[subHash] == 0) {
                // Check for injectivity against existing sub-hashed elements
                boolean isSubHashInjective = true;
                for (Node<K, V> existingNode : nodeTable) {
                    if (existingNode != newNode) {
                        int existingPrimaryHash = computeHash(existingNode.key, m);
                        if (computeHash(newNode.key, m) == existingPrimaryHash) { // Only check collisions of the same primary hash
                            // If the existing node is also sub-hashed and occupies the current subHash, it's not injective
                            if (hashTable.get(subHash) == existingNode) {
                                isSubHashInjective = false;
                                break;
                            }
                        }
                    }
                }

                if (isSubHashInjective) {
                    this.hashTable.set(subHash, newNode);
                    T[subHash] = 1;
                    return;
                }
            }
        }
    }
/*    public void rebuild() throws Exception {
        if (nodeTable.isEmpty()) {
            return; // Empty trie means basically, you made an array of size 0.
        }

        r = nodeTable.size();
        m = r * 2;
        T = new int[m];
        Arrays.fill(T, 0);

        // Split into buckets
        List<List<K>> buckets = new ArrayList<>(Collections.nCopies(r, null));
        for (int i = 0; i < r; i++) {
            buckets.set(i, new ArrayList<>());
        }

        // todo 1; tbh, I don't thik I need to sort this...
        // It's not time expensive but it IS redundant
        for (Node<K, V> tNode : nodeTable) {
            int index = computeHash(tNode.key, r);
            buckets.get(index).add(tNode.key);
        }

        // Sort buckets by size to aid in finding
        for (int i = 0; i < buckets.size() - 1; i++) {
            for (int j = 0; j < buckets.size() - i - 1; j++) {
                if (buckets.get(j).size() < buckets.get(j + 1).size()) {
                    // Swap
                    List<K> temp = buckets.get(j);
                    buckets.set(j, buckets.get(j + 1));
                    buckets.set(j + 1, temp);
                }
            }
        }

        // Find injective mapping for all buckets
        for (List<K> bucket : buckets) {
            while (!findHashForBucket(bucket)){
                // A limitation of how perfect hashes work is that
                // if there is a significantly large n, it will cause
                // the bucket size to overflow. Meaning that you cannot
                // make the hashmap any larger due to memory limitations.
                // max-1 is basically the final check. If it can't fit
                // in that sized bucket, it's way too large.
                m = (m * 2) > 0 ? m * 2 : Integer.MAX_VALUE - 1;
                T = new int[m];
                Arrays.fill(T, 0);
            }
        }

        // Fill the hashTable
        this.hashTable = new ArrayList<>(Collections.nCopies(m, null));
        for (Node<K, V> tNode : nodeTable) {
            int pos = computeHash(tNode.key, m);
            this.hashTable.set(pos, tNode);
        }
    }

    private boolean findHashForBucket(List<K> bucket) {
        int MAX_ATTEMPTS = 1000000;
        for (int i = 1; i < MAX_ATTEMPTS; ++i) {
            Set<Integer> positions = new HashSet<>();
            boolean isInjective = true;

            for (K key : bucket) {
                int position = computeSubHash(key, i);
                if (T[position] == 1 || !positions.add(position)) {
                    isInjective = false;
                    break;
                }
            }

            if (isInjective) {
                for (K key : bucket) {
                    int position = computeSubHash(key, i);
                    T[position] = 1;
                }
                return true;
            }
        }

        // Return false if a valid hash
        // isn't found and memory size exceeds limit
        return false;
    }*/

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (Node<K, V> tNode : nodeTable) {
            keys.add(tNode.key);
        }
        return keys;
    }

    private int computeHash(K key, int r) {
        return Math.abs(key.hashCode()) % r;
    }

    private int computeSubHash(K key, int l) {
        return Math.abs((key.hashCode() * 31 + l * 17)) % m;
    }

    public V get(K key){
        return get(key, true);
    }

    public V get(K key, boolean whichTable) {
        // True means we want to pull from the rebuilt data structure
        // False means we want to pull from the unbuilt data structure.
        var useTable = this.hashTable;

        // This modification here of the "get" function allows for
        // appending nodeTable in the nodeTable list BEFORE they are hashed
        if (!whichTable){
            useTable = nodeTable;
            for (int i = 0; i < useTable.size(); ++i) {
                Node<K, V> tNode = useTable.get(i);
                if (tNode.key.toString().equalsIgnoreCase(key.toString())) {
                    return tNode.value;
                }
            }
        }
        else{
            int pos = computeHash(key, m);
            Node<K, V> tNode = useTable.get(pos);
            if (tNode != null && tNode.key.equals(key)) {
                return tNode.value;
            }
        }

        return null;
    }

    public void delete(K key) {
        int pos = computeHash(key, m);
        Node<K, V> tNode = this.hashTable.get(pos);
        if (tNode != null && tNode.key.equals(key)) {
            this.hashTable.set(pos, null);
            for (int i = 0; i < nodeTable.size(); i++) {
                Node<K, V> current = nodeTable.get(i);
                if (current.key.equals(key)) {
                    nodeTable.remove(i);
                    break;
                }
            }
        }
    }
}
