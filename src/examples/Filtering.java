/*******************************************************************************
 * @fileOverview  Filtering.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/

package examples;

import java.util.LinkedList;




import configreaders.SimpleFileReaderStrategy;
import configreaders.SimpleStringReaderStrategy;
import filters.ActorBioStrategy;
import filters.ActorGenderStrategy;
import filters.CertificateStrategy;
import filters.GenresStrategy;
import filters.IFilterStrategy;
import filters.KeywordsStrategy;
import filters.MovieTitleStrategy;
import imdbalib.ImdbAL;
import imdbalib.Movie;

// TODO: Auto-generated Javadoc
/**
 * The Class Filtering.
 * @author Oleksandr Shchur
 */
public class Filtering {

	/**
	 * The main method.
	 * Here is an example, how to use IMDBAL for searching the films in the offline IMDB
	 * @author Oleksandr Shchur
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		// Instanciate IMDBAL with the path to offline IMDB database
		ImdbAL imdbal = new ImdbAL("c:\\IMDB");
		// You can skip any of these initializations
		imdbal.loadMovies();
		imdbal.addActorInfo();
		imdbal.addCertificateInfo();
		imdbal.addGenresInfo();
		imdbal.addKeywordsInfo();

		// Now apply some filters
		
		// Set the gender of cast ("F" or "M")
		//imdbal.filter(new ActorGenderStrategy("M"));
		
		// Set the age range of actor cast using minus char for range, e.g. ("20-30")
		//imdbal.filter(new ActorBioStrategy("40-50"));
		
		// Set the char sequencies that should be present in the movie title, ignore case
		imdbal.filter(new MovieTitleStrategy("who", new SimpleStringReaderStrategy()));
		imdbal.filter(new MovieTitleStrategy("doctor", new SimpleStringReaderStrategy()));
		
		//imdbal.filter(new MovieYearStrategy("1920-1960"));
		//imdbal.filter(new GenresStrategy("genres.txt", new SimpleFileReaderStrategy()));
		imdbal.filter(new KeywordsStrategy("dalek daleks", new SimpleStringReaderStrategy()));
		
		//imdbal.filter(new KeywordsStrategy("keywords.txt", new SimpleFileReaderStrategy()));
		
		// e.g. set R-rating filter 
		//imdbal.filter(new CertificateStrategy("USA:R USA:NC-17 18 USA-X", new SimpleStringReaderStrategy()));
		LinkedList<Movie> movies = imdbal.getMovies();
		int i=0;
		for (Movie m:movies) {
			++i;
			System.out.println(i+"------------->"+m.toString());
		}
		System.out.println(imdbal.getLog());
		
	}

}
