/* 
  DB2025Team03 데이터베이스 및 사용자 생성 스크립트 
  root 계정으로 실행해야 함 
*/

DROP DATABASE IF EXISTS DB2025Team03;
DROP USER IF EXISTS DB2025Team03@localhost;

CREATE USER DB2025Team03@localhost IDENTIFIED WITH caching_sha2_password BY 'DB2025Team03';
CREATE DATABASE DB2025Team03;

GRANT ALL PRIVILEGES ON DB2025Team03.* TO DB2025Team03@localhost WITH GRANT OPTION;
COMMIT;

USE DB2025Team03;

# -----------------------------
# 테이블 생성
# -----------------------------

/* 사용자 테이블 */
CREATE TABLE DB2025_User (
    user_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    birth_year INT NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK (gender IN ('남', '여')),
    email VARCHAR(100) UNIQUE
);

/* 반려동물 테이블 */
CREATE TABLE DB2025_Pet (
    pet_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    age INT,
    species VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES DB2025_User(user_id)
);

/* 시설 테이블 */
CREATE TABLE DB2025_Facility (
    facility_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    opening_hours VARCHAR(50)
);

/* 예약 테이블 */
CREATE TABLE DB2025_Reservation (
    reservation_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    facility_id INT NOT NULL,
    date DATE NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES DB2025_User(user_id),
    FOREIGN KEY (facility_id) REFERENCES DB2025_Facility(facility_id)
);

/* 리뷰 테이블 */
CREATE TABLE DB2025_Review (
    review_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    facility_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    date DATE,
    FOREIGN KEY (user_id) REFERENCES DB2025_User(user_id),
    FOREIGN KEY (facility_id) REFERENCES DB2025_Facility(facility_id)
);

/* 즐겨찾기 테이블 */
CREATE TABLE DB2025_Favorite (
    user_id INT NOT NULL,
    facility_id INT NOT NULL,
    PRIMARY KEY (user_id, facility_id),
    FOREIGN KEY (user_id) REFERENCES DB2025_User(user_id),
    FOREIGN KEY (facility_id) REFERENCES DB2025_Facility(facility_id)
);

# -----------------------------
# 더미 데이터 삽입
# -----------------------------

INSERT INTO DB2025_User VALUES
(1, '홍길동', 1990, '남', 'hong@example.com'),
(2, '김영희', 1985, '여', 'kim@example.com'),
(3, '이철수', 2000, '남', 'lee@example.com'),
(4, '박민수', 1995, '남', 'park@example.com'),
(5, '최지은', 1988, '여', 'choi@example.com');

INSERT INTO DB2025_Pet VALUES
(1, 1, '초코', 3, '강아지'),
(2, 1, '밤이', 2, '고양이'),
(3, 2, '루비', 4, '강아지'),
(4, 3, '하늘', 1, '고양이'),
(5, 4, '보리', 5, '강아지');

/*
데이터 출처:
- 동물병원: 공공데이터포털(data.go.kr) > 서울특별시 동물병원 현황
- 미용실, 펫호텔, 놀이터, 카페: 반려생활, 카카오맵, 서울열린데이터광장, 네이버지도, 블로그 참고
- 수집일 기준: 2025년 5월 11일, 서대문구 내 영업 중 시설만 선별
*/
INSERT INTO DB2025_Facility (facility_id, name, address, category, opening_hours) VALUES
(101, '가좌동물병원', '서울특별시 서대문구 응암로 77 (북가좌동)', '병원', '09:00-18:00'),
(102, '백년동물병원', '서울특별시 서대문구 가좌로 104 (홍은동)', '병원', '09:00-18:00'),
(103, '한솔동물병원', '서울특별시 서대문구 통일로 397, B428호 (홍제동)', '병원', '09:00-18:00'),
(104, '신영동물병원', '서울특별시 서대문구 통일로 489 (홍은동)', '병원', '09:00-18:00'),
(105, '라파엘종합동물병원', '서울특별시 서대문구 연희로 132 (연희동)', '병원', '09:00-18:00'),

(201, '그루밍호찌', '서울 서대문구 연희로 227 1층 101호', '미용실', ''),
(202, '그루미송', '서울 서대문구 증가로29길 12-8', '미용실', ''),
(203, '헬로펫살롱', '서울 서대문구 북가좌동', '미용실', '10:00-19:00'),
(204, '이쁘냥 고양이미용실', '서울 서대문구 충정로9길 10-7', '미용실', '10:00-20:00'),
(205, '언니네', '서울 서대문구 세검정로4가길 4-19 1층', '미용실', '10:00-20:00'),
(206, '펫티즈', '서울 서대문구 연희로22길 15-3 1층', '미용실', ''),
(207, '뽀시', '서울 서대문구 연희로22길 15-3 1층', '미용실', ''),
(208, '쥬쥬펫', '서울 서대문구 증가로13-9 2층', '미용실', '10:00-21:00'),
(209, '연희도그', '서울 서대문구 연희동', '미용실', ''),
(210, '애견미용샵 예쁜이', '서울 서대문구 홍제동', '미용실', ''),

