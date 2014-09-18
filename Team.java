import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class Team implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String partner1;
	public String partner2;
	public boolean hasgoneaff;
	public boolean hasgoneneg;
	public HashSet<Team> historyAffSet; //defines who the team has BEEN NEG AGAINST. Teams that have been aff against this team.
	public HashSet<Team> historyNegSet; //defines who the team has BEEN AFF AGAINST. Teams that have been neg against this team.
	public Set<String> daysAvailable;
	public int availablescore;
	public String notes;
	public boolean three_days_exception;
	public boolean three_day_toggle;
	public Team (String member1, String member2, Set<String> daySet){
		partner1 = member1;
		partner2 = member2;
		daysAvailable = daySet;
		availablescore = daysAvailable.size();
		historyAffSet = new HashSet<Team>();
		historyNegSet = new HashSet<Team>();
		three_day_toggle = false;
		if (daySet.size() >= 3)
			three_days_exception = true;
		else {
			three_days_exception = false;
		}
	}
	public Team(){}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
