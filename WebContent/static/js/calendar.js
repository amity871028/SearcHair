const calendarAPI = {
	getAllAction: "api-calendar?",
	newAction: "api-calendar-new",
	updateAction: "api-calendar-update",
	deleteAction: "api-calendar-delete"
}

let nowTime = new Date();
let year = nowTime.getFullYear();
let month = nowTime.getMonth() + 1;
let date = nowTime.getDate();
if(month < 10) month = "0" + month; 
if(date < 10) date = "0" + date; 
const nowTimeFormal = `${nowTime.getFullYear()}-${month}-${date}`;

const hairdressingCategory = ["洗髮", "剪髮", "染髮", "燙髮", "護髮", "其他"];
const productCategory = ["洗髮類", "潤髮類", "護髮類", "其他"];
const account = localStorage.getItem('account');

async function getAction(){
	//get year & month in title
	let currentYear = document.getElementById('calendar-year').innerHTML;
	let monthString = document.getElementById('calendar-title').innerHTML;
	let currentMonth = month_name.indexOf(monthString) + 1;
	document.getElementById('date-title').innerHTML = nowTimeFormal;
	
	let result = await FetchData.get(`${calendarAPI.getAllAction}func=activity&account=${account}&year=${currentYear}&month=${currentMonth}`);
	const activityResult = await result.json();
	result = await FetchData.get(`${calendarAPI.getAllAction}func=cost&account=${account}&year=${currentYear}&month=${currentMonth}`);
	const costResult = await result.json();
	result = await FetchData.get(`${calendarAPI.getAllAction}func=picture&account=${account}&year=${currentYear}&month=${currentMonth}`);
	const pictureResult = await result.json();
	
	const activityTbody = document.getElementById('activity-tbody');
	let tmp = "";
	activityResult.forEach(activity => {
		//update date's icon
		const currentDate = activity.startTime.split('T')[0];
		const currentLi = document.getElementById(currentDate).getElementsByTagName('li')[0];
		if(!currentLi.innerHTML.match("活動")){
			currentLi.innerHTML += `<i class="fas fa-clipboard-list" title="活動"></i>`;
		}
		//update sidebar
		if(currentDate == nowTimeFormal){
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
			tmp += `<tr id="${activity.id}" onclick="updateActivity(${activity.id}, '${activity.activityName}', '${activity.startTime}', '${activity.endTime}', '${activity.color}', ${activity.noticeTime})">
						<td class="${activity.color}-color color-display"></td>
						<td class="time">${startTime}<br>${endTime}</td>
						<td>${activity.activityName}</td>
					</tr></a>`;
		}
	});
	activityTbody.innerHTML = tmp;
	
	const costTbody = document.getElementById('cost-tbody');
	const totalCost = document.getElementById('total-cost');
	let tmpCost = 0;
	tmp = "";
	costResult.forEach(cost => {
		//update date's icon
		const currentDate = cost.time;
		const currentLi = document.getElementById(currentDate).getElementsByTagName('li')[0];
		if(!currentLi.innerHTML.match("花費")){
			currentLi.innerHTML += `<i class="fas fa-search-dollar" title="花費"></i>`;
		}
		//update sidebar
		if(currentDate == nowTimeFormal){
			tmp += `<tr id="${cost.id}" onclick="updateCost(${cost.id}, '${cost.time}', '${cost.category}', '${cost.kind}', ${cost.cost}, '${cost.description}','${cost.color}')">
						<td class="${cost.color}-color"></td>
						<td>${cost.cost}</td>
						<td>${cost.category}-${cost.kind}</td>
					</tr>`;
			tmpCost += cost.cost;
		}
	});
	totalCost.innerHTML = `總花費：${tmpCost}`
	costTbody.innerHTML = tmp;
	
	//const hairPhoto = document.getElementById('hair-photo');
	pictureResult.forEach(picture =>{
		//update date's icon
		const currentDate = picture.time;
		const currentLi = document.getElementById(currentDate).getElementsByTagName('li')[0];
		if(!currentLi.innerHTML.match("頭髮狀況")){
			currentLi.innerHTML += `<i class="fas fa-user" title="頭髮狀況"></i>`;
		}
		//update sidebar
		if(currentDate == nowTimeFormal){
			const photoDiv = document.getElementById('hair-photo-div');
			photoDiv.innerHTML = "";
			tmp = `<a href="#" id="${picture.id}" onclick="updatePicture(${picture.id}, '${picture.picture}', '${picture.description}', '${picture.time}');">
					<img src="${picture.picture}" id="hair-photo" alt="還沒有新增照片喔！">
                   	<p id="hair-description">${picture.description}</p></a>`;
			photoDiv.innerHTML = tmp;
		}
	});
}

