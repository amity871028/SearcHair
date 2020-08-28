const favoriteAPI = {
	all: 'api-favorite?',  // func = & account = 
	new: 'api-favorite-new',
	delete: 'api-favorite-delete',
	getSearch: 'api-search-detail?'
};

const account = localStorage.getItem('account');

async function getSalon(){
	let result = await FetchData.get(`${favoriteAPI.all}func=salon&account=${account}`);
	const salonIdJson = await result.json();

	const salonCards = document.getElementById('salon-cards');
    let newCard = "";
	for(let salonId of salonIdJson.id){
		result = await FetchData.get(`${favoriteAPI.getSearch}func=salon&id=${salonId}`);
		const salonDetail = await result.json();
		newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${salonId}"> \
            <div class="card"> \
                <a href="javascript:void(0)"><img class="favorite" src="img/favorite.png" id="favorte-${salonId}" onclick="updateFavorite('salon', this);" alt="favorite"></a> \
                <a href="salon-detail.html?id=${salonId}"> \
                <img class="card-img-top lozad" data-src="${salonDetail.picture}" alt="${salonDetail.name} photo"> \
                <div class="card-body"> \
        	<p class="card-text">${salonDetail.name}</p> \
                    <p class="card-text">${salonDetail.address}</p> \
                    <p class="card-text">${salonDetail.phone}</p> \
                    </div></a></div>
        </div>`;
	}
	salonCards.innerHTML = newCard;
    const observer = lozad(); // lazy load
	observer.observe();
}

async function getStylist(){
	let result = await FetchData.get(`${favoriteAPI.all}func=stylist&account=${account}`);
	const stylistIdJson = await result.json();

	const stylistCards = document.getElementById('stylist-cards');
    let newCard = "";
	for(let stylistId of stylistIdJson.id){
		result = await FetchData.get(`${favoriteAPI.getSearch}func=stylist&id=${stylistId}`);
		const stylistDetail = await result.json();
		newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${stylistId}"> \
            <div class="card"> \
                <a href="javascript:void(0)"><img class="favorite" src="img/favorite.png" id="favorte-${stylistId}" onclick="updateFavorite('stylist', this);" alt="favorite"></a>  \
                <a href="stylist-detail.html?id=${stylistId}"> \
                <img class="card-img-top lozad" data-src="${stylistDetail.picture}" alt="${stylistDetail.name} photo"> \
                <div class="card-body"> \
        	<p class="card-text">${stylistDetail.name}</p> \
                    <p class="card-text">${stylistDetail.salon}</p> \
                    <p class="card-text">${stylistDetail.address}</p> \
                    </div></a></div>
        </div>`;
	}
	stylistCards.innerHTML = newCard; 
    const observer = lozad(); // lazy load
	observer.observe();
}

async function getStylistWork(){
	let result = await FetchData.get(`${favoriteAPI.all}func=stylist_works&account=${account}`);
	const stylistWorkIdJson = await result.json();

	const stylistWorkCards = document.getElementById('stylist-work-cards');
    let newCard = "";
    let stylistWorkJson = [];
    for(stylistWorkId of stylistWorkIdJson.id){
    	result = await FetchData.get(`${favoriteAPI.getSearch}func=stylist_works&id=${stylistWorkId}`);
		const stylistWorkDetail = await result.json();
		stylistWorkJson.push(stylistWorkDetail);
    }
    
	for(let i = 0; i < stylistWorkJson.length; i++){
		let work = stylistWorkJson[i];
        let prevWork = stylistWorkJson[stylistWorkJson.length-1];
        let nextWork = stylistWorkJson[0];
        if(i != 0) prevWork = stylistWorkJson[i-1];
        if(i != stylistWorkJson.length-1) nextWork = stylistWorkJson[i+1];
    	let description = work.description.replace(/\n/g, '<br />');

    	const workHashtags = JSON.parse(work.hashtag);
    	let tmpHashtags = "";
    	for(let i = 0; i < workHashtags.length; i++){
    		tmpHashtags += `<span class="badge badge-primary">${workHashtags[i]}</span>`;
    	}
        newCard += `<div class="cssbox" id="work-${work.id}">
        <a href="javascript:void(0)"><img class="favorite" src="img/favorite.png" id="favorte-${work.id}" onclick="updateFavorite(this);" alt="favorite"></a> \
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
	stylistWorkCards.innerHTML = newCard;
}

async function getProduct(){
	let result = await FetchData.get(`${favoriteAPI.all}func=product&account=${account}`);
	const productIdJson = await result.json();

	const productCards = document.getElementById('product-cards');
    let newCard = "";
	for(let productId of productIdJson.id){
		result = await FetchData.get(`${favoriteAPI.getSearch}func=product&id=${productId}`);
		const productDetail = await result.json();
		newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${productId}"> \
            <div class="card"> \
                <a href="javascript:void(0)"><img class="favorite" src="img/favorite.png" id="favorte-${productId}" onclick="updateFavorite('product', this);" alt="favorite"></a> \
                <!--<a href="product-detail.html?id=${product.id}">--> \
                <a href="${productDetail.address}" target="_blank"> \
                <img class="card-img-top lozad" data-src="${productDetail.picture}" alt="${productDetail.name} photo"> \
                <div class="card-body"> \
        	<p class="card-text">${productDetail.name}</p> \
                    <p class="card-text">${productDetail.capacity} ml</p> \
                    <p class="card-text">NT$ ${productDetail.price}</p> \
                    </div></a></div>
        </div>`;
	}
	productCards.innerHTML = newCard;
    const observer = lozad(); // lazy load
	observer.observe();
}

