/*******************************************************************************
 * @fileOverview  MovieRating.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package analysers;

import imdbalib.Movie;

/**
 * The Class MovieRating.
 * @author Oleksandr Shchur
 */
public class MovieRating {
	public Movie movie;
	public Double rating;
	// TODO implement
	
	public MovieRating(Movie movie, Double rating) {
		this.movie = movie;
		this.rating = rating;
	}
	
}