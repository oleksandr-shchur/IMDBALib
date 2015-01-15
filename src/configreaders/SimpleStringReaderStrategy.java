/*******************************************************************************
 * @fileOverview  SimpleStringReaderStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package configreaders;

import java.util.ArrayList;

/**
 * Implements a strategy of reading list of strings(keywords, movie titles, actors, etc.) from file.
 * @author Oleksandr Shchur
 */
public class SimpleStringReaderStrategy implements IConfigReadStrategy {

	/* (non-Javadoc)
	 * @see configreaders.IConfigReadStrategy#readConfiguration(java.lang.String)
	 */
	@Override
	public ArrayList<String> readConfiguration(String config) {
		String[] ss = config.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		for (String s : ss) {
			res.add(s);
		}
		return res;
	}

}
