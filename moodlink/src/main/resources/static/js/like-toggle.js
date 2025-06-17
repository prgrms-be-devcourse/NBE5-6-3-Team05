export function setupLikeToggle() {
  console.log('setupLikeToggle 실행됨');

  document.querySelectorAll('.toggle-star-btn').forEach(btn => {

    const isOn = btn.dataset.on === 'true';
    btn.textContent = isOn ? '★' : '☆';
    btn.classList.toggle('on', isOn);

    btn.addEventListener('click', () => {
      const type = btn.dataset.type;
      const id = btn.dataset.id;

      fetch(`/api/users/like/${type}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id })
      })
      .then(res => res.json())
      .then(data => {
        const isOn = data.status;

        btn.textContent = isOn ? '★' : '☆';
        btn.classList.toggle('on', isOn);

        // 같은 type/id 가진 버튼들 동기화
        const selector = `.toggle-star-btn[data-type='${type}'][data-id='${id}']`;
        document.querySelectorAll(selector).forEach(otherBtn => {
          otherBtn.textContent = isOn ? '★' : '☆';
          otherBtn.classList.toggle('on', isOn);
        });
      })
      .catch(err => console.error('좋아요 토글 중 에러:', err));
    });
  });
}