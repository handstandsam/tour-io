package com.handstandsam.googlemaps.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.helper.DAOBase;
import com.handstandsam.googlemaps.model.RecordedPoint;
import com.handstandsam.googlemaps.model.Recording;

public class DAOImpl extends DAOBase implements DAO {

	private static Logger log = LoggerFactory.getLogger(DAOImpl.class);

	private ObjectifyFactory fact;

	DAOImpl(ObjectifyFactory fact) {
		this.fact = fact;
		registerClasses();
	}

	DAOImpl() {
		registerClasses();
	}

	private void registerClasses() {
		registerClass(Recording.class);
		registerClass(RecordedPoint.class);
	}

	private void registerClass(Class<?> clazz) {
		if (this.fact == null) {
			ObjectifyService.register(clazz);
		} else {
			this.fact.register(clazz);
		}
	}

	/**
	 * Easy access to the objectify object (which is lazily created).
	 */
	public Objectify ofy() {
		if (this.fact == null) {
			return super.ofy();
		} else {
			return this.fact.begin();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.handstandsam.googlemaps.dao.DAO#updateRecording(com.handstandsam.
	 * googlemaps.model.Recording)
	 */
	@Override
	public Key<Recording> updateRecording(Recording recording) {
		return ofy().put(recording);
	}

	@Override
	public Key<RecordedPoint> addRecordedPoint(RecordedPoint recordedPoint) {
		return ofy().put(recordedPoint);
	}

	@Override
	public List<RecordedPoint> getRecordedPoints(Long tourId) {
		Query<RecordedPoint> query = ofy().query(RecordedPoint.class).order(
				"point.time");
		if (tourId != null) {
			query.filter("recordingId", tourId);
		}
		return query.list();
	}

	@Override
	public List<Recording> getTours(String category) {
		Query<Recording> query = ofy().query(Recording.class);
		if (category != null && !category.isEmpty()) {
			query.filter("category", category);
		}
		return query.list();
	}

	@Override
	public Recording getTour(Long tourId) {
		return ofy().find(Recording.class, tourId);
	}
}