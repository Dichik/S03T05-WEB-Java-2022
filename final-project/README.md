# Repair Agency Project

## Course: Modern technologies for developing web applications on the Java platform


### Endpoints

#### signup

```POST http://localhost:8081/api/auth/signup ```
```
{
    "username": "dichikk",
    "email": "domelian@mail.com",
    "role": "USER",
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

# Assignment #2

## TOPIC: ДОСЛІДЖЕННЯ ШАБЛОНІВ ПРОЕКТУВАННЯ ТА ИНТЕРФЕЙСУ JDBC З DAO

### Мета роботи:
1) Ознайомитися з принципами проектування і шаблонами проектування, набути
   навичок застосування породжуючих і поведінкових шаблонів проектування.
2) Ознайомитися з правилами взаємодії програми на мові Java з базами даних та
   дослідити створення інтерфейсу між програмою и базою даних

### Завдання:
1) Розробити архітектуру системи згідно з варіантом завдання (побудувати за
   архітектурним шаблоном проектування MVC з використанням шаблонів проектування GoF).
2) Розробити та створити БЗ для системи згідно варіанту завдання.
3) Написати програму на мові Java, що реалізує систему за розробленою архітектурою з
   доступом до БД через інтерфейс DAO

### Методичні вказівки

Основну увагу приділити Контролеру та Моделі, тобто бек-ендній частині.
Представлення – консоль – використати тільки для демонстрації функціональності системи у
мінімальному вигляді.


### Варіант 15. Ремонтне агенство
**Існують ролі**: менеджер, майстер, користувач.

- [x] Зареєстрований користувач може створювати заявку на ремонт.
- [x] Також у користувача є рахунок, з якого здійснюється оплата за ремонт.

- #### Менеджер має можливість управляти заявками користувача:
    - [x] призначити майстра;
    - [x] визначити вартість робіт;
    - [x] змінити статус заявки: "чекає на оплату", "сплачено", "відмінено".

- [x] **Менеджер також має можливість поповнити рахунок користувача.**

- [x] **Майстер має можливість вибрати із списку заявку на ремонт та змінити її статус на "в роботі" або "виконано".**

- [x] **Після того, як заявка була виконана, користувач має можливість лишити відгук щодо роботи майстра.**

- **Менеджер повинен мати змогу подивитись звіт зі списком заявок, де він може провести сортування:**
    - [x] по даті заявки;
    - [x] статусу заявки;
    - [x] по вартості.

- **Також менеджер повинен мати змогу фільтрувати замовлення:**
    - [x] за статусом;
    - [x] за майстром, що виконує/виконував замовлення.

# Лабораторна робота #3

## ДОСЛІДЖЕННЯ ВЕБ-ЗАСТОСУНКІВ З ВИКОРИСТАННЯМ СЕРВЛЕТІВ

**Мета роботи**: Ознайомитися з архітектурою веб-застосунку на мові Java за сервлетною
технологією (Servlet API and, JSP), навчитися розробляти веб-застосунки з використанням
фільтрів, сесій.

**Завдання**: Перебудувати архитектуру системи (застосунку на мові Java), що реалізована
у лабораторній роботі #2, у веб-застосунок. Реалізація має містити:

- Аутентифікацію та авторизацію (використовуючи фільтри та/або сесії);
- Транзакції (за потребою);

Додатково завдання 3-ї роботи зробити за фреймворком Spring Boot.

# Лабораторна робота 4

## ДОСЛІДЖЕННЯ ФРЕЙМВОРКІВ МОДУЛЬНОГО ТЕСТУВАННЯ ТА ЛОГУВАННЯ

**Мета роботи**: Ознайомитися з підходами до модульного тестування, вивчити
фреймворки тестування Java-коду **JUnit та Mockito**.
**Завдання**:
Доповнити програму на мові Java, що реалізована у лабораторній роботі 3:

- модульними тестами, які покривають код Моделі;
- логуванням подій та ходу роботи застосунку
- Валідацію даних;
- Обробку помилок (користувач отримує зрозумілий для нього опис ситуації) та
  логування