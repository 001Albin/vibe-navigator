import json
from kafka import KafkaConsumer

# Configuration
KAFKA_TOPIC = 'user-interactions-topic'
KAFKA_SERVER = 'localhost:9092'

if __name__ == '__main__':
    # Create a Kafka consumer
    consumer = KafkaConsumer(
        KAFKA_TOPIC,
        bootstrap_servers=KAFKA_SERVER,
        auto_offset_reset='earliest', # Start reading at the earliest message if the consumer is new
        group_id='taste-profile-builders' # A group name for your consumers
    )

    print("Python Kafka consumer is running and waiting for messages...")

    # An infinite loop that waits for new messages from the Kafka topic
    for message in consumer:
        # Messages from Kafka are in bytes, so we decode them to a string
        message_data = message.value.decode('utf-8')
        
        # We expect the message to be a JSON string, so we parse it
        interaction_event = json.loads(message_data)
        
        print(f"Received interaction event: {interaction_event}")
        
        # --- FUTURE STEP ---
        # Here is where you would add the logic to:
        # 1. Fetch the user's interaction history from PostgreSQL.
        # 2. Use pandas and scikit-learn to re-calculate their taste profile vector.
        # 3. Save the new vector to Redis.
