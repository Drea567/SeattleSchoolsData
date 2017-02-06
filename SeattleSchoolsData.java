
/*To complete this assignment, you will need to implement and utilize a SeattleSchoolsData
object. It must have the following methods:
Instance Variables:
 - filename –- the name of the CSV file that contains the public school data
Constructors:
-  Constructor taking a filename parameter
Hint: don’t load data if you don’t need to! Loading data is an expensive operation.
Methods:
- getDistances
Parameters: name of a school (String)
Returns: map (keys: school names; values: distance from school)
- getSchoolNames
Parameters: none [Optional: implement filtering and/or column selection]
Returns: list of school names
- getPhoneBook
Parameters: none [Optional: implement filtering and/or column selection]
Returns: map (keys: school names; values: phone numbers)
- getAddressBook
Input: none [Optional: implement filtering and/or column selection]
Returns: map (keys: school names; values: addresses)*/


/*setup separate School object to store details of each school
 	create get and add methods for all 
 	
SeattleSChoolData constructor takes String of csv file name
Setup loadData method that parses the data (only if called)
	-Parses to a String[] array (per third party csv parsing JAR, opencsv)
	-Loop: String array needs to be passed into a unique School object for each school name
	-Pass School.getName() as key, full school object as value into TreeMap
*/



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


import com.opencsv.*;

public class SeattleSchoolsData{
	private String filename;
	private boolean dataLoaded;
	private Map<String, School> schoolmap = new TreeMap<String, School>();;
	
	
	public SeattleSchoolsData (String filename) throws IOException{
		this.filename = filename;
		if (this.schoolmap.isEmpty()) this.dataLoaded = false;
		
	}
	
