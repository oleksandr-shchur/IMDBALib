/*******************************************************************************
 * @fileOverview  Recommendation.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package examples;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import analysers.MovieRating;
import imdbalib.ImdbAL;
import imdbalib.Movie;

/**
 * The Class Recommendation.
 * Example, how IMDBAL can be used for giving movie recommendations (based upon keywords)
 * This approach has advantages over collaborative filtering, widely used in 2010-2015 years.
 * This can recommend rare films 
 * 
 */
public class Recommendation {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImdbAL imdbal = new ImdbAL("c:\\IMDB");
		imdbal.loadMovies();
		//imdbal.addActorInfo();
		//imdbal.addCertificateInfo();
		//imdbal.addGenresInfo();
		imdbal.addKeywordsInfo();
		
		//Map<Movie, Double> recommendList = imdbal.getRecommendedList("Robocop (1987)", 50);
		//Map<Movie, Double> recommendList = imdbal.getRecommendedList("Terminator 2: Judgment Day (1991)", 10);
		Map<Movie, Double> recommendList = imdbal.getRecommendedList("Equilibrium (2002)", 10);
		Iterator<Entry<Movie, Double>> iter = recommendList.entrySet().iterator();
		PriorityQueue <MovieRating> moviesRating = new PriorityQueue<MovieRating>(16, new MovieRatingComparator()); 
		while (iter.hasNext()) {
			Entry<Movie, Double> t = iter.next();
			if (t.getValue() < 0.01) {
				continue;
			}
			//MovieRating movieRating = this.new MovieRating();
			moviesRating.add(new MovieRating(t.getKey(), t.getValue()));
			
			
			
			//System.out.println("\n-------> "+t.getValue()+"\n"+t.getKey());
		}
		while (moviesRating.size() >=1) {
			MovieRating m = moviesRating.remove();
			System.out.println("\n-------> "+m.movie + "\n"+m.rating);
			
		}
	}
	
	
	private static class MovieRatingComparator implements Comparator<MovieRating>
	{
	    public int compare(MovieRating m1, MovieRating m2)
	    {
	        return ((m1.rating > m2.rating)?-1:1);
	    }
	};

}
