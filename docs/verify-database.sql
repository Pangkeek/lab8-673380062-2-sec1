-- Run in pgAdmin Query Tool connected to lab8shop.
-- Student: 673380062-2 SEC 1
SELECT c.relname AS table_name,
       CASE k.contype WHEN 'p' THEN 'PRIMARY KEY'
         WHEN 'f' THEN 'FOREIGN KEY' WHEN 'u' THEN 'UNIQUE'
         ELSE 'CHECK' END AS constraint_type,
       pg_get_constraintdef(k.oid) AS definition
FROM pg_class c JOIN pg_namespace n ON n.oid=c.relnamespace
JOIN pg_constraint k ON k.conrelid=c.oid
WHERE n.nspname='public' AND k.contype IN ('p','f','u','c')
AND c.relname IN ('products','product_details','reviews')
ORDER BY c.relname, constraint_type;

SELECT (SELECT count(*) FROM products) AS products,
       (SELECT count(*) FROM product_details) AS product_details,
       (SELECT count(*) FROM reviews) AS reviews;

SELECT p.id, p.name, p.detail_id, d.warranty, r.id AS review_id, r.rating
FROM products p JOIN product_details d ON d.id = p.detail_id
LEFT JOIN reviews r ON r.product_id = p.id
ORDER BY p.id, r.id;
