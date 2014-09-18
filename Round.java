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
		message = "";
	}
	public Round (){}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
