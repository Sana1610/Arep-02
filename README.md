# SimpleConcurrentWebServer

## Overview
SimpleConcurrentWebServer is a basic multi-threaded web server built in Java. It serves HTML, CSS, JavaScript files, and image files encoded in Base64. The server verifies the existance of the files prior to them being pull up into to the client in order to evade any issues with the permissions.

## Features
- **Concurrent Request Handling**: Utilizes a thread pool to manage multiple client connections simultaneously.
- **File Serving**: Serves HTML, CSS, JavaScript, and image files (PNG, JPG, JPEG) directly from the local disk.
- **Base64 Image Encoding**: Converts image files to Base64 for embedding in HTML responses.
- **POST Request Handling**: Accepts and processes POST requests to save data to a text file on the server.

## Project Structure
```
SimpleConcurrentWebServer/
│
├───src/
│   ├───main/
│   │   ├───java/
│   │   │   └───arep/
|   |   |       └───webserver/
│   │   │           └───SimpleWebServer.java
│   │   └───resources/
│   │       ├───index.html
│   │       ├───style.css
│   │       ├───script.js
│   │       ├───image.png
│   │       └───... (other resources)
```


## How to Run

1. **Ensure You Have Java Installed**:
   - Make sure you have JDK installed on your system. You can check your Java version by running:
     ```bash
     java -version
     ```
     
2. **Compile the Source Code**:
   - Compile the Java source files using the `javac` command:
     ```bash
     javac -d bin src/main/java/arep/webserver/SimpleWebServer.java
     ```

3. **Run the Web Server**:
   - After compiling, start the web server by running:
     ```bash
     java -cp bin arep.webserver.SimpleWebServer
     ```

4. **Access the Web Server**:
   - Open your web browser and navigate to:
     ```
     http://localhost:35000
     ```
   - The server will serve files located in the `src/main/resources/` directory and it is possible that the first time that it's use it's going to generate a 404 error message.

5. **Stop the Server**:
   - To stop the server, press `Ctrl + C` in the terminal where the server is running.

## Configuration
- **Web Root Directory**: The server serves files from the `src/main/resources/` directory by default, the files does not follow any naming convention.
- **Port**: The server listens on port `35000` by default.
- **Thread Pool**: The server uses a thread pool with a fixed size of 10 threads to handle concurrent requests.

## What Was Built
This project includes the following built-in functionalities:
- A simple HTTP server capable of handling GET requests for various file types (HTML, CSS, JavaScript, images).
- POST request handling to save text data into files on the server.
- Concurrent handling of client requests using Java threads.
- Base64 encoding for images embedded in HTML responses.
- Multiple error handling via html response.
![image](https://github.com/user-attachments/assets/16b0a23f-1705-4ebc-a708-2404af38a194)
![image](https://github.com/user-attachments/assets/ca7cc511-fbc3-4731-bb6f-bb62482ae48b)



## Key Differences from Sequential Servers
- **Concurrent Handling**: This server can handle multiple requests concurrently, improving performance and responsiveness.
- **Improved User Experience**: With concurrent request handling, users experience less delay and better responsiveness when accessing the server.
- **Resource-efficient**: Uses a thread pool to limit the number of threads and avoid excessive resource consumption.
- **Efficiency**: CPU and I/O resources are better utilized because threads can perform other tasks while waiting for I/O operations to complete.
- **More robust**: If one thread crashes, it does not affect other threads or the overall server.

## Author
This project was developed by Santiago Alberto Naranjo Abril https://github.com/Sana1610.