async function getCategory(){
	const allCategoryTabpanel = $('div[id^="category"]');
	allCategoryTabpanel.remove();
	$('#picture-wall-category').empty();
	let result = await FetchData.get(`${favoriteAPI.all}&func=album&account=${account}`);
	let categoryJson = await result.json();
	if(categoryJson.length == 0 && account != null){
		const result = await FetchData.post(favoriteAPI.new, {
			func: "album",
			account: account,
			albumName: "未分類"
		});
		result = await FetchData.get(`${favoriteAPI.all}&func=album&account=${account}`);
		categoryJson = await result.json();
	}

	const selection = document.getElementById('category-list');
	selection.length = 0;
	
	let i = 1;
	for(let category of categoryJson){
		$('#picture-wall-category').append(`<li><a href="#category-${category.id}" tabindex="-1" data-toggle="tab">${category.name}</a></li>`);
		let btnDiv = "";
		if(i == 1){
			btnDiv = `<button class="btn btn-primary float-right" data-toggle="modal" data-target="#edit-category-modal"><i class="far fa-edit"></i> 編輯種類</button>  `;               
			i = -1;
		}
		else {
			let option = document.createElement("option");
			option.text = category.name;
			option.value = category.id;
			selection.add(option);
		}

		btnDiv += `<button class="btn btn-primary" data-toggle="modal" data-target="#add-photo-modal"><i class="fas fa-upload"></i> 新增照片</button>
            &nbsp;
            <button class="btn btn-primary delete-btn" onclick="deletePhotoShow(this, '${category.id}');"><i class="far fa-trash-alt"></i> 刪除照片</button>`;
		
		result = await FetchData.get(`${favoriteAPI.all}&func=photo&account=${account}&album=${category.id}`);
		const photoJson = await result.json();
		let tmpPhoto = "";
		photoJson.forEach(photo => {
			tmpPhoto += `<div class="col-lg-4 col-md-6 mb-3"> \
	            <div class="card category-${category.id}"> \
	                <a href="#" id="delete-${photo.id}" style="display: none;" onclick="deletePhotoModal(${photo.id});"><i class="fas fa-trash-alt delete-icon"></i></a>  \
	                <img class="card-img-top" src="${photo.photo}"> \
	                <div class="card-body to-center"> \
	                ${photo.description}
	                    </div></div>
				</div>`;
		});
		let tabPaneDiv = document.createElement('div');
		tabPaneDiv.setAttribute('class', 'tab-pane fade');
		tabPaneDiv.id = `category-${category.id}`;
		tabPaneDiv.setAttribute('role', 'tabpanel');
		tabPaneDiv.innerHTML = `${btnDiv}
            <hr>
            <div class="row align-items-center">
                ${tmpPhoto}
            </div>`;
		document.getElementById('tab-content').appendChild(tabPaneDiv);
		
		const allPhoto = document.getElementsByClassName('square');
		for(i in allPhoto){
			if(i == "length") break;
			if(allPhoto[i].width > allPhoto[i].height) className = "wide-rectangle";
			else if(allPhoto[i].width < allPhoto[i].height) className = "high-rectangle";
			else className = "square";
			
			allPhoto[i].setAttribute('class', className);
		}
	}
	
}

