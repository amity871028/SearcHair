const typeRadio= {type0: "洗髮乳", type1: "潤髮", type2: "護髮油", type3: "染髮劑"};
const productAPI = {
		search: 'api-search?func=product',
		post: 'api-search-product'
}
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
    const result = await FetchData.get(`${productAPI.search}&page=${page}`);
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
	    const result = await FetchData.get(`${productAPI.search}&keyword=${keyword}&page=${page}`);
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
	if(checkedtype.value == "all") result = await FetchData.get(`${productAPI.search}&page=${page}`);
	else result = await FetchData.get(`${productAPI.search}&type=${typeRadio[checkedtype.value]}&page=${page}`);
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
	if(checkedFeture == 0) result = await FetchData.get(`${productAPI.search}&type=洗髮乳&page=${page}`);
	else result = await FetchData.get(`${productAPI.search}&type=洗髮乳&feature=${checkedFeture-1}&page=${page}`);
	const Json = await result.json();
	drawCard(Json, page);
	
}

async function drawCard(Json, page){
	const productCards = document.getElementById('product-cards');
    let newCard = "";
    Json.forEach( product => {
    	newCard += `<div class="col-lg-4 col-md-6 mb-3" id="${product.id}"> \
            <div class="card"> \
                <a href="javascript:void(0)" onclick="updateModal(${product.id}, '${product.name}', '${product.type}', '${product.feature}', ${product.capacity}, ${product.price}, '${product.address}', '${product.picture}');"> \
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

function updateModal(id, name, type, feature, capacity, price, address, picture){
	
	document.getElementById('product-name').value = name;
	document.getElementById('product-type').value = type;
	document.getElementById('product-feature').value = feature;
	document.getElementById('product-capacity').value = capacity;
	document.getElementById('product-price').value = price;
	document.getElementById('product-address').value = address;
	document.getElementById('show-new-picture').setAttribute('src', picture);

	document.getElementById('add-product-btn').setAttribute('style', 'display: none;');
	document.getElementById('update-product-btn').setAttribute('style', 'display: initial;');
	document.getElementById('delete-product-btn').setAttribute('style', 'display: initial;');
	
	document.getElementById('product-id').innerHTML = id;
	$('#add-product-modal').modal('show');
}

//let picture can show instantly
function readURL(input) {
	if (input.files && input.files[0]) {
	  const reader = new FileReader();
	  reader.onload = function loadPicture(e) {
	    document.getElementById('show-new-picture').setAttribute('src', e.target.result);
		  window.pictureBase64 = e.target.result;
	  };
	  reader.readAsDataURL(input.files[0]);
	}
}


function translateBase64ImgToFile(base64,filename,contentType){ // base64 to file
    var arr = base64.split(',');
    var bstr = atob(arr[1]);
    var leng = bstr.length;
    var u8arr = new Uint8Array(leng);
    while(leng--){
       u8arr[leng] =  bstr.charCodeAt(leng);
    }
    return new File([u8arr],filename,{type:contentType});
}

async function addProduct(){
	if (document.forms['product-form'].reportValidity()) {
		const idSpan = document.getElementById('product-id');
		const pictureRadio = document.getElementsByName('picture');
		let picture;
		for(let i in pictureRadio){
			if(pictureRadio[i].checked == true) {
				if(pictureRadio[i].value == 'upload'){
					if(window.pictureBase64 == "" || !window.pictureBase64){
						if(idSpan.innerHTML != -1){
							window.pictureBase64 = document.getElementById('show-new-picture').src;
							picture = window.pictureBase64;
						}
						else {
							alert("請新增照片！");
							return;
						}
					}
					else {
						var file = translateBase64ImgToFile(window.pictureBase64,"testImg.png","image/png")
						var CLOUDINARY_URL = 'https://api.cloudinary.com/v1_1/dszvkufl8/upload';
						var CLOUDINARY_UPLOAD_PRESET = 'z62aocfi';
						var formData = new FormData();
					    formData.append('file', file);
					    formData.append('upload_preset', CLOUDINARY_UPLOAD_PRESET);
					    let res = await axios({
					        url: CLOUDINARY_URL,
					        method: 'POST',
					        headers: {
					            'Content-Type': 'application/x-www-form-urlencoded'
					        },
					        data: formData
					    });
				    	picture = res.data.secure_url; //imgur's url
					}
				}
				else {
					const pictureAddress = document.getElementById('product-picture-address').value;
					if(!pictureAddress) {
						alert("請輸入圖片網址！");
						return;
					}
					picture = pictureAddress;
				}
			}
		}
		if(idSpan.innerHTML == -1){
			const result = await FetchData.post(productAPI.post, {
				action: "new",
				name: document.getElementById('product-name').value,
				type: document.getElementById('product-type').value,
				feature: document.getElementById('product-feature').value,
				capacity: document.getElementById('product-capacity').value,
				price: document.getElementById('product-price').value,
				picture: picture,
				address: document.getElementById('product-address').value,
			});
			if(result.status == 200) alert('新增成功！');
		}
		else {
			const result = await FetchData.post(productAPI.post, {
				action: "update",
				id: idSpan.innerHTML,
				name: document.getElementById('product-name').value,
				type: document.getElementById('product-type').value,
				feature: document.getElementById('product-feature').value,
				capacity: document.getElementById('product-capacity').value,
				price: document.getElementById('product-price').value,
				picture: picture,
				address: document.getElementById('product-address').value,
			});
			idSpan.innerHTML = -1;
			if(result.status == 200) alert('修改成功！');
		}
		window.pictureBase64 = "";
		$("#add-product-modal").modal('hide');
		getProduct();
	}
}

async function deleteProduct(){
	const idSpan = document.getElementById('product-id');
	const result = await FetchData.post(productAPI.post, {
		action: 'delete',
		id : parseInt(idSpan.innerHTML),
	});
	idSpan.innerHTML = -1;
	alert('刪除成功！');
	$("#add-product-modal").modal('hide');
	getProduct();
	
}

function changePictureRadio(e){
	if(e.value == "upload") {
		document.getElementById('upload-display').setAttribute('style', 'display: initial;');
		document.getElementById('show-new-picture').setAttribute('style', 'display: initial;');
		document.getElementById('product-picture-address').setAttribute('style', 'display: none !important;');
	}
	else {
		document.getElementById('upload-display').setAttribute('style', 'display: none;');
		document.getElementById('show-new-picture').setAttribute('style', 'display: none;');
		document.getElementById('product-picture-address').setAttribute('style', 'display: initial;');
	}
}

function clearModal(){
	document.getElementById('product-name').value = "";
	document.getElementById('product-type').value = "";
	document.getElementById('product-feature').value = "";
	document.getElementById('product-capacity').value = "";
	document.getElementById('product-price').value = "";
	document.getElementById('product-address').value = "";
	document.getElementById('show-new-picture').src = "img/blank.png";
	document.getElementById('product-picture-address').value = "";

	document.getElementsByName('picture')[0].checked = true;
	document.getElementById('upload-display').setAttribute('style', 'display: initial;');
	document.getElementById('show-new-picture').setAttribute('style', 'display: initial;');
	document.getElementById('product-picture-address').setAttribute('style', 'display: none !important;');

	document.getElementById('add-product-btn').setAttribute('style', 'display: initial;');
	document.getElementById('update-product-btn').setAttribute('style', 'display: none;');
	document.getElementById('delete-product-btn').setAttribute('style', 'display: none;');
	
	document.getElementById('product-id').innerHTML = -1;
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
    
    document.getElementsByName('picture').forEach(radio => {
    	radio.addEventListener('click', function change(){ changePictureRadio(this); });
    })
    
	document.getElementById('add-product-btn').addEventListener('click', addProduct);
	document.getElementById('update-product-btn').addEventListener('click', addProduct);
	document.getElementById('delete-product-btn').addEventListener('click', deleteProduct);
	document.getElementById('product-picture').addEventListener('change',  function read() { readURL(this); })
}

window.addEventListener('load', init);