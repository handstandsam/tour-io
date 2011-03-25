package com.handstandsam.googlemaps.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Id;

public class Recording implements Serializable {

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private String name;
	
	private Category category;

	@Embedded
	private List<Point> points = new ArrayList<Point>();

	public Recording() {

	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	public void addPoint(Point point){
		this.points.add(point);
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getCategory() {
		return category;
	}

}
