import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

/** 
 * This is responsible for the overall management of the database.
 * CrunchDB should deal with taking input from the user and displaying the correct
 * output while passing off the more complicated work to the corresponding
 * classes.
 */

public class CrunchDB {

	// TODO the following data structures are responsible for storing the
	// entries and snapshots related to this instance of the database. You can
	// modify them if you wish, as long as you do not change any method
	// signatures.
	private List<Entry> entries;
	private List<Snapshot> snapshots;

	/*public CrunchDB(List<Entry> entries, List<Snapshot> snapshots) {
		this.entries = entries; 
		this.snapshots = snapshots; 
	}*/
	
	public CrunchDB() {
		this.entries = new ArrayList<Entry>();
		this.snapshots = new ArrayList<Snapshot>();
	}

	/** 
	 * Displays all keys in current state.
	 */
	private void listKeys() {
		if (entries.size() == 0){
			System.out.println("no keys\n");
		}
		if (entries.size() > 0){
			for (int i = entries.size()-1; i >= 0; i--){
				System.out.println(entries.get(i).getKey());
			}
			System.out.println();
		}
	}

	/**
	 * Displays all entries in the current state.
	 */
	private void listEntries() {
		if (entries.size() == 0){
			System.out.println("no entries\n");
		}
		if (entries.size() > 0){
			for (int i = entries.size()-1; i >= 0; i--){
				String key = entries.get(i).getKey();
				String vals = entries.get(i).get();
				System.out.println(key + " " + vals);
			}
			System.out.println();
		}
	}
	
	/**
	 * Displays all snapshots in the current state.
	 */
	private void listSnapshot() {
		if (snapshots.size() == 0){
			System.out.println("no snapshots");
		}
		else {
			for (int i = snapshots.size()-1; i >= 0; i--){
				System.out.println(snapshots.get(i).getID());
			}
		}
		System.out.println();
		//System.out.println(Snapshot.listAllSnapshots(snapshots));
	}
	
	private Entry getEntry(String key){
		if (entries.size() == 0){
			return null; 
		}
		for (int i = 0; i < entries.size(); i++){
			Entry entr = entries.get(i);
			if (entr.getKey().equals(key)){
				return entr;
			}
		}
		return null;  //this happens when there is no such entry 
	}

	/**
	 * Displays entry values.
	 *
	 * @param key the key of the entry
	 */
	private void get(String key) {
		Entry entr = getEntry(key);
		if (entr != null){
			System.out.println(entr.get());
			System.out.println();
		}
		else {
			System.out.println("no such key\n");
		}
	}
	
	/**
	 * Deletes entry from current state.
	 *
	 * @param key the key of the entry
	 */
	private void del(String key) {
		List<Entry> new_ls = new ArrayList<Entry>();
		Entry entr_to_delete = getEntry(key);
		if (entr_to_delete == null) {
			System.out.println("no such key\n");
		}
		else {
			for (Entry entr : entries) {
				if (entr.equals(entr_to_delete)) {
					continue;
				}
				new_ls.add(entr);
			}
			entries = new_ls; 
			System.out.println("ok\n");
		}
	}

	/**
	 * Deletes entry from current state and snapshots.
	 *
	 * @param key the key of the entry
	 */
	private void purge(String key) {
		Entry entr_to_delete = getEntry(key);
		if (entr_to_delete != null){
			List<Entry> new_ls = new ArrayList<Entry>();
			for (Entry entr : entries) {
				if (entr.equals(entr_to_delete)) {
					continue;
				}
				new_ls.add(entr);
			}
			entries = new_ls; 
		}
		for(Snapshot snaps : snapshots){
			snaps.removeKey(key);
		}
		System.out.println("ok\n");
	}

	/**
	 * Sets entry values.
	 *
	 * @param key    the key of the entry
	 * @param values the values to set
	 */
	private void set(String key, List<Integer> values) {
		boolean key_exists = false;
		Entry entry_exists = null;
		for (int i = 0; i < entries.size(); i++){
			String key_find = entries.get(i).getKey();
			if (key.equals(key_find)){
				key_exists = true;
				entry_exists = entries.get(i);
			}
		}
		if (key_exists == false){
			Entry new_entry = new Entry(key, values);
			entries.add(new_entry);
		} else if (key_exists == true){
			entry_exists.set(values);
		}
		System.out.println("ok\n");
	}

