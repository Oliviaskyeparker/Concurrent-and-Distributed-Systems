//osp257

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    String name;
    int priority;
    Node next;
    ReentrantLock lock;

public Node(String name, int priority) {
    this.name = name;
    this.priority = priority;
    this.next = null;
    this.lock = new ReentrantLock();
}

public void setName(String name) {
    this.name = name;
}

public String getName() {
    return this.name;
}

public void setPriority(int priority) {
    this.priority = priority;
}

public int getPriority() {
    return this.priority;
}

}


public class PriorityQueue {
    private Node head;
    private Node tail;
    int count;
    int capacity;
    final ReentrantLock sizeLock = new ReentrantLock();
    final ReentrantLock fullLock = new ReentrantLock();
    final ReentrantLock emptyLock = new ReentrantLock();

    private Condition full = fullLock.newCondition();
    private Condition empty = emptyLock.newCondition();

    public PriorityQueue(int capacity) {
        this.capacity = capacity;
        this.count=0;
    }
    
    public void print() {
        Node current = head;
        current.lock.lock();
        while(current.next != null){
            System.out.println("name: " + current.getName() + " priority: " + current.getPriority());
            current = current.next;

        }
        System.out.println("name: " + current.getName() + " priority: " + current.getPriority());
    }



    public int add(String name, int priority) {
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.
        Node node = new Node(name, priority);
        if(head == null) {
            head = node;
            head.next = null;

            sizeLock.lock();
            count++;
            sizeLock.unlock();

            return 0;                       // added at the head
        }

        Node cur = head;
        cur.lock.lock();

        if(duplicate(name)) {               // if name is already present in the list
            cur.lock.unlock();
            return -1;
        }

        if(count == 1 && cur.getName().equals("")) {
            cur.name = name;
            cur.priority = priority;
            cur.next = null;
            cur.lock.unlock();
            return 0;                       // added at the head
        }

        int index = 0;
        if(head.getPriority() < priority) {     // if new node has greater priority than the head
            while(count == capacity) {            // block if priority queue is full
                fullLock.lock();
                try {
                    full.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fullLock.unlock();
            }

            node.next = head.next;
            head.lock.unlock();
            head.next = node;

            sizeLock.lock();
            count++;
            sizeLock.unlock();

            return 0;                       // added at the head
        }
        else {
            while(cur.next != null && cur.getPriority() >= priority && cur.next.getPriority() >= priority) {
                while (count == capacity) {       // block if priority queue is full
                    fullLock.lock();
                    try {
                        full.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    fullLock.unlock();
                }

                cur.lock.lock();
                cur.next.lock.lock();
                index++;
                cur.lock.unlock();
                cur.next.lock.unlock();
                cur = cur.next;
            }
            // found right place to add
            node.next = cur.next;
            cur.next = node;

            sizeLock.lock();
            count++;
            sizeLock.unlock();

            // signal that the priority queue is not empty
            emptyLock.lock();
            empty.signal();
            emptyLock.unlock();

            cur.lock.unlock();
        }

        return index;
    }

    public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.

        Node node = head;
        head.lock.lock();
        int index = 0;

        while(node.next != null) {
            node.lock.lock();
            node.next.lock.lock();

            if(node.getName().equals(name)) {
                node.lock.unlock();
                return index;
            }
            node.lock.unlock();
            node = node.next;
            index++;
        }

        if(node.getName().equals(name))
            return index;

        return -1;
    }



    public String getFirst() {
    	String result = "";

        while(head == null) {
            emptyLock.lock();
            try {
                empty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            emptyLock.unlock();
        }

        head.lock.lock();
        while(count == 0) {
            emptyLock.lock();
            try {
                empty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            emptyLock.unlock();
        }


        result = head.getName();
        head.lock.unlock();
        head = head.next;

        sizeLock.lock();
        count--;
        sizeLock.unlock();

        fullLock.lock();                    // signal because priority queue is not full anymore
        full.signal();
        fullLock.unlock();

        return result;
    }
    private boolean duplicate(String name) {
        Node node = head;
        while(node.next!= null) {
            node.lock.lock();
            node.next.lock.lock();

            if(node.getName().equals(name)) {
                node.lock.unlock();
                return true;
            }
            node.lock.unlock();
            node = node.next;
        }
        if(node.getName().equals(name))
            return true;
        return false;
    }

}
