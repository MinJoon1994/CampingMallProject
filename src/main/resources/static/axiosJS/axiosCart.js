
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

//4. 주문 처리하는 함수 선언
async function orderItemFn(orderItemData){
    const response = await axios.post('/order',orderItemData)
    return response;
}

//5. 주문 취소하는 함수 선언
async function cancelOrderId(paramData){
    const response = await axios.post('/order/'+paramData.orderId+'/cancel',paramData)
    return response;
}

//6. 장바구니에 담아 놓은 상품중 주문할 상품만 주문
async function orderCartItemAxios(paramData){
    const response = await axios.post('/order/payment',paramData);
    return response;
}

//7. 장바구니에 담아 놓은 상품중 주문할 상품 주문페이지로 보내기
async function orderPageTransferAxios(cartItems){
    const response = await axios.post('/orderpage',cartItems)
    return response;
}

//8. 바로 주문하는 함수
async function directOrder(paramData){
    const response = await axios.post('/order/payment/direct',paramData);
    return response;
}