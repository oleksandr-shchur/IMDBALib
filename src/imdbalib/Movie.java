/*******************************************************************************
 * @fileOverview  Movie.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package imdbalib;


import java.util.Set;

/**
 * The Class Movie.
 * One of the most important classes in IMDBAL
 * @author Oleksandr Shchur
 */
public class Movie {

	// TODO add toString method!

	private String title;
	private Float yearStart;
	private Float yearEnd;
	private Set<String> genres; // comedy, sci-fi, etc.
	private String rate; // R-rated, PG-13, etc.
	private String filmType; // FILM, TV, VIDEO, ...
	private boolean isSeries;
	private Set<String> certificates;
	private Set<String> keywords;
	private Cast cast;
	
	public Cast getCast() {
		return cast;
	}


	public void setCast(Cast cast) {
		this.cast = cast;
	}


	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		res.append("title: " + title + "| year: "+yearStart+ "\n");
		if (cast != null && cast.actors != null) {
			res.append("Actors: ");
			for (Actor a:cast.actors) {
				res.append("\t"+a.getName()+" "+a.getGender()+" ("+a.getAgeAt(yearStart)+")");
				if (cast.actorsRoles.containsKey(a.getName())) {
					res.append(" ["+cast.actorsRoles.get(a.getName())+"]");
				}
				if (cast.actorsOrder.containsKey(a.getName())) {
					res.append(" <"+cast.actorsOrder.get(a.getName())+"/"+cast.actorsOrder.size()+">");
				}
			}
			res.append("\n");
		}
		if (keywords != null) {
			res.append("keywords: ");
			for (String p:keywords) {
				res.append("\t"+p);
			}
		}
		if (certificates != null) {
			res.append("\ncertificates: ");
			for (String p:certificates) {
				res.append("\t"+p);
			}
		}
		if (genres != null) {
			res.append("\ncertificates: ");
			for (String p:genres) {
				res.append("\t"+p);
			}
		}
		return res.toString();
	}
	
	
	public Set<String> getKeywords() {
		return keywords;
	}
	
	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}
	
	public Set<Actor> getActors() {
		return cast.actors;
	}
	
	public void setActors(Set<Actor> actors) {
		if (cast == null) {
			cast = new Cast();
		}
		this.cast.actors = actors;
	}
	
	public boolean isSeries() {
		return isSeries;
	}
		
	public void setSeries(boolean isSeries) {
		this.isSeries = isSeries;
	}
	
	public String getFilmType() {
		return filmType;
	}
	
	public void setFilmType(String filmType) {
		this.filmType = filmType;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Float getYearStart() {
		return yearStart;
	}
	
	public void setYearStart(Float yearStart) {
		this.yearStart = yearStart;
	}
	
	public Float getYearEnd() {
		return yearEnd;
	}
	
	public void setYearEnd(Float yearEnd) {
		this.yearEnd = yearEnd;
	}
	
	public Set<String> getGenres() {
		return genres;
	}
	
	public void setGenres(Set<String> genres) {
		this.genres = genres;
	}
	
	public String getRate() {
		return rate;
	}
	
	public void setRate(String rate) {
		this.rate = rate;
	}


	/**
	 * @return the certificates
	 */
	public Set<String> getCertificates() {
		return certificates;
	}


	/**
	 * @param certificates the certificates to set
	 */
	public void setCertificates(Set<String> certificates) {
		this.certificates = certificates;
	}
	
}
