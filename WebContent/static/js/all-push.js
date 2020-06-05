const pushAPI = {
		activity: 'api-calendar?func=activity',
		hairPhoto: ''
}


const account = localStorage.getItem('account');

async function checkNewNotice(){
	let localStorageCount;
	if(localStorage.getItem('noticeCount')){
		localStorageCount = localStorage.getItem('noticeCount');
	}

	const result = await FetchData.get(`${pushAPI.activity}&account=${account}`);
    const noticeJson = await result.json();
    
    let count = 0;
    noticeJson.forEach(notice => {
		if(Date.parse(notice.noticeTimestamp).valueOf() <= new Date()){
			count++;
		}
	});
	
	if(localStorageCount < count) {
		document.getElementById('new-span').style = "display: initial";
	}
	else {
		document.getElementById('new-span').style = "display: none";
	}
}

function pushInit(){
	checkNewNotice();	
	window.setInterval(checkNewNotice, 10000);
}

window.addEventListener('load', pushInit);