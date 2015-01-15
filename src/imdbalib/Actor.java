/*******************************************************************************
 * @fileOverview  Actor.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package imdbalib;

/**
 * The Class Actor. Hold information about actor: biography, etc.
 * @author Oleksandr Shchur
 */
public class Actor {

	public enum Gender {MALE, FEMALE}; 

	private String name;
	private float dateOfBirth;
	private Gender gender;

	public Actor(String name, Gender gender, float dateOfBirth) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
	}

	public Actor(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getBirthDate() {
		return dateOfBirth;
	}

	public void setBirthDate(float birthDate) {
		this.dateOfBirth = birthDate;
	}

	public String getGender() {
		if (gender == Gender.MALE){
			return "[M]";
		} else {
			return "[F]";
		}
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public float getAgeAt (float year) {
		if (dateOfBirth == 0.0f) {
			return -5000.0f;
		} else {
			return year - dateOfBirth;
		}
	}


}
