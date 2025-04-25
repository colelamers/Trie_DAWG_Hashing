package HashMap;

import java.io.Serializable;
import java.util.*;
import Utilities.Nodes.Node;

public class PerfectHashMap<K, V> implements Serializable {
    private CustomHashMap<K, V> lookupTable;  // Use CustomHashMap for fast lookups
    private List<Node<K, V>> nodes;
    private List<Node<K, V>> table;
    private int m;
    private int r;
    private int[] T;

    // todo 1; so i think we need a bucket class, this contains the node and the
    // hash function used to hash the item. we need to perform multiple hashes
    // to perfeclty build this up. the issue is even if there is a small amount
    // of key items, the fact that you must rebuild causes issues.
    public PerfectHashMap() {
        this.nodes = new ArrayList<>();
        this.table = new ArrayList<>();
        this.lookupTable = new CustomHashMap<>();  // Initialize the CustomHashMap for lookups
    }

    public void put(K key, V value) throws Exception {
        // Check if key exists using CustomHashMap for fast O(1) lookup
        if (lookupTable.get(key) == null) {
            nodes.add(new Node<>(key, value));
            lookupTable.put(key, value);  // Insert into the lookupTable for fast access
        }
    }

    public void rebuild() throws Exception {
        if (nodes.isEmpty()) {
            return; // Empty trie means basically, you made an array of size 0.
        }

        r = nodes.size();
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
        for (Node<K, V> Node : nodes) {
            int index = computeHash(Node.key, r);
            buckets.get(index).add(Node.key);
        }

        // Sort buckets by size
        buckets.sort((a, b) -> b.size() - a.size());

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

        // Fill the hashtable
        table = new ArrayList<>(Collections.nCopies(m, null));
        for (Node<K, V> Node : nodes) {
            int pos = computeHash(Node.key, m);
            table.set(pos, Node);
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

        // Return false if a valid hash isn't found and memory size exceeds limit
        return false;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (Node<K, V> Node : nodes) {
            keys.add(Node.key);
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
        var useTable = table;

        // This modification here of the "get" function allows for
        // appending nodes in the nodes list BEFORE they are placed

        if (!whichTable){
            useTable = nodes;
            for (int i = 0; i < useTable.size(); ++i) {
                Node<K, V> Node = useTable.get(i);
                if (Node.key.toString().toUpperCase().equals(
                        key.toString().toUpperCase())) {
                    return Node.value; // Found match, return value
                }
            }
        }
        else{
            int pos = computeHash(key, m);
            Node<K, V> Node = useTable.get(pos);
            if (Node != null && Node.key.equals(key)) {
                return Node.value;
            }
        }

        return null;
    }

    public void delete(K key) {
        int pos = computeHash(key, m);
        Node<K, V> Node = table.get(pos);
        if (Node != null && Node.key.equals(key)) {
            // Mark the position as empty by setting it to null
            table.set(pos, null);
            // Optionally, remove the Node from the `nodes` list
            nodes.removeIf(e -> e.key.equals(key));
        }
    }
}
