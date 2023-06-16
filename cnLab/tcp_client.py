import socket
from socket import *
hostName = gethostname()
port = 12000
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((hostName, port))
message = input('Input a lowercase sentence: ')
clientSocket.send(message.encode())
modifiedMessage = clientSocket.recv(2048)
print(modifiedMessage.decode())
clientSocket.close()
