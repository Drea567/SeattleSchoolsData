import java.util.*;

/*[Challenge Problem:
Implement a School class to store data about individual schools; 
implement a toString method and implement Comparable; 
use this class in your SeattleSchoolsData class.]*/
		
public class School {
	private String schoolName;
	private String schoolPhone;
	private String schoolAddress;
	private String schoolLat;
	private String schoolLong;
	private String schoolType;
	

	public School(String[] details){
	}
	public School(){
	}
	
	public String getName(){
		return schoolName;
	}
	public String getPhone(){
		return schoolPhone;
	}
	public String getAddress(){
		return schoolAddress;
	}
	public double getLat(){
		Double lat= new Double(this.schoolLat);
		return lat;
	}
	public double getLong(){
		Double lon = new Double(this.schoolLong);
		return lon;
	}
	public String getType(){
		return schoolType;
	}
	
	public void addName(String name){
		this.schoolName =  name;
	}
	public void addPhone(String phone){
		this.schoolPhone =  phone;
	}
	public void addAddress(String address){
		this.schoolAddress = address;
	}
	public void addLat(String lat){
		this.schoolLat = lat;
	}
	public void addLong(String lon){
		this.schoolLong = lon;
	}
	public void addType(String type){
		this.schoolType = type;
	}
	
	public String toString(){
		String output = String.format("%s, %s, %s, %s, %s", this.getPhone(), this.getAddress(), this.getLat(), this.getLong(), this.getType());
		return output;
	}
	

}
