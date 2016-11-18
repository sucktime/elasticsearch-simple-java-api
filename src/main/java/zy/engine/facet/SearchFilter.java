package zy.engine.facet;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangGuofeng on 2016/10/20.
 */
public class SearchFilter {
    private List<Entry<String, List<?>>> termsFilters = null;
    private List<Entry<String, Range<?>>> rangeFilters = null;
    private List<Entry<String, List<?>>> excludeTermsFilters = null;

    public SearchFilter() {
        this.rangeFilters = new LinkedList<Entry<String, Range<?>>>();
        this.termsFilters = new LinkedList<Entry<String, List<?>>>();
        this.excludeTermsFilters = new LinkedList<Entry<String, List<?>>>();
    }

    public SearchFilter addTermsFilter(Entry<String, List<?>> tFilter){
        this.termsFilters.add(tFilter);
        return this;
    }

    public SearchFilter addExcludeTermsFilter(Entry<String,List<?>> exTFilter){
        this.excludeTermsFilters.add(exTFilter);
        return this;
    }

    public SearchFilter addRangeFilter(Entry<String, Range<?>> rFilter){
        this.rangeFilters.add(rFilter);
        return this;
    }

    public List<Entry<String, List<?>>> getTermsFilters(){
        return this.termsFilters;
    }

    public List<Entry<String, Range<?>>> getRangeFilters(){
        return this.rangeFilters;
    }

    public List<Entry<String, List<?>>> getExcludeTermsFilters(){
        return excludeTermsFilters;
    }

    public static SearchFilter searchFilter(){
        return new SearchFilter();
    }

    // for testing:
    public static void main(String[] args) {
        Range range = new Range(2, 10);
        System.out.println(range.getType() == Integer.class);
        //System.out.println(range.getTo())
    }

    public static class Range<T> {
        T from;
        T to;
        public Range(T from, T to){
            setFrom(from);
            setTo(to);
        }

        public Type getType(){
            return from.getClass().getComponentType();
        }

        public T getFrom() {
            return from;
        }

        public T getTo() {
            return to;
        }

        public void setFrom(T from) {
            this.from = from;
        }

        public void setTo(T to) {
            this.to = to;
        }
    }

    public static class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;

        public Entry(K key, V value){
            setKey(key);
            setValue(value);
        }

        public Type getValueType(){
            return value.getClass().getComponentType();
        }
        public K getKey() {
            return this.key;
        }

        public K setKey(K key){
            this.key = key;
            return key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }

}