package crystal.client;

import java.io.IOException;
import java.util.ArrayList;

import crystal.util.RunIt;
import crystal.util.RunIt.Output;

public class CalculateChangeTask {

	public static class changeItem {
		String _commitDate;
		String _committer;
		String _commitID;
		ArrayList<String> changedFiles;
		ArrayList<String> changedFilesContents;

		changeItem(String commitID, String commitDate, String committer) {
			_commitID = commitID;
			_commitDate = commitDate;
			_committer = committer;
			changedFiles = new ArrayList<String>();
			changedFilesContents = new ArrayList<String>();
		}
	}

	public static ArrayList<changeItem> performTask(
			ProjectPreferences _prefs) throws IOException {

		Output exeResult;
		ClientPreferences myClientPrefs = _prefs.getClientPreferences();

		String[] gitAccountArgs = { "config", "user.name", };
		exeResult = RunIt.execute(myClientPrefs.getGitPath(), gitAccountArgs,
				myClientPrefs.getTempDirectory(), false);
		String localAcc = exeResult.getOutput();

		// git log --committer=aurora -1 --pretty=format:%cD
		String[] gitCommitterArgs = { "log", "--committer=" + localAcc, "-1",
				"--pretty=format:%ci" };
		exeResult = RunIt.execute(myClientPrefs.getGitPath(), gitCommitterArgs,
				myClientPrefs.getTempDirectory(), false);
		String sinceCommitDate = exeResult.getOutput();

		String[] gitFilesArgs = { "log", "--since", sinceCommitDate,
				"--pretty=format:\"commit:%H;%ci;%cn\"", "-p" };
		exeResult = RunIt.execute(myClientPrefs.getGitPath(), gitFilesArgs,
				myClientPrefs.getTempDirectory(), false);

		ArrayList<changeItem> longList = new ArrayList<changeItem>();

		if (!exeResult.getOutput().equals("")) {

			changeItem oneCommit;
			String[] stCommit = exeResult.getOutput().split("commit:");

			for (String oneCommitStr : stCommit) {
				if (oneCommitStr.equals(""))
					continue;

				String[] oneCommitLines = oneCommitStr.split("\n");
				
				String[] headLine = oneCommitLines[0].split(";");
				oneCommit = new changeItem(headLine[0],
						headLine[1], headLine[2]);
				
				for (int i = 1; i < oneCommitLines.length; i++) {

					if (oneCommitLines[i].startsWith("+++ b")) {
						oneCommit.changedFiles.add(oneCommitLines[i]
								.substring(5));
						
						String tmpChangeFileString = "";
						
						int k;
						for (k = i + 1; k < oneCommitLines.length; k++)
							tmpChangeFileString = tmpChangeFileString
									+ oneCommitLines[k] + "\n";
						
						oneCommit.changedFilesContents.add(tmpChangeFileString);
						System.out.println(tmpChangeFileString);
						i = k-1;
					}
				}
				longList.add(oneCommit);
			}
		}
		return longList;
	}

}
