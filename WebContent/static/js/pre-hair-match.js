window.addEventListener('load', function(){
	const hairstyle = document.getElementById('hairstyle').innerHTML;
	if(hairstyle != ""){
		localStorage.setItem('hairstyle', hairstyle);
	}
	window.location.href = "./hair-match.html";
});
