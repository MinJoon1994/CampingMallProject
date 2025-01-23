
// 1. 장바구니 담기처리 함수
async function cartItem(cartItemData){
    const response = await axios.post('/cart',cartItemData)
    return response;
}

// 2. 장바구니 상품 수량 변경 서버에 요청
async function updateCartItemCountJS(cartItemId, count){
    const response = await axios.patch('/cartItem/'+cartItemId+'?count='+count)
    return response
}

// 3. 장바구니 상품 취소(삭제) 서버에 요청하기
async function deleteCartItemAxios(cartItemId){
    const response = await axios.delete('/cartItem/'+cartItemId)
    return response
}