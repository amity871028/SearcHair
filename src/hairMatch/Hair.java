package hairMatch;

public class Hair {
	String url;
	
	public Hair(String url) { 
		this.url = url;
	}

	@Override
	public String toString () {
		return "url: " + url;
	}
}
