public class Queue<T>{
	public Node<T> start;
	
	public Queue(){}
	
	public void add(T elem){
		Node<T> toAdd = new Node<T>(elem);
		if(this.start == null)
			this.start = toAdd;
		else {
			Node<T> temp = this.start;
			while (temp.next != null)
				temp = temp.next;
			temp.next = toAdd;
		}
	}
	
	public T pop(){
		if(start == null)
			return null;
		else{
			T toReturn = start.elem;
			start = start.next;
			return toReturn;
		}	
	}
}

class Node<E>{
	public E elem;
	public Node<E> next;
	
	public Node(E elem){
		this.elem = elem;
	}
}