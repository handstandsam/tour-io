package com.handstandsam.googlemaps;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.handstandsam.googlemaps.dao.DAO;
import com.handstandsam.googlemaps.dao.DAOFactory;
import com.handstandsam.googlemaps.model.Point;
import com.handstandsam.googlemaps.model.RecordedPoint;
import com.handstandsam.googlemaps.model.Recording;

@SuppressWarnings("serial")
public class GetTourServlet extends HttpServlet {

	Logger log = LoggerFactory.getLogger(GetTourServlet.class);

	private static DAO dao = DAOFactory.getDAO();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String tourIdStr = request.getParameter("id");
		Long tourId = Long.parseLong(tourIdStr);

		Recording tour = dao.getTour(tourId);
		List<Point> points = new ArrayList<Point>();

		List<RecordedPoint> recordedPoints = dao.getRecordedPoints(tourId);
		for (RecordedPoint singlePoint : recordedPoints) {
			points.add(singlePoint.getPoint());
		}
		tour.setPoints(points);

		try {
			Writer out = response.getWriter();
			String json = new Gson().toJson(tour);
			log.debug(json);
			out.append(json);
			response.setContentType("application/json");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}

	}
}
