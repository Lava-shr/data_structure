package assignment1;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;

public class random {

	public static void main(String[] args){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date t= new Date(0);
		System.out.println(dateFormat.format(t));
		
	}
	
	TreeMap<String, TreeMap<Object, Object>> myData =  new TreeMap<String, TreeMap<Object, Object>>();
	TreeMap<Object, Object> unikeyData = new TreeMap<Object, Object>();
	{
		unikeyData.put(1, "SomeValue");
	}
	{
		myData.put("asdf", unikeyData);
	}
	
}
