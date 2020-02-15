import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Application {
	private static final float dampingFactor = 0.15f;
	private static Reader reader;
	private static ArrayList<ArrayList<Integer>> outLinks;
	private static ArrayList<Float> PageRanks = new ArrayList<Float>();
	private static int N;
	private static float criterion = .000001f;
	private static String inputFileName = "linkgraph";
	private static String outputFileName;
	private static float fact;

	private static int calculatePageRank() {
		int i = 1;
		ArrayList<Float> tempPR = new ArrayList<Float>();
		while (true) {
			System.out.println("iteration " + i);
			float temp = 0.0f;
			tempPR.clear();
			for (int j = 0; j < N; j++) {
				tempPR.add(fact);
			}
			for (int j = 0; j < N; j++) {
				float pageRank = PageRanks.get(j);
				ArrayList<Integer> links = outLinks.get(j);
				if (links.size() == 0) {
					temp += (1 - dampingFactor) * (pageRank / N);
				}
			}
			for (int j = 0; j < N; j++) {
				float pageRank = PageRanks.get(j);
				ArrayList<Integer> links = outLinks.get(j);
				if (temp != 0.0f) {
					tempPR.set(j, tempPR.get(j) + temp);
				}
				if (links.size() > 0) {
					int outDegree = links.size();
					for (int lnk : links) {
						float val = tempPR.get(lnk) + (1 - dampingFactor)
								* (pageRank / outDegree);
						tempPR.set(lnk, val);
					}
				}
			}
			double sum = updateRanks(tempPR);
			if (sum < criterion)
				break;
			i++;
		}

		return i;
	}

	private static float updateRanks(ArrayList<Float> newRanks) {
		float sum = 0.0f;
		for (int i = 0; i < N; i++) {
			sum += Math.abs(PageRanks.get(i) - newRanks.get(i));
			PageRanks.set(i, newRanks.get(i));
		}
		return sum;
	}

	private static void initializePageRank() {
		float init = 1.0f / N;
		for (int j = 0; j < N; j++) {
			PageRanks.add(init);
		}
	}

	private static void writeToFile() {
		if (outputFileName == null) {
			outputFileName = inputFileName + ".result";
		}
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter(outputFileName)))) {
			for (int i = 0; i < N; i++) {
				if (reader.getMap().containsKey(i)) {
					out.println(i + "\t"
							+ PageRanks.get(reader.getMap().get(i)));
				}
			}
		} catch (IOException e) {
			System.out.println("Can not write to a file");
		}
	}

	public static void main(String[] args) {
		inputFileName = args[0];
		outputFileName = args[1];
		System.out.println("Data is loading");
		long startTime = System.currentTimeMillis();
		reader = new Reader(inputFileName);
		long endTime = System.currentTimeMillis();
		long dataLoadingTime = endTime - startTime;
		System.out
				.println("Data Loaded in " + dataLoadingTime + " mili secods");
		outLinks = reader.getAdjacencyList();
		N = outLinks.size();
		fact = dampingFactor / N;
		initializePageRank();
		System.out.println("Calculating Page Ranks");
		startTime = System.currentTimeMillis();
		int iterationCount = calculatePageRank();
		endTime = System.currentTimeMillis();
		long pageRankCalculationTime = endTime - startTime;
		long loopTimeavg = pageRankCalculationTime / iterationCount;
		System.out.println("PageRank calculated in " + pageRankCalculationTime
				+ " mili secods. Each loop took " + loopTimeavg
				+ " mili seconds");
		startTime = System.currentTimeMillis();
		writeToFile();
		endTime = System.currentTimeMillis();
		long fileWriteTime = endTime - startTime;
		System.out.println("Writing in file took " + fileWriteTime
				+ " mili secods");
		System.out.println("Finished in "
				+ (dataLoadingTime + pageRankCalculationTime + fileWriteTime)
				+ " mili seconds. Check the output file");
	}

}
