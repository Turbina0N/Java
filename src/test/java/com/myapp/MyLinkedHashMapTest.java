package com.myapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MyLinkedHashMapTest {
    private MyLinkedHashMap<String, String> myMap;
    private Map<String, String> linkedMap;

    @BeforeEach
    void setUp() {
        myMap = new MyLinkedHashMap<>();
        linkedMap = new LinkedHashMap<>();
    }

    @Test
    void testAddAndSize() {
        myMap.add("key1", "value1");
        linkedMap.put("key1", "value1");

        assertEquals(linkedMap.size(), myMap.size());
    }

    @Test
    void testContains() {
        myMap.add("key2", "value2");
        linkedMap.put("key2", "value2");

        assertTrue(myMap.contains("key2"));
        assertTrue(linkedMap.containsKey("key2"));
    }

    @Test
    void testGet() {
        myMap.add("key3", "value3");
        linkedMap.put("key3", "value3");

        assertEquals(linkedMap.get("key3"), myMap.get("key3"));
    }

    @Test
    void testRemove() {
        myMap.add("key4", "value4");
        linkedMap.put("key4", "value4");

        myMap.remove("key4");
        linkedMap.remove("key4");

        assertFalse(myMap.contains("key4"));
        assertFalse(linkedMap.containsKey("key4"));
    }

    @Test
    void testClear() {
        myMap.add("key5", "value5");
        linkedMap.put("key5", "value5");

        myMap.clear();
        linkedMap.clear();

        assertEquals(0, myMap.size());
        assertEquals(0, linkedMap.size());
    }

    @Test
    void testIterator() {
        myMap.add("key1", "value1");
        myMap.add("key2", "value2");
        linkedMap.put("key1", "value1");
        linkedMap.put("key2", "value2");

        MyLinkedHashMap.MyIterator<String> myMapIterator = myMap.iterator();
        java.util.Iterator<String> linkedMapIterator = linkedMap.keySet().iterator();

        while (myMapIterator.hasNext() && linkedMapIterator.hasNext()) {
            assertEquals(linkedMapIterator.next(), myMapIterator.next());
        }

        assertFalse(myMapIterator.hasNext());
        assertFalse(linkedMapIterator.hasNext());
    }
}
