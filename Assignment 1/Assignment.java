package assignment1;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Assignment implements SubmissionHistory {
	
	/**
	 * Default constructor
	 * Creating the nested class to store the data
	 */

	private class MySubmission implements Submission{
		private final String unikey;
		private final Integer grade;
		private final Date time;
		private MySubmission prev;
		private MySubmission next;
		
		MySubmission(String unikey, Date time,int grade) {
			this.unikey = unikey;
			this.time = time;
			this.grade = grade;
			this.prev = null;
			this. next = null;
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
		public MySubmission getPrev() {return this.prev;}
		public MySubmission getNext() {return this.next;}
		public void setPrev(MySubmission prev) {this.prev = prev;}
		public void setNext(MySubmission next) {this.next= next;}

	
	}
	
	private int size;
	//The header sentinel
	private MySubmission header;
	//The trailer sentinel
	private MySubmission trailer;
	//Creating a circular linked list
	public Assignment() {
		header = new MySubmission(null,null,0);
		trailer = new MySubmission(null,null,0);
		header.setNext(trailer);
		trailer.setPrev(header);
		size = 0;
		
		// TODO initialise your data structures
	}

	@Override
	public Integer getBestGrade(String unikey) {
		// TODO Implement this, ideally in better than O(n)
		Integer bestgrade = 0;
		boolean found = false;
		
		if(unikey == null){
			throw new IllegalArgumentException();
		}
		// creating the temp MySubmission node to get around all the nodes
		MySubmission first = new MySubmission (null,null,0);
		first = this.header.getNext();
		
		for(int i = 0 ; i < size;i++){
			//Checking if unikey is in our list of Submission
			if(first.getUnikey().equals(unikey)){
				found = true;
				if(bestgrade < first.getGrade()){ //Comparing the grade and getting the highest grade.
					bestgrade = first.getGrade();
				}	
			}
		first = first.getNext();
		}
		// if we found the Unikey then return its best grade else returning null
		if(found){
			return bestgrade;
		}else{
			return null;
		}
	}
	
	@Override
	public Submission getSubmissionFinal(String unikey) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null){
			throw new IllegalArgumentException();
		}
		// To store the final answer in this newNode
		MySubmission newNode = new MySubmission(null,null,0);
		boolean found = false;
		MySubmission first = new MySubmission (null,null,0);
		first = this.header.getNext();
		for(int i = 0 ; i < size;i++){
			
			if(first.getUnikey().equals(unikey)){
				found = true;
				newNode = first;
				break;
			}
		first = first.getNext();
		}
		if(!found){
			return null;
		}
		MySubmission second = new MySubmission(null,null,0);
		second= this.header.getNext();
		for(int i= 0 ; i < size;i++){
			
			if(second.getUnikey().equals(unikey)){
				if(newNode.getTime().compareTo(second.getTime()) < 0){
					newNode = second;
					//System.out.println(newNode.getTime());
				}
			}
			second = second.getNext();
		}
		return newNode;
	}

	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null || deadline == null){
			throw new IllegalArgumentException();
		}
	//	System.out.println(unikey +" "+ deadline);
		MySubmission newNode = new MySubmission(null,null,0);
		MySubmission first = new MySubmission (null,null,0);
		first = this.header.getNext();
		boolean found = false;
		
		// Checking if Unikey exist in the system
		for(int i = 0 ; i < size;i++){
			if(first.getUnikey().equals(unikey)){
				if(first.getTime().compareTo(deadline) <= 0){
		//			System.out.println("First time: " + first.getTime());
					found = true;
					newNode = first;
					break;
				}
			}
		first = first.getNext();
		}
		if(!found){
			return null;
		}
		// Creating node to iterate the link list
		MySubmission second = new MySubmission(null,null,0);
		second= this.header.getNext();
		//Comparing through the date
		for(int i= 0 ; i < size;i++){
			
			if(second.getUnikey().equals(unikey)){
		//		System.out.print(second.getTime()+ " compare value: ");
			//	System.out.println(second.getTime().compareTo(deadline));
				if(second.getTime().compareTo(deadline) <= 0){
			//		System.out.println(newNode.getTime().compareTo(second.getTime()));
					if((newNode.getTime().compareTo(second.getTime()) < 0)){
				//		System.out.print(newNode.getTime().compareTo(deadline) + " ");
						newNode = second;
				//		System.out.println(newNode.getTime());
					}
				}
			}
			second = second.getNext();
		}
		//System.out.println(newNode.getUnikey()+ " "+newNode.getGrade()+ " "+ newNode.getTime());
		if(newNode.getUnikey() == null){
			return null;
		}else{
			return newNode;
		}
		
	}

	// Helper method to add the new submission. Adds new submission at the beginning of the list.
	 private void addBetween(MySubmission head, MySubmission head_next, String unikey,Date timestamp, Integer grade){
		MySubmission newNode = new MySubmission(unikey,timestamp,grade);
		newNode.setPrev(head);
		newNode.setNext(head_next);
		head.setNext(newNode);
		head_next.setPrev(newNode);
		size++;
	 }
	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) {
		if(unikey == null || timestamp == null || grade == null){
			throw new IllegalArgumentException();
		}
		Submission newSub = new MySubmission(unikey,timestamp,grade);
		addBetween(this.header,this.header.getNext(),unikey,timestamp,grade);
		return newSub;
		
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
		MySubmission first = new MySubmission (null,null,0);
		first = this.header.getNext();
		for(int i =0; i< size; i++){
			if(first.getUnikey().equals(submission.getUnikey()) && first.getGrade() == submission.getGrade() && first.getTime().compareTo(submission.getTime()) == 0){
				first.getPrev().setNext(first.getNext());
				first.getNext().setPrev(first.getPrev());
				size--;
				break;
			}
			first = first.getNext();
		}
	}

	@Override
	public List<String> listTopStudents() {
		// TODO Implement this, ideally in better than O(n)
		// (you may ignore the length of the list in the analysis)
		MySubmission first = new MySubmission (null,null,0);
		List<String> best_unikey = new ArrayList<String>();
		if(size == 0){
			return best_unikey;
		}
		
		first = this.header.getNext();
		int bestgrade = 0;
		for(int i = 0; i < size ; i++){
			if(first.getGrade() > bestgrade){
				bestgrade = first.getGrade();
			}
			first = first.getNext();
		}
		
		first = this.header.getNext();
		
		for(int i = 0; i < size ; i++){
			if(first.getGrade() == bestgrade){
				best_unikey.add(first.getUnikey());
			}
			first = first.getNext();
		}
		// To remove duplicates in Arraylist
		Set<String> remove_dup = new HashSet<>();
		remove_dup.addAll(best_unikey);
		best_unikey.clear();
		best_unikey.addAll(remove_dup);
		
		return best_unikey;
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
		Map<String, ArrayList<Submission>> submissions = new HashMap<String, ArrayList<Submission>>();
		MySubmission first = new MySubmission (null,null,0);
		MySubmission second = new MySubmission (null,null,0);
		first = this.header.getNext();
		second = this.header.getNext();
		
		for(int j = 0; j<size; j++){
			String key = first.getUnikey();
			ArrayList<Submission> toAdd = new ArrayList<Submission>();
			
			while(second != trailer){
				if(key.equals(second.getUnikey())){
					toAdd.add(second);
				}
				second = second.getNext();
			}
			submissions.put(key , toAdd );
			first = first.getNext();
			second = header.getNext();
		}
//		for (String key: submissions.keySet()) {
//		    System.out.println("Key = " + key);
//		}
		List<String> listRegression = new ArrayList<>();
		
		ArrayList<Submission> sortTime = new ArrayList<>();
		ArrayList<Submission> sortGrade = new ArrayList<>();
		for(ArrayList<Submission> value : submissions.values()){
			sortTime = value;
			sortGrade = value;
			if(value.size() >1){
				// Sort from latest time to oldest time
				Collections.sort(sortTime, new Comparator<Submission>() {
					@Override
					public int compare(Submission o1, Submission o2) {
						
						return -1*o1.getTime().compareTo(o2.getTime());
					}
	
				});
				int lastGrade = sortTime.get(0).getGrade();
				//Sort according to highest grade to lowest grade
				Collections.sort(sortGrade, new Comparator<Submission>() {
					@Override
					public int compare(Submission o1, Submission o2) {
						
						return -1*o1.getGrade().compareTo(o2.getGrade());
					}
	
				});
				int bestGrade = sortGrade.get(0).getGrade();
//				for(int i = 0; i < sortGrade.size();i++){
//					System.out.print(sortGrade.get(i).getUnikey()+" ");
//					System.out.print(sortGrade.get(i).getTime()+ " ");
//					System.out.print(sortGrade.get(i).getGrade()+ " ");
//					System.out.println();
//				}
//				
//				for(int i = 0; i < sortTime.size()  ;i++){
//					System.out.print(sortTime.get(i).getUnikey()+" ");
//					System.out.print(sortTime.get(i).getTime()+ " ");
//					System.out.print(sortTime.get(i).getGrade()+ " ");
//					System.out.println();
//				}
//				System.out.println(lastGrade);
//				System.out.println(bestGrade);
				if(lastGrade < bestGrade){
					listRegression.add(sortTime.get(0).getUnikey());
					//System.out.println(sortTime.get(0).getUnikey());
				}
			}
		}
//		ArrayList<Submission> temp = new ArrayList<>();
//		temp = submissions.get("aaaa1234");
//		Collections.sort(temp, new Comparator<Submission>() {
//			@Override
//			public int compare(Submission o1, Submission o2) {
//				
//				return -1*o1.getGrade().compareTo(o2.getGrade());
//			}
//
//		});
//		for(int i = 0; i < 3 ;i++){
//			System.out.print(temp.get(i).getUnikey()+" ");
//			System.out.print(temp.get(i).getTime()+ " ");
//			System.out.print(temp.get(i).getGrade()+ " ");
//		}
//		System.out.println();
//		Collections.sort(temp, new Comparator<Submission>() {
//			@Override
//			public int compare(Submission o1, Submission o2) {
//				
//				return -1*o1.getTime().compareTo(o2.getTime());
//			}
//
//		});
//		for(int i = 0; i < 3 ;i++){
//			System.out.print(temp.get(i).getUnikey()+" ");
//			System.out.print(temp.get(i).getTime()+ " ");
//			System.out.print(temp.get(i).getGrade()+ " ");
//		}
		
		return listRegression;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
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
		SubmissionHistory history = new Assignment();
		history.add("aaaa1234", getDate("2016/09/03 09:00:00"), 66);
		history.add("aaaa1234", getDate("2016/09/03 16:00:00"), 86);
		history.add("cccc1234", getDate("2016/09/03 16:00:00"), 73);
		history.add("aaaa1234", getDate("2016/09/03 18:00:00"), 40);
		history.add("bbbb1234", getDate("2016/09/03 18:00:00"), 70);
		history.add("dddd1234", getDate("2016/09/03 18:00:00"), 80);
		Submission example6 = history.getSubmissionBefore("aaaa1234", getDate("2016/09/03 13:00:00"));
		List<String> example8 = history.listRegressions();
//		System.out.println(example8.get(0));
		
//		for(int i=0;i<example8.size();i++){
//		    System.out.println(example8.get(i));
//		} 
	//	Integer example1 = history.getBestGrade("aaaa1234");
//	//	System.out.println(example1.getTime());
//		Submission example4 = history.getSubmissionFinal("aaaa1234");
//		Submission example5 = history.getSubmissionFinal("cccc1234");
//		System.out.println(example5.getUnikey());
//		System.out.println(example5.getTime());
//		System.out.println(example5.getGrade());
		
}
}