async function updateSideBar(e, date){
	const prevDay = document.getElementsByClassName('li-active');
	if(prevDay.length != 0) prevDay[0].classList.remove('li-active');
	e.childNodes[0].classList.add('li-active');
	
	let currentYear = document.getElementById('calendar-year').innerHTML;
	let monthString = document.getElementById('calendar-title').innerHTML;
	let currentMonth = month_name.indexOf(monthString) + 1;
	
	const selected = document.getElementById(date);
	document.getElementById('date-title').innerHTML = date;
	let result = await FetchData.get(`${calendarAPI.getAllAction}func=activity&account=${account}&year=${currentYear}&month=${currentMonth}`);
	const activityResult = await result.json();
	result = await FetchData.get(`${calendarAPI.getAllAction}func=cost&account=${account}&year=${currentYear}&month=${currentMonth}`);
	const costResult = await result.json();
	result = await FetchData.get(`${calendarAPI.getAllAction}func=picture&account=${account}&year=${currentYear}&month=${currentMonth}`);
	const pictureResult = await result.json();
	
	const activityTbody = document.getElementById('activity-tbody');
	let tmp = "";
	activityResult.forEach(activity => {
		const currentDate = activity.startTime.split('T')[0];
		
		if(currentDate == date){
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
			tmp += `<tr id="${activity.id}" onclick="updateActivity(${activity.id}, '${activity.activityName}', '${activity.startTime}', '${activity.endTime}', '${activity.color}', ${activity.noticeTime})">
				<td class="${activity.color}-color color-display"></td>
				<td class="time">${startTime}<br>${endTime}</td>
				<td>${activity.activityName}</td>
			</tr></a>`;
		}
	});
	activityTbody.innerHTML = tmp;

	const costTbody = document.getElementById('cost-tbody');
	const totalCost = document.getElementById('total-cost');
	let tmpCost = 0;
	tmp = "";
	costResult.forEach(cost => {
		const currentDate = cost.time;
		if(currentDate == date){
			tmp += `<tr id="${cost.id}" onclick="updateCost(${cost.id}, '${cost.time}', '${cost.category}', '${cost.kind}', ${cost.cost}, '${cost.description}','${cost.color}')">
						<td class="${cost.color}-color"></td>
						<td>${cost.cost}</td>
						<td>${cost.category}-${cost.kind}</td>
					</tr>`;
			tmpCost += cost.cost;
		}
	});
	totalCost.innerHTML = `總花費：${tmpCost}`
	costTbody.innerHTML = tmp;

	const photoDiv = document.getElementById('hair-photo-div');
	tmp = "";
	pictureResult.forEach(picture => {
		const currentDate = picture.time;
		if(currentDate == date){
			tmp = `<a href="#" id="${picture.id}" onclick="updatePicture(${picture.id}, '${picture.picture}', '${picture.description}', '${picture.time}');">
					<img src="${picture.picture}" id="hair-photo" alt="還沒有新增照片喔！">
                   	<p id="hair-description">${picture.description}</p></a>`;
		}
		
	});
	photoDiv.innerHTML = tmp;
}

