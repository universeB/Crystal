package crystal.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import difflib.Delta;
import difflib.DiffRow;
import difflib.DiffRowGenerator;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.PatchFailedException;

public class testMain {
	public static class ChangeItem {
		ArrayList<String> changedFiles;
		ArrayList<String> changedFilesContents;

		ChangeItem() {
			changedFiles = new ArrayList<String>();
			changedFilesContents = new ArrayList<String>();
		}
	}

	private static List<String> fileToLines(String filename) {
		List<String> lines = new ArrayList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;

	}

	void parseFile(String myFile, String urFile) throws PatchFailedException {

		List<String> myFileLines = fileToLines(myFile);
		List<String> urFileLines = fileToLines(urFile);

		DiffRowGenerator.Builder builder = new DiffRowGenerator.Builder();
		builder.showInlineDiffs(false);
		DiffRowGenerator dfg = builder.build();
		List<DiffRow> rows = dfg.generateDiffRows(myFileLines, urFileLines);

		String contentLines = "";

		for (DiffRow dr : rows) {
			if (dr.getTag() == DiffRow.Tag.DELETE)
//				System.out.println("-\t" + dr.getOldLine());
				contentLines = contentLines + "-\t"+dr.getOldLine()+"\n";
			else if (dr.getTag() == DiffRow.Tag.INSERT)
//				System.out.println("+\t" + dr.getNewLine());
				contentLines = contentLines + "+\t"+dr.getNewLine()+"\n";
			else if (dr.getTag() == DiffRow.Tag.CHANGE){
//				System.out.println("+-\t" + dr.getNewLine());
				contentLines = contentLines + "+\t"+dr.getNewLine()+"\n";
				contentLines = contentLines + "-\t"+dr.getOldLine()+"\n";
			}
		}
		
		System.out.println(contentLines);
		System.out.println(rows);
	}

	testMain() throws PatchFailedException {
		parseFile("tmplog", "tmplog2");
	}

	public static void main(String[] args) throws PatchFailedException {
		new testMain();
	}

}
