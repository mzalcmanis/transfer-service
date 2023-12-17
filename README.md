# Local run

* Make sure to have Java 17 (or higher) installed on your local machine.
* Open a command line app
* Navigate to project root directory and run the following command:
```shell
./gradlew bootRun
```
To change the service port:
```shell
./gradlew bootRun --args='--server.port=8081'
```
In case of insufficient permissions on Linux run the following command:
```shell
chmod +x gradlew
```

# Usage

## Test Data

### Clients

| Client ID                            | Number of accounts                   |
|--------------------------------------|--------------------------------------|
| 84d38aae-ff55-46b9-8d74-ee75a425eba9 | 3                                    |
| 24c8fda4-59ec-4fd6-8b11-d264358f9ea6 | 3                                    |
| 7887f298-e914-4ad5-bec9-a8e916e9eede | 0                                    |

### Accounts

| Client ID                            | Account ID                           | Currency | Start balance |
|--------------------------------------|--------------------------------------|----------|---------------|
| 84d38aae-ff55-46b9-8d74-ee75a425eba9 | efd2661d-96a9-460f-aa5a-9737a450caee | USD      | 100.00        |
| 84d38aae-ff55-46b9-8d74-ee75a425eba9 | 93131577-fe7a-490e-a776-14dd66aa49ba | EUR      | 100.00        |
| 84d38aae-ff55-46b9-8d74-ee75a425eba9 | 91212bc1-8d84-4e57-94f5-6c9cc4f8714d | NOK      | 100.00        |
| 24c8fda4-59ec-4fd6-8b11-d264358f9ea6 | 2d25b876-4d40-4918-b145-fe806bb01914 | USD      | 200.00        |
| 24c8fda4-59ec-4fd6-8b11-d264358f9ea6 | 36eae1cb-7031-4df0-bf4d-7679e9845d13 | EUR      | 200.00        |
| 24c8fda4-59ec-4fd6-8b11-d264358f9ea6 | c922d577-0f22-4103-8cc5-b586a530f2a2 | NOK      | 200.00        |

## API calls

Make sure that you have curl installed. 
```shell
curl --version
```

Retrieve account list for the given client
```shell
curl http://localhost:8080/clients/84d38aae-ff55-46b9-8d74-ee75a425eba9/accounts
```
Create one or more transactions   
Windows:
```shell
curl -X POST ^
  -H "Content-Type: application/json" ^
  -d "{\"receiverAccountId\":\"36eae1cb-7031-4df0-bf4d-7679e9845d13\",\"currency\":\"EUR\",\"amount\": 1.23}" ^
  http://localhost:8080/client/me/accounts/efd2661d-96a9-460f-aa5a-9737a450caee/transactions
```
Linux:
```shell
curl -X POST \
  -H "Content-Type: application/json" \
  -d "{\"receiverAccountId\":\"36eae1cb-7031-4df0-bf4d-7679e9845d13\",\"currency\":\"EUR\",\"amount\": 1.23}" \
  http://localhost:8080/client/me/accounts/efd2661d-96a9-460f-aa5a-9737a450caee/transactions
```

Get the first page of transactions (0-based numbering):
```shell
curl "http://localhost:8080/client/me/accounts/efd2661d-96a9-460f-aa5a-9737a450caee/transactions?page=0&size=5"
```

### Error scenarios

Here are a couple of error scenarios (mind to replace ^ with \ when using Linux).

Insufficient funds

```shell
curl -X POST ^
  -H "Content-Type: application/json" ^
  -d "{\"receiverAccountId\":\"36eae1cb-7031-4df0-bf4d-7679e9845d13\",\"currency\":\"EUR\",\"amount\": 1234.23}" ^
  http://localhost:8080/client/me/accounts/efd2661d-96a9-460f-aa5a-9737a450caee/transactions
```

Wrong currency

```shell
curl -X POST ^
  -H "Content-Type: application/json" ^
  -d "{\"receiverAccountId\":\"36eae1cb-7031-4df0-bf4d-7679e9845d13\",\"currency\":\"NOK\",\"amount\": 1234.23}" ^
  http://localhost:8080/client/me/accounts/efd2661d-96a9-460f-aa5a-9737a450caee/transactions
```