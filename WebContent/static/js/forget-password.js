
var url = 'http://localhost:8080/SearcHair/ForgetPassword';


function forgetPassword() {
	const account = document.getElementById('email').value;
	const name = document.getElementById('user-name').value;
	const data = {account: `${account}`, name: `${name}`};
	
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
	  document.getElementById('forget-password-btn').addEventListener('click', forgetPassword);
}

window.addEventListener('load', init);

/**
 * 
 */