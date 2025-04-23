package HashMap;

import java.io.Serializable;
import java.util.*;

public class PerfectHashMap<K, V> implements Serializable {
    public static class Entry<K, V> implements Serializable {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private List<Entry<K, V>> entries;
    private List<Entry<K, V>> table;
    private int m;
    private int r;
    private int[] T;
    public PerfectHashMap() {
        this.entries = new ArrayList<>();
        this.table = new ArrayList<>();
    }

    public void put(K key, V value) throws Exception {
        if (!entries.contains(key)){
            entries.add(new Entry<>(key, value));
        }
    }

    public void rebuild() throws Exception {
        if (entries.isEmpty()) {
            return; // Empty trie means basically, you made an array of size 0.
        }

        r = entries.size();
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
        for (Entry<K, V> entry : entries) {
            int index = computeHash(entry.key, r);
            buckets.get(index).add(entry.key);
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
        for (Entry<K, V> entry : entries) {
            int pos = computeHash(entry.key, m);
            table.set(pos, entry);
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
        // Hash Not Found
        return false;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (Entry<K, V> entry : entries) {
            keys.add(entry.key);
        }
        return keys;
    }

    private int computeHash(K key, int r) {
        return Math.abs(key.hashCode()) % r;
    }

    private int computeSubHash(K key, int l) {
        return Math.abs(key.hashCode() + l) % m;
    }

    public V get(K key){
        return get(key, true);
    }

    public V get(K key, boolean whichTable) {
        var useTable = table;

        // This modification here of the "get" function allows for
        // appending nodes in the entries list BEFORE they are placed

        if (!whichTable){
            useTable = entries;
            for (int i = 0; i < useTable.size(); ++i) {
                Entry<K, V> entry = useTable.get(i);
                if (entry.key.toString().toUpperCase().equals(
                        key.toString().toUpperCase())) {
                    return entry.value; // Found match, return value
                }
            }
        }
        else{
            int pos = computeHash(key, m);
            Entry<K, V> entry = useTable.get(pos);
            if (entry != null && entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    public void delete(K key) {
        int pos = computeHash(key, m);
        Entry<K, V> entry = table.get(pos);
        if (entry != null && entry.key.equals(key)) {
            // Mark the position as empty by setting it to null
            table.set(pos, null);
            // Optionally, remove the entry from the `entries` list
            entries.removeIf(e -> e.key.equals(key));
        }
    }
}
