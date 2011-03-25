package com.handstandsam.googlemaps.dao;

import java.util.List;

import com.googlecode.objectify.Key;
import com.handstandsam.googlemaps.model.RecordedPoint;
import com.handstandsam.googlemaps.model.Recording;

public interface DAO {

	public abstract Key<Recording> updateRecording(Recording recording);

	public abstract Key<RecordedPoint> addRecordedPoint(RecordedPoint recordedPoint);

	public abstract List<RecordedPoint> getRecordedPoints(Long tourId);

	public abstract List<Recording> getTours(String category);

	public abstract Recording getTour(Long tourId);

}