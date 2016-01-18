package ru.vm.labs.loganayzertool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.attribute.BasicFileAttributes;

public class Analyzer {
		
	private static File report = null;
	
	private static String REPORT_NAME = "error_report.txt";

	public static void main(String[] args) throws IOException {
		
		if(args==null || args.length==0)
			throw new IllegalArgumentException("\nPlease set folderWithLogs input argument: \"-f <your folder where logs lay.>\"\n");
		
		String folderWithLogs = "";
		
		for(int j=0; j < args.length; j++)
		{
			String argument = args[j];
			
			if(argument.startsWith("-f"))
			{
				folderWithLogs = argument.replaceFirst("-f", "").trim();				
			}
		}
		
		System.out.println("Starting to analyze folder " + folderWithLogs + "\n");
		 
		
		//try to create report file
		report = new File(System.getProperty("user.dir"),REPORT_NAME);
		
		if(report.exists())
		{
			//empty the file content
			FileOutputStream writer = new FileOutputStream(report, false);
			writer.write((new String()).getBytes());
			writer.close();
		}
		else
		{
			report.createNewFile();
		}
		
		Files.walkFileTree(Paths.get(folderWithLogs), new FileVisitor<Path>() {

			// This method is called for each file visited. The basic attributes
			// of the files are also available.
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				// DEBUG full file path
				//System.out.println(file.toAbsolutePath());

				String fileErrors = getFileErrors(file.toAbsolutePath().toString());	
				
				if(fileErrors!=null)
				{
					//now add content to the report
					FileOutputStream writer = new FileOutputStream(report, true /*append content*/);
					writer.write(fileErrors.getBytes());
					writer.close();
				}

				return FileVisitResult.CONTINUE;
			}

			// if the file visit fails for any reason, the visitFileFailed
			// method is called.
			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc)
					throws IOException {
				System.out.println(exc.toString());
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path arg0,
					IOException arg1) throws IOException {

				return FileVisitResult.CONTINUE;
			}
		});
		
		
		System.out.println("Finished to analyze folder " + folderWithLogs + "\n");
		System.out.println("If no errors occured please find report at " + report.getAbsolutePath() + "\n");
				
	}

	private static String getFileErrors(String fileToAnalyze) throws IOException {
		
		StringBuilder fileErrors = new StringBuilder();
				
		Path path = Paths.get(fileToAnalyze);
		
		boolean pathExists = Files.exists(path,
				new LinkOption[] { LinkOption.NOFOLLOW_LINKS });

		if (pathExists) {
			System.out.println("start working...");
			fileErrors.append("!!!!!!! ERRORS FROM " + fileToAnalyze.toUpperCase() + ": \n");

			String content = new String(Files.readAllBytes(path));
			
			//optimyze search: check if the content contains template words first
			//if not, just return
			if(!Pattern.compile("(exception)|(Exception)").matcher(content).find())
				return null;

			// want to find all blocks starting with something like 04:38:39,981 or 2016-01-13 20:02:25,904
			Pattern p = Pattern.compile("(\\d{2}:\\d{2}:\\d{2},\\d{3}\\s{1})|(\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2},\\d{3}\\s{1})");
			Matcher m = p.matcher(content);

			int groupId = 0;
			int startIndx = 0;
			int endIndx = 0;
			
			String blocks[] = p.split(content);
									
			while (m.find()) {
				//System.out.println("Found a " + m.group() + "; groupId = " + groupId);
				System.out.println("processing " + fileToAnalyze + ": string block #" + (groupId+1) + " from " + blocks.length);
												
				// that is where the string should start
				if (groupId == 1) {
					endIndx = content.indexOf(m.group());
				}
				if (groupId > 1) {
					startIndx = endIndx;
					endIndx = content.indexOf(m.group());
				}

				// get message substring
				String logMessage = content.substring(startIndx, endIndx);
				
				//Pattern.compile("Exception").matcher(logMessage).find()

				// if the message has key word then print it
				if (Pattern.compile("(exception)|(Exception)").matcher(logMessage).find()) {
					
					//we don't need to handle this some phrases
					if(!logMessage.contains("Exception sending notification"))
					{
						fileErrors.append(logMessage);
					}
					
				}

				groupId++;

			}

			System.out.println("end working...");
			
			return fileErrors.append("\n\n").toString();
		}
		else
			return null;
	}

}
