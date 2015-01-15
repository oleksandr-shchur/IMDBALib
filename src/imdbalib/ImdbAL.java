/*******************************************************************************
 * @fileOverview  ImdbAL.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package imdbalib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import analysers.Recommender;
import filters.IFilterStrategy;
import parsers.ActorsParser;
import parsers.CertificatesParser;
import parsers.GenresParser;
import parsers.KeywordsParser;
import parsers.MoviesParser;
import printers.IFormatStrategy;

// TODO: Auto-generated Javadoc
/**
 * The Class ImdbAL.
 */
public class ImdbAL{

	/** The imdb path. */
	private String imdbPath;
	
	/** The movies. */
	private LinkedList<Movie> movies;
	
	/** The operations log. */
	private StringBuilder operationsLog; 
	
	/**
	 * Instantiates a new imdb al.
	 *
	 * @param path the path
	 */
	public ImdbAL(String path) {
		imdbPath = path;
		operationsLog = new StringBuilder(getCurrentTime() + "Init\n");
	}
	
	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	private String getCurrentTime() {
		return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss -> ").format(Calendar.getInstance().getTime());
	}
	
	/**
	 * Gets the movies.
	 *
	 * @return the movies
	 */
	public LinkedList<Movie> getMovies() {
		return movies;
	}
	
	
	/**
	 * Parse movies.list 
	 */

	public void loadMovies() {
		try {
			operationsLog.append(getCurrentTime() + "Load movies started\n");
			movies = MoviesParser.parseMovies(imdbPath);
			operationsLog.append(getCurrentTime() + "Load movies ended\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Adds the actor info.
	 */
	public void addActorInfo () {
		try {
			operationsLog.append(getCurrentTime() + "Load actors info started\n");
			ActorsParser actorsParser = new ActorsParser(); 
			movies = actorsParser.addActorInfo(imdbPath, movies);
			operationsLog.append(getCurrentTime() + "Load actors info ended\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
 	
	/**
	 * Adds the certificate info.
	 */
	public void addCertificateInfo () {
		try {
			operationsLog.append(getCurrentTime() + "Load certificate info started\n");
			movies = CertificatesParser.addCertificateInfo(imdbPath, movies);
			operationsLog.append(getCurrentTime() + "Load certificate info ended\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Adds the keywords info.
	 */
	public void addKeywordsInfo () {
		try {
			operationsLog.append(getCurrentTime() + "Load keyword info started\n");
			movies = KeywordsParser.addKeywordsInfo(imdbPath, movies);
			operationsLog.append(getCurrentTime() + "Load keyword info endeded\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Filter.
	 *
	 * @param filterStrategy the filter strategy
	 */
	public void filter(IFilterStrategy filterStrategy) {
		operationsLog.append(getCurrentTime() + "Filter with" + filterStrategy.toString() + "strategy and config:\"" + filterStrategy.getConfig() +"\" started\n");
		Iterator<Movie> iter = movies.iterator();
		while (iter.hasNext()) {
		    Movie movie = iter.next();
			if (!filterStrategy.isMovieMatchFilter(movie)) {
		        iter.remove();
		    }
		}
		operationsLog.append(getCurrentTime() + "Filter with" + filterStrategy.toString() + "strategy and config:\"" + filterStrategy.getConfig() +"\" ended\n");
	}
	
	/**
	 * Prints the.
	 *
	 * @param formatStrategy the format strategy
	 * @param filePath the file path
	 */
	public void print(IFormatStrategy formatStrategy, String filePath) {
		operationsLog.append(getCurrentTime() + "Formatting output with" + formatStrategy.toString() + "strategy to file: \"" + filePath +"\" started\n");
		formatStrategy.print(movies, filePath);
		operationsLog.append(getCurrentTime() + "Formatting output with" + formatStrategy.toString() + "strategy to file: \"" + filePath +"\" ended\n");
	}
	
	/**
	 * Gets the log.
	 *
	 * @return the log
	 */
	public String getLog() {
		return operationsLog.toString();
	}
	
	/**
	 * Save.
	 *
	 * @param path the path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void save(String path) throws IOException {
		FileOutputStream fout = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(this);
		oos.close();
	}

	/**
	 * Gets the recommended list.
	 *
	 * @param movieTitle the movie title
	 * @param maxNumOfRecommended the max num of recommended
	 * @return the recommended list
	 */
	public Map<Movie, Double> getRecommendedList (String movieTitle, int maxNumOfRecommended) {
		Recommender recommender = new Recommender(imdbPath);
		
		
		return recommender.getRecommendedList(movies, movieTitle, maxNumOfRecommended);
	}

	/**
	 * Adds the genres info.
	 */
	public void addGenresInfo() {
		try {
			operationsLog.append(getCurrentTime() + "Load genres info started\n");
			movies = GenresParser.addGenresInfo(imdbPath, movies);
			operationsLog.append(getCurrentTime() + "Load genres info endeded\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
