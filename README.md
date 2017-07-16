# Android Base Project

Base project built using [Dagger2](https://google.github.io/dagger/) for Dependency Injection and MVC architecture with Repository Pattern.
It has several managers classes that encapsulate features that are usually used in a common project, most using inversion control to decouple the implementation.

This project has the most common setttings applied. The main idea is to start any new project cloning this one.

Developed by the [LateralView](https://lateralview.co) team.

Check our [Wiki](https://github.com/LateralView/android-base-project/wiki) to learn the Best Practice in Android Development and more.

### Included Libraries

- [Dagger2](https://google.github.io/dagger/)
Compile-time dependency injection framework.
 
- [SimpleRESTClient](https://github.com/julianfalcionelli/SimpleRESTClientHandler)
Open Source Android library that allows developers to easily make request to a REST server usign VOLLEY and GSON.

- [Glide](https://github.com/bumptech/glide)
Fast and efficient open source media management and image loading framework.

- [Icepick](https://github.com/frankiesardo/icepick)
Allows us via anotations save and restore instance state.

### Useful Managers

- [Camera Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/CameraManager.java)

- [Internet Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/InternetManager.java)

- [Permissions Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/PermissionsManager.java)

- [System Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/SystemManager.java)

- [Social Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/SocialManager.java)

- [File Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/interfaces/FileManager.java)

- [Image Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/interfaces/ImageManager.java)

- [Parser Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/interfaces/ParserManager.java)

- [Shared Preferences Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/interfaces/SharedPreferencesManager.java)

- [Task Manager](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/infraestructure/manager/interfaces/TaskManager.java)

Also there is a repository for user management ([UserRepository](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/domain/repository/interfaces/UserRepository.java)) and another repository for sessions ([SessionRepository](https://github.com/LateralView/android-base-project/blob/master/app/src/main/java/co/lateralview/myapp/domain/repository/interfaces/SessionRepository.java)), which are stored in the shared preferences.

### Package Structure

```
android-base-project/app/src/main/java/co/lateralview/myapp/
└───application
│   │   ...
└───domain
│   └─── model
│   │    │   ...
│   └─── repository
│        └─── implementation
│        └─── interfaces
│        │   ...
└───infraestructure
│   └─── manager
│   │    └─── implementation
│   │    └─── interfaces
│   │    │   ...
│   └─── networking
│   │    └─── implementation
│   │    └─── interfaces
│   │    │   ...
│   └─── pushNotification
│        │   ...
└───ui
    └─── activity
    │    │   ...
    └─── broadcast
    │    │   ...
    └─── common
    │    │   ...
    └─── fragment
    │    │   ...
    └─── util
         │   ...
```

If you are going to develop a big application, we recommend to create one package for each functionality (screen).

### Base Project Architectures

There is a branch with the following architectures:

- MVC + Dagger2 + RX: Check develop+RX branch

- MVP + Dagger2: Check develop+MVP branch [more](https://github.com/LateralView/android-base-project/wiki/Develop-MVP)

- [Latest] MVP + Dagger2 + RX: Check develop+MVP+RX branch

For more information about that check our [Wiki](https://github.com/LateralView/android-base-project/wiki)

### Extra

[Here](https://github.com/LateralView/android-lateral-skeleton/wiki) you will find all the documentation related to Android Lateral naming standards, good practices, tips, useful libraries and more!

For any suggestions or if you want to join our team please contact us via [android@lateralview.net](mailto:android@lateralview.net)

Happy codding!
