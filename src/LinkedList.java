/**
 * My Singly Linked list class
 * @param <T> is the generic parameter
 */
public class LinkedList<T extends Comparable<T>> {

    Node<T> head;
    Node<T> tail;
    int size;

    public LinkedList() {
        size = 0;
    }

    /**
     * deep copies another list | O(n)
     * @param list is the other list
     */
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

        this.tail = current2;

    }

    /**
     * insertion at the end of the list | O(1)
     * @param data is the data to be inserted
     */
    public void addLast(T data) {

        Node<T> newNode = new Node<>(data);
        size++;

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            return;
        }

        tail.next = newNode;
        tail = newNode;

    }

    /**
     * empty status of the list | O(1)
     * @return true if empty
     */
    private boolean isEmpty() {

        return head == null;

    }

    /**
     * the number of elements in a list | O(1)
     * @return the size of the list
     */
    public int size() {
        return size;
    }

}
