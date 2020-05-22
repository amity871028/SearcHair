const stylistWorkDetailAPI = 'api-search-detail?func=stylist_works&id=';
const salonDetailAPI = 'api-search-detail?func=salon';

async function getSalonDetail(){
	var url = location.href;
	var idPosition = url.match("id=").index + 3;
	var id = url.substring(idPosition);
	const result = await FetchData.get(`${salonDetailAPI}&id=${id}`);
	const salon = await result.json();

    // update card
    const salonCards = document.getElementById('salon-card');
	let newCard="";
	newCard += `<!--<a href="#"><img class="favorite" src="../static/img/favorite_undo.png" id="favorte-${id}" alt="favorite"></a>-->  \
            
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
					<a href="stylist-detail.html?id=${stylist.id}">
						<img src="${stylist.picture}" class="card-img stylist-photo" alt="...">
					</a></td>`;
        
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
        	
        	tmp += `<td rowspan="2"><div class="cssbox" id="work-${work.id}">
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
          </div></td>`;
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
    getSalonDetail();
}

window.addEventListener('load', init);