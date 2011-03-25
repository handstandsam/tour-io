package com.handstandsam.googlemaps.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.handstandsam.googlemaps.dao.DAO;
import com.handstandsam.googlemaps.dao.DAOFactory;
import com.handstandsam.googlemaps.model.Point;
import com.handstandsam.googlemaps.model.RecordedPoint;

@SuppressWarnings("serial")
public class AdminController extends HttpServlet {

	public static final Logger log = LoggerFactory
			.getLogger(AdminController.class);

	private static DAO dao = DAOFactory.getDAO();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		List<RecordedPoint> allRecordedPoints = dao.getRecordedPoints(null);

		Map<Long, List<Point>> paths = new HashMap<Long, List<Point>>();

		for (RecordedPoint recordedPoint : allRecordedPoints) {
			Point point = recordedPoint.getPoint();
			Long recordingId = recordedPoint.getRecordingId();
			List<Point> points = paths.get(recordingId);
			if (points == null) {
				points = new ArrayList<Point>();
			}
			points.add(point);
			paths.put(recordingId, points);
		}
		request.setAttribute("pathIds", paths.keySet());
		request.setAttribute("paths", paths);

		forwardRequest(request, response, "/WEB-INF/pages/admin.jsp");
	}

	protected void forwardRequest(HttpServletRequest request,
			HttpServletResponse response, String destination) {
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher(destination);
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
