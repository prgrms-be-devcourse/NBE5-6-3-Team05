// /// 모달창
// // 모달 창 좋아요 버튼 연동 작성하기

import { setupLikeToggle } from '/js/like-toggle.js';

document.addEventListener("DOMContentLoaded", function () {
  const buttons = document.querySelectorAll(".details-btn");

  buttons.forEach(btn => {
    btn.addEventListener("click", () => {
      const type = btn.dataset.type; // book, movie, song 등
      const id = btn.dataset.id;

      fetch(`/api/details/${type}/${id}`)
      .then(res => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        return res.json();
      })
      .then(data => {
        openModal(type, data);
      })
      .catch(error => {
        console.error('상세 정보를 불러오는 중 오류 발생:', error);
        alert('상세 정보를 불러오는 데 실패했습니다.');
      });
    });
  });
});

async function openModal(type, data) {
  const modal = document.getElementById("detailModalResult");
  const meta = document.getElementById("custom-modal-meta");
  const likeBtn = document.getElementById("modal-like-btn");
  const searchLink = document.getElementById("modal-search-link");

  let details = "";
  let genreStr = "N/A";

  if (type === "books") {
    document.getElementById("custom-modal-title").textContent = data.book.name;
    document.getElementById("modal-img").src = data.book.bookImg;
    document.getElementById("modal-img").alt = data.book.name;
    searchLink.href = data.book.externalLink;

    details += `<p><strong>작가:</strong> ${data.book.author || 'N/A'}</p>`;
    details += `<p><strong>장르:</strong> ${data.bookGenre?.name || 'N/A'}</p>`;
    details += `<p><strong>요약:</strong> ${buildExpandable(data.book.summary || '요약 정보가 없습니다.', "summary")}</p>`;
    details += `<p><strong>설명:</strong> ${buildExpandable(data.book.description || '설명 정보가 없습니다.', "description")}</p>`;
    details += `<p><strong>출판사:</strong> ${data.book.publisher || 'N/A'}</p>`;
    details += `<p><strong>출판일:</strong> ${data.book.publishedAt || 'N/A'}</p>`;
  } else if (type === "movies") {
    if (Array.isArray(data.movieGenre) && data.movie.genre.length > 0) {
      genreStr = data.movieGenre.map(g => g.name).join(", ");
    }
    document.getElementById("custom-modal-title").textContent = data.movie.name;
    document.getElementById("modal-img").src = data.movie.movieImg;
    document.getElementById("modal-img").alt = data.movie.name;
    searchLink.href = data.movie.externalLink;

    details += `<p><strong>장르:</strong>  ${genreStr}</p>`;
    details += `<p><strong>설명:</strong> ${buildExpandable(data.movie.description || '설명 정보가 없습니다.', "description")}</p>`;
    details += `<p><strong>개봉일:</strong> ${data.movie.releaseDate || 'N/A'}</p>`;
  } else if (type === "musics") {
    document.getElementById("custom-modal-title").textContent = data.music.name;
    document.getElementById("modal-img").src = data.music.songImg;
    document.getElementById("modal-img").alt = data.music.name;
    searchLink.href = data.music.externalLink;

    details += `<p><strong>아티스트:</strong> ${data.music.artist || 'N/A'}</p>`;
    details += `<p><strong>장르:</strong> ${data.musicGenre?.name || 'N/A'}</p>`;
    details += `<p><strong>발매일:</strong> ${data.music.releasedAt || 'N/A'}</p>`;
    details += `<p><strong>요약:</strong> ${buildExpandable(data.music.summary || '요약 정보가 없습니다.', "summary")}</p>`;
    details += `<p><strong>가사:</strong> ${buildExpandable(data.music.lyrics || '가사 정보가 없습니다.', "lyrics")}</p>`;
  }

  meta.innerHTML = details;


  // 좋아요 버튼 설정
  likeBtn.dataset.type = type;
  likeBtn.dataset.id = data.id || data.book?.id || data.movie?.id || data.music?.id; // 안전하게 id 할당
  likeBtn.textContent = data.status ? '★' : '☆';

  // 좋아요 버튼 이벤트 세팅 (중복 이벤트 방지 위해 기존 리스너 제거 후 재설정)
  likeBtn.replaceWith(likeBtn.cloneNode(true));
  const newLikeBtn = document.getElementById("modal-like-btn");
  setupLikeToggle(newLikeBtn);

  modal.classList.remove("hidden");
}
// 모달 닫기 함수 - 전역에 있어야 함
function closeModal() {
  const modal = document.getElementById('detailModalResult');
  modal.classList.add('hidden');
}

// DOM이 완전히 로드되면 닫기 버튼에 이벤트 붙임
document.addEventListener("DOMContentLoaded", () => {
  const closeBtn = document.querySelector(".custom-close-btn");
  if (closeBtn) {
    closeBtn.addEventListener("click", closeModal);
  }
});

// 줄임/펼침 텍스트 함수는 그대로 유지
function buildExpandable(content, idPrefix) {
  if (!content || content.length < 100) {
    return content;
  }

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