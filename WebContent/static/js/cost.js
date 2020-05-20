const costAPI = "api-calendar?func=cost";
const account = localStorage.getItem('account');


async function getcost(){
	let currentYear = document.getElementById('calendar-year').innerHTML;
	let monthString = document.getElementById('calendar-title').innerHTML;
	let currentMonth = month_name.indexOf(monthString) + 1;

	const result = await FetchData.get(`${costAPI}&account=${account}&year=${currentYear}&month=${currentMonth}`);
	let costResult = await result.json();
	const costTbody = document.getElementById('cost-tbody');

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
	costResult.sort(sortBy('time', false, Date.parse));
	
	const totalCost = document.getElementById('total-cost');
	let tmp = "";
	let total = 0;
	costResult.forEach(cost => {
		let costDate = cost.time.split('-')[2];
		total += cost.cost;
		tmp += `<tr>
					<td class="date">${costDate}</td>
                    <td class="${cost.color}-color"></td>
                    <td class="money">$${cost.cost}</td>
                    <td class="type">${cost.category}-${cost.kind}</td>
                    <td class="description">${cost.description}</td>
		</tr>`;
	});
	totalCost.innerHTML = total;
	costTbody.innerHTML = tmp;
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
	getcost();

	document.getElementById('next').addEventListener('click', getcost);
	document.getElementById('prev').addEventListener('click', getcost);
	
	document.getElementById('chart-btn').addEventListener('click', function(){
		let currentYear = document.getElementById('calendar-year').innerHTML;
		let monthString = document.getElementById('calendar-title').innerHTML;
		let currentMonth = month_name.indexOf(monthString) + 1;
		this.href += `year=${currentYear}&month=${currentMonth}`;
	});
}

window.addEventListener('load', init);