	/**
	 * Pushes values to the front.
	 *
	 * @param key    the key of the entry
	 * @param values the values to push
	 */
	private void push(String key, List<Integer> values) {
		Entry entr = getEntry(key);
		if (entr == null){
			System.out.println("no such key\n");
		}
		else{
			entr.push(values);
			System.out.println("ok\n");
		}
	}	

	/**
	 * Appends values to the back.
	 *
	 * @param key    the key of the entry
	 * @param values the values to append
	 */
	private void append(String key, List<Integer> values) {
		Entry entr = getEntry(key);
		if (entr != null){
			entr.append(values);
			System.out.println("ok\n");
		}
		else {
			System.out.println("no such key\n");
		}
	}

	/**
	 * Displays value at index.
	 *
	 * @param key   the key of the entry
	 * @param index the index to display
	 */
	private void pick(String key, int index) {
		Entry entr = getEntry(key);
		if (entr != null){
			Integer n = entr.pick(index);
			if (n == null){
				System.out.println("index out of range\n");
			}
			else{
				System.out.println(n + "\n");
			}
			
		} else{
			System.out.println("no such key\n");
		}
	}
	
	/**
	 * Displays and removes the value at index.
	 *
	 * @param key   the key of the entry
	 * @param index the index to pluck
	 */
	private void pluck(String key, int index) {
		Entry entr = getEntry(key);
		if (entr != null){
			Integer n = entr.pluck(index);
			if (n == null){
				System.out.println("index out of range\n");
			}
			else {
				System.out.println(n + "\n");
			}
		} else {
			System.out.println("no such key\n");
		}
	}

	/**
	 * Displays and removes the front value.
	 *
	 * @param key the key of the entry
	 */
	private void pop(String key) {
		Entry entr = getEntry(key);
		if (entr == null){
			System.out.println("no such key\n");
		}
		else{
			Integer n = entr.pop();
			if (n == null){
				System.out.println("nil\n");
			} 
			else {
				System.out.println(n + "\n");
			}
		}
	}
	
	private Snapshot getSnapshot(int id){
		if (snapshots.size() == 0){
			return null;
		}
		for (int i = 0; i < snapshots.size(); i++){
			Snapshot each_snap = snapshots.get(i);
			if (each_snap.getID() == id){
				return each_snap;
			}
		}
		return null; 
	}

	/** 
	 * Deletes snapshot.
	 *
	 * @param id the id of the snapshot
	 */
	private void drop(int id) {
		Snapshot snap = getSnapshot(id);
		if (snap != null){
			List<Snapshot> new_ls = new ArrayList<Snapshot>();
			for(int i = 0; i < snapshots.size(); i++){
				if (snapshots.get(i).equals(snap)){
					continue;
				}
				new_ls.add(snapshots.get(i));
			}
			snapshots = new_ls;
			System.out.println("ok\n");
			
		} else{
			System.out.println("no such snapshot\n");
		}
	}

	/**
	 * Restores to snapshot and deletes newer snapshots.
	 *
	 * @param id the id of the snapshot
	 */
	private void rollback(int id) {
		Snapshot snap = getSnapshot(id);
		if (snap != null){
			List<Entry> rolled_ls = snap.rollback();
			entries = rolled_ls;
			
			List<Snapshot> new_snap = new ArrayList<Snapshot>();
			for (Snapshot each_snap : snapshots){
				if (each_snap.getID() > id){
					continue;
				}
				new_snap.add(each_snap);
			}
			snapshots = new_snap;
		System.out.println("ok\n");
		} else {
			System.out.println("no such snapshot\n");
		}
	}

	/** 
	 * Replaces current state with a copy of snapshot.
	 *
	 * @param id the id of the snapshot
	 */
	private void checkout(int id) {
		Snapshot snap = getSnapshot(id);
		if (snap != null){
			List<Entry> new_state = snap.getEntryls();
			this.entries = new_state;
			System.out.println("ok\n");
		} else {
			System.out.println("no such snapshot\n");
		}
		
	}

	/** 
	 * Saves the current state as a snapshot.
	 */
	private void snapshot() {
		int id = snapshots.size()+1;
		
		//create a deep copy of the entry list 
		List<Entry> clone_ls = new ArrayList<Entry>();
		for (int i = 0; i < entries.size(); i++) {
			List<Integer> val_ls = entries.get(i).getValues();
			String key = entries.get(i).getKey();
			List<Integer> new_val_ls = new ArrayList<Integer>();
			for (Integer n : val_ls){
				Integer x = n;
				new_val_ls.add(x);
			}
			Entry new_entry = new Entry(key, new_val_ls);
			clone_ls.add(new_entry);
		}
		Snapshot new_snap = new Snapshot(id, clone_ls);
		snapshots.add(new_snap);
		System.out.println("saved as snapshot " + id + "\n");
	}