	private void dataLoaded(){
		if (this.dataLoaded == false)
			try {
				loadData();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private Map<String, School> loadData() throws IOException{
		String home = System.getProperty("user.home");	
		File f = new File(home+"/data/schoolData/" + this.filename);
		String fpath = f.getAbsolutePath();
		Scanner data = new Scanner(new File(fpath));
		/*Using opencsv.jar*/
		CSVParser in = new CSVParser();
		String[] line;
		/*Hardcoding in the columns of data and the order I want.
		Not ideal for versatility of this program, but works for this 
		data set*/
		int[] column = {11, 9, 6, 1, 2, 5};
		
		/*Read each line of the CSV file, create a temp School object
		 * to be hold all the data. Populated school object is then 
		 * passed into the map as the value (key is school name).*/ 
		while(data.hasNextLine()){
		School current = new School();
		line = in.parseLine(data.nextLine());
			current.addName(line[column[0]]);
			current.addPhone(line[column[1]]);
			current.addAddress(line[column[2]]);
			current.addLat(line[column[3]]);
			current.addLong(line[column[4]]);
			current.addType(line[column[5]]);
		schoolmap.put(current.getName(), current);
		}
		data.close();
		/*Workaround to remove headers and random blank line*/
		this.schoolmap.remove("SCHOOL");
		this.schoolmap.remove("");
		
		return this.schoolmap;
		
	}
	
	public Set<String> getSchoolNames(){
		dataLoaded();
		Set<String> names = this.schoolmap.keySet();
		return names;
		}
	
	public Map<String, String> getPhoneBook(){
		dataLoaded();
		Map<String, String> phonemap = new TreeMap<String, String> ();
		Set <String> schoolKey = this.schoolmap.keySet();
		for (String schoolname : schoolKey){
			School phone =  this.schoolmap.get(schoolname);
			phonemap.put(schoolname, phone.getPhone());
		}
		return phonemap;
	}
	
	public void printSortedPhoneBook(){
		dataLoaded();
		Map<String, String> sortedphone = new TreeMap<String, String> ();
		for (String name : this.schoolmap.keySet()){
			School phone =  this.schoolmap.get(name);
			sortedphone.put(phone.getPhone(), name);
		}
		for (String num : sortedphone.keySet()){
			String name =  sortedphone.get(num);
			System.out.printf("%s: %s ", name, num);
		}
	}
	
	public Map<String, String> getAddressBook(){
		dataLoaded();
		Map<String, String> addressmap = new TreeMap<String, String> ();
		for (String schoolname : this.schoolmap.keySet()){
			School address =  this.schoolmap.get(schoolname);
			addressmap.put(schoolname, address.getAddress());
		}
		return addressmap;
	}

	public Map<String, Double> getDistances(String schoolname){
		dataLoaded();
		Map<String, Double> distancemap = new TreeMap<String, Double> ();
		School homebase = this.schoolmap.get(schoolname);
		double lat1 = homebase.getLat();
		double long1 = homebase.getLong();
		for (String school : this.schoolmap.keySet()){
			School otherschool =  this.schoolmap.get(school);
			double lat2 = otherschool.getLat();
			double long2 = otherschool.getLong();
		double distance = ZipLookup.distance(lat1, long1, lat2, long2);
		distancemap.put(school, distance);
		}
		return distancemap;
		
		
	}
	
	public void printFiveClosestSchools(String schoolname){
		Map<String, Double> distances = new TreeMap<String, Double> ();
		distances.putAll(getDistances(schoolname));
		Map<Double, String> sortedDistances = new TreeMap<Double, String> ();
		
		for (String school : distances.keySet()){
			double miles = distances.get(school);
			sortedDistances.put(miles, school);
	
		}
		/*Remove irrelevant information (distance from starting school to starting school
		 * will be zero miles)*/
		sortedDistances.remove(0.0);
		int max = 5;
		int count = 0;
		for (Double dist : sortedDistances.keySet()){
			System.out.printf("%s: %.3f\n",sortedDistances.get(dist),dist);
			count++;
			if (count == max) break;
		}
		
	}
	
	public String findSchoolFromStreet(String schooladdress){
		dataLoaded();
		
		String suff = schooladdress.substring(schooladdress.length()-2, schooladdress.length());
		for (String school : this.schoolmap.keySet()){
			String address = this.schoolmap.get(school).getAddress();
			if (address.contains(schooladdress) && address.endsWith(suff)){
				return String.format("%s is the address for: %s", schooladdress, school);
			}
			
			/*String address = this.schoolmap.get(school).getAddress();
			String breakdown[] = schooladdress.split(" ");
			if (address.contains(breakdown[0]) && address.contains(breakdown[1]) && address.contains(breakdown[2]) ){
				return String.format("%s is the address for: %s", schooladdress, school);
			}*/
		}
		return "Sorry, school not found";
	}
	
	public String toString(){
		dataLoaded();
		String output = "";
		for (String name : this.schoolmap.keySet()){
			output = output + String.format("%s, %s\n", name, this.schoolmap.get(name).toString());
		}
		
		return output;
		
	}
	
	
	
	public void testCoords(){
		dataLoaded();
		for (String school : this.schoolmap.keySet()){
			School otherschool =  this.schoolmap.get(school);
			double lat2 = otherschool.getLat();
			double long2 = otherschool.getLong();
			System.out.println(lat2 + ", " + long2);
	}
	}
	
	
	
	
	
	
	
	public static void main (String[] args) throws IOException{
		String file = "SchoolData.csv";
		SeattleSchoolsData t = new SeattleSchoolsData(file);
		
	/*	System.out.println(t.getSchoolNames().toString());
		System.out.println(t.getPhoneBook());
		System.out.println(t.getAddressBook());
		System.out.println();*/
		
		//t.printMap();
		//System.out.println(t.getPhoneBook());
		//System.out.println();
		//t.printSortedPhoneBook();

		 /* 
		
		*/
		//System.out.println(t.toString());
		System.out.println(t.getDistances("Kimball"));
		System.out.println();
		System.out.println();
		System.out.println(t.findSchoolFromStreet("3200 23rd Ave. S"));
		System.out.println(t.findSchoolFromStreet("28th Ave NW"));
		System.out.println(t.findSchoolFromStreet("5th Ave. N"));
		t.printFiveClosestSchools("Kimball");
		System.out.println();
		t.printFiveClosestSchools("Chief Sealth Int'l");

	}

		
	

}
