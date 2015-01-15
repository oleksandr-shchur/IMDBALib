/*******************************************************************************
 * @fileOverview  CertificateStrategy.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package filters;

import java.util.ArrayList;
import java.util.Set;

import configreaders.IConfigReadStrategy;
import imdbalib.Movie;

public class CertificateStrategy implements IFilterStrategy {

	private String config;
	private String strategyName;
	private ArrayList<String> configCertificates;
	
	
	
	/**
	 * Instantiates a new certificate strategy.
	 * @author Oleksandr Shchur
	 * @param config the config
	 * @param configReadStrategy the config read strategy
	 */
	public CertificateStrategy (String config, IConfigReadStrategy configReadStrategy) {
		this.config = config;
		this.configCertificates = configReadStrategy.readConfiguration(config);
		strategyName = "CertificateStrategy";
	}
	
	

	/* (non-Javadoc)
	 * @see filters.IFilterStrategy#isMovieMatchFilter(imdbal.Movie)
	 */
	public boolean isMovieMatchFilter(Movie movie) {
		Set<String> certificates = movie.getCertificates();
		if (certificates == null) {
			return false;
		}
		for (String s:configCertificates) {
			s = s.trim();
			for (String movieCertificate:certificates) {
				if (movieCertificate.contains(s)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getConfig() {
		return config;
	}

	@Override
	public String toString() {
		return strategyName;
	}
	
	
}
