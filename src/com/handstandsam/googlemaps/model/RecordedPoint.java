package com.handstandsam.googlemaps.model;

import java.io.Serializable;

import javax.persistence.Embedded;
import javax.persistence.Id;

public class RecordedPoint implements Serializable{

	/**
	 * Default Serialization UID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	private Long recordingId;
	
	@Embedded
	private Point point;
	
	public RecordedPoint() {
		
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint() {
		return point;
	}

	public void setRecordingId(Long recordingId) {
		this.recordingId = recordingId;
	}

	public Long getRecordingId() {
		return recordingId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