	/**
	 * Saves snapshot to file.
	 *
	 * @param id       the id of the snapshot 
	 * @param filename the name of the file
	 */
	private void archive(int id, String filename) {
		Snapshot snap = getSnapshot(id); 
		if (snap == null) {
			System.out.println("no such snapshot\n");
		}
		else {
			snap.archive(filename);
			System.out.println("ok\n");
		}
	}

	/**
	 * Loads and restores snapshot from file.
	 *
	 * @param filename the name of the file
	 */
	private void restore(String filename) {
		List<Entry> restore_list = Snapshot.restore(filename);
		if (!restore_list.equals(null)){
			List<Snapshot> new_snap = new ArrayList<Snapshot>();
			snapshots = new_snap;
			this.entries = restore_list;
			System.out.println("ok\n");
		}
	}

	/**
	 * Displays minimum value.
	 *
	 * @param key the key of the entry
	 */
	private void min(String key) {
		Entry entr = getEntry(key);
		if (entr != null){
			Integer l = entr.len();
			if (l == 0){
				System.out.println("nil\n");
			}
			else{
				Integer n = entr.min();
				System.out.println(n + "\n");
			}
		} else {
			System.out.println("no such key\n");
		}
	}

	/**
	 * Displays maximum value.
	 *
	 * @param key the of the entry
	 */
	private void max(String key) {
		Entry entr = getEntry(key);
		if (entr != null){
			Integer n = entr.max();
			System.out.println(n + "\n");
	 	} else {
			System.out.println("no such key\n");
		}
	}

	/**
	 * Displays the sum of values.
	 *
	 * @param key the key of the entry
	 */
	private void sum(String key) {
		Entry entr = getEntry(key);
		if (entr != null){
			Integer n = entr.sum();
			System.out.println(n + "\n");
	 	} else{
			System.out.println("no such key\n");
		}
	}

	/**
	 * Displays the number of values.
	 *
	 * @param key the key of the entry
	 */
	private void len(String key) {
		Entry entr = getEntry(key);
		if (entr != null){
			Integer n = entr.len();
			System.out.println(n + "\n");
		} else {
			System.out.println("no such key\n");
		}
	}

	/**
	 * Reverses the order of values.
	 *
	 * @param key the key of the entry
	 */
	private void rev(String key) {
		Entry entr = getEntry(key);
		if (entr != null) {
			entr.rev();
			System.out.println("ok\n");
		} else {
			System.out.println("no such key\n");
		}
	}

	/**
	 * Removes repeated adjacent values.
	 *
	 * @param key the key of the entry
	 */
	private void uniq(String key) {
		Entry entr = getEntry(key);
		if (entr != null) {
			entr.uniq();
			System.out.println("ok\n");
		} else{
			System.out.println("no such key\n");
		}
	}

	/** 
	 * Sorts values in ascending order.
	 *
	 * @param key the key of the entry
	 */
	private void sort(String key) {
		Entry entr = getEntry(key);
		if (entr != null) {
			entr.sort();
			System.out.println("ok\n");
		} else{
			System.out.println("no such key\n");
		}
	}
	
	//for displaying lists
	public static String display(List<Integer> ls) {
		String vals = "";
		if (ls.size() > 0){
			vals += ls.get(0);
			for (int i = 1; i < ls.size(); i++){
				vals += " " + ls.get(i);
			}
		}
		return "[" + vals + "]";
	}
	
	public static Integer minVal(List<Integer> ls){
		Integer min_val = ls.get(0);
		int i = 0;
		while (i < ls.size()){
			if (ls.get(i) < min_val){
				min_val = ls.get(i);
			}
			i++;
		}
		return min_val;
	}
	
	public static List<Integer> sortList(List<Integer> ls){	
		List<Integer> sort_ls = new ArrayList<Integer>();
		while (ls.size() > 0){
			Integer lowest = minVal(ls);
			sort_ls.add(lowest);
			int index = ls.indexOf(lowest);
			List<Integer> temp_ls = new ArrayList<Integer>();
			for(int i = 0; i < ls.size(); i++){
					if (i == index){
						continue;
					}
				temp_ls.add(ls.get(i));
			}
			ls = temp_ls;
		}
		return sort_ls;
	}

