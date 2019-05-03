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
		
		ArrayList<Pair> sorted = new ArrayList<Pair>();
		int times = list.size();
		for(int i = 0; i<times && i<10; i++){
			int max = list.get(0).y;
			Pair maxPair = list.get(0);
			for(Pair p : list){
				if(p.y > max){
					max = p.y;
					maxPair = p;
				}
			}
			System.out.println ("added " + maxPair.y + " position " + i);
			sorted.add(maxPair);
			list.remove(maxPair);
		}
		
		list = sorted;
		
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