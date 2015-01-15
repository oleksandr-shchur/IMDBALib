/*******************************************************************************
 * @fileOverview  AnalyserKeywordPerYear.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package analysers;

import imdbalib.Movie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * The enty class for analytics of historical keywordcount, e.g. 
 * it can give an insight how often the <i> spy</i> keyword appears in movies at different years 
 * TODO normalization is needed.
 * @author Oleksandr Shchur 
 */
public class AnalyserKeywordPerYear {

	/**
	 * Gets the keyword per year.
	 * @author Oleksandr Shchur
	 * @param movies The list of movies to analyse. 
	 *        Movies should contain the information about the year and keyword list.
	 * @param keywords List of keywords 
	 * @return The Map that maps year to number of films produced in this year that contain any one of the <b> keywords</b> 
	 */
	public static Map<Integer, Integer> getKeywordPerYear(LinkedList<Movie> movies, ArrayList<String> keywords) {
		Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
		//Map<Integer, Integer> yearFrequency = new TreeMap<Integer, Integer>();
		for (Movie m:movies) {
			
			
			if (m.getKeywords() == null) {
				continue;
			}
			movieLabel:for (String movieKeyword : m.getKeywords()) {
				for (String parameterKeyword:keywords) {
					if (movieKeyword.equalsIgnoreCase(parameterKeyword)) {
						if (m.getYearStart() < 1800) {
							break;
						}
						int year = m.getYearStart().intValue();  
						if (result.containsKey(year)) {
							result.put(year, result.get(year) + 1);
						} else {
							result.put(year, 1);
						}
						break movieLabel;
					}
				}
			}
		}
		
		
		
		return result; 
	}
	
}
