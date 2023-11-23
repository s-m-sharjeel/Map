public class Queue <T>{

    private static class Node<T> {

        Node<T> next;
        T data;

        public Node(T data) {
            this.data = data;
        }
    }

    Node<T> front;
    Node<T> rear;

    public void enqueue(T data) {

        Node<T> newNode = new Node<>(data);

        if (isEmpty()) {
            front = rear = newNode;
            return;
        }

        rear.next = newNode;
        rear = newNode;

    }

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