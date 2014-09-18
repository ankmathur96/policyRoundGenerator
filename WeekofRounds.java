import java.io.Serializable;
import java.util.ArrayList;


public class WeekofRounds implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<Round> mondayroundList;
	public ArrayList<Round> tuesdayroundList;
	public ArrayList<Round> wednesdayroundList;
	public ArrayList<Round> thursdayroundList;
	public ArrayList<Round> fridayroundList; 
	public String week;
	public ArrayList<Team> teamList; 
	
	public WeekofRounds(){}
	public WeekofRounds (ArrayList<Round> mondayrounds, ArrayList<Round> tuesdayrounds, ArrayList<Round> wedrounds, ArrayList<Round> thursrounds, ArrayList<Round> frirounds, String roundweek, ArrayList<Team> teamlist){
		mondayroundList = mondayrounds;
		tuesdayroundList = tuesdayrounds;
		wednesdayroundList = wedrounds;
		thursdayroundList = thursrounds;
		fridayroundList = frirounds;
		week = roundweek;
		teamList = teamlist;
	}
	public String toString(){
		String newline = String.format("%n");
		String toreturn = newline + newline + "Round Schedule for " + week + newline + newline;
		
		toreturn += "Monday: " + newline;
		for (Round rd :mondayroundList){
			if ((rd.affirmative == null) && (rd.negative == null))
				toreturn += "EMPTYTEAM vs. EMPTYTEAM - !!!!!! THIS IS ANOMALOUS." + newline;
			else if ((rd.affirmative == null) && (rd.negative != null))
				toreturn += "EMPTYTEAM vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative == null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. EMPTYTEAM" + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative != null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
		}
		toreturn += newline + "Tuesday:" + newline;
		for (Round rd :tuesdayroundList){
			if ((rd.affirmative == null) && (rd.negative == null))
				toreturn += "EMPTYTEAM vs. EMPTYTEAM - !!!!!! THIS IS ANOMALOUS." + newline;
			else if ((rd.affirmative == null) && (rd.negative != null))
				toreturn += "EMPTYTEAM vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative == null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. EMPTYTEAM" + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative != null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message+ newline;
		}
		
		toreturn += newline + "Wednesday:" + newline;
		for (Round rd :wednesdayroundList){
			if ((rd.affirmative == null) && (rd.negative == null))
				toreturn += "EMPTYTEAM vs. EMPTYTEAM - !!!!!! THIS IS ANOMALOUS." + newline;
			else if ((rd.affirmative == null) && (rd.negative != null))
				toreturn += "EMPTYTEAM vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative == null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. EMPTYTEAM" + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative != null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
		}
		
		toreturn += newline + "Thursday:" + newline;
		for (Round rd :thursdayroundList){
			if ((rd.affirmative == null) && (rd.negative == null))
				toreturn += "EMPTYTEAM vs. EMPTYTEAM - !!!!!! THIS IS ANOMALOUS." + newline;
			else if ((rd.affirmative == null) && (rd.negative != null))
				toreturn += "EMPTYTEAM vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative == null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. EMPTYTEAM" + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative != null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
		}
		
		toreturn += newline + "Friday:" + newline;
		for (Round rd :fridayroundList){
			if ((rd.affirmative == null) && (rd.negative == null))
				toreturn += "EMPTYTEAM vs. EMPTYTEAM - !!!!!! THIS IS ANOMALOUS." + newline;
			else if ((rd.affirmative == null) && (rd.negative != null))
				toreturn += "EMPTYTEAM vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative == null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. EMPTYTEAM" + rd.message + newline;
			else if ((rd.affirmative != null) && (rd.negative != null))
				toreturn += rd.affirmative.partner1 + "/" + rd.affirmative.partner2 + " vs. " + rd.negative.partner1 + "/" + rd.negative.partner2 + rd. message + newline;
		}
		
		return toreturn;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
