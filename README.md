# **♻️멋사마켓♻️**

멋사마켓은 당근마켓, 중고나라 등을 착안하여 만든 중고 제품 거래 플랫폼입니다.

## 🖥️ 프로젝트 소개

사용자가 중고 물품을 자유롭게 올리고, 댓글을 통해 소통하며, 최종적으로 구매 제안에 대하여 수락할 수 있는 형태의 중고 제품 거래 플랫폼을 구현한 프로젝트입니다.

## 📅 개발 기간

23.06.28 ~ 23.07.05

## ⚙️ 개발 환경

- `Java 17`
- `JDK 17`
- **IDE** : IntelliJ IDEA
- **Project** : Gradle - Groovy
- **Language** : Java 17
- **Spring Boot** : 3.1.1
- **Dependency :** Spring Web, Spring Boot DevTools, Lombok, Thymeleaf, Validation, Spring Data JPA.
- **Database :** SQLite

## 📌 멋사마켓 ERD

<img width="50%" src="https://github.com/likelion-backend-5th/MiniProject_Basic_YuDaeon/assets/124870889/2e17c9bd-49e3-4201-a5ff-64ae4e2f3596"/>


## 🚧 **중고 물품 관리** 구현 기능
**📌 중고 물품 관리 요구사항**

```
1. 누구든지 중고 거래를 목적으로 물품에 대한 정보를 등록할 수 있다.
    1. 이때 반드시 포함되어야 하는 내용은 제목, 설명, 최소 가격, 작성자이다.
    2. 또한 사용자가 물품을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
    3. 최초로 물품이 등록될 때, 중고 물품의 상태는 판매중 상태가 된다.
2. 등록된 물품 정보는 누구든지 열람할 수 있다.
    1. 페이지 단위 조회가 가능하다.
    2. 전체 조회, 단일 조회 모두 가능하다.
3. 등록된 물품 정보는 수정이 가능하다.
    1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
4. 등록된 물품 정보에 이미지를 첨부할 수 있다.
    1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.
    2. 이미지를 관리하는 방법은 자율이다.
5. 등록된 물품 정보는 삭제가 가능하다.
    1. 이때, 물품이 등록될 때 추가한 비밀번호를 첨부해야 한다.

```

## 🌱 중고 물품 관리 기능 API

- 1️⃣ **물품 등록 `POST /items`**
    - 물품, 상품 설명, 최소가격, 작성자, 비밀번호 작성시 등록 가능 (등록이 안되었다면 Bad Request 반환)
    - 등록 후 물품 상태 “판매중”으로 반환
    
    **💻 Request Body**
    
    ```
    {
        "title": "자전거 팝니다.",
        "description": "접이식 자전거입니다.",
        "minPriceWanted": 100000,
        "writer": "daeon",
        "password": "daeon1234"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "등록이 완료되었습니다."
    }
    
    ```
    
- 2️⃣ **물품 페이지 단위 조회 `GET /Items`**
    - 모든 페이지 상품 열람 가능
    
    **💻 ResponseBody**
    
    ```
    {
        "content": [
            {
                "id": 1,
                "title": "자전거 팝니다.",
                "description": "접이식 자전거입니다.",
                "imageUrl": null,
                "minPriceWanted": 100000,
                "status": "판매중",
                "writer": "daeon",
                "password": "daeon1234"
            }
        ],
        "pageable": {
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 25,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 1,
        "first": true,
        "size": 25,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "numberOfElements": 1,
        "empty": false
    }
    
    ```
    
- 3️⃣ **물품 단일 조회 `GET /items/{itemId}`**
    - 상품의 Id로 단일 물품 조회 가능 `http://localhost:8080/items/1`
    
    **💻 ResponseBody**
    
    ```
    {
        "id": 1,
        "title": "자전거 팝니다.",
        "description": "접이식 자전거입니다.",
        "imageUrl": null,
        "minPriceWanted": 100000,
        "status": "판매중",
        "writer": "daeon",
        "password": "daeon1234"
    }
    
    ```
    
