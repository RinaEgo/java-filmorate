# java-filmorate
Template repository for Filmorate project.
![filmorate_db](https://github.com/RinaEgo/java-filmorate/assets/119045429/8c130516-c0bf-4d45-a9d3-11357ce66edf)

Примеры запросов к БД:
1. Получить фильм по идентификатору (n): 
      `SELECT * 
      FROM FILM
      WHERE id = n;`

2. Получить 10 самых популярных фильмов:
     `SELECT f.*
      FROM (
          SELECT film_id,
                 COUNT(user_id) AS likes_count
          FROM likes
          GROUP BY film_id
          ) AS l
      RIGHT JOIN FILM f ON f.id = l.film_id
      ORDER BY likes_count DESC 
      LIMIT 10;`
     
3. Получить жанры фильма с id = n:
     `SELECT g.name AS genre
      FROM GENRE g
      LEFT JOIN film_genre fg ON fg.genre_id = g.genre_id
      WHERE fg.film_id = n;`
      
 4. Получить список подтвержденных друзей пользователя с id = n:
      `SELECT u.*
      FROM friends f
      JOIN USER u ON u.id = f.user_id
      WHERE f.user_id = n
            AND f.confirmed;`
