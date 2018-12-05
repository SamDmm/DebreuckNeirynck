package be.debreuckneirynck;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Rendering implements Comparable<Rendering>{
	private String documentId;
	private Set<String> timeStampsStartRendering = new LinkedHashSet<>();
	private Set<String> timeStampsGetRendering = new LinkedHashSet<>();
	private String page;
	private String startThread;
	private String uId;
	
	public Rendering(String uId) {
		this.uId = uId;
	}
	public Rendering(String documentId, String page, String startThread) {
		this.documentId = documentId;
		this.page = page;
		this.startThread = startThread;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getStartThread() {
		return startThread;
	}
	public void setStartThread(String startThread) {
		this.startThread = startThread;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	
	public Set<String> getTimeStampsStartRendering() {
		return timeStampsStartRendering;
	}
	public Set<String> getTimeStampsGetRendering() {
		return timeStampsGetRendering;
	}
	
	public void addTimeStampStartRendering(String timeStampStartRendering) {
		this.timeStampsStartRendering.add(timeStampStartRendering);
	}
	public void addTimeStampGetRendering(String timeStampGetRendering) {
		this.timeStampsGetRendering.add(timeStampGetRendering);
	}
	
	@Override
	public String toString() {
		return documentId + "; " + page + "; " + uId + "; " + startThread + "; " + timeStampsStartRendering + "; " + timeStampsGetRendering;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Rendering)) {
			return false;
		}
		Rendering r = (Rendering) o;
		return this.getuId().equals(r.getuId());
	}
	@Override
	public int hashCode() {
		int hash = 5;
		hash = hash * 17 + Objects.hashCode(this.getuId());
		return hash;
	}

	@Override
	public int compareTo(Rendering r) {
		return this.getuId().compareTo(r.getuId());
	}


}
