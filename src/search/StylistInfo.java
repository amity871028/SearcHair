package search;

import java.util.ArrayList;

public class StylistInfo {

	int id;
	String name, picture;
	ArrayList<Work> works = new ArrayList<Work>();

	public void setID(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setWorks(ArrayList<Work> works) {
		this.works = works;
	}
}