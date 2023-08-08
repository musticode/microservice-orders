### Endpoints:

**GET order** 
request
```
curl --location 'http://localhost:9090/order/9'
```
response:
```
{
    "orderId": 9,
    "orderDate": "2023-08-05T09:31:37.877044Z",
    "orderStatus": "PLACED",
    "amount": 1100,
    "productDetail": {
        "productName": "test product",
        "productId": 1,
        "quantity": 14,
        "price": 1200
    },
    "paymentDetail": {
        "paymentId": 1,
        "paymentMode": null,
        "paymentStatus": "SUCCESS",
        "paymentDate": "2023-08-05T09:31:37.945008Z"
    }
}
```

**POST place order**
request
```
curl --location 'http://localhost:8081/order/placeOrder' \
--header 'Content-Type: application/json' \
--data '{
    "productId": 1,
    "totalAmount": 1100,
    "quantity":2,
    "paymentMode" : "CASH"
}'
```
response:
```
1
```


Redis used for Request Rate Limiter
- https://medium.com/@mcakir/spring-boot-redis-72c956612e3f
- https://blog.burakkutbay.com/spring-boot-data-redis-nedir-distributed-cache-kullanimi.html/
