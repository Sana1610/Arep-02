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
   git clone https://github.com/Sana1610/Arep-02.git  
   ```

2. **Compile the Project**

   Open the project in Visual Studio Code or your preferred IDE.

   **Visual Studio Code:**

    - Open the project folder.
    - Open the integrated terminal.
    - Compile the project, you can use any Java IDE to import and build the project.

### Running the Server

To start the server, run the `MainApp` class. This will start the server on port `35000` and set up the specified routes and static file location.

**Compile the Source Code**:
   - Compile the Java source files using the `javac` command:
     ```bash
      javac -d out -sourcepath src/main/java src/main/java/arep/webserver/*.java
      ```
**Run the Web Server**:
   - After compiling, start the web server by running:
     ```bash
     java -cp out arep.webserver.SimpleWebServer
     ```


### Example Usage

1. **Access REST Services:**

    - `http://localhost:35000/hello?name=Jose`
    - `http://localhost:35000/pi`
    - `http://localhost:35000/index.html`


2. **Access Static Files:**

   Place your static files in the `src/main/resources/` directory. You can access them directly via the URL path.
   

## Screenshots
![image](https://github.com/user-attachments/assets/4dd6659b-52fb-4a84-94ae-519b16301f5d)
![image](https://github.com/user-attachments/assets/371a378c-316b-4b35-b684-676666f03987)
![image](https://github.com/user-attachments/assets/e82cf11d-d685-4ade-84c4-ff2008604da7)

## Author
This project was developed by Santiago Alberto Naranjo Abril https://github.com/Sana1610.

