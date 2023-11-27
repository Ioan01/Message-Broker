# Use an official RabbitMQ image
FROM rabbitmq:3-management

# Expose the RabbitMQ management port
EXPOSE 15672

# Expose the RabbitMQ port
EXPOSE 5672
