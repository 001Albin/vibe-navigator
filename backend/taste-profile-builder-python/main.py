import json
import redis
import psycopg2
import os  # Import the 'os' module
from kafka import KafkaConsumer

# Kafka Configuration
KAFKA_TOPIC = 'user-interactions-topic'
KAFKA_SERVER = 'localhost:9092'

# Redis Configuration
REDIS_HOST = 'localhost'
REDIS_PORT = 6379

# PostgreSQL Configuration - NOW READ FROM ENVIRONMENT VARIABLES
DB_NAME = os.getenv('DB_NAME', 'vibe_navigator_db')
DB_USER = os.getenv('DB_USER', 'postgres')
DB_PASSWORD = os.getenv('DB_PASSWORD') # Reads the password from the environment
DB_HOST = os.getenv('DB_HOST', 'localhost')
DB_PORT = os.getenv('DB_PORT', '5432')

# Check if the database password is set
if DB_PASSWORD is None:
    print("Error: DB_PASSWORD environment variable not set.")
    exit(1)

def get_media_item_genres(item_id):
    # ... (this function does not need to change) ...
    conn = None
    try:
        conn = psycopg2.connect(
            dbname=DB_NAME, user=DB_USER, password=DB_PASSWORD, host=DB_HOST, port=DB_PORT
        )
        cur = conn.cursor()
        sql_query = "SELECT genres FROM media_items WHERE id = %s"
        cur.execute(sql_query, (item_id,))
        result = cur.fetchone()
        cur.close()
        return result[0] if result else None
    except (Exception, psycopg2.DatabaseError) as error:
        print(f"Error connecting to PostgreSQL: {error}")
        return None
    finally:
        if conn is not None:
            conn.close()

if __name__ == '__main__':
    # ... (the rest of the script remains the same) ...
    try:
        r = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, db=0, decode_responses=True)
        r.ping()
        print("Connected to Redis successfully!")
    except redis.exceptions.ConnectionError as e:
        print(f"Could not connect to Redis: {e}")
        exit(1)

    consumer = KafkaConsumer(KAFKA_TOPIC, bootstrap_servers=KAFKA_SERVER, auto_offset_reset='earliest', group_id='taste-profile-builders')
    print("Python Kafka consumer is running and waiting for messages...")

    for message in consumer:
        message_data = message.value.decode('utf-8')
        interaction_event = json.loads(message_data)
        print(f"Received interaction event: {interaction_event}")
        
        item_id = interaction_event['itemId']
        genres = get_media_item_genres(item_id)
        
        if genres:
            print(f"Fetched genres for item {item_id} from PostgreSQL: {genres}")
        else:
            print(f"Could not fetch genres for item {item_id}.")
            
        user_id = interaction_event['userId']
        interaction_count = r.incr(f"user:{user_id}:interaction_count")
        print(f"User {user_id} now has {interaction_count} interactions. (Updated in Redis)")
