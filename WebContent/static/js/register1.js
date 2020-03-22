
var url = 'http://localhost:8080/SearcHair/register';


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