	/**
	 * Displays set difference of values in keys. 
	 * Needs at least two keys.
	 *
	 * @param keys the keys of the entries
	 */
	private void diff(List<String> keys) {
		List<Entry> entries = new ArrayList<Entry>();
		for (String key : keys){
			Entry entr = getEntry(key);
			entries.add(entr);
		}
		List<Integer> diff_result = sortList(Entry.diff(entries));
		String diff_display = display(diff_result);
		System.out.println(diff_display + "\n");
	}

	/**
	 * Displays set intersection of values in keys.
	 * Needs at least two keys.
	 *
	 * @param keys the keys of the entries
	 */
	private void inter(List<String> keys) {
		List<Entry> entries = new ArrayList<Entry>();
		for (String key : keys){
			Entry entr = getEntry(key);
			entries.add(entr);
		}
		List<Integer> inter_result = sortList(Entry.inter(entries));
		String inter_display = display(inter_result);
		System.out.println(inter_display + "\n");
	 }
	
	/**
	 * Displays set union of values in keys.
	 * Needs at least two keys.
	 *
	 * @param keys the keys of the entries
	 */
	private void union(List<String> keys) {
		List<Entry> entries = new ArrayList<Entry>();
		for (String key : keys){
			Entry entr = getEntry(key);
			entries.add(entr);
		}
		List<Integer> union_result = sortList(Entry.union(entries));
		String union_display = display(union_result);
		System.out.println(union_display + "\n");
	}

		
	/** 
	 * Displays cartesian product of the sets.
	 * Needs at least two keys.
	 *
	 * @param keys the keys of the entries
	 */
	private void cartprod(List<String> keys) {
		List<Entry> entry_ls = new ArrayList<Entry>();
		for(String k : keys){
			entry_ls.add(getEntry(k));
		}
		List<List<Integer>> result = Entry.cartprod(entry_ls);
		String vals = "";
		vals += display(result.get(0));
		int i = 1;
		while (i < result.size()){
			vals += " " + display(result.get(i));
			i += 1;
		}
		String display_str = "[ " + vals + " ]";
			
		System.out.println(display_str + "\n");
	}

	private static final String HELP =
		"BYE   clear database and exit\n"+
		"HELP  display this help message\n"+
		"\n"+
		"LIST KEYS       displays all keys in current state\n"+
		"LIST ENTRIES    displays all entries in current state\n"+
		"LIST SNAPSHOTS  displays all snapshots in the database\n"+
		"\n"+
		"GET <key>    displays entry values\n"+
		"DEL <key>    deletes entry from current state\n"+
		"PURGE <key>  deletes entry from current state and snapshots\n"+
		"\n"+
		"SET <key> <value ...>     sets entry values\n"+
		"PUSH <key> <value ...>    pushes values to the front\n"+
		"APPEND <key> <value ...>  appends values to the back\n"+
		"\n"+
		"PICK <key> <index>   displays value at index\n"+
		"PLUCK <key> <index>  displays and removes value at index\n"+
		"POP <key>            displays and removes the front value\n"+
		"\n"+
		"DROP <id>      deletes snapshot\n"+
		"ROLLBACK <id>  restores to snapshot and deletes newer snapshots\n"+
		"CHECKOUT <id>  replaces current state with a copy of snapshot\n"+
		"SNAPSHOT       saves the current state as a snapshot\n"+
		"\n"+
		"ARCHIVE <id> <filename> saves snapshot to file\n"+
		"RESTORE <filename> loads snapshot from file\n"+
		"\n"+
		"MIN <key>  displays minimum value\n"+
		"MAX <key>  displays maximum value\n"+
		"SUM <key>  displays sum of values\n"+
		"LEN <key>  displays number of values\n"+
		"\n"+
		"REV <key>   reverses order of values\n"+
		"UNIQ <key>  removes repeated adjacent values\n"+
		"SORT <key>  sorts values in ascending order\n"+
		"\n"+
		"DIFF <key> <key ...>   displays set difference of values in keys\n"+
		"INTER <key> <key ...>  displays set intersection of values in keys\n"+
		"UNION <key> <key ...>  displays set union of values in keys\n"+
		"CARTPROD <key> <key ...>  displays set union of values in keys";
	
