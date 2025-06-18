// /// 모달창
// // 모달 창 좋아요 버튼 연동 작성하기

// import { setupLikeToggle } from '/js/like-toggle.js';

document.addEventListener("DOMContentLoaded", () => {
  // 상세보기 버튼 클릭 시 모달 열기
  const buttons = document.querySelectorAll(".details-btn");

  buttons.forEach(btn => {
    btn.addEventListener("click", () => {
      const type = btn.dataset.type;
      const id = btn.dataset.id;

      fetch(`/api/details/${type}/${id}`)
      .then(res => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        return res.json();
      })
      .then(data => openModal(type, data))
      .catch(error => {
        console.error('상세 정보를 불러오는 중 오류 발생:', error);
        alert('상세 정보를 불러오는 데 실패했습니다.');
      });
    });
  });
});

// 모달 열기
function openModal(type, data) {
  const modal = document.getElementById("detailModalResult");
  const meta = document.getElementById("custom-modal-meta");
  const likeBtn = document.getElementById("modal-like-btn");
  const searchLink = document.getElementById("modal-search-link");

  let details = "";
  let genreStr = "N/A";

  const modalButtons = document.getElementById('modalButtons');
  modalButtons.innerHTML = ''; // 기존 버튼 초기화

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

    const buyBookBtn = document.createElement('button');
    buyBookBtn.textContent = '책 구매하러 가기 ➜';
    buyBookBtn.className = 'buy-book-btn';
    buyBookBtn.addEventListener('click', () => {
      const query = encodeURIComponent(data.book.name);
      window.open(`https://www.yes24.com/product/search?domain=ALL&query=${query}`, '_blank');
    });
    modalButtons.appendChild(buyBookBtn);

  } else if (type === "movies") {
    if (Array.isArray(data.movieGenre) && data.movieGenre.length > 0) {
      genreStr = data.movieGenre.map(g => g.name).join(", ");
    }

    document.getElementById("custom-modal-title").textContent = data.movie.name;
    document.getElementById("modal-img").src = data.movie.movieImg;
    document.getElementById("modal-img").alt = data.movie.name;
    searchLink.href = data.movie.externalLink;

    details += `<p><strong>장르:</strong>  ${genreStr}</p>`;
    details += `<p><strong>설명:</strong> ${buildExpandable(data.movie.description || '설명 정보가 없습니다.', "description")}</p>`;
    details += `<p><strong>개봉일:</strong> ${data.movie.releaseDate || 'N/A'}</p>`;

    const trailerBtn = document.createElement('button');
    trailerBtn.textContent = '예고편 보기';
    trailerBtn.className = 'trailer-btn'
    trailerBtn.addEventListener('click', () => showMovieTrailer(data.movie.name));

    const buyBtn = document.createElement('button');
    buyBtn.textContent = '영화 보러가기';
    buyBtn.className = 'watch-movie-btn'
    buyBtn.addEventListener('click', () => youtubeMovie(data.movie.name));

    modalButtons.appendChild(trailerBtn);
    modalButtons.appendChild(buyBtn);

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

    const trailerBtn = document.createElement('button');
    trailerBtn.textContent = '뮤직비디오 보기';
    trailerBtn.className = 'music-video-btn'
    trailerBtn.addEventListener('click', () => showMusicTrailer(data.music.name));
    modalButtons.appendChild(trailerBtn);
  }

  meta.innerHTML = details;

  // 좋아요 버튼 설정
  likeBtn.dataset.type = type;
  likeBtn.dataset.id = data.id || data.book?.id || data.movie?.id || data.music?.id;
  let isLiked = false;
  if (type === "books") {
    isLiked = data.status || data.book?.status || false;
  } else if (type === "movies") {
    isLiked = data.status || data.movie?.status || false;
  } else if (type === "musics") {
    isLiked = data.status || data.music?.status || false;
  }

  likeBtn.textContent = isLiked ? '★' : '☆';
  likeBtn.classList.toggle('on', isLiked);

  // 좋아요 버튼 재설정
  const newLikeBtn = likeBtn.cloneNode(true);
  likeBtn.parentNode.replaceChild(newLikeBtn, likeBtn);

