# Simple Android Chat Application with Springboot,MongoDb 

Android Kotlin WebSocket Client and Spring Websocket Chat-Application with MongoDB integartion

This repository is contains
* Android Group Chat Application - Websocket Android Client App (STOMP Client App) 
* Spring Server(with MongoDB integration) chat application 

by using a <a href="https://stomp.github.io/">STOMP</a> Protocol.

# Simple Group Chat Application
<h3>Android :</h3>
Build using

* Kotlin
* MVVM
* Hilt
* LiveData
* Data Binding
* STOMP Lib

I used the <a href="https://github.com/NaikSoftware/StompProtocolAndroid">This STOMP Library</a> and updated some dependencies like RxJava to Latest version inorder to work properly.


# Demo :

https://user-images.githubusercontent.com/46423041/192099073-af09f0f4-1c8f-4aaf-893d-b0668e6f1a2a.mp4


<h3>Server :</h3>

```
Once you run Mongo DB, 
update the 
"spring.data.mongodb.uri"
in application.properties.
```
<h5>JDK Version - 11 </h5>
After running the MongoDB, you can run the application and by default it will launch on port 8080. You can access it
from:

```
http://localhost:8080/
```
<h4>Reference :</h4> 

Server Spring Boot  - <a href="https://github.com/okanmenevseoglu/Spring-Websocket-Chat-Application-with-MongoDB">
okanmenevseoglu</a>
Stomp Client Library - <a href="https://github.com/NaikSoftware/StompProtocolAndroid">NaikSoftware</a>
