package app.turnity.shared.database.querys

const val INSERT_USER = """
            INSERT INTO users (
                id, email, password, google_id, first_name,
                last_name, preferred_name, phone, profile_photo,
                login_method, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now())
            """

const val SELECT_USER_BY_EMAIL = """
            SELECT id, email, password, google_id, first_name,
                   last_name, preferred_name, phone, profile_photo,
                   login_method, created_at, updated_at
            FROM users
            WHERE email = ?
            """

const val SELECT_USER_BY_GOOGLE = """
            SELECT id, email, password, google_id, first_name,
                   last_name, preferred_name, phone, profile_photo,
                   login_method, created_at, updated_at
            FROM users
            WHERE google_id = ?
            """

const val SELECT_USER_BY_ID = """
            SELECT id, email, password, google_id, first_name,
                   last_name, preferred_name, phone, profile_photo,
                   login_method, created_at, updated_at
            FROM users
            WHERE id = ?
            """

const val CREATE_USER_TABLE = """
                CREATE TABLE IF NOT EXISTS users (
                    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                    email text UNIQUE NOT NULL,
                    password text,
                    google_id text,
                    first_name text,
                    last_name text,
                    preferred_name text,
                    phone text,
                    profile_photo text,
                    login_method text,
                    created_at timestamptz DEFAULT now(),
                    updated_at timestamptz DEFAULT now()
                )
                """
