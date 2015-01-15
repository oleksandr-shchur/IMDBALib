/*******************************************************************************
 * @fileOverview  KeywordsStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import configreaders.IConfigReadStrategy;
import imdbalib.Movie;

public class KeywordsStrategy implements IFilterStrategy {

	private String config;
	private ArrayList<String> keywords;
	
	public KeywordsStrategy (String config, IConfigReadStrategy configReadStrategy) {
		this.config = config;
		keywords = configReadStrategy.readConfiguration(config);
	}
	
	
	/* (non-Javadoc)
	 * @see filters.IFilterStrategy#isMovieMatchFilter(imdbal.Movie)
	 */
	@Override
	/** 
	 * @param config "file path"
	 * */
	public boolean isMovieMatchFilter(Movie movie) {

		Set<String> presentKeywords = new HashSet<String>();
		Set<String> movieKeywords = movie.getKeywords();
		if (movieKeywords == null) {
			return false;
		}
		
		Iterator<String> iter = movieKeywords.iterator();
		while (iter.hasNext()) {
			String keyword = iter.next();
			for (String s:keywords) {
				if (keyword.equalsIgnoreCase(s)) {
					presentKeywords.add(keyword);
					break;
				}
			}
		}
		
		movie.setKeywords(presentKeywords);
		if (presentKeywords.size() >=1) {
			return true;
		} else {
			return false;	
		}
	}

	@Override
	public String getConfig() {
		// TODO Auto-generated method stub
		return config;
	}
}