async function updateFavorite(func, e){
	let id = parseInt(e.id.split('-')[1]);
	if(e.src.split('img')[1] == '/favorite_undo.png'){
		const result = await FetchData.post(favoriteAPI.new, {
			func: func,
			account: account,
			id: id
		});
		e.src = "img/favorite.png";
	}
	else {
		const result = await FetchData.post(favoriteAPI.delete, {
			func: func,
			account: account,
			id: id
		});
		e.src = "img/favorite_undo.png";
	}
}

async function newCategory(){
	if (document.forms['add-category-form'].reportValidity()) {
		const result = await FetchData.post(favoriteAPI.new, {
			func: "album",
			account: account,
			albumName: document.getElementById('category-name').value
		});
		if(result.status == 409) {
			alert('已經有這個相簿囉！請重新命名');
			return;
		}
		alert('新增成功！');
		$('#edit-category-modal').modal('hide');
	    getCategory();
	    document.getElementById('category-name').value = "";
	}
	
}

async function deleteCategory(){
	if (document.forms['delete-category-form'].reportValidity()) {
		const result = await FetchData.post(favoriteAPI.delete, {
			func: "album",
			account: account,
			id: parseInt($("#category-list :selected").val())
		});	
	}
	alert('刪除成功！');
	$('#edit-category-modal').modal('hide');
    getCategory();
}

async function newPhoto(){
	const albumLink = document.getElementById('picture-wall-category').getElementsByClassName('active')[0];
	let album = parseInt(albumLink.href.split('-')[1]);
	if(window.pictureBase64 == "" || !window.pictureBase64){
		alert("請新增照片！");
		return;
	}
	const result = await FetchData.post(favoriteAPI.new, {
		func: "photo",
		account: account,
		album: album,
		picture: window.pictureBase64,
		description:  document.getElementById('photo-description').value,
	});
	document.getElementById('show-new-photo').setAttribute('src', 'img/blank.png');
	document.getElementById('photo-description').value = "";
	window.pictureBase64 = "";

	alert('新增成功！');
	$('#add-photo-modal').modal('hide');
    getCategory();
}

//let picture can show instantly
function readURL(input) {
	if (input.files && input.files[0]) {
	  const reader = new FileReader();
	  reader.onload = function loadPicture(e) {
	    document.getElementById('show-new-photo').setAttribute('src', e.target.result);
		  window.pictureBase64 = e.target.result;
	  };
	  reader.readAsDataURL(input.files[0]);
	}
}

function deletePhotoShow(e, categoryId){
	const allPhoto = document.getElementsByClassName(`category-${categoryId}`);
	if(e.innerHTML == `<i class="far fa-trash-alt"></i> 刪除照片`) {
		e.innerHTML = `<i class="far fa-check-circle"></i> 完成`;
		for(let i in allPhoto){
			if(i == "length") break;
			allPhoto[i].childNodes[1].setAttribute('style', 'display: initial;');
		}
	}
	else {
		e.innerHTML = `<i class="far fa-trash-alt"></i> 刪除照片`;
		for(let i in allPhoto){
			if(i == "length") break;
			allPhoto[i].childNodes[1].setAttribute('style', 'display: none;');
		}
	}
}

function deletePhotoModal(id){
	document.getElementById('delete-id').value = id;
	$("#delete-photo-modal").modal('show');
}

async function deletePhoto(){
	id = parseInt(document.getElementById('delete-id').value);
	const result = await FetchData.post(favoriteAPI.delete, {
		func: "photo",
		account: account,
		id: id
	});
	alert('刪除成功！');
	$('#delete-photo-modal').modal('hide');
	getCategory();
}

function delayURL(url, time) {
  setTimeout(() => { window.location.href = `${url}`; }, time);
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
	// to block illegal users
	if(account == null){
		alert("無法瀏覽此頁面！請登入後再查看！");
	    delayURL('./index.html', 200);
	}
	sidebarSetting();
	
    getSalon();
    getStylist();
    getStylistWork();
    getProduct();
    getCategory();
    
    $('#myList a').on('click', function () {
        $(this).tab('show');
    });
    
    document.getElementById('add-category-btn').addEventListener('click', newCategory);
    document.getElementById('delete-category-btn').addEventListener('click', deleteCategory);

	document.getElementById('new-photo').addEventListener('change',  function read() { readURL(this); })
    document.getElementById('add-photo-btn').addEventListener('click', newPhoto);
	
    document.getElementById('delete-photo-btn').addEventListener('click', deletePhoto);
}

window.addEventListener('load', init);