import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Sticky {
	
	private double support;
	private double epsilon;
	private double delta;
	private double t;
	private int N = 0;
	private int sampling_rate;
	private int exponent = 0;
	private int max_tracked_size = 0;

	private Map<Object,Integer> tracked;
	
	public Sticky(double support, double epsilon, double delta ){

		tracked = new HashMap<Object,Integer>();
		
		this.support = support;
		this.epsilon = epsilon;
		this.delta = delta;
		
		t = Math.ceil(1 / epsilon * Math.log(1 / (support * delta)));
		
		System.out.println("t: "+ t);
		
		this.sampling_rate = (int) Math.pow(2, exponent);
	}
	
	public void add(Object x){
		
		N++;
		
		int pre_sampling_rate = this.sampling_rate;

		int floor = (int) Math.floor(N/t);
		
		if (this.N >= (((int) Math.pow(2, (this.exponent + 1))*t))) {
			this.exponent++;
			this.sampling_rate = (int) Math.pow(2, exponent);
				
			boolean success = false;
			while(!success) {
				int i = StdRandom.uniform(2);
		    	if(i == 0){
		    		success = true;
		    		break;
		    	}
		    	else {
		    		Set<Object> marked = new HashSet<Object>();
					tracked.replaceAll((key,v) ->v-1);

					for(Object y:tracked.keySet()){
						if(tracked.get(y)==0){
							marked.add(y);
						}
					}
					for(Object y:marked){
						tracked.remove(y);
					}
		    	}
			}
		}
		
		if(tracked.containsKey(x)){
			int count = tracked.get(x);
			tracked.replace(x, count+1);
		} else {
			int i = StdRandom.uniform(sampling_rate);
	    	if(i == 0){
	    		tracked.put(x, 1);
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