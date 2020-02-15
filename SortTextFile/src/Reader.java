import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Reader {
	public static void sortFile(String fileName) {
		Map<Integer, Float> map = new HashMap<Integer, Float>();
		File file = new File(fileName);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String aLine;
			String[] inputs;
			int id = 0;
			float pageRank = 0.0f;
			while ((aLine = br.readLine()) != null) {
				inputs = aLine.split("\t");
				id = Integer.parseInt(inputs[0]);
				pageRank = Float.parseFloat(inputs[1]);
				map.put(id, pageRank);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input File not found");
		} catch (IOException e) {
			System.out.println("Input File not found");
		}
		Map<Integer, Float> sortedMap = sortByValue(map);
		String outputFileName = fileName + ".Sorted.result";
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(outputFileName)))) {
			for (Map.Entry<Integer, Float> entry : sortedMap.entrySet()) {
				out.println(entry.getKey() + "\t" + entry.getValue());
			}
		} catch (IOException e) {
			System.out.println("Can not write to a file");
		}
	}

	private static Map<Integer, Float> sortByValue(Map<Integer, Float> map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void main(String[] args) {
		Reader.sortFile(args[0]);

	}

}
