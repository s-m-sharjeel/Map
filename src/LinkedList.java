/**
 * My Singly Linked list class
 * @param <T> is the generic parameter
 */
public class LinkedList<T extends Comparable<T>> {

    private Node<T> head;
    private int size;

    public LinkedList() {
        size = 0;
    }

    public T pop() {

        if (isEmpty())
            return null;

        T temp = head.data;
        head = head.next;
        return temp;

    }

    public T peek() {

        if (isEmpty())
            return null;

        return head.data;

    }

    public LinkedList(LinkedList<T> list) {

        if (list.isEmpty())
            return;

        this.size = list.size;
        this.head = new Node<>(list.head.data);

        Node<T> current1 = list.head;
        Node<T> current2 = this.head;

        while (current1.next != null) {
            current1 = current1.next;
            current2.next = new Node<>(current1.data);
            current2 = current2.next;
        }

    }

    /**
     * insertion at the end of the list
     * @param data is the data to be inserted
     */
    public void add(T data) {

        Node<T> newNode = new Node<T>(data);
        size++;

        if (isEmpty()) {
            head = newNode;
            return;
        }

        Node<T> current = head;

        while (current.next != null) {
            current = current.next;
        }

        current.next = newNode;

    }

    /**
     * insertion at the start of the list
     * @param data is the data to be inserted
     */
    public void addFirst(T data) {

        Node<T> newNode = new Node<T>(data);
        size++;

        if (isEmpty()) {
            head = newNode;
            return;
        }

        newNode.next = head;
        head = newNode;

    }

    public boolean contains(T data) {

        if (isEmpty())
            return false;

        Node<T> current = head;

        while (current != null) {

            if (current.data.equals(data))
                return true;

            current = current.next;

        }

        return false;

    }

    public T get(int i) {

        if (i >= size)
            return null;

        int index = 0;
        Node<T> current = head;

        while (index < i) {

            current = current.next;
            index++;

        }

        return current.data;

    }

    private boolean isEmpty() {

        return head == null;

    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[ ");

        Node<T> current = head;

        while (current != null) {
            s.append(current.data).append(" ");
            current = current.next;
        }

        return s + "]";
    }
}
