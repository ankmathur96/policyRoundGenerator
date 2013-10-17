import java.io.Serializable;
import java.util.Set;


public class Round implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Team affirmative;
	public Team negative;
	public boolean happenedBefore;
	public String day;
	public String message;
	public Round (Team aff, Team neg){
		affirmative = aff;
		negative = neg;
	}
	public Round (){}
	/*private boolean isUnique(){
		boolean unique = true;
		Set<Team> affSet = affirmative.historySet;
		for (Team t_aff :affSet){
			if (t_aff == negative)
				unique = false;
		}
		return unique;
	}*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
