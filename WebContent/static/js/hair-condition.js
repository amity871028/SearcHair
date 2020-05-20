const pictureAPI = "api-calendar?func=picture";
const account = localStorage.getItem('account');


async function getPicture(){
	let currentYear = document.getElementById('calendar-year').innerHTML;
	let monthString = document.getElementById('calendar-title').innerHTML;
	let currentMonth = month_name.indexOf(monthString) + 1;

	const result = await FetchData.get(`${pictureAPI}&account=${account}&year=${currentYear}&month=${currentMonth}`);
	let pictureResult = await result.json();
	const pictureCards = document.getElementById('all-photo');

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

	pictureResult.sort(sortBy('time', false, Date.parse));
	
	
	let tmp = "";
	pictureResult.forEach(picture => {
		tmp += `<div class="col-lg-3 col-md-4 col-sm-6 mb-3">
                    <div class="card">
                        <img class="card-img-top" src="${picture.picture}" alt="Card image cap">
                        <div class="card-body">
                            <p class="card-title">${picture.time}</p>
                            <p class="card-text">${picture.description}</p>
                        </div>
                    </div>
                </div>`;
	});
	pictureCards.innerHTML = tmp;
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
	getPicture();

	document.getElementById('next').addEventListener('click', getPicture);
	document.getElementById('prev').addEventListener('click', getPicture);
}

window.addEventListener('load', init);