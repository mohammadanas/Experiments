import socket

# Create a socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind to an IP and Port
server_socket.bind(("0.0.0.0", 8080))  # Listen on all network interfaces

# Listen for clients
server_socket.listen(5)
print("Server listening on port 8080...")

while True:
    client_socket, addr = server_socket.accept()  # Accept a client
    print(f"Connected by {addr}")

    # Receive and send data
    data = client_socket.recv(1024).decode()
    print(f"Received: {data}")
    client_socket.send("Yes baby!".encode())

    client_socket.close()
