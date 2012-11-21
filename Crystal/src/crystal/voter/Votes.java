package crystal.voter;

import java.io.File;
import java.util.Vector;

import crystal.client.ProjectPreferences;

public class Votes {
	static ProjectPreferences _pref;
	static String voteBoxLocalDir;

	public static void startVotingProcess(Vector<VotingChoice> options) {
		if(voteBoxLocalDir.equals(""))
				voteBoxLocalDir = newLocalVoteBox();
		
		String ID = options.get(0).toString();
		File vote = new File("voteBoxLocalDir" + ID);
		
		
		
	}

	private static String newLocalVoteBox() {
		File voteDir = new File(_pref.getClientPreferences().getGitPath()+"votes");
		return voteDir.getAbsolutePath();
	}
	
}
