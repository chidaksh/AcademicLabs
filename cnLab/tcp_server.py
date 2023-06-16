import socket
from socket import *
port = 12000
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', port))
serverSocket.listen(1)
print("Server is ready and listening")
while True:
    connectionServer, address = serverSocket.accept()
    sentence = connectionServer.recv(2048)
    modifiedSentence = sentence.decode().upper()
    connectionServer.send(modifiedSentence.encode())
    connectionServer.close()
    print("tcp connection closed")
    break

print("server is closing")
serverSocket.close()
