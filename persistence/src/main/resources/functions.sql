
--FUNCTIONS THAT MODIFIES THE ORDER OF THE CUSTOM ORDER IN THE ML CONTENT TABLE

CREATE OR REPLACE FUNCTION
--Integer arrays are all mediaid
updatecustomOrder(mlid INTEGER , firstPosition INTEGER, pageSize INTEGER, toPrev INTEGER[], currentPage INTEGER[], toNext INTEGER[])
RETURNS VOID AS $$
DECLARE
    currentPos INTEGER;
    toMove	   INTEGER;
BEGIN
--Correct the positions in the currentPage, iteration begins in currentPos
--leaving space for the ones in the previous page that will come in this one

    IF ARRAY_LENGTH(toPrev, 1) > 0 THEN
        currentPos := firstPosition + ARRAY_LENGTH(toPrev, 1);
    ELSE
        currentPos := firstPosition;
    END IF;

    IF ARRAY_LENGTH(currentPage, 1) > 0 THEN
        FOR i IN 0..ARRAY_LENGTH(currentPage, 1) LOOP
                UPDATE moovielistscontent SET customorder = currentPos WHERE mediaid = currentPage[i] AND moovielistid = mlid;
                currentPos := currentPos + 1;
            END LOOP;
    END IF;


--Check if there are medias to the page back and also check if its not the first page
    IF ARRAY_LENGTH(toPrev, 1) > 0  THEN
        currentPos := firstPosition  - ARRAY_LENGTH(toPrev, 1);
        --Update positions of the page before and move the ones in the prev page to the current
        FOR i IN 1..ARRAY_LENGTH(toPrev, 1) LOOP
                UPDATE moovielistscontent SET customorder = currentPos + ARRAY_LENGTH(toPrev, 1)   WHERE customorder = currentPos AND moovielistid = mlid;
                UPDATE moovielistscontent SET customorder = currentPos    WHERE mediaid = toPrev[i-1] AND moovielistid = mlid;
                currentPos := currentPos + 1;
            END LOOP;
    END IF;


-- Check if there media to be moved in the next page
    IF ARRAY_LENGTH(toNext, 1) > 0 THEN
        currentPos := firstPosition;
        IF ARRAY_LENGTH(toPrev, 1) > 0 THEN
            currentPos := currentPos + ARRAY_LENGTH(toPrev, 1);
        END IF;
        IF ARRAY_LENGTH(currentPage, 1) > 0 THEN
            currentPos := currentPos + ARRAY_LENGTH(currentPage, 1);
        END IF;

        toMove = (SELECT COUNT(*) FROM moovieListsContent WHERE moovieListId = mlid AND (customorder > (firstPosition + pagesize)) ) ;

        FOR i IN 0..(ARRAY_LENGTH(toNext, 1)-1) LOOP
                IF ( i <= toMove ) THEN
                    UPDATE moovielistscontent SET customorder = currentPos WHERE customorder = firstPosition + pageSize + i  AND moovielistid = mlid;
                    UPDATE moovielistscontent SET customorder = firstPosition + pageSize  + i   WHERE mediaid = toNext[i] AND moovielistid = mlid;
                ELSE
                    UPDATE moovielistscontent SET customorder = currentPos WHERE mediaid = toNext[i] AND moovielistid = mlid;
                END IF;
                currentPos := currentPos + 1;
            END LOOP;
    END IF;
END;
$$ LANGUAGE plpgsql;