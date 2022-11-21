# Store

Store is a backend app for managing the data of the store.

## Features

* Create, Read, Update and Delete Frames
* Create, Read, Update and Delete Lenses
* Create custom glasses with available frames and lenses
* Create, Read, Update and Delete Users
* Giving prices to frames and lenses based on the user currency
* Swagger documentation
* JWT authentication
* Dockerized
* Postman collection

## Getting Started

### Requirements

* [Git](https://git-scm.com/downloads) - Software for tracking changes in any set of files.

* [JDK11](https://www.oracle.com/java/technologies/downloads/#java11) - Java Development Kit.

* [IntelliJ IDEA the Ultimate edition](https://www.jetbrains.com/idea/download/#section=windows) - A great IDE.

* [Docker](https://docs.docker.com/get-docker/) - Open source containerization platform.

* [Postman](https://www.postman.com/downloads/) - Application used for API testing.

### Installation

Use any terminal to install Store.

```bash
 git clone git@github.com:Hamzanael/store.git
```

### Executing program

1. After cloning the repository open the project from Intellij IDEA.

2. Make sure to assign the spring profile equal **dev**

3. Run the docker from the docker-compose.yml file.

4. Run this command on the terminal to start the application.

```bash:
bash ./start.sh
```

### Using the application with Postman

      * Open postman app.
      * Import the postman collection from the postman folder.
      * Login with the following credentials:
        * username: admin@store.com
        * password: 123456789#
      * Copy the token from the response.
      * Go to the environment tab and add the token to the token variable.

### Swagger documentation
go to [swagger documentation](http://localhost:8080/swagger-ui/index.html) for more information about the endpoints.
or for [api json](http://localhost:8080/api) for the api json.