function updateActivity(id, name, startTime, endTime, color, noticeTime) {
	document.getElementById('activity-title').innerHTML = "修改活動";
	document.getElementById('activity-name').value = name;
	document.getElementById('activity-date').value = startTime.split('T')[0];
	document.getElementById('start-time').value = startTime.split('T')[1].substring(0, 5);
	document.getElementById('end-time').value = endTime.split('T')[1].substring(0, 5);
	$(`input[name=activity-color][value=${color}]`).attr('checked',true);
	if(noticeTime != -1){
		document.getElementById('notice-switch').checked = true;
		$("#notice-time").attr('disabled', false);
		$("#notice-time").val(noticeTime);
	}
	else {
		document.getElementById('notice-switch').checked = false;
		$("#notice-time").val(10);
		$("#notice-time").attr('disabled', true);
	}
	document.getElementById('add-activity-btn').setAttribute('style', 'display: none;');
	document.getElementById('update-activity-btn').setAttribute('style', 'display: initial;');
	document.getElementById('delete-activity-btn').setAttribute('style', 'display: initial;');
	
	document.getElementById('activity-id').innerHTML = id;
	$('#add-activity-modal').modal('show');
}

function updateCost(id, time, category, kind, cost, description, color){
	document.getElementById('cost-title').innerHTML = "修改記帳";
	document.getElementById('cost-date').value = time;
	$("#category").find(`option:contains(${category})`).attr('selected',true);
	updateOption();
	$("#kind").find(`option:contains(${kind})`).attr('selected',true);
	document.getElementById('cost-cost').value = cost;
	document.getElementById('cost-description').value = description;
	$(`input[name=cost-color][value=${color}]`).attr('checked',true);


	document.getElementById('add-cost-btn').setAttribute('style', 'display: none;');
	document.getElementById('update-cost-btn').setAttribute('style', 'display: initial;');
	document.getElementById('delete-cost-btn').setAttribute('style', 'display: initial;');
	
	document.getElementById('cost-id').innerHTML = id;
	$('#add-cost-modal').modal('show');
}

function updatePicture(id, picture, description, time){
	document.getElementById('picture-title').innerHTML = "修改紀錄頭髮";
	document.getElementById('photo-date').value = time;
	document.getElementById('show-new-photo').src = picture;
	document.getElementById('photo-description').value = description;
	document.getElementById('add-picture-btn').setAttribute('style', 'display: none;');
	document.getElementById('update-picture-btn').setAttribute('style', 'display: initial;');
	document.getElementById('delete-picture-btn').setAttribute('style', 'display: initial;');
	
	document.getElementById('picture-id').innerHTML = id;
	$('#record-hair-modal').modal('show');
}

async function postActivity(){
	if (document.forms['activity-form'].reportValidity()) {
		document.getElementById('activity-time-wrong').innerText = '';
		const activityDate = document.getElementById('activity-date').value;
		const startTime = activityDate + "T" + document.getElementById('start-time').value + ":00";
		const endTime = activityDate + "T" + document.getElementById('end-time').value + ":00";
		if(Date.parse(startTime).valueOf() > Date.parse(endTime).valueOf()){
			document.getElementById('activity-time-wrong').innerText = '時間輸入錯誤！';
		}
		else{
			const idSpan = document.getElementById('activity-id');
			const activityColorRadio = document.getElementsByName('activity-color');
			let color = "";
			for(let i in activityColorRadio){
				if(activityColorRadio[i].checked == true) color = activityColorRadio[i].value;
			}
			let noticeTime = -1;
			if(document.getElementById('notice-switch').checked){
				noticeTime = document.getElementById('notice-time').value;
			}
			// start post
			if(idSpan.innerHTML == -1){
				const result = await FetchData.post(calendarAPI.newAction, {
					func: "activity",
					account: account,
					activityName : document.getElementById('activity-name').value,
					startTime : startTime,
					endTime : endTime,
					color : color,
					noticeTime : parseInt(noticeTime),
				});
				alert("新增成功！");
			}
			else {
				const result = await FetchData.post(calendarAPI.updateAction, {
					func: "activity",
					account: account,
					id: idSpan.innerHTML,
					activityName : document.getElementById('activity-name').value,
					startTime : startTime,
					endTime : endTime,
					color : color,
					noticeTime : parseInt(noticeTime),
				});
				idSpan.innerHTML = -1;
				alert("修改成功！");
			}
			$("#add-activity-modal").modal('hide');
			$("#add-modal").modal('hide');
			getAction();
			updateSideBar(document.getElementById(activityDate), activityDate);
		}
	}
}

