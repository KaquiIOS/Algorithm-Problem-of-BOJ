
package SortingAlgorithm;

import java.io.*;
import java.util.*;
import java.lang.reflect.Array;


public class Main {

    final static int MAX_SIZE = 11;

    public static void main(String args[]) throws Exception{

        FastScanner fs = new FastScanner();
        Random rd = new Random();
        int[] input = {73,27,91,5,37,77,4,10,62,75,30};

        LinkedList<Integer> list = new LinkedList<>();

        for(int i = 0; i < MAX_SIZE; ++i)
            list.push_back(input[i]);

        list.sort(Integer.class);

        list.printList();
    }
}

/*
* Class       : LinkedList<T extends Comparable<T>>
* Description : 기본적인 링크드 리스트이다. Available List 를 사용하여 노드 재활용.
* Member      : avHeader : Node<T> - Available List header for adding or popping removedNode
*               avTail   : Node<T> - Available List tail for removeAll
*               header   : Node<T> - list header
*               tail     : Note<T> - list tail
*               size     : int     - List Size
*/

class LinkedList <T extends Comparable<T>> {


    /*
    * Class       : Node<T extends Comparable<T>>
    * Description : Node for LinkedList.
    * Member      : data : T        - data
    *             : next : Node<T>  - next node pointer
    */
    private class Node <T extends Comparable<T>> {

        public T data;
        public Node<T> next;

        // Constructor
        public Node(T _data) {
            data = _data;
            next = null;
        }

        public Node(T _data, Node<T> _next) {
            data = _data;
            next = _next;
        }
    }

    // Member of LinkedList<T extends Comparable<T>>
    private Node<T> avHeader;
    private Node<T> avTail;

    private Node<T> header;
    private Node<T> tail;
    private int size;

    // Constructor
    public LinkedList() {
        header = tail = avHeader = null;
        size = 0;
    }

    public LinkedList(T data) {
        header = tail = new Node<>(data);
        size = 1;
    }

    /*
    * Name        : size
    * Parameter   : void
    * return      : int
    * Description : return # of nodes
     */
    public int size() {
        return size;
    }


    /*
     * Name        : isEmpty
     * Parameter   : void
     * return      : boolean
     * Description : check weather tree is empty or not
     */
    public boolean isEmpty(){
        return header == null;
    }


    /*
     * Name        : getAvailableNode
     * Parameter   : data : T, next : Node<T>
     * return      : Node<T>
     * Description : if there are available nodes, return front node after applying parameter. 재활용이 가능한 노드가 있을 때, 노드의 값을 매개벼수의 값으로 변경한 후 반환
     */
    private Node<T> getAvailableNode(T data, Node<T> next) {

        if(avHeader == null) return null;

        Node<T> recycleNode = avHeader;

        avHeader = avHeader.next;

        recycleNode.next = next;
        recycleNode.data = data;

        return recycleNode;
    }


    /*
     * Name        : addRemoveNode
     * Parameter   : removedNode : Node<T>
     * return      : null
     * Description : add removed node to available list
     */
    private void addRemovedNode(Node<T> removedNode) {

        removedNode.next = null;

        if(avHeader == null) avHeader = avTail = removedNode;

        else {
            removedNode.next = avHeader;
            avHeader = removedNode;
        }
    }


    /*
     * Name        : addRemovedNodes
     * Parameter   : removedNodes : Node<T>, tail : Node<T>
     * return      : null
     * Description : 리스트 자체를 삭제할 때 available list 의 tail에 붙여넣는다.
     */
    private void addRemovedNodes(Node<T> removedNodes, Node<T> tail) {

        if(avHeader == null) {
            avHeader = removedNodes;
            avTail = tail;
        } else {
            avTail.next = removedNodes;
            avTail = tail;
        }
    }


    /*
     * Name        : isExistAvailableNode
     * Parameter   : void
     * return      : boolean
     * Description : 재활용 가능한 노드가 있으면 true, 없으면 false 반환
     */
    private boolean isExistAvailableNode() {
        return avHeader != null;
    }


    /*
     * Name        : push_back
     * Parameter   : data : T
     * return      : null
     * Description : 재활용 가능한 노드가 있으면 사용하고 없으면 새로운 노드를 만들어 리스트 맨 뒤에 연결
     */
    public void push_back(T data) {

        Node<T> recycleNode = isExistAvailableNode() ? getAvailableNode(data, null) : null;

        if(isEmpty()) {
            header = tail = (recycleNode == null) ? new Node<>(data) : recycleNode;
        }
        else {
            tail.next = (recycleNode == null) ? new Node<>(data) : recycleNode;
            tail = tail.next;
        }

        size++;
    }

    /*
     * Name        : push_front
     * Parameter   : data : T
     * return      : null
     * Description : 재활용 가능한 노드가 있다면 사용하고, 없다면 새로운 노드를 만들어 맨 앞에 연결.
     */
    public void push_front(T data) {

        Node<T> recycleNode = isExistAvailableNode() ? getAvailableNode(data, header) : null;

        if(isEmpty())
            header = tail = (recycleNode == null) ? new Node<>(data) : recycleNode;
        else
            header = (recycleNode == null) ? new Node<>(data, header) : recycleNode;

        size++;
    }


