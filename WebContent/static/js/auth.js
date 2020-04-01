/* global FetchData */
const API = {
	login : 'login',
	register : 'register',
};
function validatePassword() {
	const confirmPassword = document
			.getElementById('register-confirm-password').value;
	const password = document.getElementById('register-password').value;
	if (confirmPassword !== password) {
		document.getElementById('register-confirm-password').setCustomValidity(
				'無法和密碼匹配');
	} else {
		document.getElementById('register-confirm-password').setCustomValidity(
				'');
	}
}
async function login() {
	// validate field and show hint
	if (document.forms['login-form'].reportValidity()) {
		// start post
		const result = await
		FetchData.post(API.login, {
			userName : document.getElementById('login-user-name').value,
			password : document.getElementById('login-password').value,
		});
		if (result.status === 401) {
			// show wrong msg
			document.getElementById('login-wrong').innerText = '帳號或密碼錯誤';
		} else {
			// refresh page
			window.location.reload();
		}
	}
}

async function register() {
	// clear register-wrong content
	document.getElementById('register-wrong').innerText = '　';
	// validate filed and show hint
	if (document.forms['register-form'].reportValidity()) {
		// get input value
		const result = await
		FetchData.post(API.register, {
			account : document.getElementById('register-account').value,
			name : document.getElementById('register-name').value,
			password : document.getElementById('register-password').value,
		});
		if (result.status === 409) {
			document.getElementById('register-wrong').innerText = '此帳號已經有人使用';
		} else if (result.status === 200) {
			// refresh page
			window.location.reload();
		}
	}
}
function authInit() {
	// add event listener
	document.getElementById('login').addEventListener('click', login);
	document.getElementById('register').addEventListener('click', register);
	// validate password when password or confirm password change
	document.getElementById('register-password').addEventListener('keyup',
			validatePassword);
	document.getElementById('register-confirm-password').addEventListener(
			'keyup', validatePassword);
}

window.addEventListener('load', authInit);
