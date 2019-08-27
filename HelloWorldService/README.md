## Hello World Application

### Endpoints

#### `GET http://localhost:8080/hello-world`

Returns the default greeting.

#### `GET http://localhost:8080/hello-world?name=YourName`

Returns a greeting that is specific to the name supplied.  If the name matches a known user, the greeting attempts to include information about the current local weather. 

#### `PUT localhost:8080/hello-world/user`

Creates a user, and associates their zipcode to the username.

Body:
```json
{
	"name": "YourName",
	"zipCode": "90210"
}
```

### Details
* LocationDAO is currently used to map from a zipCode to lat/long pair.
  * Currently there is only a fake implementation that uses hard coded lat/long of Beverly Hills
* WeatherServiceClient looks up the current weather in a given location.  The current working implementation uses the Dark Sky API (we get 1000 free calls per day)
* If a user name isn't found in UserDAO, or there is a failure reading the current weather, we revert to a default greeting response.

### Still TODO
* Implement a database backed implementation of UserDAO that persists username to zipcode mappings
* Implement a database backed implementation of LocationDAO that stores real data that maps zipcode to lat/long pairs
* Use flyway to create and manage db schemas
* Use flyway to populate the location table
* Use jooq to read/query locations and read/write users
* Implement some sort of cookie / user session so users are remembered
* Add authentication?
* Pull WeatherService into a separate core service instead of running within HelloWorld, and use gRPC to call from one service to another