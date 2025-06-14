DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS topics;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50),
    password VARCHAR(100) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    is_confirmed BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INTEGER,
    role_id INTEGER,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE IF NOT EXISTS topics (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS questions (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    body VARCHAR(1024) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    is_approved BOOLEAN NOT NULL,
    author_id INTEGER NOT NULL,
    topic_id INTEGER NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id),
    FOREIGN KEY (topic_id) REFERENCES topics(id)
);

CREATE TABLE IF NOT EXISTS answers (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    body VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    question_id INTEGER NOT NULL,
    FOREIGN KEY (question_id) REFERENCES questions(id)
)
