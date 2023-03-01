## Jack Henry - Banno Team: Weather Service Assignment by Kuo-Lung (Alan) Chin

### Overview

This backend service provides the most current weather information by fetching data from Open Weather API (https://openweathermap.org/api):

Our weather service will receive http GET requests and take two parameters of lat and lon.
```
http://localhost:8080/weather
{
"lat" : 35.00,
"lon" : 139.00
}
```
Our backend's http client component will use the parameters to fetch JSON data from Open Weather API,
and parse JSON in order to construct our own HTTP Response with the following information:

- **coordinates**: the latitude and longitude that associated with the http request parameters.
- **temperature**: temperature degree in fahrenheit
- **temperature_type**: Cold [temperature < 36.00], Mild [[temperature < 70.00]], otherwise Hot
- **temperature_units**: imperial (this value in Open Weather API will be treated as fahrenheit)
  <br>


You can reference weather condition codes from: https://openweathermap.org/weather-conditions


This service has the following components:
- **http4s Server API Endpoint**: it receives a REST GET request at the endpoint of weather
- **http4s Client: it requests openweathermap API
- **Service Tier
- **Channeling in EitherT[F, Error, A]
- **Validator for Http Request JSON payload


#### Tech Stacks

| Technology    |
| ------------- |
| Cats          |
| Cats Effect   |
| fs2           |
| http4s        |
| Circe         |
   
#### A. Update Open Weather API Key:
You can use your own Open Weather API Key (appid = "YOUR_APP_ID") by updating **application.conf** file under **resources** folder

#### B. Run this Server:
Before you run this service, **please make sure your Open Weather API Key is valid**.
Please make sure the port 8080 is not in use before you run this server.
Or you can update the port number in application.conf under resources folder.

```
$ cd /PATH/TO/weather-service-assignment
$ sbt clean compile run

```
You will see the JSON Response payload as the format below:
```
{
    "temperature": 49.32,
    "feel": "Mild",
    "utcDateTime": "2023-03-01-20-22",
    "weather": "Rain",
    "description": "moderate rain"
}
```
