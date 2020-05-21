const serviceCheckbox= {type0: "洗髮", type1: "剪髮", type2: "染髮", type3: "燙髮", type4: "護髮", type5: "其他"};
const stylistAPI = 'api-search?func=stylist';
const action = {
		all: 0,
		keyword: 1,
		service: 2
	}

async function getStylist(){
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.all){
		window.action = action.all;
		showMoreBtn.value = 0;
	}
	showMoreBtn.value++;
	let page = showMoreBtn.value;
    const result = await FetchData.get(`${stylistAPI}&page=${page}`);
    const stylistJson = await result.json();
    drawCard(stylistJson, page);
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
    	getStylist();
    }
    else{
    	// to check whether keyword is difficult
    	if(window.keyword != keyword) {
    		showMoreBtn.value = 1;
    		window.keyword = keyword;
    	}
    	let page = showMoreBtn.value;
    	const result = await FetchData.get(`${stylistAPI}&keyword=${keyword}&page=${page}`);
	    const keywordJson = await result.json();
	    drawCard(keywordJson, page);
    }
}

async function searchByServiceAndPrice(event) {
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
	
	const minPrice = document.getElementById('min-price').value;
	const maxPrice = document.getElementById('max-price').value;
	
	const checkedService = $('input:checkbox[name="service"]:checked');
	const checkedServiceName = [];
	let tmp = "";
	for(let i = 0; i < checkedService.length; i++){
		tmp += `&service=${serviceCheckbox[checkedService[i].value]}`;
	}
	const result = await FetchData.get(`${stylistAPI}${tmp}&price=${minPrice}&price=${maxPrice}&page=${page}`);
    const serviceJson = await result.json();
    drawCard(serviceJson, page);
	  
}

function modifyPriceInput(){
	const checkedService = $('input:checkbox[name="service"]:checked');
	console.log(checkedService);
	const checkedServiceName = [];
	let tmp = "";
	for(let i = 0; i < checkedService.length; i++){
		tmp += `&service=${serviceCheckbox[checkedService[i].value]}`;
	}
	
	if(checkedService.length != 0){
		$("#min-price").attr('disabled', false);
		$("#max-price").attr('disabled', false);
		$("#search-price-btn").attr('disabled', false);
	}
	else{
		$("#min-price").attr('disabled', true);
		$("#max-price").attr('disabled', true);
		$("#search-price-btn").attr('disabled', true);
		getStylist();
	}
}
function drawCard(json, page){
	const stylistCards = document.getElementById('stylist-cards');
    let newCard = "";
    json.forEach(stylist => {
        newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${stylist.id}"> \
            <div class="card"> \
                <!--<a href="#"><img class="favorite" src="../static/img/favorite_undo.png" id="favorte-${stylist.id}" alt="favorite"></a>-->  \
                <a href="stylist-detail.html?id=${stylist.id}"> \
                <img class="card-img-top lozad" data-src="${stylist.picture}" alt="${stylist.name} photo"> \
                <div class="card-body"> \
        	<p class="card-text">${stylist.name}</p> \
                    <p class="card-text">${stylist.salon}</p> \
                    <p class="card-text">${stylist.address}</p> \
                    </div></a></div>
        </div>`;
    });
    stylistCards.innerHTML = newCard;
    
    const observer = lozad(); // lazy load
	observer.observe();
}

function sidebarSetting(){
    document.getElementById('dismiss').addEventListener('click', function(){
        document.getElementById('sidebar').classList.remove('active');
    });
    document.getElementById('sidebarCollapse').addEventListener('click', function(){
        document.getElementById('sidebar').classList.add('active');
    });
}

function favoriteSetting(){
    const favoriteList = document.getElementsByClassName('favorite');
    for(let i = 0; i < favoriteList.length; i++){
        favoriteList[i].addEventListener('mouseover', function(){
            this.src = "../static/img/favorite.png";
        });
        favoriteList[i].addEventListener('mouseout', function(){
            this.src = "../static/img/favorite_undo.png";
        });
    }
}

function init(){
	sidebarSetting();
	// recording action to let show more button can show correct action's page
	window.action = action.all;
    getStylist();

    document.getElementById('show-more-btn').addEventListener('click', function(){
    	if(window.action == action.all) getStylist();
    	else if(window.action == action.keyword) searchByName();
    	else if(window.action == action.service) searchByServiceAndPrice(this);
    });
    
    // favoriteSetting();
    document.getElementsByName('service').forEach(service => {
    	service.addEventListener('click', modifyPriceInput);
    });
    
    document.getElementById('search-price-btn').addEventListener('click', searchByServiceAndPrice);
    document.getElementById('search-btn').addEventListener('click', searchByName);
    document.getElementById('search').addEventListener('search', searchByName);
}

window.addEventListener('load', init);