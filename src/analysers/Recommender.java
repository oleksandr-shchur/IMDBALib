/*******************************************************************************
 * @fileOverview  Recommender.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package analysers;

import imdbalib.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import examples.Recommendation;
import parsers.KeywordsParser;

/**
 * The simple recommender. It takes a movie as template for keywords and search for movies having the same set of keywords.
 * This implementation does not take into account the similarity between different keywords, 
 * so <i>weapon</i> and <i>weapons</i> are different keywords.
 * @author Oleksandr Shchur 
 */
public class Recommender {

	private HashMap<String, Integer> keywordsFrequencies;
	private long keywordsTotalNum;
	
	/**
	 * Inits the Recommender, and gathering the statistics of keywords initing <b>keywordsFrequencies </b>.
	 * @author Oleksandr Shchur
	 * @param path The path to IMDB offline database, e.g. <b>C:\\IMDB</b>
	 */
	public Recommender(String path) {
		// TODO  create parser for keywords
		try {
			keywordsFrequencies = KeywordsParser.getKeywordsStatistics(path);
			keywordsTotalNum = 0;
			
			Iterator<Entry<String, Integer>> iter = keywordsFrequencies.entrySet().iterator();
			while (iter.hasNext()) {
				keywordsTotalNum += iter.next().getValue();
			}
			System.out.println(keywordsTotalNum);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Returns N-Best list of films recommended to the given one
	 * @author Oleksandr Shchur
	 * @param movies List of Movie objects containing keywords.
	 * @param movieTitle The title of the film serving as the template. 
	 *        This should be in a format of IMDB, including year, e.g. <i>"Equilibrium (2002)" </i> 
	 * @param maxNumOfRecommended Limit the number of movies found by this number. (not implemented yet)
	 * @return The result of recommendation search: movie mapped to rank.
	 */
	public Map<Movie, Double> getRecommendedList(LinkedList<Movie> movies, String movieTitle, int maxNumOfRecommended) {
		// work only with films that contains the keywords in the requested film
		// Find film that is requested
		Movie requestedMovie = null;
		for (Movie m:movies) {
			if (m.getTitle().equals(movieTitle)) {
				requestedMovie = m;
				break;
			}
		}

		// movie was not found - return nothing
		if (requestedMovie == null) {
			return null;
		}
		
		// get keywords of movie that we want to find recommendations
		Set<String> requestedKeyWords = requestedMovie.getKeywords();
		
		Map<String, Double> requestedMovieKeywordWeights = getKeywordsWeights(requestedMovie);
		
		// find all films that have at least one of these keywords
		Map<Movie, Double> rating = new HashMap<Movie, Double>();
		Set<String> currentKeywords = new TreeSet<String>(); 
		double similarity;
		double totalKeywordWeightInRequestedMovie = 0.0;
		double totalKeywordWeightInCandidateMovie = 0.0;
		
		for (Movie m : movies) {
			if (m.getKeywords() == null || m.getKeywords().isEmpty()) {
				continue; // optimize empty keyword comparison
			}
			currentKeywords = m.getKeywords();
			similarity = 0.0;
			totalKeywordWeightInRequestedMovie = 0.0;
			totalKeywordWeightInCandidateMovie = 0.0;
			
			for (String s : requestedKeyWords) {
				if (currentKeywords.contains(s)) {
					// TODO add something to rating
					Map<String, Double> currentMovieKeywordWeights = getKeywordsWeights(m);
					
					Iterator<Entry<String, Double>> iter = requestedMovieKeywordWeights.entrySet().iterator();
					while (iter.hasNext()) {
						// find intersection
						Entry<String, Double> requestedKeywordItem = iter.next();
						String kword = requestedKeywordItem.getKey();
						if (currentMovieKeywordWeights.containsKey(kword)) {
							totalKeywordWeightInRequestedMovie += requestedMovieKeywordWeights.get(kword);
							totalKeywordWeightInCandidateMovie += currentMovieKeywordWeights.get(kword); 
							
							//similarity += Math.sqrt(currentMovieKeywordWeights.get(kword) * requestedMovieKeywordWeights.get(kword)); 
						}
						
					}
					// we have got similarity, now this movie is processed, break
					break;
				}
			}
			similarity = Math.min(totalKeywordWeightInRequestedMovie, totalKeywordWeightInCandidateMovie);
			// here we have similarity
			rating.put(m, similarity);
		}
		return rating;
	}

	/**
	 * Gets the keywords weights.
	 * @author Oleksandr Shchur
	 * @param movie the movie
	 * @return the keywords weights
	 */
	private Map<String, Double> getKeywordsWeights (Movie movie) {
		Map<String, Double> result = new TreeMap<String, Double>();
		double scaling = 0.0; 
		for (String keyword : movie.getKeywords()) {
			if (!keywordsFrequencies.containsKey(keyword)) {
				continue;
			}
			scaling += ((double ) keywordsTotalNum) / ((double )keywordsFrequencies.get(keyword));
		}
		
		for (String keyword : movie.getKeywords()) {
			if (!keywordsFrequencies.containsKey(keyword)) {
				continue;
			}
			result.put(keyword, (((double ) keywordsTotalNum) / ((double )keywordsFrequencies.get(keyword))) / scaling);
		}

		return result;
	}
	
	/**
	 * Gets the keyword per year.
	 * @author Oleksandr Shchur
	 * @param movies The list of movies to analyse. 
	 *        Movies should contain the information about the year and keyword list.
	 * @param keywords List of keywords 
	 * @return The Map that maps year to number of films produced in this year that contain any one of the <b> keywords</b> 
	 */
	public static Map<Integer, Integer> getKeywordPerYear(LinkedList<Movie> movies, ArrayList<String> keywords) {
		Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
		//Map<Integer, Integer> yearFrequency = new TreeMap<Integer, Integer>();
		for (Movie m:movies) {
			
			
			if (m.getKeywords() == null) {
				continue;
			}
			movieLabel:for (String movieKeyword : m.getKeywords()) {
				for (String parameterKeyword:keywords) {
					if (movieKeyword.equalsIgnoreCase(parameterKeyword)) {
						if (m.getYearStart() < 1800) {
							break;
						}
						int year = m.getYearStart().intValue();  
						if (result.containsKey(year)) {
							result.put(year, result.get(year) + 1);
						} else {
							result.put(year, 1);
						}
						break movieLabel;
					}
				}
			}
		}
		
		
		
		return result; 
	}
	
	
	public static void main(String[] args) {
		Recommendation.main(args);
	}
	
	
}
