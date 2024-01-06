package com.myapp;

public class MyLinkedHashMap<K, V> {
    private class Entry {
        K key;
        V value;
        Entry next;
        Entry prev;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private class MyLinkedList {
        private Entry head, tail;

        MyLinkedList() {
            head = new Entry(null, null);
            tail = new Entry(null, null);
            head.next = tail;
            tail.prev = head;
        }

        void add(Entry entry) {
            Entry last = tail.prev;
            last.next = entry;
            entry.prev = last;
            entry.next = tail;
            tail.prev = entry;
        }

        void remove(Entry entry) {
            entry.prev.next = entry.next;
            entry.next.prev = entry.prev;
        }

        Entry find(K key) {
            Entry current = head.next;
            while (current != tail) {
                if (key.equals(current.key)) {
                    return current;
                }
                current = current.next;
            }
            return null;
        }

        int size() {
            int size = 0;
            Entry current = head.next;
            while (current != tail) {
                size++;
                current = current.next;
            }
            return size;
        }

        void clear() {
            head.next = tail;
            tail.prev = head;
        }
    }

    private class MyArrayList<T> {
        private Object[] elements;
        private int size;

        MyArrayList(int initialCapacity) {
            elements = new Object[initialCapacity];
        }

        @SuppressWarnings("unchecked")
        T get(int index) {
            return (T) elements[index];
        }

        void add(T element) {
            if (size == elements.length) {
                increaseCapacity();
            }
            elements[size++] = element;
        }

        private void increaseCapacity() {
            int newSize = elements.length * 2;
            Object[] newElements = new Object[newSize];
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            elements = newElements;
        }

        int size() {
            return size;
        }
    }

    public static interface MyIterator<T> {
        boolean hasNext();
        T next();
    }

    private final int capacity = 16;
    private MyArrayList<MyLinkedList> entries;

    public MyLinkedHashMap() {
        entries = new MyArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            entries.add(new MyLinkedList());
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode()) % capacity;
    }

    public void add(K key, V value) {
        int index = hash(key);
        MyLinkedList bucket = entries.get(index);

        Entry existing = bucket.find(key);
        if (existing != null) {
            existing.value = value;
        } else {
            Entry newEntry = new Entry(key, value);
            bucket.add(newEntry);
        }
    }

    public boolean contains(K key) {
        int index = hash(key);
        MyLinkedList bucket = entries.get(index);
        return bucket.find(key) != null;
    }

    public void remove(K key) {
        int index = hash(key);
        MyLinkedList bucket = entries.get(index);
        Entry toRemove = bucket.find(key);
        if (toRemove != null) {
            bucket.remove(toRemove);
        }
    }

    public V get(K key) {
        int index = hash(key);
        MyLinkedList bucket = entries.get(index);
        Entry entry = bucket.find(key);
        if (entry != null) {
            return entry.value;
        }
        throw new NoSuchElementException("Key not found: " + key);
    }

    public int size() {
        int size = 0;
        for (int i = 0; i < capacity; i++) {
            size += entries.get(i).size();
        }
        return size;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            entries.get(i).clear();
        }
    }

    public MyIterator<K> iterator() {
        return new MyIterator<K>() {
            private Entry current;
            private int currentBucket = 0;

            {
                moveToNextValidBucket();
            }

            private void moveToNextValidBucket() {
                while (currentBucket < entries.size() && (entries.get(currentBucket).head.next == entries.get(currentBucket).tail)) {
                    currentBucket++;
                }
                if (currentBucket < entries.size()) {
                    current = entries.get(currentBucket).head.next;
                } else {
                    current = null;
                }
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                if (!hasNext()) throw new NoSuchElementException("No more elements in the iteration");
                K key = current.key;
                current = current.next;
                if (current == entries.get(currentBucket).tail) {
                    currentBucket++;
                    moveToNextValidBucket();
                }
                return key;
            }
        };
    }

    public static class NoSuchElementException extends RuntimeException {
        public NoSuchElementException(String message) {
            super(message);
        }
    }
}
