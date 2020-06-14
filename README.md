# Ali-For-Reddit
A work in progress Reddit client for android. Developed for learning purposes. 

# Functionality
* Display posts from multiple feed; subscription and popular.
* Comments are displayed in a tree-like structure. Long-press to collapse/expand them. 
* Featuring secure OAuth2 login and Anonymous browsing.
* Supports infinite scrolling.
* Has an offline mode.

# Screenshots
![Main screen](https://i.imgur.com/DNkW2a6l.png)
![Drawer](https://i.imgur.com/5WwfNnUl.png)
![Post Detail Screen](https://i.imgur.com/pRG2svxl.png)
![Comments Tree](https://i.imgur.com/dBboR5Kl.png)

# Video
[![Demo Video](https://i.imgur.com/X2Mkbtd.png)](https://streamable.com/kjaww)

# Architecture
* The project is built in Kotlin and follows Clean architecutre, MVP-Passive View, single Activity multiple Fragments.
* Data fetched from server is stored locally using Room Persistence Library, which serves as a single source of truth.
* Has unit tests and written in test-driven development (TDD) style.
* OAuth 2.0 protocol for authentication and authorization.

# Libraries
* Dagger 2 for dependency injection.
* Rxjava for a reactive UI and asynchronous operations.
* Android Jetpack / Architecture Components
* Paging Library to enable pagination/infinite scrolling.
* Room as ORM.
* FragNav for navigation.
* Epoxy for a Recyclerview with multiple view types.
* Retrofit for networking.
