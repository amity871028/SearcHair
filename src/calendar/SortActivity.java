package calendar;

import java.util.Comparator;


public class SortActivity implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		ActivityRecord user1 = (ActivityRecord) o1;
		ActivityRecord user2 = (ActivityRecord) o2;
		
		int flag = user1.getNoticeTimestamp().compareTo(user2.getNoticeTimestamp());

		return flag;
	}
}
