#!/bin/bash
python3 generate_input.py
javac  Main.java
java Main input.txt
python3 graph.py