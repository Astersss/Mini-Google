package indexer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class FileCollector {
	private String inputDirectory;
	private File outputFile;
	private BufferedWriter bufferedWriter;
	private FileWriter fileWriter;

	public FileCollector(String inputDirectory, String outputFile)
			throws IOException {
		this.inputDirectory = inputDirectory;
		this.outputFile = new File(outputFile);
		this.outputFile.createNewFile();
		fileWriter = new FileWriter(outputFile, true);
		bufferedWriter = new BufferedWriter(fileWriter);
	}

	public void read() throws IOException {
		File inputDir = new File(inputDirectory);
		if (inputDir.exists() && inputDir.isDirectory()) {
			for (File file : inputDir.listFiles()) {
				if (file.isFile() && !file.getName().toString().endsWith("Store")) {
					byte[] encoded = Files.readAllBytes(Paths.get(file
							.getAbsolutePath()));
					write(file.getName().toString() + " " + new String(encoded, "utf-8"));
					write("\n");
				}
			}
		}
	}

	public void write(String content) throws IOException {
		bufferedWriter.write(content);
	}

	public void clean() {
		try {
			if (bufferedWriter != null)
				bufferedWriter.close();

			if (fileWriter != null)
				fileWriter.close();

		} catch (IOException ex) {

			ex.printStackTrace();

		}

	}

	public static void main(String[] args) throws IOException {
		
		
		
		
//		long start = System.currentTimeMillis();
		FileCollector fc = new FileCollector(args[0],args[1]);
//				
		fc.read();
		fc.clean();
//		long end = System.currentTimeMillis();
//		System.out.println((end - start) / 1000);
	}
}