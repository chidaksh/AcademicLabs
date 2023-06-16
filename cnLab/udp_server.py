import socket
from socket import *
port = 10102
serverSocket = socket(AF_INET, SOCK_DGRAM)
serverSocket.bind(('', port))
print("Server connection is established")
while True:
    message, clientaddress = serverSocket.recvfrom(2048)
    modifiedMessage = message.decode().upper()
    serverSocket.sendto(modifiedMessage.encode(), clientaddress)
    break
print("Server is closing")
serverSocket.close()
