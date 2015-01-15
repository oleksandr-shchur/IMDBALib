/*******************************************************************************
 * @fileOverview  ActorGenderStrategy.java
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

public class ActorGenderStrategy implements IFilterStrategy {

	private String config;
	private String strategyName;
	
	/**
	 * Instantiates a new actor gender strategy.
	 * @author Oleksandr Shchur
	 * @param config Configuration string for filter, "F" or "M"
	 */
	public ActorGenderStrategy (String config) {
		strategyName = "ActorGenderStrategy";
		if (config == "M") {
			this.config = "[M]";
		} else {
			this.config = "[F]";
		}
	}
	

	/* (non-Javadoc)
	 * @see filters.IFilterStrategy#isMovieMatchFilter(imdbal.Movie)
	 */
	public boolean isMovieMatchFilter(Movie movie) {
		Set<Actor> actors = movie.getActors();
		if (actors == null) {
			return false;
		}
		
		Iterator<Actor> iter = actors.iterator();
		while (iter.hasNext()) {
		    Actor actor = iter.next();
			if (!actor.getGender().equals(config)) {
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
