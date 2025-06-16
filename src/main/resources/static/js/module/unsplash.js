import dayjs from 'https://cdn.jsdelivr.net/npm/dayjs@1.11.13/+esm';

const API_KEY = '2BUqvrqRc5L-QN64s361sqN8x1NA5zzc17_T5CJEpHU';
const URL = 'https://api.unsplash.com/photos/random?';
const params = {
  client_id: API_KEY,
  orientation: 'landscape',
  query: 'landscape'
}

const queryString = new URLSearchParams(params);
const api = async () => {
  const response = await fetch(URL + queryString, {
    header: {
      'Accept-Version': 'v1'
    }
  });

  return response.json();
}

async function createToken() {
  const {
    urls: {full: bg},
    location: {name: location}
  } = await api();

  const newToken = {
    bg: bg,
    location: location,
    expire: dayjs().add(1, 'day')
  };

  localStorage.setItem('unsplash-token', JSON.stringify(newToken));
  return newToken;
}

const getToken = async () => {
  const storedToken = JSON.parse(localStorage.getItem('unsplash-token'));

  if (storedToken && dayjs().isBefore(storedToken.expire)) {
    return storedToken;
  }

  return createToken();
}
export {api};
export default getToken;