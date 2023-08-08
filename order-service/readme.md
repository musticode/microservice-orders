
### Order Service Endpoints

* place order
```
curl --location 'http://localhost:8081/order/placeOrder' \
--header 'Content-Type: application/json' \
--data '{
    "productId": 5,
    "totalAmount": 1100,
    "quantity":1,
    "paymentMode" : "CASH"
}'
```

**install zipkin in docker**