- 4️⃣ **물품 수정 `PUT /items/{itemId}`**
    - 비밀번호 일치 시 물품 수정 가능
    - 비밀번호 틀렸을 시 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "title": "자전거 팝니다.",
        "description": "접이식 자전거 가격인하합니다.",
        "minPriceWanted": 80000,
        "writer": "daeon",
        "password": "daeon1234"
    }
    
    ```
    

    **💻 ResponseBody**
    
    ```
    {
        "message": "물품이 수정되었습니다."
    }
    
    ```

- 5️⃣ **물품 이미지 등록 `PUT /items/{itemId}/image`**
    - 물품 등록시 추가한 비밀번호 일치 시 물품 이미지 등록 가능
    - 비밀번호 틀렸을 시 Bad Request 반환
    
    **💻 Request Body**
    
    - ![스크린샷 2023-07-05 오후 4 28 34](https://github.com/likelion-backend-5th/MiniProject_Basic_YuDaeon/assets/124870889/d9fe5c69-d867-4f97-b7ad-ded5e413a129)

    
    **💻 Response Body**
    
    - 이미지 등록하면 경로에 파일 생성
    - <img width="1624" alt="스크린샷 2023-07-05 오전 10 13 56" src="https://github.com/likelion-backend-5th/MiniProject_Basic_YuDaeon/assets/124870889/41996fc0-c6e7-4ea8-9bae-e2e358422da3">
- 6️⃣ **물품 삭제 `DELETE /items/{itemId}`**
    - 비밀번호 일치하는 경우 물품 삭제
    - 비밀번호 일치하지 않는 경우 Bad Request “비밀번호를 틀렸습니다.” 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "daeon",
        "password": "daeon1234"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "물품을 삭제했습니다."
    }
    
    ```
    

## 🚧 **중고 물품 댓글 기능**

**📌 중고 물품 댓글 기능 요구사항**

```
1. 등록된 물품에 대한 질문을 위하여 댓글을 등록할 수 있다.
    1. 이때 반드시 포함되어야 하는 내용은 대상 물품, 댓글 내용, 작성자이다.
    2. 또한 댓글을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
2. 등록된 댓글은 누구든지 열람할 수 있다.
    1. 페이지 단위 조회가 가능하다.
3. 등록된 댓글은 수정이 가능하다.
    1. 이때, 댓글이 등록될 때 추가한 비밀번호를 첨부해야 한다.
4. 등록된 댓글은 삭제가 가능하다.
    1. 이때, 댓글이 등록될 때 추가한 비밀번호를 첨부해야 한다.
5. 댓글에는 초기에 비워져 있는 답글 항목이 존재한다.
    1. 만약 댓글이 등록된 대상 물품을 등록한 사람일 경우, 물품을 등록할 때 사용한 비밀번호를 첨부할 경우 답글 항목을 수정할 수 있다.
    2. 답글은 댓글에 포함된 공개 정보이다.

```

## 🌱 중고 물품 댓글 API

- **1️⃣ 댓글 등록 `POST /items/{itemId}/comments`**
    - 대상 물품, 댓글 내용, 작성자, 비밀번호 작성시 등록 가능
    - 입력이 제대로 안된다면 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "lion",
        "password": "lion5",
        "content": "할인 가능하신가요?"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "댓글이 등록되었습니다."
    }
    
    ```
    
- **2️⃣ 댓글 전체 조회 `GET /items/{itemId}/comments`**
    - 모든 댓글 조회 가능
    - `http://localhost:8080/items/1/comments`
    
    **💻 ResponseBody**
    
    ```
    "content": [
            {
                "id": 1,
                "content": "할인 가능하신가요? 50000 정도면 고려 가능합니다",
                "reply": "안됩니다"
            },
            {
                "id": 2,
                "content": "할인해주세요",
                "reply": null
            }
    ]
    
    ```
    