async function postCost(){
	const idSpan = document.getElementById('cost-id');
	if (document.forms['cost-form'].reportValidity()) {
		const activityColorRadio = document.getElementsByName('cost-color');
		let date = document.getElementById('cost-date').value;
		let color = "";
		for(let i in activityColorRadio){
			if(activityColorRadio[i].checked == true) color = activityColorRadio[i].value;
		}
		// start post
		if(idSpan.innerHTML == -1){
			const result = await FetchData.post(calendarAPI.newAction, {
				func: "cost",
				account: account,
				time: date,
				category: document.getElementById('category').value, 
				kind: document.getElementById('kind').value, 
				cost: parseInt(document.getElementById('cost-cost').value),
				description: document.getElementById('cost-description').value,
				color: color,
			});
			alert("新增成功！");
		}
		else {
			const result = await FetchData.post(calendarAPI.updateAction, {
				func: "cost",
				id: idSpan.innerHTML,
				account: account,
				time: date,
				category: document.getElementById('category').value, 
				kind: document.getElementById('kind').value, 
				cost: parseInt(document.getElementById('cost-cost').value),
				description: document.getElementById('cost-description').value,
				color: color,
			});
			idSpan.innerHTML = -1;
			alert("修改成功！");
		}
		$("#add-cost-modal").modal('hide');
		$("#add-modal").modal('hide');
		getAction();
		updateSideBar(document.getElementById(date), date);
	}
}

async function postPicture(){
	const idSpan = document.getElementById('picture-id');
	if (document.forms['record-hair-form'].reportValidity()) {
		const newPhoto = document.getElementById('new-hair-photo').value;	
		let date = document.getElementById('photo-date').value;
		// start post
		if(idSpan.innerHTML == -1){
			if(window.pictureBase64 == "" || !window.pictureBase64){
				alert("請新增照片！");
			}
			else{
				const result = await FetchData.post(calendarAPI.newAction, {
					func: "picture",
					account: account,
					picture: window.pictureBase64,
					description: document.getElementById('photo-description').value,
					time: date,
				});
				window.pictureBase64 = "";
				
				if(result.status == 409){
					alert("你已經新增過今天的照片囉！若想修改照片請到側邊攔照片點選修改喔～");
					return;
				}
				alert("新增成功！");
			}
		}
		else {
			if(window.pictureBase64 == "" || !window.pictureBase64){
				window.pictureBase64 = document.getElementById('show-new-photo').src;
			}
			const result = await FetchData.post(calendarAPI.updateAction, {
				func: "picture",
				id: idSpan.innerHTML,
				account: account,
				picture: window.pictureBase64,
				description: document.getElementById('photo-description').value,
				time: date,
			});
			window.pictureBase64 = "";
			idSpan.innerHTML = -1;
			alert("修改成功！");
		}
		$("#record-hair-modal").modal('hide');
		$("#add-modal").modal('hide');
		getAction();
		updateSideBar(document.getElementById(date), date);
	}
}

function updateOption(){
	const categorySelection = document.getElementById('category');
	const kindSelection = document.getElementById('kind');
	kindSelection.options.length = 0;
	if(categorySelection.value == "美髮") {
		for(let i in hairdressingCategory){
			const option = document.createElement('option');
	        option.text = hairdressingCategory[i];
	        kindSelection.options.add(option);
		}
	}
	else {
		for(let i in productCategory){
			const option = document.createElement('option');
	        option.text = productCategory[i];
	        kindSelection.options.add(option);
		}
	}
}

//let picture can show instantly
function readURL(input) {
	if (input.files && input.files[0]) {
	  const reader = new FileReader();
	  reader.onload = function loadPicture(e) {
	    document.getElementById('show-new-photo').setAttribute('src', e.target.result);
		  window.pictureBase64 = e.target.result;
		  console.log(e.target.result);
	  };
	  reader.readAsDataURL(input.files[0]);
	}
}

function sidebarSetting(){
    document.getElementById('dismiss').addEventListener('click', function(){
        document.getElementById('sidebar').classList.remove('active');
    });
    document.getElementById('sidebarCollapse').addEventListener('click', function(){
        document.getElementById('sidebar').classList.add('active');
    });
}

