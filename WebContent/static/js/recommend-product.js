const typeRadio= {type0: "洗髮乳", type1: "潤髮", type2: "護髮油", type3: "染髮劑"};
const productAPI = 'api-search?func=product';
const favoriteAPI = {
	all: 'api-favorite?func=product',
	new: 'api-favorite-new',
	delete: 'api-favorite-delete'
};
const account = localStorage.getItem('account');
const action = {
		all: 0,
		keyword: 1,
		type: 2
	}

async function getProduct(){
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.all){
		window.action = action.all;
		showMoreBtn.value = 0;
	}
	showMoreBtn.value++;
	let page = showMoreBtn.value;
    const result = await FetchData.get(`${productAPI}&page=${page}`);
    const productJson = await result.json();
    drawCard(productJson, page);
}

async function searchByKeyword() {
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.keyword){
		window.action = action.keyword;
		showMoreBtn.value = 0;
	}
	showMoreBtn.value++;
    const keyword = document.getElementById('search').value;
    if(keyword == ""){
    	getProduct();
    }
    else{
    	// to check whether keyword is difficult
    	if(window.keyword != keyword) {
    		showMoreBtn.value = 1;
    		window.keyword = keyword;
    	}
    	let page = showMoreBtn.value;
	    const result = await FetchData.get(`${productAPI}&keyword=${keyword}&page=${page}`);
	    const keywordJson = await result.json();
	    drawCard(keywordJson, page);
    }
}

async function searchByType(event) {
	const showMoreBtn = document.getElementById('show-more-btn');
	if(window.action != action.type){
		window.action = action.type;
		showMoreBtn.value = 0;
	}
	else {
		if(event.id != "show-more-btn"){
			showMoreBtn.value = 0;
		}
	}
	showMoreBtn.value++;
	let page = showMoreBtn.value;
	
	const checkedtype = $('input:radio[name="type"]:checked')[0];
	if(typeRadio[checkedtype.value] == "洗髮乳"){
		document.getElementById('feature-div').setAttribute('style', 'display: initial;');
		if(page == 1) document.getElementsByName('feature')[0].checked = true;
		searchByFeature(event);
		return;
	}
	else {
		document.getElementById('feature-div').setAttribute('style', 'display: none;');
	}
	let result = "";
	if(checkedtype.value == "all") result = await FetchData.get(`${productAPI}&page=${page}`);
	else result = await FetchData.get(`${productAPI}&type=${typeRadio[checkedtype.value]}&page=${page}`);
	const typeJson = await result.json();
	drawCard(typeJson, page);
}

async function searchByFeature(event){
	const showMoreBtn = document.getElementById('show-more-btn');
	if(event.id != "show-more-btn"){
		showMoreBtn.value = 1;
	}
	let page = showMoreBtn.value;
	
	const checkedFeture = $('input:radio[name="feature"]').index($('input:radio[name="feature"]:checked'));
	let result = "";
	if(checkedFeture == 0) result = await FetchData.get(`${productAPI}&type=洗髮乳&page=${page}`);
	else result = await FetchData.get(`${productAPI}&type=洗髮乳&feature=${checkedFeture-1}&page=${page}`);
	const Json = await result.json();
	drawCard(Json, page);
	
}

async function drawCard(Json, page){
	const productCards = document.getElementById('product-cards');
    let newCard = "";
    let favoriteJson = {id: []};
    if(account != null){
		const result = await FetchData.get(`${favoriteAPI.all}&account=${account}`);
		favoriteJson = await result.json();
    }
	let favoriteImg = "";
    Json.forEach( product => {
    	let favoriteImg = "img/favorite_undo.png";
    	if(favoriteJson.id.find(element => element == product.id) != undefined) {
    		favoriteImg = "img/favorite.png";
    	}
    	newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${product.id}"> \
            <div class="card"> \
                <a href="javascript:void(0)"><img class="favorite" src="${favoriteImg}" id="favorte-${product.id}" onclick="updateFavorite(this);" alt="favorite"></a> \
                <a href="product-detail.html?id=${product.id}"> \
                <img class="card-img-top lozad" data-src="${product.picture}" alt="${product.name} photo"> \
                <div class="card-body"> \
        	<p class="card-text">${product.name}</p> \
                    <p class="card-text">${product.capacity} ml</p> \
                    <p class="card-text">NT$ ${product.price}</p> \
                    </div></a></div>
        </div>`;
    });
    if(page == 1) productCards.innerHTML = newCard;
    else productCards.innerHTML += newCard;
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
		const result = await FetchData.post(favoriteAPI.new, {
			func: "product",
			account: account,
			id: id
		});
		e.src = "img/favorite.png";
	}
	else {
		const result = await FetchData.post(favoriteAPI.delete, {
			func: "product",
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
    getProduct();

    document.getElementById('show-more-btn').addEventListener('click', function(){
    	if(window.action == action.all) getProduct();
    	else if(window.action == action.keyword) searchByKeyword();
    	else if(window.action == action.type) searchByType(this);
    });

    document.getElementsByName('type').forEach(type => {
    	type.addEventListener('click', searchByType);
    });
    document.getElementsByName('feature').forEach(feature => {
    	feature.addEventListener('click', searchByFeature);
    });
    
    document.getElementById('search-btn').addEventListener('click', searchByKeyword);
    document.getElementById('search').addEventListener('search', searchByKeyword);
}

window.addEventListener('load', init);