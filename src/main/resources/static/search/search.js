console.log("search JS 연결되었습니다.")

// 버튼과 오버레이 요소 선택
const searchBtn = document.querySelector('#search-btn');
const searchOverlay = document.querySelector('#search-overlay');
const closeBtn = document.querySelector('#close-btn');
const searchInput = document.querySelector('.search-input');
const search_submit_btn = document.querySelector('.search_submit_btn');

// Search 버튼 클릭 시 오버레이 표시
searchBtn.addEventListener('click', () => {
    searchOverlay.style.display = 'flex'; // 오버레이를 flex로 표시
});

// Close 버튼 클릭 시 오버레이 숨기기
closeBtn.addEventListener('click', (e) => {
    e.preventDefault();
    searchInput.value="";
    searchOverlay.style.display = 'none'; // 오버레이 숨기기
});

searchInput.addEventListener('keydown',(e)=>{
    if(e.key == 'Enter'){
     e.preventDefault();
     search_submit_btn.click();
    }
})