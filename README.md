# JuicyMeetingApp
-- Build the framework of the APP

-- Design the UI of the APP

-- Implement event and group list view

# API examples
Communicate with android app end using JSON string (JSON in plaintext format). Images are stored as Strings for easy transfer.

event

get upcoming events by user email
GET http://localhost:8080/juicyBackend/webapi/event/upcoming/zxq@cmu.edu

response:
```[
  {
    "imgUrl": "http://i.imgur.com/DvpvklR.png",
    "titleContextColor": 2,
    "eventDateTime": "2015-05-27 08:25:51.0",
    "creator": {
      "imgStr": "http://i.imgur.com/DvpvklR.png",
      "passwd": "9527",
      "name": "Aladin",
      "email": "eventCreator@cmu.edu"
    },
    "followers": 3,
    "imageContextColor": 1,
    "creatorEmail": "eventCreator@cmu.edu",
    "name": "CMU conference",
    "description": "This is a great event!",
    "lon": -23.5,
    "id": 1,
    "lat": 23.1
  }
]
