/*******************************************************************************
 * @fileOverview  GenresParser.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package parsers;

import imdbalib.Movie;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import utils.Utils;

/**
 * The Class GenresParser.
 */
public class GenresParser {

	/**
	 * Updates movies with genres info.
	 *
	 * @author Oleksandr Shchur
	 * @param path A path to IMDB database
	 * @param movies input movies list
	 * @return updated movies list with genres info
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static LinkedList<Movie> addGenresInfo(String path, LinkedList<Movie> movies) throws IOException {

		byte[] dat = Utils.readSmallBinaryFile(path + "\\genres.list");
		
		//	obtain films mappesd to certificates
		Map<String, Set<String>> genres = ParserUtils.parseFile(dat, getStartPosition(dat));
		
		// iterate movies and append actors info
		for (Movie movie: movies) {
			String movieTitle = movie.getTitle(); 
			
			// add certificate into current film (being iterated)
			if (genres.containsKey(movieTitle)) {
				movie.setGenres(genres.get(movieTitle));
			}

		}
		
		return movies;
	}

	/**
	 * Finds the index of byte array representing movie list to skip the header.
	 *
	 * @param dat Byte array with file content
	 * @return the start position
	 */
	private static int getStartPosition(byte[] dat) {
		//8: THE GENRES LIST
		//==================

		for (int i = 0; i < dat.length; ++i) {
			if (				
					dat[i   ] == '8' &&
					dat[i+1 ] == ':' &&
					dat[i+2 ] == ' ' &&
					dat[i+3 ] == 'T' &&
					dat[i+4 ] == 'H' &&
					dat[i+5 ] == 'E') {
				return i+38; // offset point to last 0x0A symbol befire actual data
			}
		}
	
		return -1;
	}
	
	
}
