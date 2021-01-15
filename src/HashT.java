public class HashT<K, V> {

    private Node<K, V>[] buckets;
    private int bucketsCount;
    private int size;

    public HashT() {
        buckets = new Node[10];
        bucketsCount = 10;
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private int getIndex(K key) {
        return Math.abs(key.hashCode() % bucketsCount);
    }

    public V get(K key) {
        int idx = getIndex(key);
        Node<K, V> head = buckets[idx];

        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }

            head = head.next;
        }
        return null;
    }

    public void add(K key, V value) {
        int idx = getIndex(key);
        Node<K, V> head = buckets[idx];
        Node<K, V> newNode = new Node<>(key, value);

        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;//updating the value if given key exists
                return;
            }
            head = head.next;
        }

        head = buckets[idx];
        newNode.next = head;
        buckets[idx] = newNode;
        size++;
    }

    public V remove(K key) {
        int idx = getIndex(key);
        Node<K, V> head = buckets[idx];
        Node<K, V> previous = null;

        while (head != null) {

            if (head.key.equals(key)) {
                size--;
                break;
            }

            previous = head;
            head = head.next;
        }

        if (head == null) return null;

        if (previous == null) {
            buckets[idx] = head.next;
        } else {
            previous.next = head.next;
        }

        return head.value;
    }

    public Node[] getAllNodes() {
        Node<K, V> head = null;
        Node<K, V>[] nodes = new Node[size];
        int filledIdx = 0;

        for (int i = 0; i < bucketsCount; i++) {
            head = buckets[i];
            while (head != null) {
                nodes[filledIdx] = new Node<>(head.key, head.value);
                filledIdx++;
                head = head.next;
            }
        }

        return nodes;
    }

    @Override
    public String toString() {
        Node[] nodes = getAllNodes();
        String text = "";
        for (int i = 0; i < nodes.length; i++) {
            text += nodes[i].key + " " + nodes[i].value + "\n";
        }
        return text;
    }
}