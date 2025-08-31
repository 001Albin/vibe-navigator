import json
import redis
import psycopg2
import os
import numpy as np
from kafka import KafkaConsumer

# ... (All configuration variables remain the same) ...
KAFKA_TOPIC = 'user-interactions-topic'
KAFKA_SERVER = 'localhost:9092'
REDIS_HOST = 'localhost'
REDIS_PORT = 6379
DB_NAME = os.getenv('DB_NAME', 'vibe_navigator_db')
DB_USER = os.getenv('DB_USER', 'postgres')
DB_PASSWORD = os.getenv('DB_PASSWORD')
DB_HOST = os.getenv('DB_HOST', 'localhost')
DB_PORT = os.getenv('DB_PORT', '5432')

ALL_GENRES = [
    'Action', 'Adventure', 'Animation', 'Comedy', 'Crime', 'Documentary', 'Drama', 
    'Family', 'Fantasy', 'History', 'Horror', 'Music', 'Mystery', 'Romance', 
    'Science Fiction', 'TV Movie', 'Thriller', 'War', 'Western'
]

# --- (get_media_item_genres and vectorize_genres functions remain the same) ---
# Replace the old function with this new one in main.py

def get_media_item_genres(item_id):
    """Connects to PostgreSQL and fetches all genres for a given media_item id."""
    conn = None
    try:
        # Establish a connection to the database
        conn = psycopg2.connect(
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD,
            host=DB_HOST,
            port=DB_PORT
        )
        # Create a cursor
        cur = conn.cursor()
        
        # CORRECTED SQL QUERY: Select from the helper table
        sql_query = "SELECT genre FROM media_item_genres WHERE media_item_id = %s"
        
        # Execute the query
        cur.execute(sql_query, (item_id,))
        
        # Fetch ALL results, as a movie can have multiple genres
        results = cur.fetchall()
        
        # Close the cursor
        cur.close()
        
        # Convert the list of tuples [('Action',), ('Drama',)] into a simple list ['Action', 'Drama']
        return [row[0] for row in results] if results else None
            
    except (Exception, psycopg2.DatabaseError) as error:
        print(f"Error connecting to PostgreSQL: {error}")
        return None
    finally:
        if conn is not None:
            conn.close()

def vectorize_genres(genres, all_genres_list):
    # ... no change here ...
    vector = np.zeros(len(all_genres_list))
    for genre in genres:
        if genre in all_genres_list:
            index = all_genres_list.index(genre)
            vector[index] = 1
    return vector

def update_taste_profile(redis_client, user_id, item_vector, interaction_type):
    """Fetches, updates, and saves the user's taste profile vector in Redis."""
    redis_key = f"user:{user_id}:taste_profile"
    
    # Try to get the existing profile from Redis
    existing_profile_str = redis_client.get(redis_key)
    
    if existing_profile_str is None:
        # If no profile exists, the new profile is just the item vector
        new_profile_vector = item_vector
    else:
        # If a profile exists, convert it back to a numpy array
        existing_profile_vector = np.array(json.loads(existing_profile_str))
        
        # For now, we'll use a simple weighted average.
        # Let's say we have a history of 5 interactions
        # This is a simplification; a real system would track the count.
        interaction_count = 5 
        
        if interaction_type == 'LIKE':
            # Add the new vector to the old one and re-average
            new_profile_vector = (existing_profile_vector * interaction_count + item_vector) / (interaction_count + 1)
        else: # For DISLIKE, you might subtract, but we'll keep it simple for now
            new_profile_vector = existing_profile_vector

    # Save the new profile back to Redis (convert numpy array to a JSON string)
    redis_client.set(redis_key, json.dumps(new_profile_vector.tolist()))
    
    return new_profile_vector

if __name__ == '__main__':
    # ... (Redis, Kafka, DB connection checks remain the same) ...
    if DB_PASSWORD is None:
        print("Error: DB_PASSWORD environment variable not set.")
        exit(1)
        
    r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=0, decode_responses=True)
    r.ping()
    print("Connected to Redis successfully!")

    consumer = KafkaConsumer(KAFKA_TOPIC, bootstrap_servers=KAFKA_SERVER, auto_offset_reset='earliest', group_id='taste-profile-builders')
    print("Python Kafka consumer is running and waiting for messages...")

    for message in consumer:
        message_data = message.value.decode('utf-8')
        interaction_event = json.loads(message_data)
        
        print(f"\n--- New Event Received ---")
        print(f"Received interaction event: {interaction_event}")
        
        item_id = interaction_event['itemId']
        user_id = interaction_event['userId']
        interaction_type = interaction_event['interactionType']
        
        # 1. Get genres from PostgreSQL
        genres = get_media_item_genres(item_id)
        if genres:
            print(f"Fetched genres for item {item_id} from PostgreSQL: {genres}")
        else:
            print(f"Could not fetch genres for item {item_id}.")
            continue
            
        # 2. Vectorize the item's genres
        item_vector = vectorize_genres(genres, ALL_GENRES)
        print(f"Item vector: {item_vector}")
        
        # 3. Update the user's taste profile in Redis
        updated_profile = update_taste_profile(r, user_id, item_vector, interaction_type)
        print(f"Updated user {user_id} taste profile in Redis.")
        print(f"New profile vector: {updated_profile}")
