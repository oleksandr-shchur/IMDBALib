/*******************************************************************************
 * @fileOverview  MovieYearStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package filters;

import imdbalib.Movie;

/**
 * The Class MovieYearStrategy.
 * @author Oleksandr Shchur
 */
public class MovieYearStrategy implements IFilterStrategy {

	private String config;
	private String pattern;
	float yearStart;
	float yearEnd;
	
	/**
	 * Instantiates a new movie year strategy.
	 *
	 * @param config holds th years range, e.g. "1930-1940"
	 */
	public MovieYearStrategy (String config) {
		this.pattern = config;
		String[] a = config.split("-");
		yearStart = Float.parseFloat(a[0]);
		yearEnd = Float.parseFloat(a[1]);
	}
	
	/* (non-Javadoc)
	 * @see filters.IFilterStrategy#isMovieMatchFilter(imdbal.Movie)
	 */
	@Override

	public boolean isMovieMatchFilter(Movie movie) {
		float md = movie.getYearStart();
		
		if (yearStart <= md && md <= yearEnd) {
			return true;
		} else {
			return false; 
		}
	}

	/* (non-Javadoc)
	 * @see filters.IFilterStrategy#getConfig()
	 */
	@Override
	public String getConfig() {
		// TODO Auto-generated method stub
		return config;
	}
}
