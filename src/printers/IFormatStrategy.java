/*******************************************************************************
 * @fileOverview  IFormatStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package printers;

import imdbalib.Movie;

import java.util.LinkedList;

/**
* This is an interface for different strategies of formatted output, like plain text or HTML 
* @author Oleksandr Shchur
*
*/
public interface IFormatStrategy {

	/**
	 * @param movies Movies to print
	 * @param filePath A Path to the file where output will be written
	 */
	public void print(LinkedList<Movie> movies, String filePath);
	
	
}
