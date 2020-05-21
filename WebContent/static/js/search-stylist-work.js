const stylistWorksAPI = 'api-search?func=stylist_works';
const stylistWorkDetailAPI = 'api-search-detail?func=stylist_works&id=';
const action = {
		all: 0,
		keyword: 1
	}

async function getStylistWork(){
	const showMoreBtn = document.getElementById('show-more-btn');
    const keyword = document.getElementById('search').value;
    if(keyword == "") {
    	if(window.action == action.keyword){
    		showMoreBtn.value = 0;
    		window.action = action.all;
    	}
    }
    else{
    	if(window.action == action.all){
    		showMoreBtn.value = 0;
    		window.action = action.keyword;
    	}
    	else {
        	if(window.keyword != keyword) {
        		showMoreBtn.value = 0;
        		window.keyword = keyword;
        	}
    	}
    }
	showMoreBtn.value++;
	page = showMoreBtn.value;
    console.log("window.action = " + window.action);
	let result = "";
	if(window.action == action.all){
		console.log(`${stylistWorksAPI}&page=${page}`);
		result = await FetchData.get(`${stylistWorksAPI}&page=${page}`);
	}
	else {
		console.log(`${stylistWorksAPI}&keyword=${keyword}&page=${page}`);
		result = await FetchData.get(`${stylistWorksAPI}&keyword=${keyword}&page=${page}`);
	}
    
    window.workJson = await result.json();
    const workCards = document.getElementById('work-cards');

    let newCard = "";        
    for(let i = 0; i < window.workJson.length; i++){
        let work = window.workJson[i];
        let prevWork = window.workJson[window.workJson.length-1];
        let nextWork = window.workJson[0];
        if(i != 0) prevWork = window.workJson[i-1];
        if(i != window.workJson.length-1) nextWork = window.workJson[i+1];
    	let description = work.description.replace(/\n/g, '<br />');
    
    	const workHashtags = JSON.parse(work.hashtag);
    	let tmpHashtags = "";
    	for(let i = 0; i < workHashtags.length; i++){
    		tmpHashtags += `<span class="badge badge-primary">${workHashtags[i]}</span>`;
    	}
        newCard += `<div class="cssbox" id="work-${work.id}">
        <a id="image${work.id}" href="#image${work.id}">
          <img class="cssbox_thumb" src="${work.picture}">
          <span class="cssbox_full">
            <div class="row">
              <div class="col-md-6 padding-40">
                <img src="${work.picture}" class="card-img" alt="work's photo" id="stylist-work-photo">
              </div>
              <div class="col-md-6 padding-40">
                  <div class="work-content">
                      <p id="stylist-detail">${work.stylist}</p>
                      <p id="stylist-work-description">${description}</p>
                      <p id="hashtag">${tmpHashtags}</p>
                  </div>
              </div>
            </div>
          </span>
        </a>
        <a class="cssbox_close" href="#void"></a>
        <a class="cssbox_prev" id="prev-${work.id}" href="#image${prevWork.id}">&lt;</a>
        <a class="cssbox_next" id="next-${work.id}" href="#image${nextWork.id}">&gt;</a>
      </div>`;
    }
    if(page == 1) workCards.innerHTML = newCard;
    else workCards.innerHTML += newCard;
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
    getStylistWork();
    document.getElementById('show-more-btn').addEventListener('click', getStylistWork);
    document.getElementById('search-btn').addEventListener('click', getStylistWork);
    document.getElementById('search').addEventListener('search', getStylistWork);
    // favoriteSetting();
    
    
}

window.addEventListener('load', init);