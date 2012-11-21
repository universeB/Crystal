package crystal.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import crystal.util.RunIt;
import crystal.util.RunIt.Output;

public class CalculateChangeTask {

	public static class changeItem {
		String _commitDate;
		String _committer;
		String _commitID;
		String[] changedFiles;

		changeItem(String commitID, String commitDate, String committer) {
			_commitID = commitID;
			_commitDate = commitDate;
			_committer = committer;
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
				"--pretty=format:%H;%ci;%cn", "--name-only" };
		exeResult = RunIt.execute(myClientPrefs.getGitPath(), gitFilesArgs,
				myClientPrefs.getTempDirectory(), false);

		ArrayList<changeItem> longList = new ArrayList<changeItem>();

		if (!exeResult.getOutput().equals("")) {

			StringTokenizer stCommit = new StringTokenizer(
					exeResult.getOutput(), "\n\n");
			while (stCommit.hasMoreTokens()) {
				String oneCommitStr = stCommit.nextToken();

				String[] oneCommitLines = oneCommitStr.split("\n");
				String[] oneCommitHeadItem = oneCommitLines[0].split(";");
				changeItem oneCommit = new changeItem(oneCommitHeadItem[0], oneCommitHeadItem[1],
						oneCommitHeadItem[2]);

				oneCommit.changedFiles = Arrays.copyOfRange(oneCommitLines, 1,
						oneCommitLines.length);
				longList.add(oneCommit);
			}
		}
		return longList;
	}

}
