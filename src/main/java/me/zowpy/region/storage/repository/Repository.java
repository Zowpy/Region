package me.zowpy.region.storage.repository;

public interface Repository<K, V> {

    V findById(K key);

    void save(V value);

    void delete(K key);
}
