const productDetailAPI = 'api-search-detail?func=product';
const favoriteAPI = {
		all: 'api-favorite?func=product',
		new: 'api-favorite-new',
		delete: 'api-favorite-delete'
};
const account = localStorage.getItem('account');

async function getProductDetail(){
	let url = location.href;
	let idPosition = url.match("id=").index + 3;
	let id = parseInt(url.substring(idPosition));
	let result = await FetchData.get(`${productDetailAPI}&id=${id}`);
	const product = await result.json();


    let favoriteJson = {id: []};
    if(account != null){
		result = await FetchData.get(`${favoriteAPI.all}&account=${account}`);
		favoriteJson = await result.json();
    }
    console.log(favoriteJson);
	let favoriteImg = "img/favorite_undo.png";
	if(favoriteJson.id.find(element => element == id) != undefined) {
		favoriteImg = "img/favorite.png";
	}
	
    // update card
    const productCards = document.getElementById('product-card');
	let newCard="";
	newCard += `<a href="javascript:void(0)"><img class="favorite" src="${favoriteImg}" id="favorite-${id}" alt="favorite" onclick="updateFavorite(this)"></a>  \
            
            <img class="card-img-top" src="${product.picture}" alt="${product.name} photo"> \
            <div class="card-body"> \
        	<p class="card-text">${product.name}</p> \
                    <p class="card-text">${product.capacity}ml</p> \
                    <p class="card-text">$ ${product.price}</p> \
                    </div></a></div>
		</div>`;
    productCards.innerHTML += newCard;
    

    // update information
    document.getElementById('product-name').innerHTML = product.name;
    document.getElementById('product-capacity').innerHTML = product.capacity;
    document.getElementById('product-price').innerHTML = product.price;
    
    let description = product.description;
    description = description.replace(/\n/g, '<br />');
    document.getElementById('product-description').innerHTML = description;
    
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

function init(){
	sidebarSetting();
    getProductDetail();
}

window.addEventListener('load', init);