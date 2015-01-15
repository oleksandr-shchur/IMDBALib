/*******************************************************************************
 * @fileOverview  ActorsParser.java
 * @author Quadevoluter
 * @date Jan 15, 2015
 * @param (C) Quadevoluter
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package parsers;

import imdbalib.Actor;
import imdbalib.Cast;
import imdbalib.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import utils.Utils;

/**
* A class for parsing "actors.list" and "actresses" 
* @author Oleksandr Shchur
**/
public class ActorsParser {

	public ActorsParser() {
	}
	
	/**
	 * A structure holding the single info about an actor in a film
	 * @author Oleksandr Shchur
	 *
	 */
	private class Role {
		/**
		 * a film where actor plays
		 */
		public String film;
		/**
		 * A title of role that actor plays
		 */
		public String roleTitle;
		/**
		 * A billing position for this actor in this film
		 */
		public Integer billingPosition;
	}

	/**
	 * The most important method. It updates films with actors info and actors are updated with biographies 
	 * 
	 * @author Oleksandr Shchur@param path A path to IMDB database
	 * @param movies movie list without actor info
	 * @return movies list with added actors info
	 * @throws IOException
	 */
	public LinkedList<Movie> addActorInfo(String path, LinkedList<Movie> movies) throws IOException {
		// Prepare Actors biographies
		Map<String, Float> actorBiographies = BiographiesParser.parseActorsBiographies(path);
		
		// obtain films mapped to actors
		Map<String, Cast> actorsFilmography = getFilmsStarring(path, Actor.Gender.MALE, actorBiographies);
		Map<String, Cast> actressesFilmography = getFilmsStarring(path, Actor.Gender.FEMALE, actorBiographies);
		
		// iterate movies and append actors info
		for (Movie movie: movies) {
			String movieTitle = movie.getTitle(); 
			
			// add actor info into current film (film that is being iterated)
			// first do for males. Uf no males are in cast, create emty datastructures and 
			// then merge it with female cast 
			
			if (actorsFilmography.containsKey(movieTitle)) {
				movie.setCast(actorsFilmography.get(movieTitle));
			} else {
				Cast cast = new Cast();
				cast.actors = new HashSet<Actor>();
				cast.actorsOrder = new TreeMap<String, Integer>();
				cast.actorsRoles = new TreeMap<String, String>();
				movie.setCast(cast);
			}

			// merge with female cast
			if (actressesFilmography.containsKey(movieTitle)) {
				movie.getCast().addAll(actressesFilmography.get(movieTitle));
			}
		}
		
		return movies;
	}
    
	/**
	 *  film mapped on the cast (cast has set of actors).
	 * @author Oleksandr Shchur
	 * @param path the path to offline IMDB database
	 * @param gender the gender of actor/actress (the same method ids used for actors and actresses)
	 * @param actorBiographies the actor biographies (map holding actor name and year of birth)
	 * @return the films starring films mapped to cast (actors and actresses)
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Map<String, Cast> getFilmsStarring(String path, Actor.Gender gender, Map<String, Float> actorBiographies) throws IOException {
		
		Map<String, Cast> starring = new HashMap<String, Cast>();
		 
		byte[] dat;
		
		if (gender == Actor.Gender.FEMALE) {
			dat = Utils.readSmallBinaryFile(path + "\\actresses.list");
		} else {
			dat = Utils.readSmallBinaryFile(path + "\\actors.list"); 
		}
		 
		byte chPrev, ch;
		chPrev = dat[0];

		int startPosition = getStartPosition(dat);
		int markerCurr = 0;
		int markerPrev = startPosition;
		
		// search the entire file
		Cast cast;
		
		for (int i = startPosition, size = dat.length; i < size; ++i) {
			ch = dat[i];
			// new actor found
			if (ch == 0x0A && chPrev == 0x0A) {
				markerCurr = i;
				// chunk gotcha!
				byte[] line = Arrays.copyOfRange(dat, markerPrev, markerCurr);
				int idx=0;
				int lenMinusOne = line.length - 1;
				while (idx < lenMinusOne && line[++idx] != 0x09);
				String actorName = (new String(Arrays.copyOfRange(line, 0, idx))).trim();
				ArrayList<Role> filmsWithActor = getFilms(Arrays.copyOfRange(line, idx, lenMinusOne+1));
				
				for (Role role:filmsWithActor) {
					cast = starring.get(role.film);
					if (cast == null) { // if actor field was not initialize yet, init
						cast = new Cast();
						cast.actors = new HashSet<Actor>();
						cast.actorsRoles = new TreeMap<String, String>();
						cast.actorsOrder = new TreeMap<String, Integer>();
						
						starring.put(role.film, cast);
					}
					Actor actor = new Actor(actorName, gender);
					if (actorBiographies != null && actorBiographies.containsKey(actorName)) {
						actor.setBirthDate(actorBiographies.get(actorName));
					}
					cast.actors.add(actor);
					if (role.roleTitle != null) {
						cast.actorsRoles.put(actorName, role.roleTitle);	
					}
					if (role.billingPosition != null) {
						cast.actorsOrder.put(actorName, role.billingPosition);	
					}
				}
				markerPrev = markerCurr;
			}
			chPrev = ch; 
		}
		return starring;
	}
	
	
	/**
	 * Gets the start position of file containing information to be parsed 
	 * @author Oleksandr Shchur
	 * @param dat the Byte array holding the file with film cast
	 * @return the start position in this file
	 */
	private static int getStartPosition(byte[] dat) {
		for (int i = 0; i < dat.length; ++i) {
			if (				
					dat[i   ] == 0x73 &&
					dat[i+1 ] == 0x20 &&
					dat[i+2 ] == 0x0A &&
					dat[i+3 ] == '-' &&
					dat[i+4 ] == '-' &&
					dat[i+5 ] == '-' &&
					dat[i+6 ] == '-' &&
					dat[i+7 ] == 0x09 &&
					dat[i+8 ] == 0x09 &&
					dat[i+9 ] == 0x09 &&
					dat[i+10] == '-' &&
					dat[i+11] == '-' &&
					dat[i+12] == '-' &&
					dat[i+13] == '-' &&
					dat[i+14] == '-' &&
					dat[i+15] == '-'	) {
				//dat[i+15] = 0x0A; // mark the beginning
				return i+15;
			}
		}
		return -1;
	}

