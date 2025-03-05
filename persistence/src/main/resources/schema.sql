--User
CREATE TABLE IF NOT EXISTS users (
    userId                          SERIAL PRIMARY KEY,
    email                           VARCHAR(255) UNIQUE NOT NULL,
    username                        VARCHAR(30) UNIQUE NOT NULL,
    password                        TEXT NOT NULL,
    role                            INTEGER NOT NULL

);

--MoovieLists
CREATE TABLE IF NOT EXISTS moovieLists(
    moovieListId                        SERIAL PRIMARY KEY,
    userId                              INTEGER NOT NULL,
    name                                VARCHAR(255) NOT NULL,
    description                         TEXT,
    type                                INTEGER NOT NULL,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
    UNIQUE(userId,name)
);

--Media
CREATE TABLE IF NOT EXISTS media(
    mediaId                        SERIAL PRIMARY KEY,
    type                           BOOLEAN NOT NULL,
    name                           VARCHAR(255) NOT NULL,
    originalLanguage               VARCHAR(2),
    adult                          BOOLEAN NOT NULL,
    releaseDate                    DATE,
    overview                       TEXT NOT NULL,
    backdropPath                   VARCHAR(255),
    posterPath                     VARCHAR(255),
    trailerLink                    VARCHAR(255),
    tmdbRating                     FLOAT NOT NULL,
    status                         VARCHAR(20) NOT NULL
);

--MoovieListsContent
CREATE TABLE IF NOT EXISTS moovieListsContent(
    moovieListId                        INTEGER NOT NULL,
    mediaId                            INTEGER NOT NULL,
    customOrder                         INTEGER NOT NULL,
    id SERIAL PRIMARY KEY,
    FOREIGN KEY(moovieListId) REFERENCES moovieLists(moovieListId) ON DELETE CASCADE,
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--Reviews
CREATE TABLE IF NOT EXISTS reviews(
    reviewId                                    SERIAL PRIMARY KEY,
    userId                                      INTEGER NOT NULL,
    mediaId                                     INTEGER NOT NULL,
    rating                                      INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 5),
    reviewContent                               TEXT,
    lastModified                                TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE,
    UNIQUE(userId,mediaId)
);

