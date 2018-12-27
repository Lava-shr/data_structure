package assignment1;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;


public class AssignmentTree implements SubmissionHistory {
	
	/**
	 * Default constructor
	 * Creating the nested class to store the data
	 */

	private class MySubmission implements Submission{
		private final String unikey;
		private final Integer grade;
		private final Date time;
		
		MySubmission(String unikey, Date time,int grade) {
			this.unikey = unikey;
			this.time = time;
			this.grade = grade;
		}
		
		@Override
		public String getUnikey() {
			// TODO Auto-generated method stub
			return this.unikey;
		}

		@Override
		public Date getTime() {
			// TODO Auto-generated method stub
			return this.time;
		}
		@Override
		public Integer getGrade() {
			// TODO Auto-generated method stub
			return this.grade;
		}

	}
//	TreeMap<String, TreeMap<Object, Object>> myData;
//	private TreeMap<String, ArrayList <TreeMap<Date,Integer>>> myData;
//	private TreeMap<String , ArrayList <TreeMap<Date, Integer>> myData2;
	private TreeMap<String, TreeMap<Date , Integer>> myData;
	private TreeMap<String, TreeMap<Integer , Date>> myData_gradeKey;
	private TreeMap<Integer, TreeMap<String,Date>> myData_topStudents;
	
	public AssignmentTree() {
		// TODO initialise your data structures
	//	TreeMap<String, TreeMap<Object, Object>> myData =  new TreeMap<String, TreeMap<Object, Object>>();
	//	TreeMap<String, ArrayList <TreeMap<Date,Integer>>> myData1 =  new TreeMap<String, ArrayList<TreeMap<Date,Integer>>>();
		this.myData = new TreeMap<String, TreeMap<Date , Integer>>();
		this.myData_gradeKey = new TreeMap<String, TreeMap<Integer , Date>>();
		this.myData_topStudents = new TreeMap<Integer, TreeMap<String,Date>>();
	}


	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) {
		if(unikey == null || timestamp == null || grade == null){
			throw new IllegalArgumentException();
		} 
		Submission newSubmission = new MySubmission(unikey,timestamp,grade);
		//TreeMap<String, TreeMap<Object, Object>> myData =  new TreeMap<String, TreeMap<Object, Object>>();
		TreeMap<Date, Integer> unikeyData = new TreeMap<>();
		TreeMap<Integer, Date> unikeyData2 = new TreeMap<>();
		TreeMap<String,Date> unikeyData3 = new TreeMap<>();
		
		{
			unikeyData.put(timestamp, grade);
			unikeyData2.put(grade, timestamp);
			unikeyData3.put(unikey, timestamp);
		}
		
		if(!this.myData.containsKey(unikey)){
			//means this unikey is doesn't exist in the TreeMap
			this.myData.put(unikey, unikeyData);
			this.myData_gradeKey.put(unikey, unikeyData2);
		}else{
			this.myData.get(unikey).putAll(unikeyData);
			this.myData_gradeKey.get(unikey).putAll(unikeyData2);
		}
		if(!this.myData_topStudents.containsKey(grade)){
			this.myData_topStudents.put(grade, unikeyData3);
		}else{
			this.myData_topStudents.get(grade).putAll(unikeyData3);
		}
		
		return newSubmission;
	
}
	@Override
	public Integer getBestGrade(String unikey) {
		// TODO Implement this, ideally in better than O(n)
		
		for(Map.Entry<String, TreeMap<Date, Integer>> entry : myData.entrySet())
		{
		   TreeMap<Date,Integer>  entry2= entry.getValue();
		   for(Map.Entry <Date, Integer> entrie : entry2.entrySet()){
			   System.out.print("Key: " + entry.getKey());
			   System.out.println(" Inner key: "+entrie.getKey() +" Value: "+entrie.getValue());
		   }
		   
		}
		for(Map.Entry<String, TreeMap<Integer, Date>> entry : myData_gradeKey.entrySet())
		{
		   TreeMap<Integer,Date>  entry2= entry.getValue();
		   for(Map.Entry <Integer, Date> entrie : entry2.entrySet()){
			   System.out.print("Key: " + entry.getKey());
			   System.out.println(" Inner key: "+entrie.getKey() +" Value: "+entrie.getValue());
		   }
		}
//		for(Map.Entry<Integer, TreeMap<String, Date>> entry : myData_topStudents.entrySet())
//		{
//		   TreeMap<String,Date>  entry2= entry.getValue();
//		   System.out.print("Key: " + entry.getKey());
//		   for(Map.Entry <String, Date> entrie : entry2.entrySet()){
//			   
//			   System.out.println(" Inner key: "+entrie.getKey() +" Value: "+entrie.getValue());
//		   }
//		}
		if(unikey == null){
			throw new IllegalArgumentException();
		}
		if(!myData_gradeKey.containsKey(unikey)){
			return null;
		}
		Integer grade =  myData_gradeKey.get(unikey).lastEntry().getKey();
		return grade;
	}
	
	@Override
	public Submission getSubmissionFinal(String unikey) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null){
			throw new IllegalArgumentException();
		}
		Integer grade =  myData.get(unikey).lastEntry().getValue();
		Date date = 	 myData.get(unikey).lastEntry().getKey();
		Submission toReturn = new MySubmission(unikey,date,grade);
		return toReturn;
	}

	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null || deadline == null){
			throw new IllegalArgumentException();
		}
		
		Integer grade = myData.get(unikey).floorEntry(deadline).getValue();
		Date date = myData.get(unikey).floorEntry(deadline).getKey();
		Submission toReturn = new MySubmission (unikey,date,grade);
		
		return toReturn;
	}

	@Override
	public void remove(Submission submission) {
		// TODO Implement this, ideally in better than O(n)
		if(submission == null){
			throw new IllegalArgumentException();
		}
		if(submission.getUnikey() == null || submission.getGrade()== null || submission.getTime() == null){
			throw new IllegalArgumentException();
		}
		// Getting all the values to delete from our data structure
		String unikeyToDelete = submission.getUnikey();
		Date dateToDelete = submission.getTime();
		Integer gradeToDelete = submission.getGrade();
		// To track number of submission for that unikey
		int numberOfSubmission = 0;
		//if unikey doesn't exist we just do nothing
		if(!this.myData.containsKey(unikeyToDelete)){
			return;
		}
		// Deleting from first data structure
		TreeMap<Date, Integer> temp = myData.get(unikeyToDelete);
		if(!temp.containsKey(dateToDelete)){
			return;
		}else{
			temp.remove(dateToDelete);
		}
		if(myData.get(unikeyToDelete) == null){ // means only one submission for that unikey so we delete this unikey
			myData.remove(unikeyToDelete);
		}
		
		// Deleting from second data structure
		TreeMap<Integer , Date> temp2 = myData_gradeKey.get(unikeyToDelete);
		if(!temp2.containsKey(gradeToDelete)){
			return;
		}else{
			temp2.remove(gradeToDelete);
		}
		if(myData_gradeKey.get(unikeyToDelete) == null){// means only one submission for that unikey so we delete this unikey
			myData_gradeKey.remove(unikeyToDelete);
		}
		
		// Deleting from third Data Structure
		TreeMap<String , Date> temp3 = myData_topStudents.get(gradeToDelete);
		if(!temp3.containsKey(unikeyToDelete)){
			return;
		}else{
			temp3.remove(unikeyToDelete);
		}
		if(myData_topStudents.get(gradeToDelete) == null){// means only one submission for that unikey so we delete this unikey
			myData_topStudents.remove(gradeToDelete);
		}
	}
			// Deleting from first data structure
