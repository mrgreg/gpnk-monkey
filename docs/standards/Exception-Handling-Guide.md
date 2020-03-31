# Exception Handling Guide

This document describes our guidelines for exception handling as used and implemented in this project.

## Summary

In short, we expect the code to throw an Exception (checked or Runtime, custom or standard) most suitable to 
the logic of the code itself. Thus, a specific method is only concerned with its own logic, while the
exception handling approach (described in this guide) automatically takes care of how the Exception is 
presented in a REST `Response`.

## Do Not Use Checked Exceptions

Java 8 functional paradigm (e.g. Functions and lambdas) effectively does not support checked Exceptions. To keep things 
simple we adopt the same approach here and discourage the use of checked Exceptions in favor of runtime Exceptions. If your
code needs to throw an Exception, throw a runtime Exception. If a standard API that you are using throws a checked Exception,
re-throw it as a runtime Exception.

Having discouraged the use of checked Exceptions, it becomes even more important to document the runtime Exceptions
explicitly thrown in the body of a method in a Javadoc comment for this method. So, please javadoc the runtime Exceptions 
you throw. 

The following Exception Handling approach relies on the fact that **only runtime Exceptions are used** within the system. 

## Exception Handling Approach

For the purpose of Exception Handling, we conceptually distinguish between two levels of code in a REST web application: 
- the `Resource` level, which serves as the frontline of a web application which communicates with the 
rest of the world.
- the logic levels lower down from `Resource` that implement the logic of a web application.

### `Resource` Level Exceptions

The logic of the `Resource` level may incorporate certain checks that result in an Exception. For example, such checks
may reveal incorrect input or that a requested object does not exist. At this level, we expect the code to throw 
standard JAX-RS exceptions (`javax.ws.rs.*Exception`) with an appropriate error code and a clear problem message.  
For example, if the caller supplies input values that do match the expected range,
a `Resource` method may respond with a `javax.ws.rs.BadRequestException`, thus returning a 400 HTTP error code with an 
appropriate message informing the caller which input parameter did not match the expected range.

In the case where there isn't an suitable standard JAX-RS Exception, a `WebApplicationException` with an appropriate 
error code and message may be thrown.

For convenience, here is a table of common standard JAX-RS exceptions: 

Exception   | Status Code   | Description
----------- | -----------   | -----------
BadRequestException | 	400 |	Malformed message
NotAuthorizedException |	401 |	Authentication failure
ForbiddenException | 	403 |	Not permitted to access
NotFoundException |	404 |	Couldn’t find resource
NotAllowedException |	405 |	HTTP method not supported
NotAcceptableException |	406 |	Client media type requested not supported
NotSupportedException |	415 |	Client posted media type not supported
InternalServerErrorException |	500 |	General server error
ServiceUnavailableException  |	503 |	Server is temporarily unavailable or busy

### Logic Level Exceptions

Logic level code is expected to throw Exceptions that are best suited to communicate a particular exceptional situation 
to the rest of the application. Any Exception type, be it checked or Runtime, standard or custom, can be used by the 
developer to best express the exceptional condition from a logical standpoint. 

There are two main requirements for the Exceptions at this level:
- Exception provides an appropriate detailed message. 
- Exception is **completely unaware** of HTTP error codes and HTTP `Response`. These details will be handled at a 
higher level in the web application.

### Exception Handling

Suppose we threw an Exception, what happens then? And how do we respond to the caller in this case?

Conceptually, there are two things we need to address when an Exception occurs:
- Log it. This is very important for debugging purposes!
- Return an appropriate `Response` to the caller, which contains a representative error code and a clear error message.

We expect subclasses of WebApplicationException thrown at the `Resource` level to result in a `Response` similar to the
example presented below, where the code and the message are those of the WebApplicationException:
```Json
{
  "code": 400,
  "message": "Invalid name 'Nadya1'. A name should only contain characters."
}
``` 

We group all Logic Level Exceptions into the HTTP error code of 500, thus returning the following (example) `Response` 
to the caller:
```Json
{
  "code": 500,
  "message": "There was an error processing your request. It has been logged (ID eb671ec0329d14fa).",
  "details": "User Larry already exists"
}
``` 

In some circumstances, it may be desirable to assign a different HTTP error code for a particular Logic Level Exception.
This can be done by creating an additional `Mapper` for the Exception as described in the Implementation section below.

In rare circumstances, one may want complete control over how a particular Exception is presented to the caller.
This is possible and described below, but is discouraged as a frequent practice as we would like to keep the Exception 
handling uniform.    


### Exception Handling Implementation

Exception Handling is implemented by a customized `LoggingExceptionMapper` class named `GenericExceptionMapper`. 
`GenericExceptionMapper` is the default Exception Mapper provided to all web applications relying on our
framework. `GenericExceptionMapper` always returns a `Response` with a `500` HTTP error code. `GenericExceptionMapper`
class resides in the `common` module in our framework. 

