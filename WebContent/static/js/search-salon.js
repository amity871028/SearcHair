const serviceCheckbox= {type0: "洗髮", type1: "剪髮", type2: "染髮", type3: "燙髮", type4: "護髮", type5: "其他"};
const salonAPI = 'api-search?func=salon';
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

function drawCard(Json, page){
	const salonCards = document.getElementById('salon-cards');
    let newCard = "";
    Json.forEach( salon => {
    	newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${salon.id}"> \
            <div class="card"> \
                <a href="#"><img class="favorite" src="img/favorite_undo.png" id="favorte-${salon.id}" onclick="changeFavorite(this);" alt="favorite"></a>  \
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
    //favoriteSetting();
}

function changeFavorite(event){
	console.log(event.src.split('img/')[1]);
	console.log("favorite.png");
	let favoriteImg = "img/favorite.png";
	let undoFavoriteImg = "img/favorite_undo.png";
	if(event.src.match("favorite_undo.png")){
		event.src = favoriteImg;
	}
	else {
		event.src = undoFavoriteImg;
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

function favoriteSetting(){
    const favoriteList = document.getElementsByClassName('favorite');
	console.log(favoriteList);
	console.log(favoriteList.length);
    for(let i = 0; i < favoriteList.length; i++){
        favoriteList[i].addEventListener('mouseover', function(){
            this.src = "img/favorite.png";
        });
        favoriteList[i].addEventListener('mouseout', function(){
            this.src = "img/favorite_undo.png";
        });
    }
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