//			for(Map.Entry<String, TreeMap<Date, Integer>> entry : myData.entrySet())
//			{
//				if(unikeyToDelete.equals(entry.getKey())){
//				   TreeMap<Date,Integer>  entry2 = entry.getValue();
//					if(entry2.containsKey(dateToDelete)){
//						entry2.remove(dateToDelete, gradeToDelete);
//					}
//				}
//			}

	@Override
	public List<String> listTopStudents() {
		// TODO Implement this, ideally in better than O(n)
		// (you may ignore the length of the list in the analysis
		TreeMap<String, Date> temp = myData_topStudents.lastEntry().getValue();
		Set<String> topStudents = temp.keySet();
		
		return (List<String>) topStudents;
	}

	/**
	 * Get all the students whose most recent submissions have lower grades than
	 * their best submissions
	 * 
	 * @return a list of unikeys
	 */
	@Override
	public List<String> listRegressions() {
		// TODO Implement this, ideally in better than O(n^2)
		List<String> temp = new ArrayList<String>();
		 temp.add("asfdadsf");
		 return temp;
	}
	
	
	
	
	
	
	
	/*
	 * Start of Main method
	 */
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private static Date getDate(String s) {
		try {
			return df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
			return null;
	}
	
	public static void main(String[] args){
		SubmissionHistory history = new AssignmentTree();
		history.add("cccc1234", getDate("2016/09/03 04:00:00"), 89);
		history.add("aaaa1234", getDate("2016/09/03 19:00:00"), 86);
		history.add("aaaa1234", getDate("2016/09/03 05:00:00"), 86);
		history.add("aaaa1234", getDate("2016/09/03 15:00:00"), 90);
		history.add("bbbb1234", getDate("2016/09/03 15:00:00"), 86);


		
		Integer example1 = history.getBestGrade("aaaa1234");
		System.out.println("Best grade: " +example1);
		Submission example4 = history.getSubmissionFinal("aaaa1234");
		System.out.println(example4.getUnikey()+" "+example4.getTime()+ " "+example4.getGrade());
	}
}	
