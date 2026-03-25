#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    set -a
    source .env
    set +a
else
    # Use default development values
    export LOCAL_PORT=8080
    export DATABASE_URL="jdbc:postgresql://localhost:5432/turnity"
    export DATABASE_USER="postgres"
    export DATABASE_PASSWORD="postgres"
    export JWT_SECRET="my_super_secret_key_for_jwt_12345"
    export JWT_ISSUER="turnity-api"
    export JWT_AUDIENCE="turnity-web"
    export JWT_REALM="turnity"
    export JWT_EXPIRATION_MINUTES=60
    export GOOGLE_CLIENT_ID="dummy_client_id"
    export GOOGLE_CLIENT_SECRET="dummy_client_secret"
    export GOOGLE_CALLBACK_URL="http://localhost:8080/auth/google/callback"
    
    echo "No .env file found. Using default development values."
    echo "Create .env file based on .env.example to customize settings."
fi

echo "Starting Turnity API..."
./gradlew run
