/*******************************************************************************
 * @fileOverview  SimpleFileReaderStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package configreaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Implements a strategy of reading list of strings(keywords, movie titles, actors, etc.) from file.
 * @author Oleksandr Shchur
 */
public class SimpleFileReaderStrategy implements IConfigReadStrategy {

	/* (non-Javadoc)
	 * @see configreaders.IConfigReadStrategy#readConfiguration(java.lang.String)
	 */
	@Override
	public ArrayList<String> readConfiguration(String config) {
		ArrayList<String> res = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(new File(config));

			while (sc.hasNext()) {
				res.add(sc.next());
			}

			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
