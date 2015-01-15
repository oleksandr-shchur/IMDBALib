/*******************************************************************************
 * @fileOverview  KeywordsParser.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package parsers;

import imdbalib.Movie;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import utils.Utils;

/**
* A class for parsing "keywords.list" file and extracting keywords 
* @author Oleksandr Shchur
**/
public class KeywordsParser {

	/**
	 * @author Oleksandr Shchur
	 * @param path A path to IMDB database
	 * @param movies list of movies without keyword information
	 * @return updated movies list with keywords
	 * @throws IOException
	 */
	public static LinkedList<Movie> addKeywordsInfo(String path, LinkedList<Movie> movies) throws IOException {

		// obtain films mappesd to certificates
		//Map<String, Set<String>> keywords = parseKeywords(path);
		byte[] dat = Utils.readSmallBinaryFile(path + "\\keywords.list");
		Map<String, Set<String>> keywords = ParserUtils.parseFile(dat, getStartPosition(dat));
		
		
		// iterate movies and append actors info
		for (Movie movie: movies) {
			String movieTitle = movie.getTitle(); 
			
			// add certificate into current film (being iterated)
			if (keywords.containsKey(movieTitle)) {
				movie.setKeywords(keywords.get(movieTitle));
			}
		}
		return movies;
	}
	
	// 8: THE KEYWORDS LIST
	// ====================
	
	//com>***8: THE KE
	//YWORDS LIST*====
	//================
	//**"#1 Single" (2
	
	/**
	 * Finds the index of byte array representing movie list to skip the header.
	 *
	 * @param dat Byte array containing the whole file
	 * @return the start position of file containing movie
	 */
	private static int getStartPosition(byte[] dat) {
		//=================
		for (int i = 0; i < dat.length; ++i) {
			if (				
					dat[i   ] == '8' &&
					dat[i+1 ] == ':' &&
					dat[i+2 ] == ' ' &&
					dat[i+3 ] == 'T' &&
					dat[i+4 ] == 'H' &&
					dat[i+5 ] == 'E' &&
					dat[i+21] == '=' &&
					dat[i+40] == '=' &&
					dat[i+41] == 0x0A &&
					dat[i+42] == 0x0A)
			{
				return i+43; // offset point to last 0x0A symbol befire actual data
			}
		}
		return -1;
	}
	
	/**
	 * Gets the keywords statistics: how often the, keyword encounters
	 *
	 * @param path the path to IMDB
	 * @return the keywords statistics mapping the keyword to this keyword count
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static HashMap<String, Integer> getKeywordsStatistics (String path) throws IOException {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		Scanner sc = new Scanner(new File(path + "\\keywords.list"));    
	    while (sc.hasNext()) {
	    	if (sc.next().equalsIgnoreCase("keywords") && 
	    		sc.next().equalsIgnoreCase("in") &&
	    		sc.next().equalsIgnoreCase("use:")) {
	    		break;
	    	}
	    }
	    String number = "";
	    String keyword = "";
	    
	    while (sc.hasNext()) {
	    	keyword = sc.next();
	    	number = sc.next();
	    	if (number.equals("Submission")) {
	    		sc.close();
	    		return result;
	    	}
	    	result.put(keyword, Integer.parseInt(number.substring(1, number.length()-1)));
	    }
	    sc.close();
		return null;
	}
	
}