async function deleteAction(func){
	const idSpan = document.getElementById(`${func}-id`);
	const result = await FetchData.post(calendarAPI.deleteAction, {
		func: func,
		account: account,
		id : parseInt(idSpan.innerHTML),
	});
	idSpan.innerHTML = -1;
	window.location.reload();
}

function initialDate(){
	document.getElementById('activity-date').value = nowTimeFormal;
	document.getElementById('start-time').value = "00:00";
	document.getElementById('end-time').value = "00:00";
	document.getElementById('cost-date').value = nowTimeFormal;
	document.getElementById('photo-date').value = nowTimeFormal;
}

function clearModal(action){
	let date = document.getElementById('date-title').innerHTML;
	
	if(action == 'activity'){
		document.getElementById('activity-title').innerHTML = "新增活動";
		document.getElementById('activity-name').value = "";
		document.getElementById('activity-date').value = date;
		document.getElementById('start-time').value = "00:00";
		document.getElementById('end-time').value = "00:00";
		$(`input[name=activity-color][value=red]`).attr('checked',true);
		document.getElementById('notice-switch').checked = false;
		$("#notice-time").attr('disabled', true);
		document.getElementById('add-activity-btn').setAttribute('style', 'display: initial;');
		document.getElementById('update-activity-btn').setAttribute('style', 'display: none;');
		document.getElementById('delete-activity-btn').setAttribute('style', 'display: none;');
	}
	else if(action == 'cost'){
		document.getElementById('cost-title').innerHTML = "新增記帳";
		document.getElementById('cost-date').value = date;
		$("#category").find(`option:contains(美髮)`).attr('selected',true);
		updateOption();
		document.getElementById('cost-cost').value = 0;
		document.getElementById('cost-description').value = "";
		$(`input[name=activity-color][value=red]`).attr('checked',true);
		document.getElementById('add-cost-btn').setAttribute('style', 'display: initial;');
		document.getElementById('update-cost-btn').setAttribute('style', 'display: none;');
		document.getElementById('delete-cost-btn').setAttribute('style', 'display: none;');
	}
	else {
		document.getElementById('picture-title').innerHTML = "新增紀錄頭髮";
		document.getElementById('photo-date').value = date;
		document.getElementById('show-new-photo').src = "img/blank.png";
		document.getElementById('photo-description').value = "";
		document.getElementById('add-picture-btn').setAttribute('style', 'display: initial;');
		document.getElementById('update-picture-btn').setAttribute('style', 'display: none;');
		document.getElementById('delete-picture-btn').setAttribute('style', 'display: none;');

	}
	document.getElementById(`${action}-id`).innerHTML = -1;
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
	initialDate();
	getAction();
	sidebarSetting();

	document.getElementById('add-activity-btn').addEventListener('click', postActivity);
	document.getElementById('update-activity-btn').addEventListener('click', postActivity);
	document.getElementById('delete-activity-btn').addEventListener('click', function(){deleteAction('activity');});
	document.getElementById('add-cost-btn').addEventListener('click', postCost);
	document.getElementById('update-cost-btn').addEventListener('click', postCost);
	document.getElementById('delete-cost-btn').addEventListener('click', function(){deleteAction('cost');});
	document.getElementById('add-picture-btn').addEventListener('click', postPicture);
	document.getElementById('update-picture-btn').addEventListener('click', postPicture);
	document.getElementById('delete-picture-btn').addEventListener('click', function (){deleteAction("picture");});
	document.getElementById('notice-switch').addEventListener('change', function(){
		const noticeSelection = document.getElementById('notice-time');
		if(this.checked) noticeSelection.disabled = false;
		else noticeSelection.disabled = true;
	});
	
	document.getElementById('category').addEventListener('change', updateOption);
	document.getElementById('new-hair-photo').addEventListener('change',  function read() { readURL(this); })

	document.getElementById('next').addEventListener('click', getAction);
	document.getElementById('prev').addEventListener('click', getAction);

}

window.addEventListener('load', init);