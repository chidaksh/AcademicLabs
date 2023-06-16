import socket
import threading
import json

class Manager:
    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.active_peers = {}
        self.lock = threading.Lock()
        self.update = False
        # self.files = {}

    def start(self):
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.bind((self.ip, self.port))
        self.socket.listen()
        print(f"Manager is listening on {self.ip} via port number {self.port}")
        while True:
            conn, addr = self.socket.accept()
            t = threading.Thread(
                target=self.handle_connection, args=(conn, addr))
            t.daemon = True
            t.start()
            t.join()

    def handle_connection(self, conn, addr):
        data = conn.recv(1024)
        if data:
            request = json.loads(data.decode())
            if request["task"] == "add":
                self.add_peer(request['addr'], request['port'])
                # self.update = True
                print(f"Added peer {request['addr'],request['port']} to active peers")
            elif request['task'] == "remove":
                self.remove_peer(request['addr'], request['port'])
                # self.update = True
                print(
                    f"Removed peer {request['addr'],request['port']} from active peers as they went offline")
            elif request['task'] == "read":
                conn.sendall(self.get_active_peers().encode())
            elif request['task'] == "update":
                # print("Updated in manager")
                conn.sendall(self.get_active_peers().encode())
                # self.update = False
        conn.close()

    def add_peer(self, add, port):
        with self.lock:
            if (add,port) not in list(self.active_peers.keys()):
                self.active_peers[(add, port)] = port

    def remove_peer(self, add, port):
        with self.lock:
            if (add,port) in list(self.active_peers.keys()):
                del self.active_peers[(add, port)]
            # print(f"Removed peer: {add}:{port}")

    def get_active_peers(self):
        with self.lock:
            return json.dumps({"peers": list(self.active_peers.keys())})

if __name__ == "__main__":
    port_num = 10101
    manager = Manager("127.0.0.1", port_num)
    try:
        manager.start()
    except KeyboardInterrupt:
        manager.socket.close()
