package be.debreuckneirynck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	private static final Path PAD = Paths.get("server.log");
	private static RenderingData renderingData = new RenderingData();
	private static Map<String, Integer> doubleIds = new LinkedHashMap<>();
	private static int startRenderingsWithoutGet;
	
	public static void main(String[] args) {
		extractData();
		createXML();
	}
	
	public static void extractData() {
		try (BufferedReader reader = Files.newBufferedReader(PAD)) {
			for (String line; (line = reader.readLine()) != null;) {
				if (line.contains("Executing request startRendering")) {
					renderingData.addRendering(line);
					
				} else if (line.contains("Service startRendering returned")) {
					renderingData.checkDoubleIds(line, doubleIds);
					renderingData.linkUidToRendering(line);
					
				} else if (line.contains("Executing request getRendering")) {
					renderingData.addTimeStampGetRenderingToRendering(line);
				}
				startRenderingsWithoutGet = renderingData.countStartRenderingsWithoutGet();
			}
		} catch (IOException ex) {	
			ex.printStackTrace();
		}
	}
	
	public static void createXML() {
		if (!(RenderingData.getRenderingDataList().isEmpty())) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document doc = documentBuilder.newDocument();

				Element rootElement = doc.createElement("report");
				doc.appendChild(rootElement);

				for (Rendering rendering : RenderingData.getRenderingDataList()) {
					Element renderingElement = doc.createElement("rendering");
					rootElement.appendChild(renderingElement);

					Element documentElement = doc.createElement("document");
					documentElement.appendChild(doc.createTextNode(rendering.getDocumentId()));
					renderingElement.appendChild(documentElement);
					Element pageElement = doc.createElement("page");
					pageElement.appendChild(doc.createTextNode(rendering.getPage()));
					renderingElement.appendChild(pageElement);
					Element uIdElement = doc.createElement("uId");
					uIdElement.appendChild(doc.createTextNode(rendering.getuId()));
					renderingElement.appendChild(uIdElement);
					for (String timeStamp : rendering.getTimeStampsStartRendering()) {
						Element startElement = doc.createElement("start");
						startElement.appendChild(doc.createTextNode(timeStamp));
						renderingElement.appendChild(startElement);
					}
					for (String timeStamp : rendering.getTimeStampsGetRendering()) {
						Element getElement = doc.createElement("get");
						getElement.appendChild(doc.createTextNode(timeStamp));
						renderingElement.appendChild(getElement);
					}
				}

				Element summaryElement = doc.createElement("summary");
				rootElement.appendChild(summaryElement);

				Element countElement = doc.createElement("count");
				countElement
						.appendChild(doc.createTextNode(String.valueOf(RenderingData.getRenderingDataList().size())));
				summaryElement.appendChild(countElement);
				Element duplicatesElement = doc.createElement("duplicates");
				duplicatesElement.appendChild(doc.createTextNode(String.valueOf(doubleIds.size())));
				summaryElement.appendChild(duplicatesElement);
				Element unnecessaryElement = doc.createElement("unnecessary");
				unnecessaryElement.appendChild(doc.createTextNode(String.valueOf(startRenderingsWithoutGet)));
				summaryElement.appendChild(unnecessaryElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(doc);
				StreamResult streamResult = new StreamResult(new File("renderingData.xml"));
				transformer.transform(domSource, streamResult);

			} catch (Exception ex) {
				ex.printStackTrace();
			} 
		}
		else {
			System.out.println("no renderings found");
		}
	}

}
