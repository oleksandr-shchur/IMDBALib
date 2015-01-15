/*******************************************************************************
 * @fileOverview  CertificatesParser.java
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
* A class for parsing "certificates.list" file 
* @author Oleksandr Shchur
**/
public class CertificatesParser {

	/**
	 * Updates movies list with certificate info
	 * @author Oleksandr Shchur
	 * @param path A path to IMDB database
	 * @param movies input movies list
	 * @return updated movies list with certificate info
	 * @throws IOException
	 */
	public static LinkedList<Movie> addCertificateInfo(String path, LinkedList<Movie> movies) throws IOException {

		byte[] dat = Utils.readSmallBinaryFile(path + "\\certificates.list");
		
		//	obtain films mappesd to certificates
		Map<String, Set<String>> certificates = ParserUtils.parseFile(dat, getStartPosition(dat));
		
		// iterate movies and append actors info
		for (Movie movie: movies) {
			String movieTitle = movie.getTitle(); 
			
			// add certificate into current film (being iterated)
			if (certificates.containsKey(movieTitle)) {
				movie.setCertificates(certificates.get(movieTitle));
			}

		}
		
		return movies;
	}

	/**
	 * Finds the index of byte array representing movie list to skip the header.
	 * @author Oleksandr Shchur
	 * @param dat the dat
	 * @return the start position
	 */
	private static int getStartPosition(byte[] dat) {
		//=================
		for (int i = 0; i < dat.length; ++i) {
			if (				
					dat[i   ] == '=' &&
					dat[i+1 ] == '=' &&
					dat[i+2 ] == '=' &&
					dat[i+3 ] == '=' &&
					dat[i+4 ] == '=' &&
					dat[i+5 ] == '=') {
				return i+18; // offset point to last 0x0A symbol befire actual data
			}
		}
		return -1;
	}


}
