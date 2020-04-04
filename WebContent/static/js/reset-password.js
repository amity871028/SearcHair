
var url = 'http://localhost:8080/SearcHair/ResetPwdServlet';


function resetPassword() {
	const newPwd = document.getElementById('new-password').value;
	const data = {password: `${newPwd}`};
	
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
	  document.getElementById('reset-password-btn').addEventListener('click', resetPassword);
}

window.addEventListener('load', init);

/**
 * 
 */