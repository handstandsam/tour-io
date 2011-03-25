package com.handstandsam.googlemaps;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.handstandsam.googlemaps.dao.DAO;
import com.handstandsam.googlemaps.dao.DAOFactory;
import com.handstandsam.googlemaps.model.Category;
import com.handstandsam.googlemaps.model.Recording;
import com.handstandsam.googlemaps.util.RESTUtil;

@SuppressWarnings("serial")
public class StartRecordingServlet extends HttpServlet {
	
	Logger log = LoggerFactory.getLogger(StartRecordingServlet.class);
	
	private static DAO dao = DAOFactory.getDAO();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String body = RESTUtil.getRequestPostBody(request);
		try {
			JSONObject bodyObj = new JSONObject(body);
			String tournameStr = bodyObj.getString("tourname");
			String categoryStr = bodyObj.getString("category");
			Recording recording = new Recording();
			Category category = Category.valueOf(categoryStr);
			recording.setName(tournameStr);
			recording.setCategory(category);
			Key<Recording> recordingKey = dao.updateRecording(recording);

			try {
				Writer out = response.getWriter();
				String json = new Gson().toJson(recordingKey);
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
