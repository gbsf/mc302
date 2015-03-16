package pt.c02classes.s01knowledge.s02app.util;

public class KeyPair<K, V> {

    private final K key;
    private final V value;

    public KeyPair(K key, V value) {
        if (key == null)
            throw new IllegalArgumentException("key");
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyPair<?, ?> keyPair = (KeyPair<?, ?>) o;

        return key.equals(keyPair.key);

    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