// 모달 좋아요 버튼에 직접 이벤트 추가
  newLikeBtn.addEventListener('click', function() {
    const type = this.dataset.type;
    const id = this.dataset.id;

    fetch(`/api/users/like/${type}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ id })
    })
    .then(res => res.json())
    .then(data => {
      const isOn = data.status;

      // 모달 버튼 업데이트
      this.textContent = isOn ? '★' : '☆';
      this.classList.toggle('on', isOn);

      // 같은 type/id 가진 다른 버튼들도 동기화
      const selector = `.toggle-star-btn[data-type='${type}'][data-id='${id}']`;
      document.querySelectorAll(selector).forEach(otherBtn => {
        if (otherBtn !== this) {
          otherBtn.textContent = isOn ? '★' : '☆';
          otherBtn.classList.toggle('on', isOn);
        }
      });
    })
    .catch(err => {
      console.error('좋아요 토글 중 에러:', err);
      alert('좋아요 처리 중 오류가 발생했습니다.');
    });
  });
  // 닫기 버튼 이벤트 연결 (모달 열릴 때마다 새로 등록)
  const closeBtn = document.querySelector(".custom-close-btn");
  if (closeBtn) {
    closeBtn.onclick = closeModal;
  }

  modal.classList.remove("hidden");
}

// 모달 닫기
function closeModal() {
  const modal = document.getElementById('detailModalResult');
  modal.classList.add('hidden');
}

// 줄임/펼침 텍스트 생성
function buildExpandable(content, idPrefix) {
  if (!content || content.length < 100) return content;

  const shortId = `${idPrefix}-short`;
  const fullId = `${idPrefix}-full`;
  const btnId = `${idPrefix}-btn`;

  return `
    <span id="${shortId}" style="display:block; overflow: hidden; text-overflow: ellipsis; max-height: 4.5em;">
      ${content}
    </span>
    <span id="${fullId}" style="display:none;">${content}</span>
    <button onclick="toggleText('${shortId}', '${fullId}', '${btnId}')" id="${btnId}" class="text-button">전체 보기</button>
  `;
}

// 줄임/펼침 전환
function toggleText(shortId, fullId, btnId) {
  const shortEl = document.getElementById(shortId);
  const fullEl = document.getElementById(fullId);
  const btnEl = document.getElementById(btnId);

  const isShortVisible = shortEl.style.display !== "none";
  shortEl.style.display = isShortVisible ? "none" : "block";
  fullEl.style.display = isShortVisible ? "block" : "none";
  btnEl.textContent = isShortVisible ? "간략히 보기" : "전체 보기";
}


// 붙인거

/* 카테고리 토글 */
function showCategory(id) {
  document.querySelectorAll('.category').forEach(c => c.style.display = 'none');
  document.getElementById(id).style.display = 'block';
}

document.addEventListener('DOMContentLoaded', () => showCategory('movie'));

function showDetailModalMovie(title, releaseDate, summary, thumbnail) {
  /* ===== 제목 ===== */
  document.getElementById('movieTitle').textContent = title;
  /* ===== 연도 ===== */
  document.getElementById('movieYear').textContent = releaseDate.split('-')[0] || releaseDate;
  /* ===== 영화 내용 ===== */
  document.getElementById('movieSummary').innerHTML = summary;
  /* ===== 썸네일 ===== */
  const img = document.getElementById('movieBg');
  img.src = thumbnail;

  /* ===== 모달 표시 ===== */
  document.getElementById('movieDetailModal').classList.add('active');

}


function youtubeMovie(title) {
  fetch(`/api/trailer/movie?title=${encodeURIComponent(title)}`)
  .then(res => res.json())
  .then(data => {
    if (data.movieVideoId) {
      const videoUrl = `https://www.youtube.com/watch?v=${data.movieVideoId}`;
      window.open(videoUrl, '_blank');
    } else {
      alert('영화를 찾을 수 없습니다.');
    }
  })
  .catch(() => alert('영화를 불러오는 중 오류가 발생했습니다.'));
}

function showDetailModalMusic(title, releaseDate, summary, thumbnail, type, singer) {
  /* ===== 제목·연도 ===== */
  document.getElementById('musicTitle').textContent = title;
  document.getElementById('musicYear').textContent =
      releaseDate.split('-')[0] || releaseDate;
  document.getElementById("musicDes").textContent = "가사";
  document.getElementById("singer").textContent = singer;

  /* ===== 가사·줄바꿈 처리 ===== */
  const processedSummary = summary
  .replace(/\n/g, '<br>')      // 줄바꿈 → <br>
  .replace(/\[/g, '<br><br>[') // [ 앞에 <br><br>
  .replace(/\]/g, ']<br>');    // ] 뒤에 <br>
  document.getElementById('musicSummary').innerHTML = processedSummary;

  /* ===== 썸네일 ===== */
  const img = document.getElementById('musicBg');
  img.src = thumbnail;

  /* ===== 모달 표시 ===== */
  document.getElementById('musicDetailModal').classList.add('active');
}


function showDetailModalBook(title, releaseDate, summary, thumbnail, type) {
  /* ===== 제목·연도 ===== */
  document.getElementById('bookTitle').textContent = title;
  document.getElementById('bookYear').textContent =
      releaseDate.split('-')[0] || releaseDate;

  document.getElementById("bookDes").textContent = "책 소개";

  /* ===== 도서 내용 ===== */
  document.getElementById('bookSummary').innerHTML = summary;

  /* ===== 썸네일 ===== */
  const img = document.getElementById('bookBg');
  img.src = thumbnail;

  /* ===== 모달 표시 ===== */
  document.getElementById('bookDetailModal').classList.add('active');

  /* ===== 검색 버튼 ===== */
  const searchBtn = document.getElementById('modalSearchBtn');

  // 책 구매 버튼
  searchBtn.textContent = '책 구매하러 가기 ➜' ;

  // 활성화 + 클릭 이벤트
  searchBtn.disabled = false;
  searchBtn.onclick = () => {
    const query = encodeURIComponent(title);
    window.open(`https://www.yes24.com/product/search?domain=ALL&query=${query}`, '_blank');
  };
}

