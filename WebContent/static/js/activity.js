const activityAPI = "api-calendar?func=activity";
const account = localStorage.getItem('account');


async function getActivity(){
	let currentYear = document.getElementById('calendar-year').innerHTML;
	let monthString = document.getElementById('calendar-title').innerHTML;
	let currentMonth = month_name.indexOf(monthString) + 1;

	const result = await FetchData.get(`${activityAPI}&account=${account}&year=${currentYear}&month=${currentMonth}`);
	let activityResult = await result.json();
	const activityTbody = document.getElementById('activity-tbody');

	// to sort activityResult by date
	var sortBy = function (filed, rev, primer) {
	    rev = (rev) ? -1 : 1;
	    return function (a, b) {
	        a = a[filed];
	        b = b[filed];
	        if (typeof (primer) != 'undefined') {
	            a = primer(a);
	            b = primer(b);
	        }
	        if (a < b) { return rev * -1; }
	        if (a > b) { return rev * 1; }
	        return 1;
	    }
	};
	activityResult.sort(sortBy('startTime', false, Date.parse));
	
	let tmp = "";
	activityResult.forEach(activity => {
		let activityDate = activity.startTime.split('T')[0].split('-')[2];
		let startTime = activity.startTime.split('T')[1].substring(0, 5);
		let endTime = activity.endTime.split('T')[1].substring(0, 5);
		if(parseInt(startTime.split(':')[0]) < 12){
			startTime = "早上" + startTime;
		}
		else {
			startTime = "下午" + startTime;
		}
		if(parseInt(endTime.split(':')[0]) < 12){
			endTime = "早上" + endTime;
		}
		else {
			endTime = "下午" + endTime;
		}
		tmp += `<tr>
					<td class="date">${activityDate}</td>
                    <td class="${activity.color}-color"></td>
                    <td class="time">${startTime}<br>${endTime}</td>
                    <td class="activity-name">${activity.activityName}</td>
		</tr>`;
	});
	activityTbody.innerHTML = tmp;
}

function delayURL(url, time) {
  setTimeout(() => { window.location.href = `${url}`; }, time);
}

function init(){
	// to block illegal users
	if(account == null){
		alert("無法瀏覽此頁面！請登入後再查看！");
	    delayURL('./index.html', 200);
	}
	getActivity();

	document.getElementById('next').addEventListener('click', getActivity);
	document.getElementById('prev').addEventListener('click', getActivity);
}

window.addEventListener('load', init);