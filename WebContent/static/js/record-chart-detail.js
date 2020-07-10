const costAPI = "api-calendar?func=cost";
const account = localStorage.getItem('account');
categorySet = ["美髮", "商品"]
kindSet = {
		category0: ["洗髮", "剪髮", "染髮", "燙髮", "護髮", "其他"],
		category1: ["洗髮類", "潤髮類", "護髮類", "其他"]
}

let url = location.href;
let categoryPosition = url.match("category=").index + 9;
let yearPosition = url.match("year=").index + 5;
let monthPosition = url.match("month=").index + 6;
let category = url.substring(categoryPosition, categoryPosition+1)
let year = url.substring(yearPosition, monthPosition-7);
let month = url.substring(monthPosition);

function drawCanvas(kindTotal){
	let length = kindTotal.length;
	let allKind = kindSet[`category${category}`];
	let ctx = document.getElementById("chart").getContext('2d');
	let myChart = new Chart(ctx, {
	    type: 'pie',
	    data: {
	        labels: allKind,
	        datasets: [{            
	            data: kindTotal,
	            backgroundColor: [
	                '#a8d8ea',
	                '#aa96da',
	                '#fcbad3',
	                '#ffffd2',
	                '#FED3A9',
	                '#8FF9BF'
	            ],
	            borderWidth: 1
	        }]
	    },
	    options: {}
	});
}

async function getcost(){

	document.getElementById('date').innerHTML = `${year}年${month}月`;
	const result = await FetchData.get(`${costAPI}&account=${account}&year=${year}&month=${month}`);
	let costResult = await result.json();
	const totalCost = document.getElementById('total-cost');
	let tmp = "";
	let total = 0;
	let kindTotal = [0, 0, 0, 0, 0, 0];
	costResult.forEach(cost => {
		if(cost.category == categorySet[category]){
			total += cost.cost;
			const allKind = kindSet[`category${category}`];
			for(let i = 0; i < allKind.length; i++){
				if(cost.kind == allKind[i]){
					kindTotal[i] += cost.cost;
					break;
				}
			}
		}
	});
	totalCost.innerHTML = total;
	drawCanvas(kindTotal);
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
	
	document.getElementById('back').href = `record-chart.html?year=${year}&month=${month}`;
	
}

window.addEventListener('load', init);