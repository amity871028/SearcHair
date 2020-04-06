
<<<<<<< HEAD
var url = 'http://127.0.0.1:8080/SearcHair/register';
=======
var url = 'register';
>>>>>>> branch 'master' of https://github.com/amity871028/SearcHair.git


function register() {
	
	const account = document.getElementById('register-account').value;
	const name = document.getElementById('register-name').value;
	const password = document.getElementById('register-password').value;
	const data = {account: `${account}`, password: `${password}`, name: `${name}`};

	fetch(url, {
		  method: 'POST', // or 'PUT'
		  body: JSON.stringify(data), // data can be `string` or {object}!
		  headers: new Headers({
		    'Content-Type': 'application/json'
		  })
		}).then((response) => {
		    return response.status; 
		  }).then((jsonData) => {
		    console.log(jsonData);
		  }).catch((err) => {
		    console.log('error:', err);
		})
}


function init() {
	  document.getElementById('register').addEventListener('click', register);
}

window.addEventListener('load', init);

