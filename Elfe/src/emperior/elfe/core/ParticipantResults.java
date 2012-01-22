package emperior.elfe.core;

import java.util.ArrayList;

public class ParticipantResults {
	public String getParticipantName() {
		return participantName;
	}

	public ArrayList<AllTaskResults> getResultsForAllTasks() {
		return resultsForAllTasks;
	}

	private String participantName;
	private ArrayList<AllTaskResults> resultsForAllTasks;
	
	public ParticipantResults(String participantName, ArrayList<AllTaskResults> resultsForAllTasks) {
		this.participantName = participantName;
		this.resultsForAllTasks = resultsForAllTasks;
	}
}
