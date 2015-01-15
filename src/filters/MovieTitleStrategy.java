/*******************************************************************************
 * @fileOverview  MovieTitleStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package filters;

import configreaders.IConfigReadStrategy;
import imdbalib.Movie;

/**
 * The Class MovieTitleStrategy.
 */
public class MovieTitleStrategy implements IFilterStrategy {

	private String config;
	private String pattern;
	
	public MovieTitleStrategy (String config, IConfigReadStrategy configReadStrategy) {
		this.pattern = config.toLowerCase();
		// TODO implement other strategy
	}
	
	@Override

	public boolean isMovieMatchFilter(Movie movie) {

		if (movie.getTitle().toLowerCase().contains(pattern)) {
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