- 3️⃣ **댓글 단위 조회 `GET /items/{itemId}/comments/{commentsId]`**
    - 모든 댓글 페이지 단위 조회 가능
    - `http://localhost:8080/items/1/comments/1`
    
    **💻 ResponseBody**
    
    ```
    {
        "item_id": 1,
        "writer": "lion",
        "password": "lion5",
        "content": "할인 가능하신가요?",
        "reply": null
    }
    
    ```
    
- **4️⃣ 댓글 수정 `PUT /items/{itemId}/comments/{commentId}`**
    - 비밀번호 일치 시 수정 가능
    - 비밀번호 일치하지 않을 시 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "lion",
        "password": "lion5",
        "content": "할인 가능하신가요? 50000 정도면 고려 가능합니다"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "댓글이 수정되었습니다."
    }
    
    ```
    
- **5️⃣ 답글 등록 `PUT /items/{itemId}/comments/{commentId}/reply`**
    - 등록된 댓글에 비밀번호 작성시 답글 등록 및 수정 가능
    - 비밀번호 일치하지 않을 시 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "daeon",
        "password": "daeon1234",
        "reply": "안됩니다"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "댓글에 답변이 추가되었습니다."
    }
    
    ```
    
- **6️⃣ 댓글 삭제 `DELETE /items/{itemId}/comments/{commentId}`**
    - 비밀번호 일치 시 삭제 가능
    - 비밀번호 일치하지 않을 시 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "yde",
        "password": "1234"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "댓글을 삭제했습니다."
    }
    
    ```
    

## 🚧 **구매 제안 기능**

**📌 구매 제안 요구사항**
    
```
1. 등록된 물품에 대하여 구매 제안을 등록할 수 있다.
    1. 이때 반드시 포함되어야 하는 내용은 대상 물품, 제안 가격, 작성자이다.
    2. 또한 구매 제안을 등록할 때, 비밀번호 항목을 추가해서 등록한다.
    3. 구매 제안이 등록될 때, 제안의 상태는 제안 상태가 된다.
2. 구매 제안은 대상 물품의 주인과 등록한 사용자만 조회할 수 있다.
    1. 대상 물품의 주인은, 대상 물품을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다. 이때 물품에 등록된 모든 구매 제안이 확인 가능하다. 페이지 기능을 지원한다.
    2. 등록한 사용자는, 조회를 위해서 자신이 사용한 작성자와 비밀번호를 첨부해야 한다. 이때 자신이 등록한 구매 제안만 확인이 가능하다. 페이지 기능을 지원한다.
3. 등록된 제안은 수정이 가능하다.
    1. 이때, 제안이 등록될때 추가한 작성자와 비밀번호를 첨부해야 한다.
4. 등록된 제안은 삭제가 가능하다.
    1. 이때, 제안이 등록될때 추가한 작성자와 비밀번호를 첨부해야 한다.
5. 대상 물품의 주인은 구매 제안을 수락할 수 있다.
    1. 이를 위해서 제안의 대상 물품을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다.
    2. 이때 구매 제안의 상태는 수락이 된다.
6. 대상 물품의 주인은 구매 제안을 거절할 수 있다.
    1. 이를 위해서 제안의 대상 물품을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다.
    2. 이때 구매 제안의 상태는 거절이 된다.
7. 구매 제안을 등록한 사용자는, 자신이 등록한 제안이 수락 상태일 경우, 구매 확정을 할 수 있다.
    1. 이를 위해서 제안을 등록할 때 사용한 작성자와 비밀번호를 첨부해야 한다.
    2. 이때 구매 제안의 상태는 확정 상태가 된다.
    3. 구매 제안이 확정될 경우, 대상 물품의 상태는 판매 완료가 된다.
    4. 구매 제안이 확정될 경우, 확정되지 않은 다른 구매 제안의 상태는 모두 거절이 된다.

