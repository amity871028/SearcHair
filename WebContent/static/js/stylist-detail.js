
const stylistDetailAPI = 'api-search-detail?func=stylist';
const stylistWorkDetailAPI = 'api-search-detail?func=stylist_works&id=';



async function getstylistDetail(){
	var url = location.href;
	var idPosition = url.match("id=").index + 3;
	var id = url.substring(idPosition);
	const result = await FetchData.get(`${stylistDetailAPI}&id=${id}`);
	const stylist = await result.json();

    // update card
    const stylistCards = document.getElementById('stylist-card');
	let newCard="";
	newCard += `<!--<a href="#"><img class="favorite" src="../static/img/favorite_undo.png" id="favorte-${id}" alt="favorite"></a>-->  \
            
            <img class="card-img-top" src="${stylist.picture}" alt="${stylist.name} photo"> \
            <div class="card-body"> \
    		<p class="card-text">${stylist.name}</p> \
    		<p class="card-text">${stylist.salon}</p> \
            <p class="card-text">${stylist.address}</p> \s
		</div>`;
    stylistCards.innerHTML += newCard;
    
    // update information
    document.getElementById('stylist-name').innerHTML = stylist.name;
    document.getElementById('salon-link').href = `/salon-detail?id=${stylist.salon_id}`;
    document.getElementById('salon').innerHTML = stylist.salon;
    document.getElementById('address-link').href = "https://www.google.com.tw/maps/place/" + stylist.address;
    document.getElementById('salon-address').innerHTML = stylist.address;
    
    // update service
    const serviceCards = document.getElementById('service-cards');
    newCards = "";
    if(stylist.service.length == 0){
    	newCards = "<h5>無</h5>";
    	serviceCards.setAttribute('class', '');
    }
    else {
	    stylist.service.forEach((service, idx) => {
	    	let price = "";
	    	if(service.max_price == 100000){
	    		if(service.min_price == -1) price = "<br>";
	    		else price = `$${service.min_price}起`;
	    	}
	    	else price = `$${service.min_price}~$${service.max_price}`;
	    	newCards += `<div class="card-size">
	                        <div class="card">
	                            <a class="nav-link" href="#service${idx}-modal"  data-toggle="modal">
	                                <div class="card-body">
	                                    <p class="card-text">${service.name}<br>${price}</p>
	                                </div> 
	                            </a>
	                        </div>
	                    </div>`;
	    	
	
	        // update service modal
	    	let description = service.description.replace(/\n/g, '<br />');
	    	if(description == "NULL") description="無資訊。";
	        const serviceModal = document.createElement('div');
	        serviceModal.setAttribute('class', 'modal fade');
	        serviceModal.setAttribute('id', `service${idx}-modal`);
	        serviceModal.innerHTML = `<div class="modal-dialog" role="document">
	                <div class="modal-content">
	                    <div class="modal-header">
	                        <h5 class="modal-title">${service.name} - 更多資訊</h5>
	                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                        <span aria-hidden="true">&times;</span>
	                        </button>
	                    </div>
	                    <div class="modal-body">
	                        <div class="form-group">
	                            ${description}
	                        </div>
	                    </div>
	                </div>
	            </div>`;
	        
	        document.body.appendChild(serviceModal);
	        
	    });
    }
    serviceCards.innerHTML = newCards;
    
    
    // update works
    const workCards = document.getElementById('work-cards');
    newCards = "";
    if(stylist.works.length == 0){
    	newCards = "<h5>無</h5>";
    	workCards.setAttribute('class', '');
    }
    else {
	    for(let i = 0; i < stylist.works.length; i++){
	        let work = stylist.works[i];
	        let prevWork = stylist.works[stylist.works.length-1];
	        let nextWork = stylist.works[0];
	        if(i != 0) prevWork = stylist.works[i-1];
	        if(i != stylist.works.length-1) nextWork = stylist.works[i+1];
	      
	    	let description = "";
	    	let stylistDetail = stylist.name;
	    	if(work.description != "NULL") description = work.description.replace(/\n/g, '<br />');
	    	if(stylist.job_title != "NULL") stylistDetail += `(${stylist.job_title})`;
	    	
	    	const workHashtags = JSON.parse(work.hashtag);
	    	let tmpHashtags = "";
	    	for(let i = 0; i < workHashtags.length; i++){
	    		tmpHashtags += `<span class="badge badge-primary">${workHashtags[i]}</span>`;
	    	}
	    	
	    	newCards += `<div class="cssbox" id="work-${work.id}">
	            <a id="image${work.id}" href="#image${work.id}">
	              <img class="cssbox_thumb" src="${work.picture}">
	              <span class="cssbox_full">
	                <div class="row">
	                  <div class="col-md-6 padding-40">
	                    <img src="${work.picture}" class="card-img" alt="work's photo" id="stylist-work-photo">
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
	          </div>`;
	    }
    }
    workCards.innerHTML = newCards;

    
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
    var favorite_list = document.getElementsByClassName('favorite');
    for(let i = 0; i < favorite_list.length; i++){
        favorite_list[i].addEventListener('mouseover', function(){
            this.src = "../static/img/favorite.png";
        });
        favorite_list[i].addEventListener('mouseout', function(){
            this.src = "../static/img/favorite_undo.png";
        });
    }
}

function init(){
	sidebarSetting();
    getstylistDetail();
}

window.addEventListener('load', init);