--Movies
CREATE TABLE IF NOT EXISTS movies(
    mediaId                         INTEGER NOT NULL,
    runtime                         INTEGER,
    budget                          BIGINT,
    revenue                         BIGINT,
    directorId                      INTEGER,
    director                        VARCHAR(255),
    UNIQUE(mediaId),
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--TV
CREATE TABLE IF NOT EXISTS tv(
    mediaId                        INTEGER NOT NULL,
    lastAirDate                    DATE,
    nextEpisodeToAir               DATE,
    numberOfEpisodes               INTEGER,
    numberOfSeasons                INTEGER,
    UNIQUE(mediaId),
    FOREIGN KEY(mediaId) REFERENCES media(mediaId) ON DELETE CASCADE
);

--Genres
CREATE TABLE IF NOT EXISTS genres(
    genreid                 INTEGER NOT NULL ,
    genrename               VARCHAR(100) NOT NULL,
    PRIMARY KEY(genreid)
);


--MediaGenres
CREATE TABLE IF NOT EXISTS mediagenres(
    mediaId                    INTEGER NOT NULL,
    genreId                    INTEGER NOT NULL,
    PRIMARY KEY(mediaId,genreId),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE,
    FOREIGN KEY(genreid)       REFERENCES genres(genreId) ON DELETE CASCADE
);


--Actors
CREATE TABLE IF NOT EXISTS actors(
    actorId                 INTEGER NOT NULL,
    actorName               VARCHAR(100) NOT NULL,
    profilePath             VARCHAR(255),
    PRIMARY KEY (actorid)
);

--MediaActors
CREATE TABLE IF NOT EXISTS mediaactors (
    mediaid   INTEGER NOT NULL,
    actorid INTEGER NOT NULL,
    charactername VARCHAR(100),
    PRIMARY KEY(mediaid, actorid),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE,
    FOREIGN KEY(actorid)    REFERENCES actors(actorid) ON DELETE CASCADE
);

--Creators
CREATE TABLE IF NOT EXISTS creators(
    creatorId                          INTEGER NOT NULL,
    creatorName                        VARCHAR(100) NOT NULL,
    PRIMARY KEY (creatorid)
);

CREATE TABLE IF NOT EXISTS mediacreators (
    mediaid   INTEGER NOT NULL,
    creatorid INTEGER NOT NULL,
    PRIMARY KEY(mediaid, creatorid),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE,
    FOREIGN KEY(creatorid)    REFERENCES creators(creatorid) ON DELETE CASCADE
);

--Providers
CREATE TABLE IF NOT EXISTS providers(
    providerId                     INTEGER NOT NULL,
    providerName                   VARCHAR(100) NOT NULL,
    logoPath                       VARCHAR(100) NOT NULL,
    PRIMARY KEY(providerid)
);

--MediaProviders
CREATE TABLE IF NOT EXISTS mediaproviders (
    mediaid   INTEGER NOT NULL,
    providerid INTEGER NOT NULL,
    PRIMARY KEY(mediaid, providerid),
    FOREIGN KEY(mediaId)       REFERENCES media(mediaId) ON DELETE CASCADE,
    FOREIGN KEY(providerid)    REFERENCES providers(providerid) ON DELETE CASCADE
);

--MODIFICATIONS FOR SPRINT 2

--MoovieListsLikes
CREATE TABLE IF NOT EXISTS moovieListsLikes(
    moovieListId                        INTEGER NOT NULL,
    userId                              INTEGER NOT NULL,
    UNIQUE(moovieListId,userId),
    FOREIGN KEY(moovieListId) REFERENCES moovieLists(moovieListId) ON DELETE CASCADE,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);

--UserImages
CREATE TABLE IF NOT EXISTS userImages(
    image                               BYTEA NOT NULL,
    userId                              INTEGER NOT NULL,
    UNIQUE(userId),
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);

--Verification Tokens
CREATE TABLE IF NOT EXISTS verificationTokens(
   userId INT NOT NULL,
   token TEXT NOT NULL,
   expirationDate DATE NOT NULL,
   FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);

/*
--Modifications in table created before for Sprint 2
--1
ALTER TABLE users
    ADD username VARCHAR(30),
    ADD password TEXT,
    ADD role INTEGER;
--2
UPDATE users
SET username = users.email,
    password = 'default_password',
    role = 0
WHERE username IS NULL;
--3
ALTER TABLE users
    ALTER COLUMN username SET NOT NULL,
    ADD CONSTRAINT unique_username UNIQUE (username);
--4
ALTER TABLE users
    ALTER COLUMN username SET NOT NULL,
    ALTER COLUMN password SET NOT NULL,
    ALTER COLUMN role SET NOT NULL;


--Modificaciones a las MoovieLists

ALTER TABLE moovieLists ADD COLUMN type INTEGER;
UPDATE moovieLists SET type = 1;
ALTER TABLE moovieLists ALTER COLUMN type SET NOT NULL;


--Modifications a Reviews (ratings)
UPDATE reviews SET rating=rating/2
ALTER TABLE reviews DROP CONSTRAINT reviews_rating_check;
ALTER TABLE reviews ADD CONSTRAINT check_rating_range CHECK (rating BETWEEN 1 AND 5);
 */


--MODIFICATIONS FOR SPRINT 3

--ReviewsLikes
CREATE TABLE IF NOT EXISTS reviewsLikes (
    reviewId 		    INTEGER NOT NULL,
    userId			    INTEGER NOT NULL,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(reviewId) REFERENCES reviews(reviewId) ON DELETE CASCADE,
    UNIQUE(reviewId, userId)
);


--BannedMessage
CREATE TABLE IF NOT EXISTS  bannedMessage (
    modUserId	        INTEGER NOT NULL,
    bannedUserId        INTEGER NOT NULL,
    message             TEXT,
    FOREIGN KEY(modUserId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(bannedUserId) REFERENCES users(userId) ON DELETE CASCADE,
    UNIQUE(bannedUserId)
);


--MoovieListsFollows
CREATE TABLE IF NOT EXISTS moovieListsFollows(
    moovieListId                        INTEGER NOT NULL,
    userId                              INTEGER NOT NULL,
    UNIQUE(moovieListId,userId),
    FOREIGN KEY(moovieListId) REFERENCES moovieLists(moovieListId) ON DELETE CASCADE,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE
);

/*
--Modifications in table created before for Sprint 2

Hay que modificar el reviews que tiene el like ahi, pero en realidad es un count en reviewsLikes

--Reviews change
ALTER TABLE reviews DROP COLUMN reviewlikes;

--MoovieLists changes
ALTER TABLE moovielistscontent DROP COLUMN status;

ALTER TABLE moovieListsContent ADD COLUMN customOrder INTEGER;

--Following function is to set an custom order for the moovielistcontent before setting the column to not null and for it to have a default value;
--START FUNCTION
CREATE OR REPLACE FUNCTION initcustomorder() RETURNS VOID AS $$
DECLARE
    idx int;
    med int;
    ord int;
BEGIN
    ord := 1;

    -- Loop for moovielistid
    FOR idx IN (SELECT moovielistid FROM moovielists) LOOP
        -- Loop through the content media with id = idx
        FOR med IN (SELECT mediaid FROM moovielistscontent WHERE moovielistid = idx) LOOP
            UPDATE moovielistscontent SET customorder = ord WHERE moovielistid = idx AND mediaid = med;
            ord := ord + 1;
        END LOOP;
		ord := 1;
    END LOOP;
END;
$$LANGUAGE plpgsql;
SELECT initcustomorder();
--END FUNCTION

ALTER TABLE moovieListsContent ALTER COLUMN customOrder SET NOT NULL;
ALTER TABLE moovieListsContent ADD CONSTRAINT unique_moovieList_media UNIQUE (moovieListId, mediaId)

ALTER TABLE media DROP COLUMN totalrating;
ALTER TABLE media DROP COLUMN votecount;


 */

--MODIFICATIONS FOR SPRINT 5

--MoovieListsReviews
CREATE TABLE IF NOT EXISTS moovieListsReviews(
    moovieListReviewId                          SERIAL PRIMARY KEY,
    userId                                      INTEGER NOT NULL,
    moovieListId                                INTEGER NOT NULL,
    reviewContent                               TEXT,
    lastModified                                TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY(userId) REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(moovieListId) REFERENCES moovieLists(moovieListId) ON DELETE CASCADE,
    UNIQUE(userId,moovieListId)
);


--MoovieListsReviewsLikes
CREATE TABLE IF NOT EXISTS moovieListsReviewsLikes (
    moovieListReviewId 	                        INTEGER NOT NULL,
    userId			                            INTEGER NOT NULL,
    FOREIGN KEY(userId)                         REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(moovieListReviewId)             REFERENCES moovieListsReviews(moovieListReviewId) ON DELETE CASCADE,
    UNIQUE(moovieListReviewId, userId)
);

--ReviewComments
-- CREATE TABLE IF NOT EXISTS reviewComments(
--     reviewId    INTEGER NOT NULL,
--     commentId      INTEGER NOT NULL,
--     FOREIGN KEY(commentId)     REFERENCES comments(commentId) ON DELETE CASCADE,
--     FOREIGN KEY(reviewId)   REFERENCES reviews(reviewId) ON DELETE CASCADE,
--     UNIQUE(reviewId, commentId)
-- );

--Comments
CREATE TABLE IF NOT EXISTS comments(
    commentId   SERIAL PRIMARY KEY,
    reviewId    INTEGER NOT NULL,
    userId      INTEGER NOT NULL,
    content     TEXT,

    FOREIGN KEY(reviewId)   REFERENCES reviews(reviewId) ON DELETE CASCADE,
    FOREIGN KEY(userId)     REFERENCES users(userId) ON DELETE CASCADE
);

--CommentLikes
CREATE TABLE IF NOT EXISTS commentLikes(
    commentId   INTEGER NOT NULL,
    userId      INTEGER NOT NULL,

    FOREIGN KEY(userId)     REFERENCES users(userId) ON DELETE CASCADE,
    FOREIGN KEY(commentId)  REFERENCES comments(commentId) ON DELETE CASCADE,
    UNIQUE(userId, commentId)
);

--CommentDislikes
CREATE TABLE IF NOT EXISTS commentDislikes(
       commentId   INTEGER NOT NULL,
       userId      INTEGER NOT NULL,

       FOREIGN KEY(userId)     REFERENCES users(userId) ON DELETE CASCADE,
       FOREIGN KEY(commentId)  REFERENCES comments(commentId) ON DELETE CASCADE,
       UNIQUE(userId, commentId)
);

/*
 --MODIFICATIONS FOR SPRINT 5
 */

--REPORTS

--ReportsComments
CREATE TABLE IF NOT EXISTS reportsComments (
         reportId           SERIAL PRIMARY KEY,
         type               INTEGER NOT NULL,
         content            TEXT,
         reportedBy         INTEGER NOT NULL,
         commentId          INTEGER NOT NULL,
         report_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
         resolved           BOOLEAN DEFAULT FALSE,
         FOREIGN KEY(reportedBy)     REFERENCES users(userId) ON DELETE CASCADE,
         FOREIGN KEY(commentId)     REFERENCES comments(commentId) ON DELETE CASCADE
);

--ReportsReviews
CREATE TABLE IF NOT EXISTS reportsReviews (
         reportId           SERIAL PRIMARY KEY,
         type               INTEGER NOT NULL,
         content            TEXT,
         reportedBy         INTEGER NOT NULL,
         reviewId           INTEGER NOT NULL,
         report_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
         resolved           BOOLEAN DEFAULT FALSE,
         FOREIGN KEY(reportedBy)     REFERENCES users(userId) ON DELETE CASCADE,
         FOREIGN KEY(reviewId)     REFERENCES reviews(reviewId) ON DELETE CASCADE
);

--ReportsMoovieListReviews
CREATE TABLE IF NOT EXISTS reportsMoovieListReviews (
         reportId           SERIAL PRIMARY KEY,
         type               INTEGER NOT NULL,
         content            TEXT,
         reportedBy         INTEGER NOT NULL,
         moovieListReviewId        INTEGER NOT NULL,
         report_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
         resolved           BOOLEAN DEFAULT FALSE,
         FOREIGN KEY(reportedBy)     REFERENCES users(userId) ON DELETE CASCADE,
         FOREIGN KEY(moovieListReviewId)     REFERENCES moovieListsReviews(moovieListReviewId) ON DELETE CASCADE
);

--ReportsMoovieLists
CREATE TABLE IF NOT EXISTS reportsMoovieLists (
          reportId           SERIAL PRIMARY KEY,
          type               INTEGER NOT NULL,
          content            TEXT,
          reportedBy         INTEGER NOT NULL,
          moovieListId       INTEGER NOT NULL,
          report_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          resolved           BOOLEAN DEFAULT FALSE,
          FOREIGN KEY(reportedBy)     REFERENCES users(userId) ON DELETE CASCADE,
          FOREIGN KEY(moovieListId)     REFERENCES moovieLists(moovieListId) ON DELETE CASCADE
);

/*
-- ALTER TABLES FOR OLD SCHEMA
-- For reportsComments
ALTER TABLE reportsComments
    ALTER COLUMN type TYPE INTEGER USING type::INTEGER,
    ALTER COLUMN type SET NOT NULL;

-- For reportsMoovieListReviews
ALTER TABLE reportsMoovieListReviews
    ALTER COLUMN type TYPE INTEGER USING type::INTEGER,
    ALTER COLUMN type SET NOT NULL;

-- For reportsMoovieLists
ALTER TABLE reportsMoovieLists
    ALTER COLUMN type TYPE INTEGER USING type::INTEGER,
    ALTER COLUMN type SET NOT NULL;

-- For reportsReviews
ALTER TABLE reportsReviews
    ALTER COLUMN type TYPE INTEGER USING type::INTEGER,
    ALTER COLUMN type SET NOT NULL;
*/


/*
ALTER TABLE moovielistscontent DROP CONSTRAINT moovielistscontent_moovielistid_mediaid_key;
ALTER TABLE moovielistscontent ADD COLUMN id SERIAL PRIMARY KEY;
UPDATE moovielistscontent mlc SET id = DEFAULT;
ALTER TABLE actors DROP COLUMN charactername;

ALTER TABLE bannedmessage DROP CONSTRAINT "bannedmessage_pkey";
ALTER TABLE bannedmessage ADD PRIMARY KEY (banneduserid);

ALTER TABLE reviews ADD COLUMN lastModified TIMESTAMP;
UPDATE reviews SET lastModified = NULL;
ALTER TABLE moovieListsReviews ADD COLUMN lastModified TIMESTAMP;
UPDATE moovieListsReviews SET lastModified = NULL;

 */
