package api;

public class CalendarColor {
	private final static String red = "#FF9999";
	private final static String orange = "#FFCC99";
	private final static String yellow = "#FFFF99";
	private final static String green = "#99CC66";
	private final static String blue = "#99CCFF";
	private final static String indigo = "#336699";
	private final static String purple =  "#CC99CC";
	
	public static String setColor(String color) {
		if(color.equals(red))
			return "red";
		else if (color.equals(orange))
			return "orange";
		else if (color.equals(yellow))
			return "yellow";
		else if (color.equals(green))
			return "green";
		else if (color.equals(blue))
			return "blue";
		else if (color.equals(indigo))
			return "indigo";
		else if (color.equals(purple))
			return "purple";
		else 
			return "none";
	}
}
