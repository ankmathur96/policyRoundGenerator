import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Collections;

public class roundGenerator {
	
	final static int NUMBER_OF_WEEKS_TO_REMEMBER = 3; //number of weeks of history we will keep in account.
	
	/**Reads teamlist in from file and returns ArrayList of fully initialized Team structures.*/
	private static ArrayList<Team> generateTeamList() throws IOException, FileNotFoundException{
		File file = new File (System.getProperty("user.home") + File.separator + "teamlist.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		List<String> teamListfromfile = new ArrayList<String>();
		ArrayList<Team>  teamList = new ArrayList<Team>();
		ArrayList<WeekofRounds> pastweeks = new ArrayList<WeekofRounds>();
		String line = null;
		
		boolean historyread = true;
		try{
			pastweeks = readRoundHistory();
		}
		catch(Exception e){
			historyread = false;
			System.out.println("History reading EXCEPTION");
		}
		//read in String team names from teamlist file.
		while ((line = reader.readLine()) != null)
			teamListfromfile.add(line);
		
		//assign team information and initialize team objects.
		for (String teamline :teamListfromfile){
			Set<String> daysAvailable= new HashSet<String>();
			String team = "";
			String member1 = "";
			String member2 = "";
			String day1 = "";
			String day2 = "";
			String day3 = "";
			String day4 = "";
			int slashindex =0;
			boolean isfirstday = true;
			boolean issecondday = true;
			boolean isthirdday = true;
			boolean isfourthday = true;
			String[] tokens = teamline.split(" ");
			team = tokens[0];
			Pattern slashPattern = Pattern.compile("[/]+");
			Matcher slashMatcher = slashPattern.matcher(team);
			slashMatcher.find();
			try{
				slashindex = slashMatcher.start();
			}
			catch (Exception e){
				slashindex = 0;
				System.out.println("Slash not found. Full team name was added to member2.");
			}
			member1 = team.substring(0, (slashindex));
			member2 = team.substring((slashindex+1));
			
			try{
				day1 = tokens[1];
			}
			catch (Exception e){
				System.out.println("NO FIRST DAY EXCEPTION");
				isfirstday = false;
			}
			
			try{
				day2 = tokens[2];
			}
			catch (Exception e){
				System.out.println("NO SECOND DAY EXCEPTION");
				issecondday = false;
			}
			
			try{
				day3 = tokens[3];
				System.out.println("THIRD DAY FOUND");
			}
			catch (Exception e){
				System.out.println("NO THIRD DAY EXCEPTION");
				isthirdday = false;
			}
			
			try{
				day4 = tokens[4];
				System.out.println("FOURTH DAY FOUND");
			}
			catch (Exception e){
				System.out.println("NO FOURTH DAY EXCEPTION");
				isfourthday = false;
			}
			
			if (isfirstday)
				daysAvailable.add(day1);
			if (issecondday)
				daysAvailable.add(day2);
			if (isthirdday)
				daysAvailable.add(day3);
			if (isfourthday)
				daysAvailable.add(day4);
			
			Team currentTeam = new Team(member1, member2, daysAvailable);
			if (historyread){
				Pair<HashSet<Team>, HashSet<Team>> historyPair = returnHistoryForTeam(currentTeam, pastweeks);
				currentTeam.historyAffSet = historyPair.getFirst();
				currentTeam.historyNegSet = historyPair.getSecond();
			}
			
			teamList.add(currentTeam);
		}
		reader.close();
		return teamList;
	}
	
	/**Reads ArrayList of history rounds from file and returns that list. */
	
	@SuppressWarnings("unchecked")
	private static ArrayList<WeekofRounds> readRoundHistory() throws FileNotFoundException, IOException{
		InputStream inputfile = new FileInputStream(System.getProperty("user.home") + File.separator + "roundhistory.ser");
		InputStream inputbuffer = new BufferedInputStream(inputfile);
		ObjectInput inputmodule = new ObjectInputStream(inputbuffer);
		ArrayList<WeekofRounds> pastweeks = new ArrayList<WeekofRounds>();
		try{
			pastweeks = (ArrayList<WeekofRounds>) inputmodule.readObject();
		}
		catch (Exception e){
			System.out.println("Tried to read in round history but failed to read in object.");
		}
		
		inputmodule.close();
		
		return pastweeks;
	}
	
	/**Assigns team history to a team. */
	private static Pair<HashSet<Team>, HashSet<Team>> returnHistoryForTeam(Team teamtocheck, ArrayList<WeekofRounds> pastweeks){
		HashSet<Team> affSet = new HashSet<Team>();
		HashSet<Team> negSet = new HashSet<Team>();
		
		for (int i=0; i < NUMBER_OF_WEEKS_TO_REMEMBER; i++){
			if (i >= (pastweeks.size()))
				continue;
			WeekofRounds thisweek = pastweeks.get(i);
			
			Pair<Team, String> returnedmodule = new Pair<>();
			returnedmodule = findTeamAndReturnOpponent(teamtocheck, thisweek.mondayroundList);
			if ((returnedmodule.getSecond()).equals("aff"))
				affSet.add(returnedmodule.getFirst());
			if ((returnedmodule.getSecond()).equals("neg"))
				negSet.add(returnedmodule.getFirst());
			
			returnedmodule = findTeamAndReturnOpponent(teamtocheck, thisweek.tuesdayroundList);
			if ((returnedmodule.getSecond()).equals("aff"))
				affSet.add(returnedmodule.getFirst());
			if ((returnedmodule.getSecond()).equals("neg"))
				negSet.add(returnedmodule.getFirst());
			
			returnedmodule = findTeamAndReturnOpponent(teamtocheck, thisweek.wednesdayroundList);
			if ((returnedmodule.getSecond()).equals("aff"))
				affSet.add(returnedmodule.getFirst());
			if ((returnedmodule.getSecond()).equals("neg"))
				negSet.add(returnedmodule.getFirst());
			
			returnedmodule = findTeamAndReturnOpponent(teamtocheck, thisweek.thursdayroundList);
			if ((returnedmodule.getSecond()).equals("aff"))
				affSet.add(returnedmodule.getFirst());
			if ((returnedmodule.getSecond()).equals("neg"))
				negSet.add(returnedmodule.getFirst());
			
			returnedmodule = findTeamAndReturnOpponent(teamtocheck, thisweek.fridayroundList);
			if ((returnedmodule.getSecond()).equals("aff"))
				affSet.add(returnedmodule.getFirst());
			if ((returnedmodule.getSecond()).equals("neg"))
				negSet.add(returnedmodule.getFirst());
		}
		
		//at this stage, affSet (teams that have gone aff against currentTeam) and negSet (vv of affSet) should be populated.
		Pair<HashSet<Team>, HashSet<Team>> returnmodule = new Pair<HashSet<Team>, HashSet<Team>>(affSet, negSet);
		
		return returnmodule;
	}
	/**Given a list of rounds, searches the rounds for a team, returns the team's opponent and side in the Pair structure */
	private static Pair<Team, String> findTeamAndReturnOpponent(Team teamtocheck, ArrayList<Round> dayList){
		Team opponent = new Team();
		String opponentside = "";
		for (Round rd :dayList){
			Team roundnegative = rd.negative;
			Team roundaffirmative = rd.affirmative;
			
			if (teamtocheck.equals(roundaffirmative)){
				opponent = roundnegative;
				opponentside = "neg";
			}
			
			if (teamtocheck.equals(roundnegative)){
				opponent = roundaffirmative;
				opponentside = "aff";
			}
				
		}
		
		Pair<Team, String> returnmodule = new Pair<Team,String>(opponent, opponentside);
		
		return returnmodule;
	}
	/**Rewrites round history by placing current week in first position, previous week in 2nd, week before that in 3rd. */
	private static boolean writeRoundHistory(WeekofRounds thisweek) throws FileNotFoundException, IOException{
		ArrayList<WeekofRounds> pastweeks = new ArrayList<WeekofRounds>();
		boolean failure = false;
		try{
			pastweeks = readRoundHistory();
		}
		catch(Exception e){
			pastweeks.add(thisweek);
			System.out.println("History reading exception.");
			OutputStream file = new FileOutputStream(System.getProperty("user.home") + File.separator + "roundhistory.ser");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput outputmodule = new ObjectOutputStream(buffer);
			outputmodule.writeObject(pastweeks);
			outputmodule.close();
			failure = true;
		}
		if (failure)
			return false;
		if (pastweeks == null)
			return false;
		
		WeekofRounds week1 = new WeekofRounds();
		WeekofRounds week2 = new WeekofRounds();
		if (pastweeks.size() > 0)
			week1 = pastweeks.get(0);
		if (pastweeks.size() > 1)
			week2 = pastweeks.get(1);
		
		pastweeks.add(thisweek);
		pastweeks.add(week1);
		pastweeks.add(week2);
		
		OutputStream file = new FileOutputStream(System.getProperty("user.home") + File.separator + "roundhistory.ser");
		OutputStream buffer = new BufferedOutputStream(file);
		ObjectOutput outputmodule = new ObjectOutputStream(buffer);
		
		try{
			outputmodule.writeObject(pastweeks);
		}
		catch (Exception e){
			System.out.println("Exception writing object.");
			return false;
		}
		finally{
			outputmodule.close();
		}
		
		return true;
	}
	
	/** Sorts teams by which days they are available into 5 daySets*/
	private static ArrayList<ArrayList<Team>> sortTeamsbyDay(ArrayList<Team> teamList) {
		ArrayList<Team> mondayList = new ArrayList<Team>();
		ArrayList<Team> tuesdayList = new ArrayList<Team>();
		ArrayList<Team> wednesdayList = new ArrayList<Team>();
		ArrayList<Team> thursdayList = new ArrayList<Team>();
		ArrayList<Team> fridayList = new ArrayList<Team>();
		
		for (Team tm :teamList){
			Set<String> daysAvailable = tm.daysAvailable;
			for (String day :daysAvailable){
				if (day.equals("M"))
					mondayList.add(tm);
				if (day.equals("Tu"))
					tuesdayList.add(tm);
				if (day.equals("W"))
					wednesdayList.add(tm);
				if (day.equals("Th"))
					thursdayList.add(tm);
				if (day.equals("F"))
					fridayList.add(tm);
			}
		}
		
		ArrayList<ArrayList<Team>> returnmodule = new ArrayList<ArrayList<Team>>();
		returnmodule.add(mondayList);
		returnmodule.add(tuesdayList);
		returnmodule.add(wednesdayList);
		returnmodule.add(thursdayList);
		returnmodule.add(fridayList);
		
		return returnmodule;
	}
	
	/**Creates rounds for a week given a teamlist and returns a WeekofRounds object*/
	private static WeekofRounds createPairing(ArrayList<Team> teamList, String roundweek){

		ArrayList<Round> mondayroundList = new ArrayList<Round>();
		ArrayList<Round> tuesdayroundList = new ArrayList<Round>();
		ArrayList<Round> wednesdayroundList = new ArrayList<Round>();
		ArrayList<Round> thursdayroundList = new ArrayList<Round>();
		ArrayList<Round> fridayroundList = new ArrayList<Round>();
		
		ArrayList<ArrayList<Team>> teamsByDays = sortTeamsbyDay(teamList);
		//unpack teams by days
		ArrayList<Team> mondayList = teamsByDays.get(0);
		ArrayList<Team> tuesdayList = teamsByDays.get(1);
		ArrayList<Team> wednesdayList = teamsByDays.get(2);
		ArrayList<Team> thursdayList = teamsByDays.get(3);
		ArrayList<Team> fridayList = teamsByDays.get(4);
		
		if (!mondayList.isEmpty())
			mondayroundList = pairTeamsByDay(mondayList, teamList);
		if (!tuesdayList.isEmpty())
			tuesdayroundList = pairTeamsByDay(tuesdayList, teamList);
		if (!wednesdayList.isEmpty())
			wednesdayroundList = pairTeamsByDay(wednesdayList, teamList);
		if (!thursdayList.isEmpty())
			thursdayroundList = pairTeamsByDay(thursdayList, teamList);
		if (!fridayList.isEmpty())
			fridayroundList = pairTeamsByDay(fridayList, teamList);
		//initialize a week based on the current lists for rounds.
		WeekofRounds currentWeek = new WeekofRounds(mondayroundList, tuesdayroundList, wednesdayroundList, thursdayroundList, fridayroundList, roundweek, teamList);
		return currentWeek;
	}
	
	/**MAIN FUNCTION: Takes in a list of teams, along with the global team list, and returns a round pairing. */
	private static ArrayList<Round> pairTeamsByDay (ArrayList<Team> dayList, ArrayList<Team> teamList){
		ArrayList<Round> dayPairing = new ArrayList<Round>();
		
		//need based evaluation.
		ArrayList<Team> needaff = new ArrayList<Team>();
		ArrayList<Team> needneg = new ArrayList<Team>();
		ArrayList<Team> nopref = new ArrayList<Team>();
		
		//sort teams into these 3 categories, which we will use to perform ranked placement.
		for (Team tm :dayList){
			int globalListLocation = locateTeaminList(tm, teamList);
			Team globalteam = teamList.get(globalListLocation);
			//retrieve this team from the teamList.
			if (!globalteam.hasgoneaff && !globalteam.hasgoneneg){
				nopref.add(globalteam);
				continue;
			}
			if (!globalteam.hasgoneaff && globalteam.hasgoneneg){
				needaff.add(globalteam);
				continue;
			}
			if (!globalteam.hasgoneneg && globalteam.hasgoneaff){
				needneg.add(globalteam);
				continue;
			}
			if (globalteam.hasgoneaff && globalteam.hasgoneneg){
				nopref.add(globalteam);
			}
		}
		
		//attempt to pair people in the only aff list with the people in the only neg list.
		if (!needaff.isEmpty() && !needneg.isEmpty())
			pairTwoLists(needaff, needneg, teamList, dayPairing);
		//attempt to pair people in the aff list with those who can do both.
		if (!needaff.isEmpty() && !nopref.isEmpty())
			pairTwoLists(needaff, nopref, teamList, dayPairing);
		//attempt to pair people in the both list with the neg list.
		if (!nopref.isEmpty() && !needneg.isEmpty())
			pairTwoLists(nopref, needneg, teamList, dayPairing);
		if (!nopref.isEmpty())
			pairOneList(nopref, teamList, dayPairing);
		
		//now, we should just have the leftovers. throw everyone into 1 pile now and pair again.
		ArrayList<Team> remainderList = new ArrayList<Team>();
		remainderList.addAll(needaff);
		remainderList.addAll(needneg);
		remainderList.addAll(nopref); 
		if (!remainderList.isEmpty())
			pairOneList(remainderList, teamList, dayPairing);
		//1 team may be leftover.
		if (!remainderList.isEmpty()){
			Team aff = remainderList.get(0);
			Round dummyRound = new Round(aff, null);
			dayPairing.add(dummyRound);
		}
			
		return dayPairing;
	}
	
	/**This function takes in 1 list, the global list, and the current pairing structure. It changes these by reference.*/
	private static void pairOneList (ArrayList<Team> list, ArrayList<Team> teamList, ArrayList<Round> dayPairing){//problem is with the deletion function, which is failing to delete anything from the lists.
		int listindex = 0;
		ArrayList<Integer> dropList = new ArrayList<Integer>();
		
		while (listindex < list.size()){

			//first, check if the dropList already contains the index we would consider. If that is the case, this team has already been paired and should not be considered.
			if (dropList.contains(listindex)){
				listindex = listindex +1;
				continue;
			}				
			Team tm1 = list.get(listindex);
			Team tm2 = new Team();
			//this additional complication is needed in the case that we hit the end of the list and k > size of the list. That triggers NPE.
			int k = listindex + 1;
			if (k < list.size())
				tm2 = list.get(k);
			else {
				listindex = listindex + 1; //this would just ensure that we break from the while loop.
				continue;
			}
				
			boolean skiphistorycheck = false;
			boolean breaktonext = false;
			boolean conflict = historyCheck(tm1, tm2);
			if (conflict)
				conflict = historyCheck(tm2, tm1); //check if switching the teams removes the conflict.
			else //no conflict. therefore, skip rest of the logic.
				skiphistorycheck = true;
			
			if (!skiphistorycheck && conflict) //if switching doesn't work (conflict = true), then iterate k over the list and historycheck each scenario.
				while (conflict){
					k = k+1;
					if (k < list.size()){
						tm2 = list.get(k);
						conflict = historyCheck(tm1, tm2);
					}
					else{ //this would mean there is no one the team can go aff against. we should break from this team and move on.
						breaktonext = true;
						break;
					}
				}
			else if (!skiphistorycheck && !conflict){ //the switch was effective. therefore, switch the teams.
				Team tmp = new Team();
				tmp = tm1;
				tm1 = tm2;
				tm2 = tmp;				
			}
			if (breaktonext){ //in case, this team cannot be paired.
				listindex = listindex + 1;
				continue;
			}
				
			//we have finally gotten a tm1 and a tm2 that can be paired without a conflict of history.
			Round newRound = new Round(tm1, tm2);
			String potentialconflict = "";
			int t1location = locateTeaminList(tm1, teamList); // this is so we can edit the global list with the histories and the aff/neg status.
			if (tm1.hasgoneaff) //if someone ends up going aff twice, this message is the conflict.
				potentialconflict = "T1 Has Already Gone Aff.";
			tm1.hasgoneaff = true;
			tm1.historyNegSet.add(tm2);
			tm1.availablescore = tm1.availablescore+1; //incrementing the score reduces likelihood of being picked later.
			teamList.set(t1location, tm1); //change the team's status in the global teamlist.
			dropList.add(listindex);
				
			int t2location = locateTeaminList(tm2, teamList);
			if (tm2.hasgoneneg)
				potentialconflict += "T2 Has Already Gone Neg";
			tm2.hasgoneneg = true;
			tm2.historyAffSet.add(tm1);
			tm2.availablescore = tm2.availablescore+1;
			teamList.set(t2location, tm2);
			dropList.add(k);
				
			newRound.message = potentialconflict;
			dayPairing.add(newRound);
			listindex = listindex + 1;
		}
		//now, we need to remove the teams we paired from the list we paired.
		//sort dropList from greatest to least so that you can delete without iterative problems.
		Collections.sort(dropList, Collections.reverseOrder());
		
		for (int dropindex :dropList)
			list.remove(dropindex);
		//no need to return. we passed the structure in by reference, and it has been edited as such.
	}
	
	/**Pairs two exclusive lists by adding rounds to a Roundlist. */
	private static void pairTwoLists (ArrayList<Team> affList, ArrayList<Team> negList, ArrayList<Team> teamList, ArrayList<Round> dayPairing){
		if (!affList.isEmpty() && !negList.isEmpty()){
			ArrayList<Integer> dropList1 = new ArrayList<Integer>();
			ArrayList<Integer> dropList2 = new ArrayList<Integer>();
			int nofchecks = Math.min(affList.size(), negList.size());
			for (int i =0; i < nofchecks; i++){
				if (dropList1.contains(i))
					continue;
				Team tm1 = affList.get(i);
				int k = i;
				if (dropList2.contains(k))
					continue;
				Team tm2 = negList.get(k);
				boolean conflict = historyCheck (tm1, tm2);
				boolean breaktonext = false;
				while (conflict){
					k = i+1;
					if (dropList2.contains(k))
						continue;
					if (k < negList.size()){
						tm2 = negList.get(k);
						conflict = historyCheck(tm1, tm2);
					}
					else{
						breaktonext = true;
						break;
					}
				}
				
				if (breaktonext)
					break;
				Round newRound = new Round(tm1, tm2);
						
				int t1location = locateTeaminList(tm1, teamList);
				tm1.hasgoneaff = true;
				tm1.historyNegSet.add(tm2);
				tm1.availablescore = tm1.availablescore+1; //incrementing the score reduces likelihood of being picked later.
				teamList.set(t1location, tm1); //change the team's status in the global teamlist.
				dropList1.add(i);
				
				int t2location = locateTeaminList(tm2, teamList);
				tm2.hasgoneneg = true;
				tm2.historyAffSet.add(tm1);
				tm2.availablescore = tm2.availablescore+1;
				teamList.set(t2location, tm2);
				dropList2.add(k);
				
				dayPairing.add(newRound);			
			}
			Collections.sort(dropList1, Collections.reverseOrder());
			Collections.sort(dropList2, Collections.reverseOrder());
			
			for (int dropindex :dropList1)
				affList.remove(dropindex);
			for (int dropindex :dropList2)
				negList.remove(dropindex);
			
		}
	}

	/**Takes in 2 teams and checks if the aff has gone aff against that neg team before. */ 
	private static boolean historyCheck (Team aff, Team neg){
		if (aff.historyNegSet == null)
			return false;
		if (aff.historyNegSet.contains(neg))
			return true;
		else {
			return false;
		}
	}
	
/**Locates a team in a list of teams. Returns location index.
 * 
 * @param tm
 * @param list
 * @return
 */
	private static int locateTeaminList (Team tm, ArrayList<Team> list){
		String targetpartner1 = tm.partner1;
		String targetpartner2 = tm.partner2;
		int location = list.size() + 1;
		for (int targetindex = 0; targetindex < list.size(); targetindex++){
			Team eachteam = list.get(targetindex);
			String name1 = eachteam.partner1;
			String name2 = eachteam.partner2;
			if ((name1.equals(targetpartner1)) && (name2.equals(targetpartner2)))
				location = targetindex;
			//check for reverse as well.
			if ((name2.equals(targetpartner1)) && (name1.equals(targetpartner2)))
				location = targetindex;
		}
		
		if (location == (list.size() + 1))
			throw new IllegalArgumentException("Team not found in list.");
		
		return location;
	}
	
	/** DEPRECATED: Holistic pairing approach. Iterates over the team instead of the day. */
	private static Pair<Team, ArrayList<Round>> placeTeamInDay (Team currentTeam, ArrayList<Round> dayRoundList){
		boolean done = false;
		if (dayRoundList.isEmpty() && !done){
			Round newRound = new Round();
			if (!currentTeam.hasgoneaff || currentTeam.three_day_toggle){
				newRound.affirmative = currentTeam;
				currentTeam.hasgoneaff = true;
				done = true;
			}
			else if ((!currentTeam.hasgoneneg || currentTeam.three_day_toggle) && !done){
				newRound.negative = currentTeam;
				currentTeam.hasgoneneg = true;
				done = true;
			}
			dayRoundList.add(newRound);
			//create a round and place the team in in it as either aff or neg and break.
		}
			
		if (!done && containsEmptyRound(dayRoundList)){
			Pair<Integer, String> openlocationandtype = findEmptyRound(dayRoundList);
			int openindex = openlocationandtype.getFirst();
			String opentypeString = openlocationandtype.getSecond();
			if ((!currentTeam.hasgoneaff || currentTeam.three_day_toggle) && opentypeString.equals("aff")){
				Round openround = dayRoundList.get(openindex);
				Team opponentTeam = openround.negative;
				if ((currentTeam.historyNegSet != null) && (!currentTeam.historyNegSet.contains(opponentTeam))){	
					openround.affirmative = currentTeam;
					currentTeam.historyNegSet.add(opponentTeam);
					dayRoundList.set(openindex, openround);
					currentTeam.hasgoneaff = true;
					done = true;
				}
			}
			if ((!currentTeam.hasgoneneg || currentTeam.three_day_toggle) && !done && opentypeString.equals("neg")){
				Round openround = dayRoundList.get(openindex);
				Team opponentTeam = openround.affirmative;
				if ((currentTeam.historyAffSet != null) && (!currentTeam.historyAffSet.contains(opponentTeam))){
					openround.negative = currentTeam;
					currentTeam.historyNegSet.add(opponentTeam);
					currentTeam.hasgoneneg = true;
					dayRoundList.set(openindex, openround);
					done = true;
				}
			}	
			//check if the team is eligible for that. if so, place them and break.
		}
		if (!done){
				Round rd = new Round();
				rd.affirmative = currentTeam;
				dayRoundList.add(rd);
			//create a new round and place them in it.
		}
		if ((currentTeam.hasgoneaff) && (currentTeam.hasgoneneg) && (currentTeam.three_days_exception))
			currentTeam.three_day_toggle = true;
		Pair<Team, ArrayList<Round>> returnmodule = new Pair<Team, ArrayList<Round>>(currentTeam, dayRoundList);
		return returnmodule;
	}
	
	/** Check whether a day contains a round with an empty space*/
	private static boolean containsEmptyRound(ArrayList<Round> roundlist){
		boolean isempty = false;
		for (Round rd :roundlist){
			if (rd.affirmative == null)
				isempty = true;
			if (rd.negative == null)
				isempty = true;
		}
		return isempty;
	}
	
	/** Locates where the empty round is and returns integer and type of space that's open.*/
	private static Pair<Integer, String> findEmptyRound(ArrayList<Round> roundlist){
		int openindex = 0;
		String openposition = "";
		int counter = 0;
		for (Round rd :roundlist){
			if (rd.affirmative == null){
				openindex = counter;
				openposition = "aff";
			}
				
			if (rd.negative == null){
				openindex = counter;
				openposition = "neg";
			}
			counter = counter+1;
		}
		Pair<Integer, String> returnitem = new Pair<>(openindex, openposition);
		return returnitem;
	}
	
	/**UNUSED: Conflict resolution function. Probably should just be rewritten. Currently makes no sense. */
	private static ArrayList<Round> removeConflicts(ArrayList<Round> dayList){
		ArrayList<Round> optimizedlist = new ArrayList<Round>();
		LinkedHashSet<Integer> dropSet = new LinkedHashSet<Integer>();
		
		if (!containsEmptyRound(dayList))
			return dayList;
		initialround:
		for (int i = 0; i < dayList.size(); i++){
			String openlocation ="";
			Round round1 = dayList.get(i);
			
			if ((round1.affirmative == null))
				openlocation = "aff";
			else if (round1.negative == null)
				openlocation = "neg";
			else {
				continue;
			}
			
			int replacedindex = 0;
			boolean wasconflict = false;
			for (int j = i+1; j < dayList.size(); j++){
			while (!wasconflict){
				boolean done = false;
				Round round2 = dayList.get(j);
				if (round2.affirmative == null){
					if (openlocation.equals("aff")){
						round2.affirmative = round2.negative;
						round2.message = "(Aff team has been relocated from a different round.)";
						done = true;
					}
					if (!done && openlocation.equals("neg")){
						round2.affirmative = round2.affirmative;
						round2.message = "(Aff team has been relocated from a different round.)";
						done = true;
					}
				}
				if (round2.negative == null){
					if (openlocation.equals("aff")){
						round2.negative = round1.affirmative;
						round2.message = "(Neg team has been relocated from a different round.)";
						done = true;
					}
					if (openlocation.equals("neg")){
						round2.negative = round1.negative;
						round2.message = "(Neg team has been relocated from a different round.)";
						done = true;
					}
				}
				dayList.set(j, round2);
				if (done){
					wasconflict = true;
					replacedindex = j;
				}
				//at this point, round 2 is just not empty.
			} //while
			} //for #2
			//if relocation, then kill round1.
			if (wasconflict){
				dropSet.add(i);
				continue initialround;
			}
		} //for #1
		
		for (int f = 0; f < dayList.size(); f++){
			if (dropSet.contains(f))
				continue;
			else{
				optimizedlist.add(dayList.get(f));
			}
		}
		
		return optimizedlist;
	}
	
	/** DEPRECATED/UNUSED: Optimizes a week of rounds to have the least possible empty rounds */
	private static WeekofRounds optimizeRounds (WeekofRounds thisweek, ArrayList<Team> teamList){
		ArrayList<Round> mondayList = thisweek.mondayroundList;
		ArrayList<Round> tuesdayList = thisweek.tuesdayroundList;
		ArrayList<Round> wednesdayList = thisweek.wednesdayroundList;
		ArrayList<Round> thursdayList = thisweek.thursdayroundList;
		ArrayList<Round> fridayList = thisweek.fridayroundList;
		
		ArrayList<Round> optimizedmondayList = new ArrayList<Round>();
		optimizedmondayList = removeConflicts(mondayList);
		
		ArrayList<Round> optimizedtuesdayList = new ArrayList<Round>();
		optimizedtuesdayList = removeConflicts(tuesdayList);
		
		ArrayList<Round> optimizedwednesdayList = new ArrayList<Round>();
		optimizedwednesdayList = removeConflicts(wednesdayList);
		
		ArrayList<Round> optimizedthursdayList = new ArrayList<Round>();
		optimizedthursdayList = removeConflicts(thursdayList);
		
		ArrayList<Round> optimizedfridayList = new ArrayList<Round>();
		optimizedfridayList = removeConflicts(fridayList);
		
		WeekofRounds optimizedWeek = new WeekofRounds(optimizedmondayList, optimizedtuesdayList, optimizedwednesdayList, optimizedthursdayList, optimizedfridayList, thisweek.week, teamList);
		
		//at this point, there will be a lot of empty rounds.
		//cascading pref up? for now, just increase the amount of rounds that will be happening to a maximum.
		//order of conflict resolution should be: team switch-side, aff twice/neg twice, history
		//absolute worst case scenario - resolve round issues by moving teams and adding a conflict note.
		return optimizedWeek;
	}
	
	/**Runs generation code.*/
	public static void main(String[] args) throws IOException {
		System.out.println("Policy Debate Round Generator (v. 2.0) by Ankit Mathur");
		System.out.println("Please enter the week of rounds you wish to generate.");
		Scanner input = new Scanner(System.in);
		String roundweek = input.nextLine();
		//if (roundweek.equals("admin"))
			//adminpanel();
		ArrayList<Team> teamList = generateTeamList();
		WeekofRounds thisweek = createPairing(teamList, roundweek);
		boolean writesuccess = writeRoundHistory(thisweek);
		if (!writesuccess)
			System.out.println("ALERT - ROUND HISTORY FAILED TO WRITE.");
		
		input.close();
		System.out.println(thisweek.toString());
		
	}

}
