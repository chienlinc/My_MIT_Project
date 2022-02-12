import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class FrequentTester {
	public static void main(String args[]){
			
		String filename = "10^8_z_11.txt";
		String output_filename = "Space_Output_10^8_z_11_s5.txt";
		String output_all_filename = "Space_Output_all_10^8_z_11_s5.txt";
		String output_memory_filename = "Space_Output_memory_10^8_z_11_s5.txt";
		String output_time_filename = "Space_Output_time_10^8_z_11_s5.txt";
		String output_max_filename = "Space_Output_max_10^8_z_11_s5.txt";
		
		double support = 0.00001;
		long num_of_items = 100000000;
		
		Space ss = new Space(support);
	
		try(BufferedReader br = new BufferedReader(new FileReader(filename))){ 
        	
			Runtime runtime = Runtime.getRuntime();
			long startTime = System.nanoTime();	
			String line;
			
		    while ((line = br.readLine()) != null) {
		    	long c = Long.parseLong(line.replace("\n", ""));
                ss.add(c);
		    }
		    
		    Set<Object> items = ss.getItems();
		    Collection<Integer> items_value = ss.getValues();
		    int max_tracked_size = ss.getMaxTrackedSize();
		    writeToFile(output_max_filename, Integer.toString(max_tracked_size));
		    
		    long endTime = System.nanoTime();
		    long timeElapsed = endTime - startTime;
		    writeToFile(output_time_filename, Long.toString(timeElapsed));
		    
			runtime.gc();
			long memory_gc = runtime.totalMemory() - runtime.freeMemory();
			writeToFile(output_memory_filename, Long.toString(memory_gc));
		    
		    int count = 0;
		    
		    // output f >= support * num_of_items
		    for (Object item: items) {
		    	
		    	if ((int) items_value.toArray()[count] >= num_of_items*support) {
		    		String entry = item + "," + items_value.toArray()[count];		    	
			    	writeToFile(output_filename, entry);
		    	}
		    	
		    	String entry = item + "," + items_value.toArray()[count];		    	
		    	writeToFile(output_all_filename, entry);
		    	count++;
		    }
            
        } catch(FileNotFoundException ex) {                            
            System.err.println("No file: "+filename);
    	
        }catch(IOException e){
			System.err.println("File '" + filename + "' has IO issues.\n");
		    System.exit(1);
		}
	}

	public static void writeToFile(String filename, String entry) {
	    try {
	      FileWriter myWriter = new FileWriter(filename, true);
	      BufferedWriter bw = new BufferedWriter(myWriter); 
	      bw.write(entry+ "\n");
	      bw.close();
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
}
