import java.util.ArrayList;
//https://beginnersbook.com/2013/12/how-to-clone-an-arraylist-to-another-arraylist/
//https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html
public class TableList{
	private ArrayList<Pair> list = new ArrayList<Pair>();
	
	public void add(String x, int y){
		Pair toAdd = new Pair(x,y);
		list.add(toAdd);
	}
	
	public void sort(){

		@SuppressWarnings("unchecked")
		ArrayList<Pair> toSort = (ArrayList<Pair>)list.clone();
		
		for(int i = 0; i<list.size(); i++){
			int max = toSort.get(0).y;
			Pair maxPair = toSort.get(0);
			for(Pair p : toSort){
				if(p.y > max){
					max = p.y;
					maxPair = p;
				}
			}
			list.set(i,maxPair);
			toSort.remove(maxPair);
		}
	}
	
	public Pair get(int i){
		return list.get(i);
	}
	
	public int size(){
		return list.size();
	}
}
class Pair{
	public String x;
	public int y;
	
	public Pair(String s, int i){
		x = s;
		y = i;
	}
}