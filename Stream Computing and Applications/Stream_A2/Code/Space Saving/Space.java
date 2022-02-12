import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Space {
	
	private double support;
	private int t;
	private Map<Object,Integer> tracked;
	private int max_tracked_size = 0;
	
	public Space(double support) {
		tracked = new HashMap<Object,Integer>();
		t = (int) Math.ceil(1 / support);
	}
	
	public void add(Object x){
		
		if(tracked.containsKey(x)){
			int count = tracked.get(x);
			tracked.replace(x, count+1);
		} else {
			if(tracked.size()<t){
				tracked.put(x, 1);
			} else {
				int min = Collections.min(tracked.values());
				tracked.remove(getMinKey(tracked, min));
				tracked.put(x, min + 1);
			}
		}
		
		int cur_tracked_size = tracked.size();
		if (cur_tracked_size > this.max_tracked_size) {
			this.max_tracked_size = cur_tracked_size;
		}
	}
	
	public <K, V> K getMinKey(Map<K, V> map, V value) {
	    for (Entry<K, V> entry : map.entrySet()) {
	        if (entry.getValue().equals(value)) {
	            return entry.getKey();
	        }
	    }
	    return null;
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
