/*******************************************************************************
 * @fileOverview  GenresStrategy.java
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

/**
 * The Class GenresStrategy.
 * @author Oleksandr Shchur
 */
public class GenresStrategy implements IFilterStrategy {

	private String config;
	private ArrayList<String> genres;
	
	public GenresStrategy (String config, IConfigReadStrategy configReadStrategy) {
		this.config = config;
		genres = configReadStrategy.readConfiguration(config);
	}
	
	@Override
	/** 
	 * @author Oleksandr Shchur
	 * @param config "file path"
	 * */
	public boolean isMovieMatchFilter(Movie movie) {

		Set<String> presentGenres = new HashSet<String>();
		Set<String> movieGenres = movie.getGenres();
		if (movieGenres == null) {
			return false;
		}
		
		Iterator<String> iter = movieGenres.iterator();
		while (iter.hasNext()) {
			String genre = iter.next();
			for (String s:genres) {
				if (genre.equalsIgnoreCase(s)) {
					presentGenres.add(genre);
					break;
				}
			}
		}
		
		movie.setGenres(presentGenres);
		if (presentGenres.size() >=1) {
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