    /*
     * Name        : insert
     * Parameter   : idx : int, data : T
     * return      : null
     * Description : 지정된 위치에 data 삽입. 위치가 리스트의 크기보다 크거나 빈 리스트인경우 맨 뒤에 연결.
     */
    public void insert(int idx, T data) {

        if(isEmpty() || size <= idx) {
            push_back(data);
            return;
        }

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(int i = 0; i < idx; ++i, preNode = curNode, curNode = curNode.next);

        Node<T> recycleNode = isExistAvailableNode() ? getAvailableNode(data, curNode) : null;

        preNode.next = (recycleNode == null) ? new Node<>(data, curNode) : recycleNode;

        size++;
    }


    /*
     * Name        : remove_front
     * Parameter   : void
     * return      : null
     * Description : 맨 앞의 노드를 삭제(재활용 리스트에 연결). 빈 리스트면 삭제를 수행하지 않고 종료한다.
     */
    public void remove_front() {

        if(isEmpty()) return;

        Node<T> removedNode = header;
        header = header.next;

        addRemovedNode(removedNode);

        size--;
    }


    /*
     * Name        : remove_tail
     * Parameter   : void
     * return      : null
     * Description : 맨 뒤의 노드를 삭제(재활용 리스트에 연결). 빈 리스트면 삭제를 수행하지 않고 종료한다.
     */
    public void remove_tail() {

        if(isEmpty()) return;

        Node<T> removedNode = tail;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(; curNode != tail; preNode = curNode, curNode = curNode.next);

        tail = preNode;
        preNode.next = null;

        addRemovedNode(removedNode);

        size--;
    }



    /*
     * Name        : removeByIndex
     * Parameter   : idx : int
     * return      : null
     * Description : 입력된 인덱스 위치에 있는 노드를 삭제(재활용 리스트에 연결). 인덱스가 범위를 벗어나거나 빈 리스트면 삭제를 수행하지 않고 종료.
     */
    public void removeByIndex(int idx) {

        if(isEmpty() || size <= idx) return;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(int i = 0; i < idx; ++i, preNode = curNode, curNode = curNode.next);

        Node<T> removedNode = curNode;
        preNode.next = curNode.next;

        addRemovedNode(removedNode);

        size--;
    }


    /*
     * Name        : removeByData
     * Parameter   : data : T
     * return      : null
     * Description : 입력된 데이터를 탐색하면서 발견하면 해당 노드 삭제(재활용 리스트에 연결). 데이터가 없으면 종료.
     */
    public void removeByData(T data) {

        if(isEmpty()) return;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(; curNode.data != data && curNode != null ; preNode = curNode, curNode = curNode.next);

        if(curNode == null) return;

        Node<T> removedNode = curNode;
        preNode.next = curNode.next;

        addRemovedNode(removedNode);

        size--;
    }


    /*
     * Name        : removeAll
     * Parameter   : void
     * return      : null
     * Description : 모든 노드를 삭제한다(재활용 리스트에 연결)
     */
    public void removeAll() {
        addRemovedNodes(header, tail);
        header = tail = null;
        size = 0;
    }


    /*
     * Name        : search
     * Parameter   : data : T
     * return      : null
     * Description : 입력된 데이터를 리스트에서 검색한다. 빈 리스트라면 검색을 수행하지 않고 종료한다.
     */
    public boolean search(T data) {

        if(isEmpty()) return false;

        Node<T> preNode = header;
        Node<T> curNode = preNode;

        for(; curNode.data != data && curNode != null ; preNode = curNode, curNode = curNode.next);

        if(curNode == null) return false;
        else return true;
    }


    // Runtime 도중에 Generic Type인 T 를 찾을방법은..메인에서 클래스 타입을 넘겨주는 방법..
    // 다른 방법 찾아보기
    public void sort(Class<T> clazz) {

        //T[] arr = (T[])(new Object[size]);
        T[] arr = (T[])Array.newInstance(clazz, size);

        Node<T> node = header;

        for(int i = 0; node != null; node = node.next, ++i)
            arr[i] = node.data;

        Arrays.sort(arr);

        node = header;

        for(int i = 0; node != null; node = node.next, ++i)
            node.data = arr[i];
    }


    /*
     * Name        : printList
     * Parameter   : void
     * return      : null
     * Description : 리스트를 출력한다.
     */
    public void printList() throws IOException {

        if(isEmpty()) return;

        Node<T> startNode = header;

        OutputStream out = new BufferedOutputStream(System.out);

        for(; startNode != null; startNode = startNode.next)
            out.write((startNode.data.toString() + "\n").getBytes());

        out.flush();
    }
}

class FastScanner {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private StringTokenizer st;

    public int nextInt() throws Exception {
        if(st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return Integer.parseInt(st.nextToken());
    }

    public long nextLong() throws Exception {
        if(st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return Long.parseLong(st.nextToken());
    }

    public String nextLine() throws Exception {
        if(st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }
}


/*
 * Name        :
 * Parameter   :
 * return      :
 * Description :
 */