(301, '펫티즈', '서울 서대문구 연희로22길 15-3 1층', '호텔', ''),
(302, '쥬쥬펫', '서울 서대문구 증가로13-9 2층', '호텔', '10:00-21:00'),
(303, '내품애센터', '서울 서대문구 모래내로 333', '보호소', '09:00-21:00'),
(304, '연희도그', '서울 서대문구 연희동', '호텔', ''),
(305, '애견호텔 해피하우스', '서울 서대문구 북가좌동', '호텔', ''),
(306, '도그하우스', '서울 서대문구 홍제동', '호텔', ''),
(307, '퍼피하우스', '서울 서대문구 연희동', '호텔', ''),
(308, '펫하우스', '서울 서대문구 남가좌동', '호텔', ''),
(309, '러블리펫', '서울 서대문구 홍은동', '호텔', ''),
(310, '펫빌리지', '서울 서대문구 충정로3가', '호텔', ''),

(401, '안산 반려견 놀이터', '서울 서대문구 안산자락길', '놀이터', ''),
(402, '내품애센터 옥상 놀이터', '서울 서대문구 모래내로 333', '놀이터', '09:00-21:00'),
(403, '영천동 반려견 놀이터', '서울 서대문구 영천동 5-644', '놀이터', ''),
(404, '북가좌동 반려견 놀이터', '서울 서대문구 북가좌동', '놀이터', ''),
(405, '홍제천 반려견 놀이터', '서울 서대문구 홍제천로', '놀이터', ''),
(406, '연희동 반려견 놀이터', '서울 서대문구 연희동', '놀이터', ''),
(407, '남가좌동 반려견 놀이터', '서울 서대문구 남가좌동', '놀이터', ''),
(408, '홍은동 반려견 놀이터', '서울 서대문구 홍은동', '놀이터', ''),
(409, '충정로 반려견 놀이터', '서울 서대문구 충정로3가', '놀이터', ''),
(410, '연희로 반려견 놀이터', '서울 서대문구 연희로', '놀이터', ''),

(501, '해피앤피스커피클럽', '서울 서대문구 연희동', '카페', ''),
(502, '카페곳', '서울 서대문구 홍제동 279-5', '카페', ''),
(503, '행복감', '서울 서대문구 연희동 192-28', '카페', '12:00-20:00'),
(504, '보틀팩토리', '서울 서대문구 홍연길 26 1층', '카페', ''),
(505, '컬러드 빈', '서울 서대문구 연희동 129-6 신관', '카페', ''),
(506, '랩트', '서울 서대문구 연희동 126-9 1층', '카페', ''),
(507, '런어웨이', '서울 서대문구 홍제동 334-107 1층', '카페', ''),
(508, '무지개', '서울 서대문구 홍은동 404-1', '카페', ''),
(509, '스웨이커피스테이션', '서울 서대문구 연희동 126-1', '카페', '11:00-22:00'),
(510, '에스크투데이', '서울 서대문구 연희동 191-4 2층', '카페', '12:00-21:00');

INSERT INTO DB2025_Reservation VALUES
(1, 1, 101, '2025-05-10', '진료'),
(2, 2, 102, '2025-05-11', '미용'),
(3, 3, 103, '2025-05-11', '음료'),
(4, 1, 105, '2025-05-12', '숙박'),
(5, 5, 101, '2025-05-13', '진료');

INSERT INTO DB2025_Review VALUES
(1, 1, 101, 5, '좋았어요', '2025-05-10'),
(2, 2, 102, 4, '괜찮아요', '2025-05-11'),
(3, 3, 103, 3, '보통이에요', '2025-05-11'),
(4, 4, 104, 5, '최고예요', '2025-05-12'),
(5, 5, 105, 2, '별로예요', '2025-05-13');

INSERT INTO DB2025_Favorite VALUES
(1, 101),
(2, 102),
(3, 103),
(4, 104),
(5, 105);

/* --------------------------------------------
  1. VIEW 정의 (요구사항 4, 8)
-------------------------------------------- */

/* 평균 평점이 4 이상인 시설 목록 */
CREATE VIEW DB2025_View_TopRatedFacility AS
SELECT F.facility_id, F.name, F.category, ROUND(AVG(R.rating), 2) AS avg_rating
FROM DB2025_Review R
JOIN DB2025_Facility F ON R.facility_id = F.facility_id
GROUP BY F.facility_id, F.name, F.category
HAVING AVG(R.rating) >= 4;

/* 사용자별 예약 + 리뷰 활동 요약 */
CREATE VIEW DB2025_View_UserActivity AS
SELECT U.user_id, U.name,
       COUNT(DISTINCT R.reservation_id) AS total_reservations,
       COUNT(DISTINCT V.review_id) AS total_reviews
