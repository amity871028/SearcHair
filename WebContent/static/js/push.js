const pushAPI = {
	allNotice: 'api-calendar?func=activity',
	deleteNotice: 'api-calendar-delete?func=notice'
}

const account = localStorage.getItem('account');


async function getNotice(){
	const result = await FetchData.get(`${pushAPI.allNotice}&account=${account}`);
    const noticeJson = await result.json();

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
	        if (a > b) { return rev * -1; }
	        if (a < b) { return rev * 1; }
	        return 1;
	    }
	};
	noticeJson.sort(sortBy('noticeTimestamp', false, Date.parse));
	
    const pushTbody = document.getElementById('push-tbody');
    let tmp = "";
    let count = 0;
	noticeJson.forEach(notice => {
		if(Date.parse(notice.noticeTimestamp).valueOf() <= new Date()){
			count++;
			const date = notice.startTime.split('T')[0];
			let startTime = notice.startTime.split('T')[1].substring(0, 5);
			let endTime = notice.endTime.split('T')[1].substring(0, 5);
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
			let noticeTime = notice.noticeTime + "分鐘後";
			if(parseInt(noticeTime) == 1440) noticeTime = "一天後";
			tmp += `<tr>
					<td class="date">${date}</td>
        			<td class="time">${startTime}<br>${endTime}</td>
        			<td class="activity-name">${notice.activityName}</td>
        			<td class="notice-time">${noticeTime}</td>
        			<td class="close-button"><button class="btn btn-secondary" id=${notice.id} onclick="closeNotice(this.id);">關閉通知</button></td>
        		<tr>`;
		}
	});
	pushTbody.innerHTML = tmp;
	localStorage.setItem('noticeCount', count);
}

async function closeNotice(id){
	const result = await FetchData.post(pushAPI.deleteNotice, {
		func: "notice",
		account: account,
		id: id,
	});
	if(result.status == 409){
		alert("發生錯誤！");
	}
	else {
		getNotice();
	}
}

function init() {
	getNotice();
	window.setInterval(getNotice, 10000);
	
}

window.addEventListener('load', init);