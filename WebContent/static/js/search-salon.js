const serviceCheckbox= {type0: "洗髮", type1: "剪髮", type2: "染髮", type3: "燙髮", type4: "護髮", type5: "其他"};
const salonAPI = 'api-search?func=salon';
const favoriteAPI = {
	all: 'api-favorite?func=salon',
	new: 'api-favorite-new',
	delete: 'api-favorite-delete'
};
const account = localStorage.getItem('account');
const action = {
		all: 0,
		keyword: 1,
		service: 2
	}

async function getSalon(){
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.all){
		window.action = action.all;
		showMoreBtn.value = 0;
	}
	showMoreBtn.value++;
	let page = showMoreBtn.value;
    const result = await FetchData.get(`${salonAPI}&page=${page}`);
    const salonJson = await result.json();
    drawCard(salonJson, page);
}

async function searchByName() {
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.keyword){
		window.action = action.keyword;
		showMoreBtn.value = 0;
	}
	showMoreBtn.value++;
    const keyword = document.getElementById('search').value;
    if(keyword == ""){
    	getSalon();
    }
    else{
    	// to check whether keyword is difficult
    	if(window.keyword != keyword) {
    		showMoreBtn.value = 1;
    		window.keyword = keyword;
    	}
    	let page = showMoreBtn.value;
	    const result = await FetchData.get(`${salonAPI}&keyword=${keyword}&page=${page}`);
	    const keywordJson = await result.json();
	    drawCard(keywordJson, page);
    }
}

async function searchByService(event) {
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.service){
		window.action = action.service;
		showMoreBtn.value = 0;
	}
	else {
		if(event.id != "show-more-btn"){
			showMoreBtn.value = 0;
		}
	}
	showMoreBtn.value++;
	let page = showMoreBtn.value;
	
	const checkedService = $('input:checkbox[name="service"]:checked');
	const checkedServiceName = [];
	let tmp = "";
	for(let i = 0; i < checkedService.length; i++){
		tmp += `&service=${serviceCheckbox[checkedService[i].value]}`;
	}
	if(tmp == "") {
		window.action = action.all;
		showMoreBtn.value = 0;
		page = 1;
	}
	console.log(`${salonAPI}${tmp}&page=${page}`);
	const result = await FetchData.get(`${salonAPI}${tmp}&page=${page}`);
	const serviceJson = await result.json();
	drawCard(serviceJson, page);
}

async function drawCard(Json, page){
	const salonCards = document.getElementById('salon-cards');
    let newCard = "";
    let favoriteJson = {id: []};
    if(account != null){
		const result = await FetchData.get(`${favoriteAPI.all}&account=${account}`);
		favoriteJson = await result.json();
    }
	let favoriteImg = "";
    Json.forEach( salon => {
    	if(salon.name == 'free') return;
    	let favoriteImg = "img/favorite_undo.png";
    	if(favoriteJson.id.find(element => element == salon.id)) {
    		favoriteImg = "img/favorite.png";
    	}
    	newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${salon.id}"> \
            <div class="card"> \
                <a href="javascript:void(0)"><img class="favorite" src="${favoriteImg}" id="favorte-${salon.id}" onclick="updateFavorite(this);" alt="favorite"></a> \
                <a href="salon-detail.html?id=${salon.id}"> \
                <img class="card-img-top lozad" data-src="${salon.picture}" alt="${salon.name} photo"> \
                <div class="card-body"> \
        	<p class="card-text">${salon.name}</p> \
                    <p class="card-text">${salon.address}</p> \
                    <p class="card-text">${salon.phone}</p> \
                    </div></a></div>
        </div>`;
    });
    if(page == 1) salonCards.innerHTML = newCard;
    else salonCards.innerHTML += newCard;
    const observer = lozad(); // lazy load
	observer.observe();
}

async function updateFavorite(e){
	if(account == null){
		alert("登入才可以加入我的最愛喔！");
		return;
	}
	let id = parseInt(e.id.split('-')[1]);
	if(e.src.split('img')[1] == '/favorite_undo.png'){
		console.log("add!");
		const result = await FetchData.post(favoriteAPI.new, {
			func: "salon",
			account: account,
			id: id
		});
		e.src = "img/favorite.png";
	}
	else {
		console.log("delete!");
		const result = await FetchData.post(favoriteAPI.delete, {
			func: "salon",
			account: account,
			id: id
		});
		e.src = "img/favorite_undo.png";
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

function init(){
	sidebarSetting();
	// recording action to let show more button can show correct action's page
	window.action = action.all;
    getSalon();

    document.getElementById('show-more-btn').addEventListener('click', function(){
    	if(window.action == action.all) getSalon();
    	else if(window.action == action.keyword) searchByName();
    	else if(window.action == action.service) searchByService(this);
    });
    
    document.getElementsByName('service').forEach(service => {
    	service.addEventListener('click', searchByService);
    });
    
    document.getElementById('search-btn').addEventListener('click', searchByName);
    document.getElementById('search').addEventListener('search', searchByName);
}

window.addEventListener('load', init);