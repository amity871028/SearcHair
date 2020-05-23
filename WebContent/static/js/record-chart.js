const costAPI = "api-calendar?func=cost";
const account = localStorage.getItem('account');

let url = location.href;
let yearPosition = url.match("year=").index + 5;
let monthPosition = url.match("month=").index + 6;
let year = url.substring(yearPosition, monthPosition-7);
let month = url.substring(monthPosition);

function drawCanvas(hardressTotal, productTotal){
	let ctx = document.getElementById("chart").getContext('2d');
	if(hardressTotal == 0 && productTotal == 0){
		let myChart = new Chart(ctx, {
		    type: 'pie',
		    data: {
		        labels: ["無花費"],
		        datasets: [{            
		            data: [1],
		            backgroundColor: [
		                '#a8d8ea'
		            ],
		            borderWidth: 1
		        }]
		    },
		    options: {}
		});
	}
	else {
		let myChart = new Chart(ctx, {
		    type: 'pie',
		    data: {
		        labels: ["美髮", "商品"],
		        datasets: [{            
		            data: [hardressTotal, productTotal],
		            backgroundColor: [
		                '#a8d8ea',
		                '#fcbad3'
		            ],
		            borderWidth: 1
		        }]
		    },
		    options: {onClick: graphClickEvent}
		});
	}
}

function graphClickEvent(event, array){
	let category = array[0]._index;
	window.location = `record-chart-detail.html?category=${category}&year=${year}&month=${month}`;
}
async function getcost(){

	document.getElementById('date').innerHTML = `${year}年${month}月`;
	const result = await FetchData.get(`${costAPI}&account=${account}&year=${year}&month=${month}`);
	let costResult = await result.json();

	const totalCost = document.getElementById('total-cost');
	const hairdressCost = document.getElementById('hairdress-cost');
	const productCost = document.getElementById('product-cost');
	let tmp = "";
	let total = 0;
	let hairdressTotal = 0;
	let productTotal = 0;
	
	costResult.forEach(cost => {
		total += cost.cost;
		if(cost.category == "美髮"){
			hairdressTotal += cost.cost;
		}
		else {
			productTotal += cost.cost;
		}
		
	});
	totalCost.innerHTML = hairdressTotal + productTotal;
	hairdressCost.innerHTML = hairdressTotal;
	productCost.innerHTML = productTotal;
	
	drawCanvas(hairdressTotal, productTotal);
}
function delayURL(url, time) {
	  setTimeout(() => { window.location.href = `${url}`; }, time);
	}

function init(){
	if(account == null){
		alert("無法瀏覽此頁面！請登入後再查看！");
	    delayURL('./index.html', 200);
	}
	getcost();
	
}

window.addEventListener('load', init);