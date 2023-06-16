import socket
import os
import threading
import time
import json
import sys
import random

MANAGER_IP = "127.0.0.1"
MANAGER_PORT = 10101
BUFFER_SIZE = 1024


class Peer:
    def __init__(self, name, peer_ip="127.0.0.1", peer_listeningport=10102):
        self.name = name
        self.active_peers = {}
        self.shareable_files = None
        self.ip = MANAGER_IP
        self.port = MANAGER_PORT
        self.myip = peer_ip
        self.listening_port = peer_listeningport
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # print(socket.gethostname())
        self.server_socket.bind((peer_ip, self.listening_port))
        self.server_socket.listen(1)
        self.server_socket.settimeout(3)
        self.available = True
        self.stop = False
        self.sendinglock = threading.Lock()
        self.listeninglock = threading.Lock()
        self.listeningsemaphore = threading.Semaphore(1)
        self.receivingsemaphore = threading.Semaphore(1)
        # self.managersemaphore = threading.Semaphore(1)
        self.downloaded_files = []
        self.currfile = None
        self.canDownload = False
        self.sendpeers = False
        self.numpeers = 0
        self.downfrompeers = {}
        self.start = 0
        self.last = 0
        self.fraglen = 0
        self.file_size = sys.maxsize
        self.lastFrag = False
        self.flag = 0
        self.fileFlag = self.listening_port

    def add_peer(self):
        # Connect to manager and request list of active peers
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((MANAGER_IP, MANAGER_PORT))
            req = json.dumps(
                {"task": "add", "port": self.listening_port, "addr": self.myip})
            s.sendall(req.encode())
            # data = s.recv(BUFFER_SIZE)
            # with self.sendinglock:
            #     self.active_peers = data.decode().split(",")
        # print(self.active_peers)
        return

    def read_peers(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((MANAGER_IP, MANAGER_PORT))
            req = json.dumps({"task": "read", "port": self.listening_port})
            s.send(req.encode())
            data = s.recv(BUFFER_SIZE)
            if data:
                request = json.loads(data.decode())
                if "peers" in request:
                    updated_peers = {(addr, port): port for addr,
                                     port in request["peers"]}
                    self.active_peers.update(updated_peers)
        print(f"Active peers are {self.active_peers}")
        return

    def get_shareable_files(self, files):
        self.shareable_files = files

    def get_ToBeRequested_files(self, files):
        self.requested_files = files

    def send_file_fragment(self, conn, file_path, start_byte, end_byte):
        # Send file fragment to requesting peer
        with open(file_path, "rb") as f:
            f.seek(int(start_byte))
            data = list(f.read(int(end_byte) - int(start_byte)))
            reqs = json.dumps({"data": data})
        return reqs

    def ForEachFile(self, file_name):
        if file_name is None:
            return
        ls = []
        ls = list(self.active_peers.keys())
        for (peer, port) in ls:
            if port != self.fileFlag and port!=self.listening_port:
                with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                    try:
                        # print("Connecting")
                        s.settimeout(3.0)
                        s.connect((peer, port))
                        req = json.dumps(
                            {"task": "calculate_numpeers", "filename": file_name})
                        s.send(req.encode())
                        # print("request sent")
                        # s.send(f"fetch_file:{file_name}".encode())
                        try:
                            data = s.recv(BUFFER_SIZE)
                        except Exception as e:
                            print(
                                f"Error while receiving data: {e}")
                            self.flag += 1
                            self.stop = True
                            continue
                        if data:
                            request = json.loads(data.decode())
                            # print(port,request)
                            if "filestatus" in request:
                                if request['filestatus'] == True:
                                    self.numpeers += 1
                                    self.downfrompeers[(
                                        peer, port)] = port
                                    continue
                                else:
                                    self.fileFlag = port
                                    continue
                        else:
                            print("Didn't receive any data")
                            continue
                    except Exception as e:
                        # print(request)
                        print(f"Error connecting to {peer} while handshaking: {e}")
                        self.flag += 1
                        self.stop = True
                        continue
        return

    def fetch_file_fragments(self):
        # Broadcast file request to all active peers
        # print("fragments")
        while True:
            # print(self.requested_files)
            # time.sleep(1)
            self.listeninglock.acquire()
            if self.flag >= 5:
                self.flag = 0
                time.sleep(random.randint(1, 5))
            self.listeninglock.release()
            if len(self.requested_files) == 0:
                print("All requested files are received")
                return
            for filename in self.requested_files:
                # print("starting semp")
                self.listeninglock.acquire()
                self.receivingsemaphore.acquire()
                self.listeningsemaphore.acquire()
                # self.managersemaphore.acquire()
                fragments = []
                downloaded_fragments = []
                downloaded_bytes = 0
                if self.available:
                    self.available = False
                    self.currfile = filename
                    perf = threading.Thread(target=self.ForEachFile, args=(filename,))
                    perf.daemon = True
                    perf.start()
                    perf.join()
                    self.fileFlag = self.listening_port
                    # print(self.downfrompeers.keys())
                    # print(f"Done with calculating numpeers {self.numpeers}")
                    if self.numpeers != 0:
                        self.file_size = os.path.getsize(self.currfile)
                        self.fraglen = self.file_size/self.numpeers
                    else:
                        self.stop = True
                    for (peer, port) in list(self.downfrompeers.keys()):
                        if self.stop == True or self.flag >=5:
                            self.flag = 0
                            break
                        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                            try:
                                s.settimeout(3.0)
                                # print('entered here')
                                s.connect((peer, port))
                                # print("connect")
                                if self.lastFrag:
                                    req = json.dumps(
                                        {"task": "fetch_file", "filename": self.currfile, "start": self.last, "last": self.file_size})
                                    s.send(req.encode())
                                else:
                                    req = json.dumps(
                                        {"task": "fetch_file", "filename": self.currfile, "start": self.last, "last": self.last+self.fraglen})
                                    s.send(req.encode())
                                # print("request sent")
                                # s.send(f"fetch_file:{file_name}".encode())
                                try:
                                    data = s.recv(BUFFER_SIZE)
                                except Exception as e:
                                    print(
                                        f"Error while receiving data in second for loop: {e}")
                                    self.stop = True
                                    break
                                if data:
                                    request = json.loads(data.decode())
                                    if "available" in request:
                                        # print(request['available'])
                                        if request["available"] == False:
                                            print(
                                                f"Peer is Not available at {port}")
                                            continue
                                        else:
                                            data = s.recv(BUFFER_SIZE)
                                            if data:
                                                request = json.loads(
                                                    data.decode())
                                                if "msg" in request:
                                                    print(request['msg'])
                                                    continue
                                                elif "file_size" in request:
                                                    # print("hi")
                                                    # print(request["data"])
                                                    # file_size = request["file_size"]
                                                    self.last += self.fraglen
                                                    if self.last + self.fraglen > self.file_size:
                                                        self.lastFrag = True
                                                    fragments = request['data']
                                                    downloaded_fragments.extend(
                                                        fragments)
                                                    downloaded_bytes += len(
                                                        bytes(fragments))
                                                    # self.canDownload = True
                                    else:
                                        print(
                                            f"Unexpected response from {port}")
                                        continue
                                else:
                                    print(f"No response from {port}")
                                    self.flag += 1
                                    continue
                            except Exception as e:
                                print(f"Error connecting to {peer}: {e}")
                                self.flag += 1
                                continue
                    if downloaded_bytes >= self.file_size:
                        bytes_data = bytes(downloaded_fragments)
                        with open("peer1.txt", "wb") as f:
                            # print(downloaded_fragments)
                            f.write(bytes_data)
                            print(
                                f"File {self.currfile} is successfully received by {self.name}")
                        self.downloaded_files.append(self.currfile)
                        if self.currfile not in self.shareable_files:
                            self.shareable_files.append(self.currfile)
                        if self.currfile in self.requested_files:
                            self.requested_files.remove(self.currfile)
                self.available = True
                self.stop = False
                self.lastFrag = False
                # self.managersemaphore.release()
                self.listeningsemaphore.release()
                self.receivingsemaphore.release()
                self.listeninglock.release()
                # return

    def handle_request(self, conn, addr):
        # Handle incoming requests from other peers
        try:
            data = conn.recv(BUFFER_SIZE)
            if data:
                request = json.loads(data.decode())
                # print(f"Connected to peer")
                if request["task"] == "fetch_file":
                    file_name = request["filename"]
                    # print("filename: ", file_name)
                    file_path = ""
                    for shareable_file in self.shareable_files:
                        if file_name in shareable_file:
                            file_path = shareable_file
                            break
                    if file_path:
                        # file_size = os.path.getsize(file_path)
                        start_byte = request['start']
                        end_byte = request['last']
                        req = json.dumps(
                            {"file_size": end_byte-start_byte, "data": None})
                        reqs = self.send_file_fragment(
                            conn, file_path, start_byte, end_byte)
                        req_dict = json.loads(req)
                        req_dict['data'] = None
                        reqs_dict = json.loads(reqs)
                        req_dict['data'] = reqs_dict['data']
                        req = json.dumps(req_dict)
                        # print("response ready")
                        conn.send(req.encode())
                        # print(f"Response: {req}")
                        # print("response sent")
                    else:
                        req = json.dumps({"msg": "file is not available"})
                        conn.send(req.encode())
                elif request["task"] == "fetch_fragment":
                    file_name = request["filename"]
                    file_path = ""
                    for shareable_file in self.shareable_files:
                        if file_name in shareable_file:
                            file_path = shareable_file
                            break
                    if file_path:
                        req = json.dumps(
                            {"file_size": {start_byte}-{end_byte}, "data": ""})
                        reqs = self.send_file_fragment(
                            conn, file_path, request["start"], request["end"])
                        req_dict = json.loads(req)
                        req_dict['data'] = None
                        reqs_dict = json.loads(reqs)
                        req_dict['data'] = reqs_dict['data']
                        req = json.dumps(req_dict)
                        conn.send(req.encode())
                else:
                    req = json.dumps({"msg": "Received unknown request"})
                    conn.send(req.encode())
                    # print(f" from {request['port']}:{self.listening_port}")
        except Exception as e:
            print(f"Exception {e} was raised")
            return
        print("Request Handled")

    def inform_manager(self):
        # Inform manager that peer is going offline
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((MANAGER_IP, MANAGER_PORT))
            req = json.dumps(
                {"task": "remove", "port": self.listening_port, "addr": self.myip})
            s.send(req.encode())
        return

    def close(self):
        # Inform manager before going offline
        self.inform_manager()
        # Close server socket
        print(f"\n{self.name} offline")
        self.server_socket.close()
        # self.socket.close()
        return

    def listen_requests(self, conn, addr):
        # Start listening for incoming requests
        # print(f"{self.name} started listening on port {self.port}")
        try:
            self.available = False
            self.handle_request(conn, addr)
        except KeyboardInterrupt:
            self.available = True
            self.close()
        return

    def inform_availability(self, conn):
        # Send availability message to requesting peer
        # peer_thread = threading.Thread(target=peer.start_listening)
        # print("informing")
        # print("lock entered")
        # peer_thread.daemon = True
        # peer_thread.start()
        req = json.dumps({"available": self.available})
        conn.send(req.encode())
        # print(f"connection send {req}")

    def firstReply(self, conn, addr):
        if self.available:
            print(f"Other peers are heard from this port")
            if self.sendpeers == False:
                data = conn.recv(BUFFER_SIZE)
                if data:
                    request = json.loads(data.decode())
                    if "task" in request:
                        if request['task'] == "calculate_numpeers":
                            if request['filename'] in self.shareable_files:
                                req = json.dumps({"filestatus": True})
                                conn.send(req.encode())
                                # print("fiflestatus message sent")
                                # print(req)
                                self.sendpeers = True
                            else:
                                req = json.dumps({"filestatus": False})
                                conn.send(req.encode())
        return

    def response(self, conn, addr):
        # print("Entered")
        # self.listeninglock.acquire()
        self.inform_availability(conn)
        self.listen_requests(conn, addr)
        self.sendpeers = False
        # self.listeninglock.release()

    def start_listening(self):
        # self.socket.listen()
        # print("Function started")
        while True:
            # with self.listeninglock:
            # print("hii")
            # time.sleep(1)
            print("started listening")
            self.listeninglock.acquire()
            self.listeningsemaphore.acquire()
            try:
                # print("trying")
                conn, addr = self.server_socket.accept()
            except TimeoutError:
                self.listeningsemaphore.release()
                self.listeninglock.release()    
                continue
            self.receivingsemaphore.acquire()
            # self.managersemaphore.acquire()
            if self.available:
                if self.sendpeers == False:
                    self.firstReply(conn, addr)
                    # self.managersemaphore.release()
                    self.receivingsemaphore.release()
                    self.listeningsemaphore.release()
                    self.listeninglock.release()
                    # print("always here")
                    # print(self.sendpeers)
                    continue
                else:
                    self.response(conn, addr)
            self.available = True
            # self.managersemaphore.release()
            self.receivingsemaphore.release()
            self.listeningsemaphore.release()
            self.listeninglock.release()
            # print("exited listening")
            # self.sendpeers = False
            # listen = threading.Thread(target = self.response,args=(conn,addr))
            # listen.daemon = True
            # listen.start()
            # print("Thread started")

    def manager(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((self.ip, self.port))
            req = json.dumps(
                {"task": "update", "port": self.listening_port})
            s.send(req.encode())
            data = s.recv(BUFFER_SIZE)
            self.listeninglock.acquire()
            if data:
                request = json.loads(data.decode())
                if "peers" in request:
                    updated_peers = {
                        (addr, port): port for addr, port in request["peers"]}
                    self.active_peers.update(updated_peers)
                    print("Updated active peers list")
                    # print(self.active_peers)
            self.listeninglock.release()

    def listen_Manager(self):
        while True:
            t = threading.Thread(target=self.manager)
            t.daemon = True
            t.start()
            t.join()
            # self.managersemaphore.acquire()
            # self.managersemaphore.release()


if __name__ == "__main__":
    peer = Peer("Sudeshna", peer_ip="127.0.0.2", peer_listeningport=7000)
    print(peer.name)
    try:
        peer.add_peer()
        peer.read_peers()
        peer.get_shareable_files(["file1.txt"])
        peer.get_ToBeRequested_files(['file2.txt'])
        manager_thread = threading.Thread(target=peer.listen_Manager)
        manager_thread.daemon = True
        manager_thread.start()
        peer_thread = threading.Thread(target=peer.start_listening)
        peer_thread.daemon = True
        peer_thread.start()
        req_peer = threading.Thread(
            target=peer.fetch_file_fragments)
        req_peer.daemon = True
        req_peer.start()
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("\n")
        print("Sharable files: ",*peer.shareable_files)
        print("Requested files: ",*peer.requested_files)
        print("Downloaded files: ",*peer.downloaded_files)
        peer.close()
