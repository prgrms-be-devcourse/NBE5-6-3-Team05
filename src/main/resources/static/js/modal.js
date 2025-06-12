
  /// 모달창
  // 모달 창 좋아요 버튼 연동 작성하기
  document.addEventListener("DOMContentLoaded", function () {
  const buttons = document.querySelectorAll(".details-btn");

  buttons.forEach(btn => {
  btn.addEventListener("click", () => {
  const type = btn.dataset.type; // book, movie, song
  const id = btn.dataset.id;

  fetch(`/api/details/${type}/modal/${id}`)
  .then(res => res.json())
  .then(data => {
  openModal(type, data);
});
});
});
});

  function openModal(type, data) {
  const modal = document.getElementById("detailModalResult");
  const body = document.getElementById("modal-body");
  const meta = document.getElementById("custom-modal-meta");
  const likeBtn = document.getElementById("modal-like-btn");
  const searchLink = document.getElementById("modal-search-link");

// 제목 + 이미지는 항상 고정 위치
  document.getElementById("custom-modal-title").textContent = data.name;
  document.getElementById("modal-img").src = data[`${type}Img`];
  document.getElementById("modal-img").alt = data.name;

// 링크 설정
  searchLink.href = data.externalLink;

// 타입별 부가 정보 + description
  let details = "";

  if (type === "book") {
  details += `<p><strong>작가:</strong> ${data.author}</p>`;
  details += `<p><strong>장르:</strong> ${data.genre}</p>`;
  details += `<p><strong>요약:</strong> ${buildExpandable(data.summary, "summary")}</p>`;
  details += `<p><strong>설명:</strong> ${buildExpandable(data.description,
  "description")}</p>`;
  details += `<p><strong>출판사:</strong> ${data.publisher}</p>`;
  details += `<p><strong>출판일:</strong> ${data.publishedAt}</p>`;

} else if (type === "movie") {
  details += `<p><strong>장르:</strong> ${data.genre}</p>`;
  details += `<p><strong>설명:</strong> ${buildExpandable(data.description,
  "description")}</p>`;     //간략히
  details += `<p><strong>개봉일:</strong> ${data.releaseDate}</p>`;
} else if (type === "song") {
  details += `<p><strong>아티스트:</strong> ${data.artist}</p>`;
  details += `<p><strong>장르:</strong> ${data.genre}</p>`;
  details += `<p><strong>발매일:</strong> ${data.releasedAt}</p>`;
  details += `<p><strong>요약:</strong> ${buildExpandable(data.summary, "summary")}</p>`;
  details += `<p><strong>가사:</strong> ${buildExpandable(data.lyrics, "lyrics")}</p>`;
}

  meta.innerHTML = details;

// 별 버튼 설정
  likeBtn.dataset.type = type;
  likeBtn.dataset.id = data.id;
  likeBtn.textContent = '★';
  likeBtn.classList.remove("on");
  if (data.status) {
  likeBtn.classList.add("on");
}

  modal.classList.remove("hidden");
}

  function closeModal() {
  document.getElementById("detailModalResult").classList.add("hidden");
}

  //모달 창의 텍스트 줄여서 보기
  function buildExpandable(content, idPrefix) {
  const shortId = `${idPrefix}-short`;
  const fullId = `${idPrefix}-full`;
  const btnId = `${idPrefix}-btn`;

  return `
<span id="${shortId}" style="display:block; overflow: hidden; text-overflow: ellipsis; max-height: 4.5em;">
      ${content}
    </span>
<span id="${fullId}" style="display:none;">
      ${content}
    </span>
<button onclick="toggleText('${shortId}', '${fullId}', '${btnId}')" id="${btnId}" class="text-button">전체 보기</button>
`;
}

  function toggleText(shortId, fullId, btnId) {
  const shortEl = document.getElementById(shortId);
  const fullEl = document.getElementById(fullId);
  const btnEl = document.getElementById(btnId);

  const isShortVisible = shortEl.style.display !== "none";
  shortEl.style.display = isShortVisible ? "none" : "block";
  fullEl.style.display = isShortVisible ? "block" : "none";
  btnEl.textContent = isShortVisible ? "간략히 보기" : "전체 보기";
}
