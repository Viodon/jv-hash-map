package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    public static final int DEFAULT_CAPACITY = 16;
    public static final float LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private int capacity;
    private int size = 0;

    @Override
    public void put(K key, V value) { // index = hashCode(key) % capacity
        int index;
        if (size == 0) {
            table = new Node[DEFAULT_CAPACITY];
            capacity = DEFAULT_CAPACITY;
        } else if (size > table.length * LOAD_FACTOR) {
            table = resize(table);
        }
        index = getIndex(key);
        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }
        Node<K, V> curent = table[index];
        while (curent != null) {
            if (curent.key == key || (curent.key != null && curent.key.equals(key))) {
                curent.value = value;
                return;
            }
            curent = curent.next;
        }
        curent = table[index];
        table[index] = new Node<>(key, value, curent);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key);
        Node<K, V>[] table = getTable(); //added local variable
        if (table == null || size == 0 || table.length < index) { //added checking size == 0
            return null;
        }
        Node<K, V> current = table[index];
        while (current != null) {
            if (current.key == key || (current.key != null && current.key.equals(key))) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node<K, V>[] getTable() {
        return table;
    }

    private Node<K, V>[] resize(Node<K, V>[] table) {
        int index;
        Node<K, V> next;
        Node<K, V>[] newTable = new Node[table.length * 2];
        capacity *= 2;
        for (Node<K, V> node : table) {
            while (node != null) {
                next = node.next;
                index = getIndex(node.key);
                node.next = newTable[index];
                newTable[index] = node;
                node = next;
            }
        }
        return newTable;
    }

    private int getIndex(K key) {
        return key == null ? 0 : key.hashCode() & (capacity - 1);
    }

    static class Node<K, V> {
        private K key; //added private modifier
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) { //added public modifier
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
