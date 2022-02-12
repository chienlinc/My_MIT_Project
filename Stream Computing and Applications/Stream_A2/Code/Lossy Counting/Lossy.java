import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lossy {
	
	private double support;
	private double epsilon;
	private double w;
	private int N = 0;
	private int D = 0;
	
	private Map<Object,Integer> tracked;
	private int max_tracked_size = 0;
	
	public Lossy(double support, double epsilon) {
		this.support = support;
		this.epsilon = epsilon;
		this.w = 1/epsilon;
		
		tracked = new HashMap<Object,Integer>();
		
	}
	
	public void add(Object x){
		
		N++;
		
		if(tracked.containsKey(x)){
			int count = tracked.get(x);
			tracked.replace(x, count+1);
		} else {
			tracked.put(x, 1 + this.D);
		}
		
		if (this.D != Math.floor(this.N / this.w)) {
			this.D++;
			
			Set<Object> marked = new HashSet<Object>();
			
			for(Object y:tracked.keySet()){
				if(tracked.get(y) == this.D){
					marked.add(y);
				}
			}
			
			for(Object y:marked){
				tracked.remove(y);
			}	
		}
		
		int cur_tracked_size = tracked.size();
		if (cur_tracked_size > this.max_tracked_size) {
			this.max_tracked_size = cur_tracked_size;
		}
	}
	
	public int getMaxTrackedSize() {
		return this.max_tracked_size;
	}
	
	public Set<Object> getItems(){
		return tracked.keySet();
	}
	
	public Collection<Integer> getValues(){
		return tracked.values();
	}
}