	public static void bye() {
		System.out.println("bye");
	}
	
	public static void help() {
		System.out.println(HELP);
	}
	
	public static void main(String[] args) {
		CrunchDB db = new CrunchDB();
		Scanner scan = new Scanner(System.in);
		while(true){
			System.out.print("> ");
			String s = scan.nextLine();
			String[] data = s.split(" ");
			String command = data[0].toLowerCase();
			if (data.length == 1){
				if (command.equals("bye")){
					bye();
					break;
				}
				else if (command.equals("help")){
					help();
					System.out.println();
				}
				else if (command.equals("snapshot")){
					db.snapshot();
				}
				else{
					System.out.println();
				}
			}
			else if (data.length >= 2){
				//List<String> command_lskey = new ArrayList<>(List.of("INTER", "UNION", "DIFF", "CARTPROD"));
				if (command.equals("inter") || command.equals("union") || command.equals("diff") || command.equals("diff") || command.equals("cartprod")){
					List<String> key_ls = new ArrayList<String>();
					for (int i = 1; i < data.length; i++){
						key_ls.add(data[i]);
					}
					if (command.equals("inter")){
						db.inter(key_ls);
					}
					else if (command.equals("union")){
						db.union(key_ls);
					}
					else if (command.equals("diff")){
						db.diff(key_ls);
					}
					else if (command.equals("inter")){
						db.diff(key_ls);
					}
					else if (command.equals("cartprod")){
						db.cartprod(key_ls);
					}
					
				}
				else{
					if (data.length == 2){
						if (command.equals("rollback") || command.equals("checkout") || (command.equals("drop"))){
							int id = Integer.parseInt(data[1]);
							//List<String> command_id = new ArrayList<>(List.of("ROLLBACK", "CHECKOUT", "DROP"));
							if (command.equals("rollback")){
								db.rollback(id);
							}
							else if (command.equals("checkout")){
								db.checkout(id);
							}
							else if (command.equals("drop")){
								db.drop(id);
							}
						}
						else{
							String key = data[1];
							//List<String> command_key = new ArrayList<>(List.of("MIN", "MAX", "LEN", "SUM", "UNIQ", "REV", "LIST", "PURGE", "GET", "DEL"));
							if (command.equals("list")){
								if (key.toLowerCase().equals("keys")){
									db.listKeys();
								} else if (key.toLowerCase().equals("entries")){
									db.listEntries();
								} else if (key.toLowerCase().equals("snapshots")){
									db.listSnapshot();
								}
							}
							else if (command.equals("restore")){
								db.restore(key); //key here is the file name
							}
							else if (command.equals("min")){
								db.min(key);
							}
							else if (command.equals("max")){
								db.max(key);
							}
							else if (command.equals("len")){
								db.len(key);
							}
							else if (command.equals("sum")){
								db.sum(key);
							}
							else if (command.equals("uniq")){
								db.uniq(key);
							}
							else if (command.equals("sort")){
								db.sort(key);
							}
							else if (command.equals("rev")){
								db.rev(key);
							}
							else if (command.equals("purge")){
								db.purge(key);
							}
							else if (command.equals("get")){
								db.get(key);
							}
							else if (command.equals("del")){
								db.del(key);
							}
							else if (command.equals("pop")){
								db.pop(key);
							}
							else{
								System.out.println();
							}
						}
					}
					else if (data.length > 2){
						//List<String> command_lsint = new ArrayList<>(List.of("PICK", "PLUCK", "POP", "PUSH", "APPEND"));		
						if (command.equals("archive")){
							int id = Integer.parseInt(data[1]);
							String filename = data[2];
							db.archive(id, filename);
						}
						else {
							String key2 = data[1];
							int index = Integer.parseInt(data[2]);
							if (command.equals("pick")){
								db.pick(key2, index);
							}
							else if (command.equals("pluck")){
								db.pluck(key2, index);
							}
							else{
								List<Integer> ls = new ArrayList<Integer>();
								for (int i = 2; i < data.length; i++){
									ls.add(Integer.valueOf(data[i]));
								}
								if (command.equals("push")){
									db.push(key2, ls);
								}
								else if(command.equals("append")){
									db.append(key2, ls);
								}
								else if(command.equals("set")){
									db.set(key2, ls);
								}
								else{
									System.out.println();
								}
							}
						}
					}
				}
			}
		}
	}
}
