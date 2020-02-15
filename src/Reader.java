import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
	private String linkGraphFileName;
	private ArrayList<ArrayList<Integer>> outLinks = new ArrayList<ArrayList<Integer>>();
	private HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

	public Reader(String linkGraphFileName) {
		this.linkGraphFileName = linkGraphFileName;
		constructEdgeList();
	}

	private void constructEdgeList() {
		File file = new File(linkGraphFileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String aLine;
			String[] inputs;
			int from = 0;
			int to = 0;
			int id = 0;
			while ((aLine = br.readLine()) != null) {
				inputs = aLine.split("\t");
				from = Integer.parseInt(inputs[0]);
				to = Integer.parseInt(inputs[1]);
				if (map.containsKey(from)) {
					from = map.get(from);
				} else {
					map.put(from, id);
					from = id++;
					outLinks.add(from, new ArrayList<Integer>());
				}
				if (map.containsKey(to)) {
					to = map.get(to);
				} else {
					map.put(to, id);
					to = id++;
					outLinks.add(to, new ArrayList<Integer>());
				}
				outLinks.get(from).add(to);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found");
		} catch (IOException e) {
			System.out.println("Input File not found");
		}
	}

	public ArrayList<ArrayList<Integer>> getAdjacencyList() {
		return outLinks;
	}

	public HashMap<Integer, Integer> getMap() {
		return this.map;
	}
}
