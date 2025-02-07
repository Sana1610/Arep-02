# Simple Web Framework

This project is a simple web framework implemented in Java that handles REST services and static files. It allows developers to define REST endpoints using lambda functions, extract query parameters, and serve static files from a specified directory.

## Features

1. **GET Static Method for REST Services**: Define REST services with lambda functions.
   ```java
   get("/hello", (req, res) -> "hello world!");
   ```

2. **Query Value Extraction Mechanism**: Extract and use query parameters in REST services.
   ```java
   get("/hello", (req, res) -> "hello " + req.getValues("name"));
   ```

3. **Static File Location Specification**: Specify the folder for static files.
   ```java
   staticfiles("webroot/public");
   ```

4. **Serving Static Files**: Serve HTML, CSS, JavaScript, and image files.

## Getting Started

### Prerequisites

- Java 11 or later
- Visual Studio Code (preferred IDE) or any Java IDE

### Installation

1. **Clone the Repository**
   ```sh
   git clone https://github.com/juaneortiz1/lab02Arep.git   
   ```

2. **Compile the Project**

   Open the project in Visual Studio Code or your preferred IDE.

   **Visual Studio Code:**

    - Open the project folder.
    - Open the integrated terminal.
    - Compile the project, you can use any Java IDE to import and build the project.

### Running the Server

To start the server, run the `MainApp` class. This will start the server on port `8080` and set up the specified routes and static file location.

```sh
java -cp bin edu.escuelaing.arep.ASE.app.MainApp
```

### Example Usage

1. **Access REST Services:**

    - `http://localhost:8080/hello?name=Pedro`
    - `http://localhost:8080/pi`
    - `http://localhost:8080/index.html`


2. **Access Static Files:**

   Place your static files in the `src/main/resources/` directory. You can access them directly via the URL path.

## Testing

### Running Tests

Tests can be run to ensure the correctness of the framework.


### Test Results

**Test 1: `testHandleGetRequest`**

- **Description**: Validates the handling of a GET request for a static file.
- **Expected Result**: HTTP 200 OK with content type `text/html`.



**Test 2: `testHandlePostRequest`**

- **Description**: Tests handling of POST requests and saving data to a file.
- **Expected Result**: File creation confirmation and HTTP 200 OK response.





**Test 3: `testAddQueryParam`**
- **Description**: Tests the addQueryParam method of the Request class. It ensures that query parameters are added correctly to a request and can be retrieved.
- **Expected Result**: The query parameter "key1" should have the value "value1".




**Test 4: `testGetValues`**
- **Description**: Tests the getValues method of the Request class. It ensures that the values of query parameters can be retrieved correctly.
- **Expected Result**: The query parameter "key2" should have the value "value2".




**Test 5: `testGetRoute`**
- **Description**: Tests the RouteManager class to ensure that routes are correctly registered and retrieved. It checks if a route registered with a path returns the expected response when handled.
- **Expected Result**: The route for "/test" should return "Test Response".



## Screenshots



## Author
This project was developed by Santiago Alberto Naranjo Abril https://github.com/Sana1610.

