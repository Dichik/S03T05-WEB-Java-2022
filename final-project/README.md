# Repair Agency Project

## Course: Modern technologies for developing web applications on the Java platform


### Endpoints

#### signup

```POST http://localhost:8081/api/auth/signup ```
```
{
    "username": "dichikk",
    "email": "domelian@mail.com",
    "role": ["USER"],
    "password": "password"
}
```

#### signin

```POST POST http://localhost:8081/api/auth/signin```
```
{
    "username": "dichikk",
    "password": "password"
}
```