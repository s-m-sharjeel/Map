public class LinkedList<T> {

    public static class Node<T>{

        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
        }

    }

    Node<T> head;
    int size;

    public LinkedList() {
        size = 0;
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