	/**
	 *  Get roles list for one actor in the file actors.list
	 *  holds many speciic fixes, because offline IMDB files are tricky to parse
	 * @author Oleksandr Shchur
	 * @param data the data
	 * @return the films
	 */
	private ArrayList<Role> getFilms(byte[] data) {
		 
		ArrayList<Role> roles = new ArrayList<Role>();	
		String text = new String(data).trim();
		String[] lines = text.split("\t\t\t");
		//System.out.println(text);

		//String s1 =// Jeg fandt en sang põ vejen (1977) (TV)  [Himself]
		// "Matador" (1978) {Brikkerne (#3.2)}  [Jernbanearbejder]
		// Hondo (1953)  [Johnny Lowe]  <9>
		// Shaheed (1965)  <37>
		// Jôniksen vuosi (1977)  (uncredited)  [M.Sc. (Econ.) Bergstr¡m]
		// Youngsville (????)  [Robert]  <7>
		// "To fag frem" (1993) {(#4.11)}  [Himself - Blomsterbinder]
		// "Hotel Cösar" (1998) {(#22.40)}  (credit only)  [Juan Carlos Brubak]  <13>
		// The Talented Mr. Stone (2001) (V)  (as Braden Stone)
		// Surrender (2008) (V) {{SUSPENDED}}  [Roxy]
		// "The Car's the Star" (1994) {Mini}  [Herself - Mini Owner]  <10>
		
		for (String line : lines) {
			// scan line from the end
			// stop when found tokens: "}", "(TV)", "(V)", "(1234)", "(????)",
			//"Aquý hay tomate" (2003) {(2006-10-11)}  [Herself]
			//"Aquý hay tomate" (2003) {(2006-10-11)}
			
			int i = line.length();
			do {
				--i;
				if (i <= 0) {
					break;
				}
				char ch = line.charAt(i);
				int pos = i;
				if (ch == ']') { // skip
					while (line.charAt(i--) != '[') {
						if (i < 0) {//fix for Inki Toh Aisi Ki Thaisi (2011) (V)  (as Moin Ali ]Jaan])  <37>
							i = pos-1;
							break;
						}
					}
				}
				ch = line.charAt(i);
				
				if (ch == '}') { // "{"
					break;
				}
				if (ch == ')') {
					char ch1 = line.charAt(i-1);
					char ch2 = line.charAt(i-2);
					char ch3 = line.charAt(i-3);
					if ( ch1 == 'G' && ch2 == 'V' && ch3 == '(') { // "(VG)"
						break;
					}
					if ( ch1 == 'V') { // "(V)"
						if (ch2 == '(' || (ch2 == 'T' && ch3 == '(')) { // "(TV)"
							break;
						}
					}
					char ch4 = line.charAt(i-4);
					char ch5 = line.charAt(i-5);
					if (ch5 == '(' && 
							(
								(ch1 == '?' || (ch1 >= '0' && ch1 <= '9')) &&
								(ch2 == '?' || (ch2 >= '0' && ch2 <= '9')) &&
								(ch3 == '?' || (ch3 >= '0' && ch3 <= '9')) &&
								(ch4 == '?' || (ch4 >= '0' && ch4 <= '9'))
							)							
						) {
						break;
					}
				}
			} while (i > 0);
			Role role = new Role();
			role.film = line.substring(0, i+1).trim();
			roles.add(role);
			
			// get role and cast number info
			// get first from []  and last from <>
			// TODO optimize speed
			// TODO refactor/extract method
			String actorRole = Utils.between(line.substring(i+1, line.length()), "[", "]");
			ArrayList<String> tmp = Utils.getStringsByTwoTokens(line.substring(i+1, line.length()), "<", ">");
			int tmpSize = tmp.size();
			String castOrdinal = "";
			if (tmpSize >= 1) {
				castOrdinal = tmp.get(tmp.size()-1);
			}
			
			if (actorRole.length()>=1) {
				role.roleTitle = actorRole; 
			}
			if (castOrdinal.length()>=1) {
				try {
					role.billingPosition = Integer.parseInt(castOrdinal);
				} catch (Exception e) {
					
				}
			}
		} 
		return roles;
	}
	
	
}
