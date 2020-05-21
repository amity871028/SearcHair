const costAPI = "api-calendar?func=cost";
const account = localStorage.getItem('account');

var url = location.href;
var categoryPosition = url.match("category=").index + 9;
var yearPosition = url.match("year=").index + 5;
var monthPosition = url.match("month=").index + 6;
var year = url.substring(yearPosition, monthPosition-7);
var month = url.substring(monthPosition);

function drawCanvas(hardressTotal, productTotal){
	var ctx = document.getElementById("chart").getContext('2d');
	var myChart = new Chart(ctx, {
	    type: 'pie',
	    data: {
	        labels: ["美髮", "商品"],
	        datasets: [{            
	            data: [hardressTotal, productTotal],
	            backgroundColor: [
	                'rgba(255, 99, 132)',
	                'rgba(54, 162, 235)',
	            ],
	            borderWidth: 1
	        }]
	    },
	    options: {onClick: graphClickEvent}
	});
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