[`LoggingExceptionMapper`](https://github.com/dropwizard/dropwizard/blob/master/dropwizard-jersey/src/main/java/io/dropwizard/jersey/errors/LoggingExceptionMapper.java)
 is a DropWizard class that first logs a full exception stack trace and then sends the following `Response` to the caller:
 
 ```Json
{
  "code": 500,
  "message": "There was an error processing your request. It has been logged (ID 5ed4e044e449103c)."
}
``` 

We have customized `LoggingExceptionMapper` in our `GenericExceptionMapper` class to also include the Exception's own 
message in the `details` field as demonstrated below:

 ```Json
{
  "code": 500,
  "message": "There was an error processing your request. It has been logged (ID eb671ec0329d14fa).",
  "details": "User Larry already exists"
}
``` 
Before `GenericExceptionMapper` sends the above `Response` to the caller, it logs the complete `Exception` stack trace 
as follows:
 
```
ERROR [2020-04-01 20:57:37,516] io.dropwizard.jersey.errors.LoggingExceptionMapper: Error handling a request: eb671ec0329d14fa
! com.gpnk.persistence.UserAlreadyExistsException: User Larry already exists
! at com.gpnk.persistence.UserDAOImpl.createUser(UserDAOImpl.java:52)
...
```
Note that the ID in the log (`Error handling a request: eb671ec0329d14fa`) matches the ID returned to the caller in the 
response (`It has been logged (ID eb671ec0329d14fa)`).

`WebApplicationException` and its subclasses are handled slightly differently, where the Exception's error code and its
message are returned in the `code` and `message` fields of the `Response`. We rely on the unchanged behavior of DropWizard's
[`LoggingExceptionMapper`](https://github.com/dropwizard/dropwizard/blob/master/dropwizard-jersey/src/main/java/io/dropwizard/jersey/errors/LoggingExceptionMapper.java)
class in this case. For example, a `Response` from a `BadRequestException` is as follows:

```Json
{
  "code": 400,
  "message": "Here is a WebApplicationException demonstration (thrown at the Resource level)."
}
``` 

In some circumstances, when it is desirable to assign a different HTTP error code for a particular Logic Level Exception,
a custom `ExceptionMapper` can be created that uses the provided `ExceptionResponseHelper` class in its implementation as
follows:
```Java
import com.gpnk.common.response.ExceptionResponseHelper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Demonstrates a custom mapper with a custom status code.
 */
@Provider
public class CustomResponseCodeExceptionMapper implements ExceptionMapper<CustomResponseCodeException> {

    @Override
    public Response toResponse(CustomResponseCodeException e) {
        // use a made-up status code
        ExceptionResponseHelper helper = new ExceptionResponseHelper(510);
        return helper.toResponse(e);
    }
}

```
A custom `ExceptionMapper` implementation should use a representative response error code. The above example uses the 
response error code of `510` for demonstration purposes only.

Here is a `Response` generated by the above example:
```Json
{
  "code": 510,
  "message": "There was an error processing your request. It has been logged (ID b8d42bd012fc696a).",
  "details": "CustomResponseCodeException message."
}
```

The example `CustomResponseCodeExceptionMapper` class is provided in the `common` module in the 
`com.gpnk.common.response.examples` package.

In rare circumstances, when one wants complete control over how a particular Exception is presented to the caller,
an `ExceptionMapper` can be created that fully determines the structure of the `Response` returned to the caller.
Of extreme importance is the fact that **the custom `ExceptionMapper` is now fully responsible to logging the Exception**
before returning the response. For the purpose of logging an Exception's stack trace, `ExceptionResponseHelper` class
provides a `log` method that returns the `ID` of the logged Exception:
```Java
public long log(Exception e) {...}
```
An example of a fully customized Exception response implementation is presented in the `FullyCustomizedExceptionMapper` 
class:
```Java
import com.gpnk.common.response.ExceptionResponseHelper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Demonstrates a fully customized ExceptionMapper.
 * Any ExceptionMapper needs to log the Exception before building a Response.
 */
@Provider
public class FullyCustomizedExceptionMapper implements ExceptionMapper<FullyCustomizedSampleException> {

    @Override
    public Response toResponse(FullyCustomizedSampleException e) {
        ExceptionResponseHelper helper = new ExceptionResponseHelper(-1);
        long loggedExceptionId = helper.log(e);
        SampleEntity response = new SampleEntity(String.format("%016x", loggedExceptionId), "Custom HTTP Code 555", e.getLocalizedMessage());

        return Response.status(555)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(response)
                .build();
    }
}

```
In the above example, the code first uses the `ExceptionResponseHelper helper` to log the Exception's stack trace and
then constructs a custom response entity of type `SampleEntity`, which is returned to the caller. The response then 
looks as follows:
```Json
{
  "exceptionId": "3fbd1c0aa185aa09",
  "field1": "Custom HTTP Code 555",
  "field2": "Exception like no other :) Demos a fully customized ExceptionMapper."
}
``` 

This example in its entirety is available in  in the `common` module in the `com.gpnk.common.response.examples` package.

Once again we repeat that fully customizing a response on an Exception may be desirable in rare cases, but is 
discouraged as a frequent practice as we would like to keep the Exception handling uniform.    
