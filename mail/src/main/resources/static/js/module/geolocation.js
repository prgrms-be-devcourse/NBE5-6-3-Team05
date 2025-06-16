const options = {
  enableHighAccuracy: true,
  timeout: 5000,
  maximumAge: 0,
};

const api = () => {
  return new Promise((resolve, reject) => {
    navigator.geolocation.getCurrentPosition(pos => {
      const crd = pos.coords;
      resolve({latitude: crd.latitude, longitude: crd.longitude});
    }, err => {
      reject(err);
    }, options);
  });
};

export default api;

