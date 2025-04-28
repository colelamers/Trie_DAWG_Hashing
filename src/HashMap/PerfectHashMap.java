package HashMap;

import java.io.Serializable;
import java.util.*;
import Utilities.Nodes.Node;

public class PerfectHashMap<K, V> implements Serializable {
    private CustomHashMap<K, V> storeLookup;  // Use CustomHashMap for fast lookups
    private CustomHashMap<K, Integer> keyHashLookup;  // Use CustomHashMap for fast lookups
    private List<Node<K, V>> storeTable;
    private List<Node<K, V>> rebuiltTable; //
    private int m; // Size of the hash table
    private int r; // Number of nodes in the table
    private int[] T; // Marked positions for injectivity (used to track collisions)
    private List<Integer> knownSubHashSeeds = new ArrayList<Integer>();

    public PerfectHashMap() {
        this.storeTable = new ArrayList<>();
        this.rebuiltTable = new ArrayList<>();
        this.storeLookup = new CustomHashMap<>();
        this.keyHashLookup = new CustomHashMap<>();
    }

    public void put(K key, V value) throws Exception {
        if (storeLookup.get(key) == null) {
            storeTable.add(new Node<>(key, value));
            storeLookup.put(key, value);
        }
    }

    public void rebuild() throws Exception {
        if (storeTable.isEmpty()) {
            return;
        }

        r = storeTable.size();
        m = r * 2; // Initial size, can be adjusted if needed later
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
            if (tNode.key.equals("then")){
                hash = computeHash(tNode.key, m);

            }
            if (T[hash] == 0) {
                keyHashLookup.put(tNode.key, hash);
                this.rebuiltTable.set(hash, tNode);
                T[hash] = 1;
            }else{
                findInjectiveSubHash(bucket);
            }
        }

        if (!validateNoCollisions()){
            throw new Exception("Collision Detected! Not a Perfect HashMap");
        }
    }

    private void findInjectiveSubHash(List<Node<K, V>> bucket) {
        int MAX_ATTEMPTS = 1000000;
        for (Node<K, V> newNode : bucket) {
            for (int i = 1; i < MAX_ATTEMPTS; ++i) {
                int subHash = computeSubHash(newNode.key, i);
                if (T[subHash] == 0) {
                    // No collision, we can safely add this node
                    keyHashLookup.put(newNode.key, subHash);
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
        // If we are using the rebuilt rebuiltTable, check primary hash first
        if (!useRebuilt) {
            int pos = computeHash(key, m);
            Node<K, V> tNode = this.rebuiltTable.get(pos);
            if (tNode != null && tNode.key.equals(key)) {
                return tNode.value;
            } else {
                var x = keyHashLookup.get(key);
                Node<K, V> maybeNode = this.rebuiltTable.get(x);
                if (maybeNode != null && maybeNode.key.equals(key)) {
                    return maybeNode.value;
                }
            }
        } else {
            // If not using the rebuilt table,
            // just search in lookup hashmap for quick gets
            return storeLookup.get(key);
        }

        return null;
    }

    public void delete(K key) {
        int pos = computeHash(key, m);
        Node<K, V> tNode = this.rebuiltTable.get(pos);

        if (tNode != null && tNode.key.equals(key)) {
            this.rebuiltTable.set(pos, null);
            for (int i = 0; i < storeTable.size(); ++i) {
                Node<K, V> node = storeTable.get(i);
                if (node.key.equals(key)) {
                    storeTable.remove(i);
                    break;
                }
            }
        }
    }

    public boolean validateNoCollisions() {
        Set<Integer> occupiedPositions = new HashSet<>();

        for (int i = 0; i < rebuiltTable.size(); ++i) {
            Node<K, V> node = rebuiltTable.get(i);
            if (node != null) {
                if (occupiedPositions.contains(i)) {
                    return false;
                }
                occupiedPositions.add(i);
            }
        }

        return true;
    }

}
