/*******************************************************************************
 * @fileOverview  Utils.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;


/**
 * This is auxiliary class holding some static methods .
 *
 * @author Oleksandr Shchur
 */
public class Utils {
	
	/**
	 *  returns the position of year in IMDB-style like (1943).
	 *  Is quick optimized parser for offline IMDB  years
	 *
	 * @param str the str
	 * @return on success returns the year position in string, otherwise returns -1
	 */
	public static int getYearPosition(String str){
		for (int i = 0, len = str.length(); i < len - 5; ++i) {
			if (
					str.charAt(i) == '(' &&  
					str.charAt(i+1) >='1' &&
					str.charAt(i+1) <='2' &&
					str.charAt(i+2) >='0' &&
					str.charAt(i+2) <='9' &&
					str.charAt(i+3) >='0' &&
					str.charAt(i+3) <='9' &&
					str.charAt(i+4) >='0' &&
					str.charAt(i+4) <='9'
										
				) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Removes all double quotes from the string
	 * @param str String to be processed
 	 * @return string with all double quotes removed
	 */
	public static String removeQuotes(String str) {
		StringBuilder tmp = new StringBuilder();
		for (int i = 0, len = str.length(); i < len; ++i) {
			char ch = str.charAt(i);
			if (ch != '"') {
				tmp.append(ch);
			}				
		}
		return tmp.toString();
	}
	
	/**
	 * Reads file to a byte array
	 * @param aFileName Absolute path to a file
	 * @return byte array of file read
	 * @throws IOException
	 */
	public static byte[] readSmallBinaryFile(String aFileName) throws IOException {
	    Path path = Paths.get(aFileName);
	    return Files.readAllBytes(path);
	}
	
	/**
	 * converts string representation of a month to numerical one, for example  "june" -> 6
	 * @param monthStr string holding month in IMDB format
	 * @return integer ordinal of month
	 */
	public static Integer getMonth (String monthStr) {
		// January, February, March, April, May, June, July, August, September, October, November, December
		if (monthStr.length() <= 2) {
			return 13;
		}
		int res; 
		switch (monthStr.charAt(0)) {
			case 'J': res = (monthStr.charAt(1) == 'a')?1:((monthStr.charAt(2) == 'n')?6:7); break;
			case 'F': res = 2;break;
			case 'M': res = (monthStr.charAt(2) == 'r')?3:5;break;
			case 'A': res = (monthStr.charAt(1) == 'p')?4:8;break;
			case 'S': res = 9;break;
			case 'O': res = 10;break;
			case 'N': res = 11;break;
			case 'D': res = 12;break;
			default: res = 13;
		}
		//check
		switch (res) {
		case 1:  return (monthStr.compareToIgnoreCase("January") == 0)?1:13;
		case 2:  return (monthStr.compareToIgnoreCase("February") == 0)?2:13;
		case 3:  return (monthStr.compareToIgnoreCase("March") == 0)?3:13;
		case 4:  return (monthStr.compareToIgnoreCase("April") == 0)?4:13;
		case 5:  return (monthStr.compareToIgnoreCase("May") == 0)?5:13;
		case 6:  return (monthStr.compareToIgnoreCase("June") == 0)?6:13;
		case 7:  return (monthStr.compareToIgnoreCase("July") == 0)?7:13;
		case 8:  return (monthStr.compareToIgnoreCase("August") == 0)?8:13;
		case 9:  return (monthStr.compareToIgnoreCase("September") == 0)?9:13;
		case 10: return (monthStr.compareToIgnoreCase("October") == 0)?10:13;
		case 11: return (monthStr.compareToIgnoreCase("November") == 0)?11:13;
		case 12: return (monthStr.compareToIgnoreCase("December") == 0)?12:13;
		default: return 13;
		}
		
	}

	/**
	 * Converts arbitrary string representation of date to a floating-point year variable, for example "1 june 1943" -> 1943.5
	 * @param date string holding the date in IMDB format
	 * @return floating point value of the year  
	 */
	public static Float getYear (String date) {
		if (date.length() <= 3) {
			return -1.0f;
		}
		
		Float yearResult = -5.0f;
		// format:1964, .... 
		Integer year = parseYear(date);
		if (year >= 1000) {
			return (float )year;
		}
		
		// format:14 july 1943, ...
		if ((date.charAt(0) >= '1' && date.charAt(0) <= '3' && date.charAt(1) >= '0' && date.charAt(1) <= '9') || 
			((date.charAt(0) >= '1' && date.charAt(0) <= '9') && (date.charAt(1) < '0' || date.charAt(1) > '9'))) {
			String[] dateChunks = date.split(" ");
			// case 19?? or 194?
			if (dateChunks[0].trim().length() > 2){ // something wrong with format
				return -2.0f;
			}
			Integer day = new Integer(dateChunks[0].trim());
			Integer month = Utils.getMonth (dateChunks[1].trim());
			
			// case "17 April"
			if (dateChunks.length <= 2) {
				return -3.0f;
			}
			year = parseYear(dateChunks[2].trim());
			if (year <= 999) {
				return -4.0f;
			}
			Integer yearInt = Integer.parseInt(dateChunks[2].trim().substring(0,4));
			return new Float(yearInt + ((month-1) + (day-1)/30.0)/12.0); 
		}
		// case format: November 1934
		String[] dateChunks = date.split(" ");
		Integer month = Utils.getMonth (dateChunks[0].trim());
		if (dateChunks.length >= 2) {
			String yearTmp = dateChunks[1].trim();
			if (yearTmp.length() >=4) {
				year = parseYear(yearTmp.substring(0,4));
				if (month != 13) {
					return (float) ((float )year + (month - 1)/12.0);
				} else {
					return (float )year;
				}
			}
		}
		return yearResult;
	}
	
	/**
	 * Converts string representation of year to a numerical one.
	 * @param year String holding the year
	 * @return year as Integer or -1 on error
	 */
	public static Integer parseYear (String year) {
		if (year.length() >= 4 &&
			year.charAt(3) >= '0' && year.charAt(3) <= '9' &&
			year.charAt(2) >= '0' && year.charAt(2) <= '9' &&
			year.charAt(1) >= '0' && year.charAt(1) <= '9' &&
			year.charAt(0) >= '1' && year.charAt(0) <= '2') {
				return new Integer(year.substring(0,4));  
		}
		return -1;
	}

	/**
	 * Strictly matches two set of strings. If at least one string in set1 is in set 3 (exact match, then return true
	 * Is not optimized 
	 * @param set1 A Set holding first set of strings
	 * @param set2 A Set holding first set of strings
	 * @return true if there exists at least one string that is in Set1 and in Set2
	 */
	public static boolean strictMatch (Set<String> set1, Set<String> set2) {
		
		for(String s2:set2) {
			for(String s1:set1) {
				if (s1.equals(s2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the string that is between two tokens
	 * 
	 * @param source The string to be processed
	 * @param token1 First token
	 * @param token2 Second Token
	 * @return the string that is between two tokens
	 */
	public static String between(String source, String token1, String token2) {
		if (source == null) {
			return "";
		}
		int a, b; // positions of token 1 and 2
		if ((a = source.indexOf(token1)) == -1) {
			return "";
		}
		a += token1.length();
		if ((b = source.substring(a).indexOf(token2)) == -1) {
			return "";
		}
		b += a;
		if (a > b) {
			return ""; // incorrect token position
		}
		return source.substring(a, b);
	}

	/**
	 * Delete all between two tokens
	 * @param source String to be processed
	 * @param token1 First token
	 * @param token2 Second token
	 * @return the resulting string with cut down the portion between two tokens
	 */
	public static String deleteAllBetween(String source, String token1,	String token2) {
		int a, b; // positions of token 1 and 2
		if ((a = source.indexOf(token1)) == -1)
			return source;
		if ((b = source.indexOf(token2)) == -1)
			return source;
		if (a > b)
			return source; // incorrect token position, do nothing
		return source.substring(0, a)
				+ source.substring(b + token2.length(), source.length());
	}

	/**
	 * Gets all the strings as arraylist that are within two tokens in the
	 * text.
	 * @param text the text to extract substrings from
	 * @param leftToken the left token
	 * @param rightToken the right token
	 * @return result
	 */
	public static ArrayList<String> getStringsByTwoTokens(String text,	String leftToken, String rightToken) {
		ArrayList<String> res = new ArrayList<String>();
		// position of token left
		int a = 0;
		// position of token right
		int b = 0;
		while (a != -1 || b != -1) {
			if ((a = text.indexOf(leftToken, b)) == -1) {
				return res;
			}
			if ((b = text.indexOf(rightToken, a + leftToken.length())) == -1) {
				return res;
			}
			// incorrect token position
			if (a > b) {
				continue;
			}
			res.add(text.substring(a + leftToken.length(), b));
		}
		return res;
	}
}