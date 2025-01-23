console.log("네비바 JS 연결")

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