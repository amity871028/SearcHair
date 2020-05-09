package hairMatch;

import java.util.ArrayList;

public class HairStyle {
	String urlPrefix;
	ArrayList<String> photos = new ArrayList<String>();

	public HairStyle(String urlPrefix, ArrayList<String> photos) {
		this.urlPrefix = urlPrefix;
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "urlPrefix: " + urlPrefix + "," + "photos:" + photos;
	}

}