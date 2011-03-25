package com.handstandsam.googlemaps;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.handstandsam.googlemaps.dao.DAO;
import com.handstandsam.googlemaps.dao.DAOFactory;
import com.handstandsam.googlemaps.model.Category;
import com.handstandsam.googlemaps.model.RecordedPoint;
import com.handstandsam.googlemaps.model.Recording;
import com.handstandsam.googlemaps.util.RESTUtil;

@SuppressWarnings("serial")
public class GetToursServlet extends HttpServlet {

	Logger log = LoggerFactory.getLogger(GetToursServlet.class);

	private static DAO dao = DAOFactory.getDAO();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String body = RESTUtil.getRequestPostBody(request);
		try {
			String category = null;
			if (body.isEmpty()) {
				JSONObject bodyObj = new JSONObject(body);
				category = bodyObj.getString("category");
			}
			List<Recording> tours = dao.getTours(category);

			Map<String, List<Recording>> toursByCategory = new HashMap<String, List<Recording>>();
			for (Recording tour : tours) {
				Category c = tour.getCategory();
				if (c == null) {
					c = Category.OTHER;
				}
				List<Recording> toursInCat = toursByCategory.get(c.toString());
				if (toursInCat == null) {
					toursInCat = new ArrayList<Recording>();
				}
				List<RecordedPoint> recordedPoints = dao.getRecordedPoints(tour
						.getId());
				if (recordedPoints != null && recordedPoints.size() > 1) {
					toursInCat.add(tour);
					toursByCategory.put(c.toString(), toursInCat);
				}
			}

			try {
				Writer out = response.getWriter();
				String json = new Gson().toJson(toursByCategory);
				log.debug(json);
				out.append(json);
				response.setContentType("application/json");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
