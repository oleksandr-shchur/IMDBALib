/*******************************************************************************
 * @fileOverview  ActorBioStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package filters;

import imdbalib.Actor;
import imdbalib.Movie;

import java.util.Iterator;
import java.util.Set;

// TODO 
// import configreaders.IConfigReadStrategy;


/** 
 * @author Oleksandr Shchur
 * @param config Sets age range for actors, Example: "50.0-60.5"
 * 
 */
public class ActorBioStrategy implements IFilterStrategy {
	
	private String config;
	private String strategyName;
	
	public ActorBioStrategy (String config) {
		this.config = config;
		strategyName = "ActorBioStrategy";
	}
	
	
	/* (non-Javadoc)
	 * @see filters.IFilterStrategy#isMovieMatchFilter(imdbal.Movie)
	 */
	public boolean isMovieMatchFilter(Movie movie) {
	//	System.out.println(movie.toString());
		Set<Actor> actors = movie.getActors();
		if (actors == null) {
			return false;
		}
		float md = movie.getYearStart();
		
		String[] a = config.split("-");
		float la = Float.parseFloat(a[0]);
		float ua = Float.parseFloat(a[1]);
		
		float aa; 
		
		Iterator<Actor> iter = actors.iterator();
		while (iter.hasNext()) {
			Actor actor = iter.next();
			aa = actor.getAgeAt(md);
			if (la > aa || aa > ua) {
				iter.remove();
			}
		}

		if (actors.size() >=1) {
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
	
	@Override
	public String toString() {
		return strategyName;
	}
	
	
}
