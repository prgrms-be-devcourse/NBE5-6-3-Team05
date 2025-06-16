const API_URL = 'https://dapi.kakao.com/v3/search/book?';
const API_KEY = 'KakaoAK b97ae609182be460fa5dcd9f800bf917';

const api = async params => {
  const queryString = new URLSearchParams(params).toString();
  const response = await fetch(API_URL + queryString, {
    headers: {Authorization: API_KEY}
  });

  return response.json();
}

export default api;

