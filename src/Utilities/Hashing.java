package Utilities;

import java.io.Serializable;

public class Hashing {
    public static class HashFunction<K> implements Serializable {
        int a, b;
        static final int p = Integer.MAX_VALUE;

        public HashFunction(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int apply(K key, int tableSize) {
            int x = key.hashCode();
            long hash = ((long) a * x + b) % p;
            return (int) (hash % tableSize);
        }
    }
}