function showMovieTrailer(title) {
  // 영화 예고편
  fetch(`/api/trailer/movie?title=${encodeURIComponent(title)}`)
  .then(res => res.json())
  .then(data => {
    if (data.movieVideoId) {
      const iframe = document.getElementById('trailerMovieIframe');
      iframe.src = `https://www.youtube.com/embed/${data.movieVideoId}?autoplay=1`;
      document.getElementById('trailerMovieModal').style.display = 'flex';
      closeModal();
    } else {
      alert('예고편을 찾을 수 없습니다.');
    }
  })
  .catch(() => alert('영화를 불러오는 중 오류가 발생했습니다.'));

}

function showMusicTrailer(title) {
  // 음악 유튜브 실행
  fetch(`/api/trailer/music?title=${encodeURIComponent(title)}`)
  .then(res => res.json())
  .then(data => {
    if (data.musicVideoId) {
      const iframe = document.getElementById('trailerMusicIframe');
      iframe.src = `https://www.youtube.com/embed/${data.musicVideoId}?autoplay=1`;
      document.getElementById('trailerMusicModal').style.display = 'flex';
      closeModal();
    } else {
      alert('음악을 찾을 수 없습니다.');
    }
  })
  .catch(() => alert('음악을 불러오는 중 오류가 발생했습니다.'));

}

function handleModalBackgroundClick(event) {
  if (event.target.id === 'trailerMovieModal') {
    closeTrailerModal('trailerMovieModal', 'trailerMovieIframe');
  }
  if (event.target.id === 'trailerMusicModal') {
    closeTrailerModal('trailerMusicModal', 'trailerMusicIframe');
  }

}

// // DOM이 로드된 후 이벤트 바인딩
// document.addEventListener('DOMContentLoaded', () => {
//   const movieModal = document.getElementById('trailerMovieModal');
//   const movieCloseBtn = movieModal?.querySelector('.trailer-modal-close');
//
//   if (movieModal) {
//     movieModal.addEventListener('click', handleModalBackgroundClick);
//   }
//
//   if (movieCloseBtn) {
//     movieCloseBtn.addEventListener('click', () => {
//       closeTrailerModal('trailerMovieModal', 'trailerMovieIframe');
//     });
//   }
//
//   const musicModal = document.getElementById('trailerMusicModal');
//   const musicCloseBtn = musicModal?.querySelector('.trailer-modal-close');
//
//   if (musicModal) {
//     musicModal.addEventListener('click', handleModalBackgroundClick);
//   }
//
//   if (musicCloseBtn) {
//     musicCloseBtn.addEventListener('click', () => {
//       closeTrailerModal('trailerMusicModal', 'trailerMusicIframe');
//     });
//   }
// });


document.addEventListener('click', (event) => {
  // 1. 트레일러 모달 내부의 'X' 닫기 버튼 클릭 처리
  // 클릭된 요소가 'trailer-modal-close' 클래스를 가지고 있는지 확인
  if (event.target.classList.contains('trailer-modal-close')) {
    const modal = event.target.closest('.trailer-modal'); // 가장 가까운 부모 모달 요소를 찾음
    if (modal) {
      const modalId = modal.id;
      let iframeId;
      if (modalId === 'trailerMovieModal') {
        iframeId = 'trailerMovieIframe';
      } else if (modalId === 'trailerMusicModal') {
        iframeId = 'trailerMusicIframe';
      }
      closeTrailerModal(modalId, iframeId);
    }
  }

  // 2. 트레일러 모달의 어두운 배경 영역 클릭 처리
  // 클릭된 요소가 'trailerMovieModal' 또는 'trailerMusicModal' 자체인지 확인
  if (event.target.id === 'trailerMovieModal') {
    closeTrailerModal('trailerMovieModal', 'trailerMovieIframe');
  } else if (event.target.id === 'trailerMusicModal') {
    closeTrailerModal('trailerMusicModal', 'trailerMusicIframe');
  }
});

// closeTrailerModal 함수는 현재 코드를 그대로 사용합니다.
function closeTrailerModal(modalId, iframeId) {
  const modal = document.getElementById(modalId);
  const iframe = document.getElementById(iframeId);

  if (modal && iframe) {
    iframe.src = ''; // iframe src를 비워 재생을 멈춥니다.
    modal.style.display = 'none'; // 모달을 숨깁니다.
  }
}
window.toggleText = toggleText;