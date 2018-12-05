package be.debreuckneirynck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RenderingData {
	private static List<Rendering> renderingDataList = new ArrayList<>() ;
	
	public static List<Rendering> getRenderingDataList() {
		return renderingDataList;
	}

	public void addRendering(String line) {
		String documentId = documentId(line);
		String timeStampStartRendering = timeStamp(line);
		String page = page(line);
		String startThread = startThread(line);
		
		Rendering rendering = new Rendering(documentId, page, startThread);
		rendering.addTimeStampStartRendering(timeStampStartRendering);
		renderingDataList.add(rendering);
	}
	
	public Map<String, Integer> checkDoubleIds(String line, Map<String, Integer> doubleIds) {
		String uId = uIdStartRenderingReturned(line);
		for (Rendering rendering : renderingDataList) {
			// bestaat uId al?
			if (rendering.getuId() != null && rendering.getuId().equals(uId)) {
				// uId toevoegen aan map met dubbele uIds en hun aantal duplicaten
				if (!doubleIds.containsKey(uId)) {
					doubleIds.put(uId, 1);
				} else {
					doubleIds.put(uId, doubleIds.get(uId) + 1);
				}
			}
		}
		return doubleIds;
	}
	
	public void linkUidToRendering(String line) {
		String uId = uIdStartRenderingReturned(line);
		String startThread = startThread(line);
		for (Rendering rendering : renderingDataList) {
			// uId toevoegen aan renderingdata met zelfde startThread en nog zonder uId
			if (rendering.getStartThread().equals(startThread) && rendering.getuId() == null) {	
					rendering.setuId(uId);
			}
		}
	}
	
	public void addTimeStampGetRenderingToRendering(String line) {
		String timeStampGetRendering = timeStamp(line);
		String uId = uIdRequestGetRendering(line);
		for (Rendering rendering : renderingDataList) {
			if (rendering.getuId() != null && rendering.getuId().equals(uId)) {
				rendering.addTimeStampGetRendering(timeStampGetRendering);
			}
		}
	}
	
	public int countStartRenderingsWithoutGet() {
		int startRenderingsWithoutGet = 0;
		for (Rendering renderingData : renderingDataList) {
			if (renderingData.getTimeStampsGetRendering().isEmpty()) {
				startRenderingsWithoutGet++;
			}
		}
		return startRenderingsWithoutGet;
	}
	
	public static final String documentId(String line) {
		return line.substring(line.lastIndexOf('[') + 1, line.lastIndexOf(','));
	}
	public static final String startThread(String line) {
		return line.substring(line.indexOf('[') + 1, line.indexOf(']'));
	}
	public static final String timeStamp(String line) {
		return line.substring(0, line.indexOf("[") - 1);
	}
	public static final String page(String line) {
		return line.substring(line.lastIndexOf(',') + 2, line.lastIndexOf(']'));
	}
	public static final String uIdStartRenderingReturned(String line) {
		return line.substring(line.lastIndexOf(' ') + 1);
	}
	public static final String uIdRequestGetRendering(String line) {
		return line.substring(line.lastIndexOf('[') + 1, line.lastIndexOf(']'));
	}
}
