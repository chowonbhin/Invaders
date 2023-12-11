CREATE OR REPLACE FUNCTION ranking(difficulty INT)
    RETURNS TABLE(ranking INT, id VARCHAR(255), client_name VARCHAR(255), score INT) AS $$
BEGIN
    RETURN QUERY
        SELECT
                    RANK() OVER (ORDER BY c.high_score[difficulty] DESC) :: int,
                    c.id,
                    c.client_name,
                    c.high_score[difficulty]
        FROM Client as c
        WHERE c.high_score IS NOT NULL AND difficulty <= array_length(c.high_score, 1);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION con_ranking(difficulty INT, userid VARCHAR(255))
    RETURNS TABLE(ranking INT, id VARCHAR(255), client_name VARCHAR(255), score INT, country VARCHAR(255)) AS $$
BEGIN
    RETURN QUERY
        SELECT
                    RANK() OVER (ORDER BY c.high_score[difficulty] DESC) :: int,
                    c.id,
                    c.client_name,
                    c.high_score[difficulty],
                    c.country

        FROM Client as c
        WHERE c.high_score IS NOT NULL AND difficulty <= array_length(c.high_score, 1) AND userid = c.id;
END;
$$ LANGUAGE plpgsql;