
var url = 'http://localhost:8080/SearcHair/login';


function login() {
	const account = document.getElementById('login-user-name').value;
	const password = document.getElementById('login-password').value;
	const data = {account: `${account}`, password: `${password}`};
	
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
	  document.getElementById('login').addEventListener('click', login);
}

window.addEventListener('load', init);

