package HashMap;

import java.io.Serializable;
import java.util.*;
import Utilities.Nodes.Node;

public class PerfectHashMap<K, V> implements Serializable {
    private CustomHashMap<K, V> lookupTable;  // Use CustomHashMap for fast lookups
    private List<Node<K, V>> storeTable;
    private List<Node<K, V>> rebuiltTable; //
    private int m; // Size of the hash table
    private int r; // Number of nodes in the table
    private int[] T; // Marked positions for injectivity (used to track collisions)
    private List<Integer> knownSubHashSeeds = new ArrayList<Integer>();

    public PerfectHashMap() {
        this.storeTable = new ArrayList<>();
        this.rebuiltTable = new ArrayList<>();
        this.lookupTable = new CustomHashMap<>();
    }

    public void put(K key, V value) throws Exception {
        if (lookupTable.get(key) == null) {
            storeTable.add(new Node<>(key, value));
            lookupTable.put(key, value);
        }
    }

    public void rebuild() throws Exception {
        if (storeTable.isEmpty()) {
            return; // Empty table means nothing to hash
        }

        r = storeTable.size();
        m = r * 2; // Initial size, can be adjusted if needed
        T = new int[m];
        Arrays.fill(T, 0); // Fill T with 0 to mark unoccupied slots
        this.rebuiltTable = new ArrayList<>(Collections.nCopies(m, null));

        Map<Integer, List<Node<K, V>>> buckets = new HashMap<>();
        for (Node<K, V> tNode : storeTable) {
            int primaryHash = computeHash(tNode.key, m);
            List<Node<K, V>> bucket = buckets.get(primaryHash);
            if (bucket == null) {
                bucket = new ArrayList<>();
                buckets.put(primaryHash, bucket);
            }
            bucket.add(tNode);
        }

        for (var entry : buckets.entrySet()) {
            List<Node<K, V>> bucket = entry.getValue();
            // No collision, just place it in the rebuiltTable
            Node<K, V> tNode = bucket.get(0);
            int hash = computeHash(tNode.key, m);
            if (T[hash] == 0) {
                this.rebuiltTable.set(hash, tNode);
                T[hash] = 1;
            }
            findInjectiveSubHash(bucket);
        }
    }

    private void findInjectiveSubHash(List<Node<K, V>> bucket) {
        int MAX_ATTEMPTS = 1000000;
        for (Node<K, V> newNode : bucket) {
            for (int i = 1; i < MAX_ATTEMPTS; ++i) {
                int subHash = computeSubHash(newNode.key, i);
                if (T[subHash] == 0) {
                    // No collision, we can safely add this node
                    knownSubHashSeeds.add(subHash);
                    this.rebuiltTable.set(subHash, newNode);
                    T[subHash] = 1;
                    break; // Exit the sub-hash loop
                }
            }
        }
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (Node<K, V> tNode : storeTable) {
            keys.add(tNode.key);
        }
        return keys;
    }

    private int computeHash(K key, int r) {
        return Math.abs(key.hashCode()) % r;
    }

    private int computeSubHash(K key, int seed) {
        return Math.abs((key.hashCode() * 31 + seed * 17)) % m;
    }

    public V get(K key) {
        return get(key, true);
    }

    public V get(K key, boolean useRebuilt) {
        // If useRebuilt is true, use the rebuiltTable; otherwise, use storeTable
        var useTable = useRebuilt ? this.rebuiltTable : this.storeTable;

        // If we are using the rebuilt rebuiltTable, check primary hash first
        if (useRebuilt) {
            int pos = computeHash(key, m);
            Node<K, V> tNode = useTable.get(pos);
            if (tNode != null && tNode.key.equals(key)) {
                return tNode.value;
            } else {
                // Try sub-hashing (check the seed stored in the node)
                for (int i = 0; i < knownSubHashSeeds.size(); ++i) {
                    Node<K, V> maybeNode = useTable.get(knownSubHashSeeds.get(i));
                    if (maybeNode != null && maybeNode.key.equals(key)) {
                        return maybeNode.value;
                    }
                }
            }
        } else {
            // If not using the rebuilt table,
            // just search in storeTable (no sub-hashing)
            for (Node<K, V> tNode : useTable) {
                if (tNode.key.equals(key)) {
                    return tNode.value;
                }
            }
        }

        return null; // Not found
    }

    public void delete(K key) {
        int pos = computeHash(key, m);
        Node<K, V> tNode = this.rebuiltTable.get(pos);
        if (tNode != null && tNode.key.equals(key)) {
            this.rebuiltTable.set(pos, null);
            // Remove from storeTable as well
            storeTable.removeIf(node -> node.key.equals(key));
        }
    }
}
