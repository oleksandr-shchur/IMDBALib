/*******************************************************************************
 * @fileOverview  BiographiesParser.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import utils.Utils;

/**
* A class for parsing "biographies.list" file 
* @author Oleksandr Shchur
**/
public class BiographiesParser {

	/**
	 * returns a basic bio for ann aactor holding name and date of birth.
	 * If databirth is absent then this actro is not in this map.
	 * @author Oleksandr Shchur
	 * @param path
	 * @return a map that maps bate of birth of an actor on his/her name
	 * @throws FileNotFoundException
	 */
	public static Map<String, Float> parseActorsBiographies(String path) throws FileNotFoundException {
		HashMap<String, Float> data = new HashMap<String, Float>();
	    Scanner sc = new Scanner(new File(path+"\\biographies.list"));    
	    String currentName = "";
	    String currentDate = "";
	    
	    while (sc.hasNextLine())
	    {
	    	//if (i++>40000) break;
	    	String currentLine = sc.nextLine();
	    	// name detected
	    	if (currentLine.startsWith("NM:")){
	    		currentName = currentLine.substring(3).trim();
	    		continue;
	    	}
	    	// Date of birth detected
	    	if (currentLine.startsWith("DB:")){
	    		currentDate = currentLine.substring(3).trim();
	    		continue;
	    	}
	    	// detect next
	    	if (currentLine.startsWith("------")) {
	    		if (currentDate != "" && currentName != "") {
	    			Float yearTmp = Utils.getYear(currentDate);
	    			if (!(yearTmp>=1800 && yearTmp<=2015)) {
	    				continue;
	    			} else {
	    				//System.out.println("NM: " + currentName + " DB:" + yearTmp);
	    				data.put(currentName, yearTmp);
	    				currentName = "";
	    	    		currentDate = "";
	    			}
	    		}
	    	}
	    }
		sc.close();
		return data;
	}
}