FROM DB2025_User U
LEFT JOIN DB2025_Reservation R ON U.user_id = R.user_id
LEFT JOIN DB2025_Review V ON U.user_id = V.user_id
GROUP BY U.user_id, U.name;

/* --------------------------------------------
  2. INDEX 생성 (요구사항 6, 7)
-------------------------------------------- */

CREATE INDEX idx_facility_category ON DB2025_Facility(category);
CREATE INDEX idx_facility_address ON DB2025_Facility(address);
CREATE INDEX idx_review_rating ON DB2025_Review(rating);
CREATE INDEX idx_reservation_date ON DB2025_Reservation(date);

/* 인덱스를 활용하는 예시 쿼리 */
/* 시설 카테고리별 검색 */
SELECT * FROM DB2025_Facility WHERE category = '미용실';

/* 리뷰 평점 4 이상 */
SELECT * FROM DB2025_Review WHERE rating >= 4;

/* --------------------------------------------
  3. 트랜잭션 처리 (요구사항 9)
-------------------------------------------- */

START TRANSACTION;

/* 예약과 동시에 즐겨찾기 추가 */
INSERT INTO DB2025_Reservation (reservation_id, user_id, facility_id, date, service_type)
VALUES (6, 2, 301, '2025-05-20', '호텔');

INSERT INTO DB2025_Favorite (user_id, facility_id)
VALUES (2, 301);

COMMIT;

/* 오류 발생 시 ROLLBACK 가능 */
/* ROLLBACK; */

/* --------------------------------------------
  4. 서브쿼리 & 조인 쿼리 (요구사항 10, 11)
-------------------------------------------- */

/* 서브쿼리: 평균 평점이 4 이상인 시설 조회 */
SELECT * FROM DB2025_Facility
WHERE facility_id IN (
    SELECT facility_id
    FROM DB2025_Review
    GROUP BY facility_id
    HAVING AVG(rating) >= 4
);

/* 조인: 유저가 남긴 리뷰와 시설명 함께 출력 */
SELECT U.name AS user_name, F.name AS facility_name, R.rating, R.content
FROM DB2025_Review R
JOIN DB2025_User U ON R.user_id = U.user_id
JOIN DB2025_Facility F ON R.facility_id = F.facility_id;

/* --------------------------------------------
  5. 동적 쿼리 예시 (요구사항 12)
-------------------------------------------- */

/* Java에서 사용될 입력 기반 SELECT 쿼리 예시 */
/* 사용자 입력: 지역명 '연희동', 카테고리 '카페' */
SELECT *
FROM DB2025_Facility
WHERE address LIKE '%연희동%'
  AND category = '카페';

/* --------------------------------------------
  6. CRUD 인터페이스용 쿼리 예시 (요구사항 14~17)
-------------------------------------------- */

/* INSERT 예시: 신규 예약 등록 */
INSERT INTO DB2025_Reservation (reservation_id, user_id, facility_id, date, service_type)
VALUES (7, 3, 502, '2025-05-22', '카페 이용');

/* UPDATE 예시: 리뷰 내용 수정 */
UPDATE DB2025_Review
SET content = '시설도 좋고 친절했어요!'
WHERE review_id = 2;

/* DELETE 예시: 즐겨찾기 삭제 */
DELETE FROM DB2025_Favorite
WHERE user_id = 2 AND facility_id = 102;

/* SELECT 예시: 사용자별 예약 내역 확인 */
SELECT R.reservation_id, F.name AS facility_name, R.date, R.service_type
FROM DB2025_Reservation R
JOIN DB2025_Facility F ON R.facility_id = F.facility_id
WHERE R.user_id = 1;

ALTER TABLE DB2025_Review MODIFY review_id INT AUTO_INCREMENT;
# ALTER TABLE DB2025_Reservation MODIFY reservation_id INT AUTO_INCREMENT;	/* 예약 ID 자동 증가 */


/* 예약 기능용 수정 */
CREATE TABLE DB2025_Slot (
    slot_id INT NOT NULL,
    facility_id INT NOT NULL,
    time_range VARCHAR(50),
    is_reserved BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (slot_id, facility_id),
    FOREIGN KEY (facility_id) REFERENCES DB2025_Facility(facility_id)
);

    
INSERT INTO DB2025_Slot (slot_id, facility_id, time_range) VALUES
(1, 101, '09:00~10:00'),
(2, 101, '10:00~11:00'),
(3, 101, '11:00~12:00'),
(4, 101, '13:00~14:00'),
(5, 101, '14:00~15:00');

ALTER TABLE DB2025_Reservation
ADD COLUMN slot_id INT,
ADD FOREIGN KEY (slot_id, facility_id) REFERENCES DB2025_Slot(slot_id, facility_id);

