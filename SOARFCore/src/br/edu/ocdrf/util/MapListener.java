package br.edu.ocdrf.util;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapListener<K, V> implements Map<K, V> {

    private final Map<K, V> delegatee;
    
    private IMapPutListener iMapPutListener;
    
    public void addPutListener(IMapPutListener iMapPutListener){
        this.iMapPutListener=iMapPutListener;
    }

    public MapListener(Map<K, V> delegatee) {
        this.delegatee = delegatee;
    }

    @Override
    public int size() {
        return delegatee.size();
    }

    @Override
    public boolean isEmpty() {
        return delegatee.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegatee.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegatee.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegatee.get(key);
    }

    @Override
    public V put(K key, V value) {
        if(iMapPutListener!=null)
            iMapPutListener.putListener();
        return delegatee.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return delegatee.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegatee.putAll(m);
    }

    @Override
    public void clear() {
        delegatee.clear();
    }

    @Override
    public Set<K> keySet() {
       return  delegatee.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegatee.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegatee.entrySet();
    }

}
