public class PriorityQueue<T extends Comparable<T>>{

    private static class Node<T> {

        Node<T> next;
        T data;

        public Node(T data) {
            this.data = data;
        }
    }

    Node<T> front;
    Node<T> rear;

    /**
     * inserts data in order (ascending)
     * @param data is the data to be inserted
     */
    public void enqueue(T data) {

        Node<T> newNode = new Node<>(data);

        if (isEmpty()) {
            front = rear = newNode;
            return;
        }

        if (data.compareTo(front.data) <= 0) {
            newNode.next = front;
            front = newNode;
            return;
        }

        if (data.compareTo(rear.data) >= 0) {
            rear.next = newNode;
            rear = newNode;
            return;
        }

        Node<T> current = front;

        while (data.compareTo(current.next.data) > 0)
            current = current.next;

        newNode.next = current.next;
        current.next = newNode;

    }

    /**
     * dequeues the first element in the list
     * @return the element
     */
    public T dequeue() {

        if(isEmpty())
            throw new RuntimeException("queue is empty!");

        T temp = front.data;
        front = front.next;

        if (front == null)
            rear = null;

        return temp;

    }

    public boolean isEmpty() {

        return front == null && rear == null;

    }

}