console.log("메인 자바스크립트 연결")

/* 스크롤시 네비게이션바 글꼴 색변경 */
$(window).scroll(function(){
    var conBTop = $(".showBox").offset().top;
    var conBBottom = conBTop + $(".showBox").height();

    /*네비바메뉴 conB 진입 Y좌표 850 */
    if($(this).scrollTop() > 880 && $(this).scrollTop() < conBBottom){
        $('.nav ul li a').addClass("active");
        $('.Login-nav button').addClass("active");
        $('.left-nav button').addClass("active");

     } /*3820은 conD 시작 Y좌표*/
     else if($(this).scrollTop()>3820){
        $('.nav ul li a').addClass("active");
        $('.Login-nav button').addClass("active");
        $('.left-nav button').addClass("active");
     }
     else if($(this).scrollTop()>700 && $(this).scrollTop() < conBBottom){
        $('.showBox').addClass("on") /*페이드인*/
     }
     else if($(this).scrollTop()>500 && $(this).scrollTop() < conBBottom){
        $('.showBox').removeClass("on");  /*페이드아웃*/
     }
     else{
        $('.nav ul li a').removeClass("active");
        $('.Login-nav button').removeClass("active");
        $('.left-nav button').removeClass("active");
    }
})

/*커뮤니티 버튼 클릭시 토글*/
document.querySelector(".com_btn").addEventListener("click",function(e){
   e.preventDefault();
   const com_box=document.querySelector("#com_box");

   if(com_box.classList.contains("hidden")){
      com_box.classList.remove("hidden");
      com_box.classList.add("visible");
   } else{
      com_box.classList.remove("visible");
      com_box.classList.add("hidden");
   }
})