```

## 🌱 구매 제안 기능 API

- **1️⃣ 제안 등록 `POST /items/{itemId}/proposals`**
    - 물품, 제안 가격, 작성자, 비밀번호 입력
    - 입력이 안되었다면 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "carot",
        "password": "carotcarot",
        "suggestedPrice": 70000
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "구매 제안이 등록되었습니다."
    }
    
    ```
    
- 2️⃣ **제안 조회 `http://localhost:8080/items/1/proposals?writer=daeon&password=daeon1234&page=1`**
    - 페이지 단위 조회 가능
    - 대상 물품 주인의 경우, 작성자와 비밀번호 첨부 시 모든 구매 제안 확인 가능
    - 작성자 및 비밀번호 불일치 시 Bad Request 반환
    
    **💻 Request Body**
    
    - ![스크린샷 2023-07-05 오후 5 06 15](https://github.com/likelion-backend-5th/MiniProject_Basic_YuDaeon/assets/124870889/b32a92da-b491-4428-b510-0a7c5741a099)
    - 제안 등록 사용자의 경우, 작성자와 비밀번호 첨부 필요. 자신이 제안한 제안만 확인 가능(페이지 기능 지원)
    - 작성자 및 비밀번호 불일치 시 Bad Request 반환
    
    **💻 Request Body**
    
    - ![스크린샷 2023-07-05 오후 5 04 36](https://github.com/likelion-backend-5th/MiniProject_Basic_YuDaeon/assets/124870889/cbf13640-bfaf-402b-8158-e8bb2eee32a6)
- **3️⃣ 제안 수정 `PUT /items/{itemId}/proposals/{proposalId}`**
    - 작성자와 비밀번호가 일치하는 경우 수정 가능
    - 작성자와 비밀번호가 일치하지 않는 경우 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "carot",
        "password": "carotcarot",
        "suggestedPrice": 80000
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "제안이 수정되었습니다."
    }
    
    ```
    
- **4️⃣ 제안 삭제 `DELETE /items/{itemId}/proposals/{proposalId}`**
    - 작성자와 비밀번호가 일치하는 경우 삭제
    - 작성자와 비밀번호가 일치하지 않는 경우 Not Found 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "hoho",
        "password": "ho"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "제안을 삭제했습니다."
    }
    
    ```
    
- **5️⃣ 제안 수락 `PUT /items/{itemId}/proposals/{proposalId}`**
    - 작성자와 비밀번호가 일치하는 경우 - 수락 / 거절 가능
    - 작성자와 비밀번호가 일치하지 않는 경우 Bad Request 반환
    
    수락의 경우 `http://localhost:8080/items/1/proposals/1`
    
    **💻 Request Body**
    
    ```
    {
        "writer": "daeon",
        "password": "daeon1234",
        "status": "수락"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "제안의 상태가 변경되었습니다."
    }
    
    ```
    
    거절의 경우  `http://localhost:8080/items/1/proposals/2`
    
    **💻 Request Body**
    
    ```
    {
        "writer": "daeon",
        "password": "daeon1234",
        "status": "거절"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "제안의 상태가 변경되었습니다."
    }
    
    ```
    
- 6️⃣ **제안 확정 `PUT /items/{itemId}/proposals/{proposalId}`**
    - 제안 상태가 “수락”일 경우에만 확정 가능
    - 제안의 상태가 거절일 경우에 확정 할 시 Bad Request 반환
    - 작성자와 비밀번호가 일치하지 않는 경우 Bad Request 반환
    
    **💻 Request Body**
    
    ```
    {
        "writer": "carot",
        "password": "carotcarot",
        "status": "확정"
    }
    
    ```
    
    **💻 ResponseBody**
    
    ```
    {
        "message": "구매가 확정되었습니다."
    }
    
    ```
    

### 📁 PostMan - Json 파일 첨부

[mutsamarket.postman_collection.json.zip](https://github.com/likelion-backend-5th/MiniProject_Basic_YuDaeon/files/11956272/mutsamarket.postman_collection.json.zip)
