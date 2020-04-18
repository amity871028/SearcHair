package search;

public class StylistWorks{ 
	int id;
	String picture,stylist,stylist_job_title,description,hashtag;
	
    public StylistWorks(int id,String picture,String stylist,String stylist_job_title,String description,String hashtag) {
		this.id = id;
    	this.picture = picture;
		this.stylist = stylist;
		this.stylist_job_title = stylist_job_title;
		this.description = description;
		this.hashtag = hashtag;
    }
    
    @Override
	public String toString () {
		return "id:"+id+","+
				"picture:"+picture+","+
				"stylist:"+stylist+","+
				"stylist_job_title:"+stylist_job_title+","+
				"description:"+description+","+
				"hashtag:"+hashtag;
	}
}