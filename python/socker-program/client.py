import socket

# Create a socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to server (replace 'localhost' with server IP if needed)
client_socket.connect(("localhost", 8080))

# Send data
client_socket.send("Hello, Jannuwaa!".encode())

# Receive response
response = client_socket.recv(1024).decode()
print(f"Server says: {response}")

client_socket.close()  # Close connection

