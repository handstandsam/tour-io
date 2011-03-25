package com.handstandsam.googlemaps;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.handstandsam.googlemaps.dao.DAO;
import com.handstandsam.googlemaps.dao.DAOFactory;
import com.handstandsam.googlemaps.model.RecordedPoint;
import com.handstandsam.googlemaps.util.RESTUtil;

@SuppressWarnings("serial")
public class RecordPointServlet extends HttpServlet {
	private static DAO dao = DAOFactory.getDAO();

	private static Logger log = LoggerFactory.getLogger(RecordPointServlet.class);
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String body = RESTUtil.getRequestPostBody(request);
		log.debug(body);
		RecordedPoint recordedPoint = new Gson().fromJson(body.toString(), RecordedPoint.class);
		dao.addRecordedPoint(recordedPoint);
	}
}
