# FreeX, feel free to exchange.
FreeX (Free to eXchange) is a mobile application for online and face-to-face currency exchange transactions.
This is the team project required by ECE 651, instructed by Prof. Werner Dietl.
My job is the development of the front end in Android platform.
=======
## Main Contributions

*  Integrated most of the functions into __Fragment__ classes, decreased the number of Activities to 3, thus cut down the switching cost.

*  Used __Aynctask__ as a multi-thread programming technique to communicate with the server using HTTP protocol.

*  Stored the cookie in a global instance of __Application__, and extracted the session when needed to identify different users from heterogeneous devices.

*  __Regular expression__ is used to check the validity of user input; MD5 algorithm is used for encryption through transmission; Used __currencylayer__ API to get real-time exchange rates;
Used __ZXing__ API to support exchange transactions through scanning QR code.

*  Got a total score of __93%__ after the professor's evaluation.
---
