# Foreign Exchange API  
  
Simple foreign exchange application which has the following functions:   
- Exchange Rate API  
- Conversion API   
- Conversion List API  
  
## API List   
### **Exchange Rate API:** 
Endpoint: http://localhost:9090/api/foreign-exchange/rate   
Method: POST
  
Sample Input:   
```json  
{  
  "sourceCurrency": "USD",  
  "targetCurrency": "TRY"  
}  
```  
  
Sample Output:  
```json  
{  
  "pair": "USD/TRY",  
  "rate": 9.7266  
}  
```  
[Test on Swagger](http://localhost:9090/swagger-ui/index.html?configUrl=/api-docs/swagger-config#/Foreign%20Exchange%20API/getExchangeRate)

### Conversion API  
Endpoint: http://localhost:9090/api/foreign-exchange/conversion   
Method: POST

Sample Input:   
```json  
{  
  "sourceCurrency": "EUR",  
  "sourceAmount": 100,  
  "targetCurrency": "TRY"  
}  
```  
  
Sample Output:  
```json  
{  
  "targetAmount": "1,126.05",  
  "transactionId": "d5a5ff24c41843b4a390397bff3f6056"  
}  
```  
[Test on Swagger](http://localhost:9090/swagger-ui/index.html?configUrl=/api-docs/swagger-config#/Foreign%20Exchange%20API/getConversation)
  
### Conversion List API
Endpoint: http://localhost:9090/api/foreign-exchange/conversions
Method: POST
Parameters(optional): page, size 

Since system has millions of conversion records, Pageable is used to return a small part of records, called page.

At least one of the inputs(not necessarily one, can be both used), which are `transactionId`and `transactionDate`must be given. `JPA Specification` is used to create queries dynamically by given criteria. 
  
Sample Input:   
```json  
{
  //"transactionId": "61839d60dbbd4b5dbb2022e8c92f1d29",
  "transactionDate": "2021-12-08"
}
```  
  
Sample Output:  
```json  
{
  "content": [
    {
      "id": 459,
      "sourceCurrencyCode": "KRW",
      "targetCurrencyCode": "PLN",
      "amount": 4610,
      "targetAmount": 366080.1,
      "transactionId": "c2016c3c04404fb7a3869c38d517ae2f",
      "transactionDate": [
        2021,
        12,
        8
      ]
    }
  ],
  "pageable": {
    "sort": {
      "unsorted": true,
      "empty": true,
      "sorted": false
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 20,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 6,
  "totalPages": 1,
  "numberOfElements": 6,
  "sort": {
    "unsorted": true,
    "empty": true,
    "sorted": false
  },
  "size": 20,
  "number": 0,
  "first": true,
  "empty": false
}
```  
[Test on Swagger](http://localhost:9090/swagger-ui/index.html?configUrl=/api-docs/swagger-config#/Foreign%20Exchange%20API/getConversationList)


## DB Configuration
H2 Database has been used for persisting exchange conversions. For test purposes and simulating pageable responses, 1000 random `Conversion` records are created before the application is started.

Conversion Entity:

![Screen Shot 2021-11-10 at 16 58 41](https://user-images.githubusercontent.com/2103017/141133038-b843da40-6457-4c4f-a5e2-7c705d51f17c.png)

Records:

![Screen Shot 2021-11-10 at 16 58 50](https://user-images.githubusercontent.com/2103017/141133022-20dde558-c963-4ad3-8cdd-299c524cb938.png)

## Application Links
Base URL: http://localhost:9090
H2 Console: http://localhost:9090/h2-console
Swagger: http://localhost:9090/swagger-ui/index.html?configUrl=/api-docs/swagger-config#/
Frankfurter API URL: https://api.frankfurter.app
Frankfurter Documentation: https://www.frankfurter.app/
Javadoc: /javadoc/index.html
