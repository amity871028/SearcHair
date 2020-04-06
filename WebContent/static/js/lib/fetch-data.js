// eslint-disable-next-line no-unused-vars
class FetchData {
  static get(url) {
    return fetch(url, {
      cache: 'no-cache',
      credentials: 'same-origin',
      headers: {
        'user-agent': 'Mozilla/4.0 MDN Example',
        'content-type': 'application/json',
      },
      method: 'GET',
      mode: 'cors',
      redirect: 'follow',
      referrer: 'no-referrer',
    });
  }

  static post(url, data) {
    return fetch(url, {
      body: JSON.stringify(data),
      cache: 'no-cache',
      credentials: 'same-origin',
      headers: {
        'user-agent': 'Mozilla/4.0 MDN Example',
        'content-type': 'application/json',
      },
      method: 'POST',
      mode: 'cors',
      redirect: 'follow',
      referrer: 'no-referrer',
    });
  }

  static postForm(url, data) {
    return fetch(url, {
      body: data,
      cache: 'no-cache',
      credentials: 'same-origin',
      method: 'POST',
      mode: 'cors',
      redirect: 'follow',
      referrer: 'no-referrer',
    });
  }
}
