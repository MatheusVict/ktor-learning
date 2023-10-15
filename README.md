# API LEARNING KTOR

## Description
It saves notes in a database and allows you to view them, delete them and add new ones.
And also allows you to view the notes of other users.
Besides to register and login an user with JWT

## Dependencies:
```gradle
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    // https://mvnrepository.com/artifact/org.ktorm/ktorm-core
    implementation("org.ktorm:ktorm-core:3.5.0")
    // https://mvnrepository.com/artifact/com.mysql/mysql-connector-j
    implementation("com.mysql:mysql-connector-j:8.1.0")
    // https://mvnrepository.com/artifact/org.mindrot/jbcrypt
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.ktor:ktor-auth:1.6.8")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:1.6.8")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
```

## Serialization is required to requests and response data class:
Example:

```kotlin
@Serializable
data class User(
    val id: Int,
    val username: String,
    val password: String
)
```

## To configure serialization:

```kotlin
fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
}

```
## Data Base connection:

```kotlin
object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = ""
    )

```
Just need to call it to conect

## Entity Definition:

```kotlin
object UserEntity: Table<Nothing>("users") {
    val id = int("id").primaryKey()
    val username = varchar("username")
    val password = varchar("password")
```

## application.conf:

```conf
secret = "secret111"
issuer = "http://0.0.0.0:8080/"
audience = "http://0.0.0.0:8080/hello"

```

## To configure autentication:

```kotlin
fun Application.module() {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val tokenManager = TokenManager(config)

    install(Authentication) {
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").getString()
            validate { jwtCredential ->
                if (jwtCredential.payload.getClaim("username").asString().isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }
}
```
## Token Manager:


```kotlin
class TokenManager(
    private val config: HoconApplicationConfig
) {

    private val audience = config.property("audience").toString()
    private val secret = config.property("secret").toString()
    private val issuer = config.property("issuer").toString()
    private val expirationDate = System.currentTimeMillis() + 86400000

    fun generateJWTToken(user: User): String? {


        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withClaim("id", user.id)
            .withClaim("expirationDate", expirationDate)
            .withExpiresAt(Date(expirationDate))
            .sign(Algorithm.HMAC256(secret))


    }

    fun verifyJWTToken(): JWTVerifier =
        JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

}
```

# Routes:

## Register User:

> /register

```POST```

```json
{
  "username": "chapolin",
  "password": "colorado"
}
```

Return: ```STATUS 200```

## Login:
> /login

```POST```

```json
{
  "username": "chapolin",
  "password": "colorado"
}

```

Return:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJpby5rdG9yLnNlcnZlci5jb25maWcuSG9jb25BcHBsaWNhdGlvbkNvbmZpZyRIb2NvbkFwcGxpY2F0aW9uQ29uZmlnVmFsdWVANjc5NGFjMGIiLCJpc3MiOiJpby5rdG9yLnNlcnZlci5jb25maWcuSG9jb25BcHBsaWNhdGlvbkNvbmZpZyRIb2NvbkFwcGxpY2F0aW9uQ29uZmlnVmFsdWVANWNiNWJiODgiLCJ1c2VybmFtZSI6ImVkZnNhdSIsImlkIjo0LCJleHBpcmF0aW9uRGF0ZSI6MTY5NjYxNDA5MTg0NSwiZXhwIjoxNjk2NjE0MDkxfQ.VBCV-ShZrEDIgt3LQF1n2UUHimbdQjDrRwteT2h3YFg"
}
```

## Get yourself:

> /me

```GET```

Headers:

```headers
GET /echo/get/json HTTP/1.1
Accept: application/json
Authorization: Bearer <Your token>
```

**OBS:** Every request down here needs a bearer token

## Create notes:
> /notes

```POST```

```json
{
  "note": "fdajlkçssss"
}

```

Return:

```json
{
  "data": "fdajlkçssss",
  "success": true
}
```


## Get one note:
> /notes/{id}

```GET```


Return:

```json
{
  "id": 1,
  "note": "ieaiaiai"
}
```

## Get all notes:
> /notes

```GET```


Return:

```json
[
  {
    "id": 2,
    "note": "fdajlkçssss"
  }
]
```

## Delete one note:
> /notes/{id}

```DELETE```


Return: ```STATUS 204```

## Update one note:
> /notes/{id}

```PUT```

```json
{
  "note": "ieaiaiai"
}
```

Return:

```json
{
  "data": "ieaiaiai",
  "success": true
}
```


