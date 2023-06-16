import socket
from socket import *
hostName = gethostname()
port = 10102
clientSocket = socket(AF_INET, SOCK_DGRAM)
message = input('Input a lowercase sentence: ')
clientSocket.sendto(message.encode(), (hostName, port))
modifiedMessage, serveraddress = clientSocket.recvfrom(2048)
modifiedMessage = modifiedMessage.decode()
print(modifiedMessage,serveraddress)
clientSocket.close()