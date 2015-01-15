/*******************************************************************************
 * @fileOverview  IConfigReadStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package configreaders;

import java.util.ArrayList;

/**
 * The Interface IConfigReadStrategy.
 * It creates the list of strings parsed from different sources, currently it is space separated string and text file
 * @author Oleksandr Shchur
 */
public interface IConfigReadStrategy {
	
	/**
	 * Read configuration.
	 * @author Oleksandr Shchur
	 * @param config The source of configuration: can be a list of keywords separated by spaces, or path to file where keywords are
	 * @return the array list containing keywords.
	 */
	ArrayList<String> readConfiguration(String config);
}
