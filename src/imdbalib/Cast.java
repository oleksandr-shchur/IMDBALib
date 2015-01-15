/*******************************************************************************
 * @fileOverview  Cast.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package imdbalib;

import java.util.Map;
import java.util.Set;

/** one Cast per one film, contains info about actors, their role names and ordinal number in cast list */
public class Cast {
	/** Set of actors in the film */
	public Set<Actor> actors;
	
	/**maps actor name on his/her role title     */
	public Map<String, String> actorsRoles;
	
	/**maps actor name on his/her role ordinal number in cast list     */
	public Map<String, Integer> actorsOrder;

	public void addAll(Cast cast) {
	
		this.actors.addAll(cast.actors);
		this.actorsRoles.putAll(cast.actorsRoles);
		this.actorsOrder.putAll(cast.actorsOrder);
	}
	
}
