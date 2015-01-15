/*******************************************************************************
 * @fileOverview  MoviesParser.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package parsers;

import imdbalib.Movie;

import java.io.IOException;
import java.util.LinkedList;

import utils.Utils;

/**
* A class for parsing "movies.list" file and extracting movies titles, and years 
* @author Oleksandr Shchur
**/
public class MoviesParser {
	
	/**
	 * Parses and returns the initial seed of films. Later this list can be updated by other methods, 
	 * but this one has to be called the first.
	 * @author Oleksandr Shchur
	 * @param path A path to IMDB database
	 * @return list of movies
	 * @throws IOException
	 */
	public static LinkedList<Movie> parseMovies(String path) throws IOException {
		LinkedList<Movie> res = new LinkedList<Movie>(); 
		String moviesPath = path + "\\movies.list"; 
		byte[] dat = Utils.readSmallBinaryFile(moviesPath);
		byte ch;

		int startPosition = getStartPosition(dat);
	
		// search the entire file
		int size = dat.length;
		int pos = startPosition;
		String title;
		StringBuilder years = new StringBuilder();
		int yearStart = -1;
		int yearEnd;
		
		//"'Untold" (2004)                                        2004-2005
		//"'Wag kukurap" (2004)                                   2004-????
		//"'Wag kukurap" (2004) {Her Lover}                       2006
		//"'Way Out" (1961)                                       1961-????
		// hay tomate" (2003) {(2006-10-11)}
		// 0x09 is TAB symbol and is delimite between film and year data in the file
		while (pos < size) {
			int startPos = pos;
			while (++pos < size && (ch = dat[pos]) != 0x09);

			if (pos >= size) {
				break;
			}

			title = new String(dat, startPos+1, pos-startPos); 
			
			while ((ch = dat[++pos]) == 0x09);
			years.append((char)ch);
			while ((ch = dat[++pos]) != 0x0A) {
				years.append((char)ch);
			}
			// here we have film and years strings
			// let's parse years string
			try {
				yearStart = new Integer(years.toString().substring(0, 4));
			} catch (NumberFormatException e) {
				yearStart = -1;
			}
			
			if (years.length() >= 6) { // 1994-???? or 1994-2005 format type
				
				if (years.toString().charAt(5) == '?') {
					yearEnd = -1;
				} else {
					yearEnd = new Integer(years.toString().substring(5, 9));
				}
			} else {
				yearEnd = -2;
			}
			Movie movie = new Movie();
			movie.setYearStart((float) yearStart);
			movie.setYearEnd((float) yearEnd);
			movie.setTitle(title.toString().trim());
			
			res.add(movie);
			years.setLength(0);
		} 
		return res;
	}
	
	/**
	 * Finds the index of byte array representing movie list to skip the header.
	 * @author Oleksandr Shchur
	 * @param dat byte array containing the movies list file
	 * @return the start position (skip the header of the offline IMDB file)
	 */
	private static int getStartPosition(byte[] dat) {
		for (int i = 0; i < dat.length; ++i) {
			if (				
					dat[i   ] == 'M' &&
					dat[i+1 ] == 'O' &&
					dat[i+2 ] == 'V' &&
					dat[i+3 ] == 'I' &&
					dat[i+4 ] == 'E' &&
					dat[i+5 ] == 'S') {
				return i+24; // offset point to last 0x0A symbol befire actual data
			}
		}
		return -1;
	}

}
