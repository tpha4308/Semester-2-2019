import java.util.List;
import java.util.ArrayList;
import java.io.File; 
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Snapshot deals with storing the id and current state of the database. 
 * As well as storing this data, the Snapshot class should manage operations
 * related to snapshots.
 */

public class Snapshot {
	private int id;
	private List<Entry> entries;

	public Snapshot(int id, List<Entry> entries) {
		this.id = id;
		this.entries = entries;
	}
	
	
	public int getID() {
		return id; 
	}
	
	public List<Entry> getEntryls(){
		return entries; 
	}

	/**
	 * Finds and removes the key.
	 *
	 * @param key the key to remove
	 */
	public void removeKey(String key) {
		List<Entry> new_ls = new ArrayList<Entry>();
		if (entries.size() > 0){
			for (int i = 0; i < entries.size(); i++){
				Entry entr = entries.get(i);
				if (entr.getKey().equals(key)){
					continue;
				}
				new_ls.add(entr);
			}
		}
		entries = new_ls;
	}

	/**
	 * Finds the list of entries to restore.
	 *
	 * @return the list of entries in the restored state
	 */
	public List<Entry> rollback() {
		return entries;
	}


	/**
	 * Saves the snapshot to file.
	 *
	 * @param filename the name of the file
	 */
	public void archive(String filename) {
		try{
			File f = new File("/home/" + filename);
			if (f.exists() == false){
				f.createNewFile();
			}
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(filename);
				for (Entry each_entry : entries){
					List<Integer> values = each_entry.getValues();
					String s = String.format("%s|%s", each_entry.getKey(), values.get(0)); 
					int i = 1;
					while (i < values.size()) {
						s += "," + values.get(i);
						i += 1;
					}
					pw.println(s);		
				}
			} catch (FileNotFoundException e){
				e.printStackTrace();
			} finally {
				if (pw != null) {
					pw.close(); // **** closing it flushes it and reclaims resources ****
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Loads and restores a snapshot from file.
	 *
	 * @param  filename the name of the file
	 * @return          the list of entries in the restored state
	 */
	public static List<Entry> restore(String filename) {
		File f = new File(filename);
		try{
			List<Entry> restore_ls = new ArrayList<Entry>();
			Scanner scan = new Scanner(f);
			while (scan.hasNextLine()){
				String s = scan.nextLine();
				String[] data = s.split("\\|");
				if (data.length == 2){
					try{
						String key = data[0];
						String values_string = data[1];
						List<Integer> each_values = new ArrayList<Integer>();
						String[] values = values_string.split(",");
						for (String val : values){
							Integer x = Integer.valueOf(val);
							each_values.add(x);
						}
						Entry new_entry = new Entry(key, each_values);
						restore_ls.add(new_entry);
					}
					catch(NumberFormatException e){
						return null;
					}
				}
			}
			return restore_ls;
		}
		catch (FileNotFoundException e){
			return null;
		}
	}

	/**
	 * Formats all snapshots for display.
	 *
	 * @param  snapshots the snapshots to display
	 * @return           the snapshots ready to display
	 */
	public static String listAllSnapshots(List<Snapshot> snapshots) {
		String s = "";
		if (snapshots.size() == 0){
			s += "no snapshots\n";
		}
		if (snapshots.size() > 0) {
			s += snapshots.get(0).id;
			for (int i = 1; i < snapshots.size(); i++){
				int id2 = snapshots.get(i).id; 
				s += "\n" + id2;
			}
			/*for(int i= snapshots.size()-1; i >= 0; i--){
				int id2 = snapshots.get(i).id; 
				s += id2 + "\n";
			}*/
		}
		return s;
	}
}
