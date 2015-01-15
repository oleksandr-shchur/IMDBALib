/*******************************************************************************
 * @fileOverview  IFilterStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package filters;

import imdbalib.Movie;

/**
 * The Interface IFilterStrategy.
 * 
 */
public interface IFilterStrategy {
	
	/**
	 * Checks if movie filter is matched.
	 * @author Oleksandr Shchur
	 * @param movie the movie object
	 * @return true, if is movie match filter. The filter srategy is defined by inherited strategies
	 * 
	 */
	boolean isMovieMatchFilter(Movie movie);
		
	String toString();
	String getConfig();
}
