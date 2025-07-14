# 📚 Course Search Application (Spring Boot + Elasticsearch)

This is a backend application that provides **full-text search**, **filtering**, **pagination**, and **sorting** for a list of educational courses stored in Elasticsearch. It’s built with **Spring Boot** and integrates the **Spring Data Elasticsearch** library to interact with the Elasticsearch cluster.

---

## 🚀 Features

- 🔍 Full-text keyword search (title & description)
- 🔠 Fuzzy search (typo-tolerant for keywords)
- 🎯 Filters by:
  - Age range (minAge, maxAge)
  - Price range (minPrice, maxPrice)
  - Category and course type
  - Upcoming session date (`nextSessionDate`)
- ⏫ Sorting by:
  - `price` ascending/descending
  - `nextSessionDate` (default)
- 📄 Pagination
- 📦 Clean REST API

---

## ⚙️ Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Data Elasticsearch
- Elasticsearch 9.0.3 Docker image
- Lombok
- Maven

---

## 🛠️ Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/Kira0911/course-search.git
cd course-search
```

### 2. Start Elasticsearch locally

> Make sure Elasticsearch is running on `http://localhost:9200`

You can use this command in terminal (make sure docker destop is running on your pc, the docker compose file is in the root of project):

```bash
docker-compose up -d
```

---

### 3. Build and run the Spring Boot app

```bash
./mvnw spring-boot:run
```

---

## 📬 API Usage

### Endpoint

```
GET /api/search
```

### Query Parameters

| Param       | Type    | Description                                             |
| ----------- | ------- | ------------------------------------------------------- |
| `q`         | String  | Keyword search on title/description                     |
| `minAge`    | Integer | Minimum age filter                                      |
| `maxAge`    | Integer | Maximum age filter                                      |
| `minPrice`  | Double  | Minimum price filter                                    |
| `maxPrice`  | Double  | Maximum price filter                                    |
| `category`  | String  | Course category filter                                  |
| `type`      | String  | Course type filter (e.g. CLUB, ONE_TIME)                |
| `startDate` | Instant | Filter by upcoming sessions from date                   |
| `sort`      | String  | `priceAsc`, `priceDesc`, or default (`nextSessionDate`) |
| `page`      | Int     | Page number (starts from 0)                             |
| `size`      | Int     | Page size                                               |

---

### Example Request

```http
GET http://localhost:8080/api/search?q=math&minAge=5&maxPrice=150&sort=priceAsc&page=0&size=5
```

### Sample Response

```json
{
  "total": 2,
  "courses": [
    {
      "id": "2",
      "title": "Math Magic",
      "description": "Learn about math magic in a fun way!",
      "category": "Math",
      "type": "CLUB",
      "gradeRange": "1st–3rd",
      "minAge": 8,
      "maxAge": 12,
      "price": 120.0,
      "nextSessionDate": "2025-07-20T10:00:00Z"
    }
  ]
}
```

---

## 📁 Project Structure

```
src/main/java/
├── controller/           → REST API
├── document/             → CourseDocument (Elasticsearch model)
├── service/              → Search logic using Criteria API
├── dto/                  → SearchResponse DTO
├── config/               → DataLoader for indexing courses automatically
```

---

## 📌 Notes

- All course data is indexed under the `courses` index in Elasticsearch.
- The courses are indexed automatically on startup using the DataLoader and CommandLineRunner
- `Instant` dates must follow ISO 8601 format: `2025-07-20T10:00:00Z`.a
  -docker-compose.yml contains needed config for loading ElasticSearch image
- I was busy in my Job so the assignment took a little more than required time to make but i've tried my best to complete it asap !!
  Thanks for your time and have a great day :)

---

## 🧑‍💻 Author

**Harsh Gurjar**  
Backend Developer | Java & MERN Stack | Continuous Learner
🔗 [GitHub @Kira0911](https://github.com/Kira0911)

---

## 📜 License

This project is licensed under some licence :p (no it's just an assignment !)
