const salonDetailAPI = 'api-search-detail?func=salon';
const favoriteAPI = {
		all: 'api-favorite?func=salon',
		new: 'api-favorite-new',
		delete: 'api-favorite-delete'
};
const account = localStorage.getItem('account');

async function getSalonDetail(){
	let url = location.href;
	let idPosition = url.match("id=").index + 3;
	let id = parseInt(url.substring(idPosition));
	let result = await FetchData.get(`${salonDetailAPI}&id=${id}`);
	const salon = await result.json();


    let favoriteJson = {id: []};
    if(account != null){
		result = await FetchData.get(`${favoriteAPI.all}&account=${account}`);
		favoriteJson = await result.json();
    }
	let favoriteImg = "img/favorite_undo.png";
	if(favoriteJson.id.find(element => element == id)) {
		favoriteImg = "img/favorite.png";
	}
	
    // update card
    const salonCards = document.getElementById('salon-card');
	let newCard="";
	newCard += `<a href="javascript:void(0)"><img class="favorite" src="${favoriteImg}" id="favorite-${id}" alt="favorite" onclick="updateFavorite(this)"></a>  \
            
            <img class="card-img-top" src="${salon.picture}" alt="${salon.name} photo"> \
            <div class="card-body"> \
    	<p class="card-text">${salon.name}</p> \
                <p class="card-text">${salon.address}</p> \
                <p class="card-text">${salon.phone}</p> \
		</div>`;
    salonCards.innerHTML += newCard;
    
    // update information
    document.getElementById('salon-name').innerHTML = salon.name;
    document.getElementById('address-link').href = "https://www.google.com.tw/maps/place/" + salon.address;
    document.getElementById('salon-address').innerHTML = salon.address;
    document.getElementById('salon-phone').innerHTML = salon.phone;
    let businessTime = salon.businessTime;
    businessTime = businessTime.replace(/\n/g, '<br />');
    document.getElementById('salon-business-time').innerHTML = businessTime;
    
    //update stylist & works
    const stylistAndWorksCards = document.getElementById('stylist-and-works');
    let tmp = "";
    const stylists = salon.stylist_info;
    
    stylists.forEach(stylist =>{
    	tmp += `<tr><td class="stylist-photo-td">
    				<div class="shadow">
					<a href="stylist-detail.html?id=${stylist.id}">
						<img src="${stylist.picture}" class="card-img stylist-photo" alt="...">
					</a><div></td>`;
        for(let i = 0; i < stylist.works.length; i++){
            let work = stylist.works[i];
            let prevWork = stylist.works[stylist.works.length - 1];
            let nextWork = stylist.works[0];
            if(i != 0) prevWork = stylist.works[i-1];
            if(i != stylist.works.length-1) nextWork = stylist.works[i+1];
            let description = "";
        	let stylistDetail = stylist.name;
        	if(work.description != "NULL") description = work.description.replace(/\n/g, '<br />');
        	if(stylist.stylist_job_title != "NULL") stylistDetail += `(${stylist.stylist_job_title})`;
        	const workHashtags = JSON.parse(work.hashtag);
        	let tmpHashtags = "";
        	for(let i = 0; i < workHashtags.length; i++){
        		tmpHashtags += `<span class="badge badge-primary">${workHashtags[i]}</span>`;
        	}
        	
        	tmp += `<td rowspan="2">
        	<div class="row align-items-center">
	        	<div class="cssbox" id="work-${work.id}">
		            <a id="image${work.id}" href="#image${work.id}">
		              <img class="cssbox_thumb" src="${work.picture}">
		              <span class="cssbox_full">
	    				<div class="row">
	    					<div class="col-md-6 padding-40">
	    						<img src="${work.picture}" class="card-img" alt="work's photo">
	    					</div>
	    					<div class="col-md-6 padding-40">
	    						<div class="work-content">
	    							<p id="stylist-detail">${stylistDetail}</p>
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
          </div></div></td>`;
        }
        for(let i = stylist.works.length; i < 3; i++){
        	tmp += `<td rowspan="2"><div class="blank"><img src="img/blank.png"></div></td>`;
        }
        tmp += `</tr><tr><th>${stylist.name}</th></tr>`;
    });
    stylistAndWorksCards.innerHTML = tmp;
}

function sidebarSetting(){
    document.getElementById('dismiss').addEventListener('click', function(){
        document.getElementById('sidebar').classList.remove('active');
    });
    document.getElementById('sidebarCollapse').addEventListener('click', function(){
        document.getElementById('sidebar').classList.add('active');
    });
}

async function updateFavorite(e){
	if(account == null){
		alert("登入才可以加入我的最愛喔！");
		return;
	}
	let id = parseInt(e.id.split('-')[1]);
	if(e.src.split('img')[1] == '/favorite_undo.png'){
		const result = await FetchData.post(favoriteAPI.new, {
			func: "salon",
			account: account,
			id: id
		});
		e.src = "img/favorite.png";
	}
	else {
		const result = await FetchData.post(favoriteAPI.delete, {
			func: "salon",
			account: account,
			id: id
		});
		e.src = "img/favorite_undo.png";
	}
}

function init(){
	sidebarSetting();
    getSalonDetail();
}

window.addEventListener('load', init);