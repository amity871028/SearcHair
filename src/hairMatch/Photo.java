package hairMatch;

public class Photo {
	String userName, color, url;

	public Photo(String userName, String color, String url) {
		this.userName = userName;
		this.color = color;
		this.url = url;
	}

	@Override
	public String toString() {
		return "userName:" + userName + "," + "color:" + color + "," + "url:" + url;
	}
}
