package com.handstandsam.googlemaps.model;

public enum Category {
	BUSINESS("Business"), CULTURE("Culture"), FOOD("Food"), OUTDOORS("Outdoors"), SIGHTSEEING(
			"Sightseeing"), OTHER("Other");

	private String text;

	Category(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
